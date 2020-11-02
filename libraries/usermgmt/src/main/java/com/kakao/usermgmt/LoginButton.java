/*
  Copyright 2014-2019 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.usermgmt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kakao.auth.AuthType;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 로그인 버튼
 * 로그인 layout에 {@link LoginButton}을 선언하여 사용한다.
 *
 * @author MJ
 */
public class LoginButton extends FrameLayout {

    private Fragment supportFragment;

    public LoginButton(Context context) {
        super(context);
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 로그인 버튼 클릭시 세션을 오픈하도록 설정한다.
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inflate(getContext(), R.layout.kakao_login_layout, this);
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // 카톡 또는 카스가 존재하면 옵션을 보여주고, 존재하지 않으면 바로 직접 로그인창.
                final List<AuthType> authTypes = getAuthTypes();
                onClickLoginButton(authTypes);
            }
        });
    }

    protected List<AuthType> getAuthTypes() {
        final List<AuthType> availableAuthTypes = new ArrayList<>();

        if (Session.getCurrentSession().getAuthCodeManager().isTalkLoginAvailable()) {
            availableAuthTypes.add(AuthType.KAKAO_TALK);
            availableAuthTypes.add(AuthType.KAKAO_TALK_ONLY);
        }
        if (Session.getCurrentSession().getAuthCodeManager().isStoryLoginAvailable()) {
            availableAuthTypes.add(AuthType.KAKAO_STORY);
        }
        availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);

        AuthType[] authTypes = KakaoSDK.getAdapter().getSessionConfig().getAuthTypes();
        if (authTypes == null || authTypes.length == 0 || (authTypes.length == 1 && authTypes[0] == AuthType.KAKAO_LOGIN_ALL)) {
            authTypes = AuthType.values();
        }
        availableAuthTypes.retainAll(Arrays.asList(authTypes));

        if (availableAuthTypes.contains(AuthType.KAKAO_TALK)) {
            availableAuthTypes.remove(AuthType.KAKAO_TALK_ONLY);
        }
        // 개발자가 설정한 것과 available 한 타입이 없다면 직접계정 입력이 뜨도록 한다.
        if (availableAuthTypes.size() == 0) {
            availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        }
        return availableAuthTypes;
    }

    protected void onClickLoginButton(final List<AuthType> authTypes) {
        if (authTypes.size() == 1) {
            openSession(authTypes.get(0));
        } else {
            final Item[] authItems = createAuthItemArray(authTypes);
            ListAdapter adapter = createLoginAdapter(authItems);
            final Dialog dialog = createLoginDialog(authItems, adapter);
            showDialogWhilePreservingSystemVisibility(dialog);
        }
    }

    /*
        Dialog 를 show 할 때 immersive / fullscreen mode 가 깨지는 이슈가 있다.
        https://stackoverflow.com/a/23207365/1509425
        위 링크에 Focusable flag 를 사용하여 fullscreen / immersive mode 를 깨지 않도록 임시로 지정해준 후,
        show() 후에 원복시키는 workaround 가 나와있다.
     */
    private void showDialogWhilePreservingSystemVisibility(final Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        dialog.show();
        if (getActivity() == null) return;
        if (window != null) {
            window.getDecorView().setSystemUiVisibility(
                    getActivity().getWindow().getDecorView().getSystemUiVisibility());
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    /**
     * 가능한 AuhType들이 담겨 있는 리스트를 인자로 받아 로그인 어댑터의 data source로 사용될 Item array를 반환한다.
     *
     * @param authTypes 가능한 AuthType들을 담고 있는 리스트
     * @return 실제로 로그인 방법 리스트에 사용될 Item array
     */
    private Item[] createAuthItemArray(final List<AuthType> authTypes) {
        final List<Item> itemList = new ArrayList<>();
        if (authTypes.contains(AuthType.KAKAO_TALK)) {
            itemList.add(new Item(R.string.com_kakao_kakaotalk_account, R.drawable.talk, R.string.com_kakao_kakaotalk_account_tts, AuthType.KAKAO_TALK));
        }
        if (authTypes.contains(AuthType.KAKAO_TALK_ONLY)) {
            itemList.add(new Item(R.string.com_kakao_kakaotalk_account, R.drawable.talk, R.string.com_kakao_kakaotalk_account_tts, AuthType.KAKAO_TALK_ONLY));
        }
        if (authTypes.contains(AuthType.KAKAO_STORY)) {
            itemList.add(new Item(R.string.com_kakao_kakaostory_account, R.drawable.story, R.string.com_kakao_kakaostory_account_tts, AuthType.KAKAO_STORY));
        }
        if (authTypes.contains(AuthType.KAKAO_ACCOUNT)) {
            itemList.add(new Item(R.string.com_kakao_other_kakaoaccount, R.drawable.account, R.string.com_kakao_other_kakaoaccount_tts, AuthType.KAKAO_ACCOUNT));
        }

        return itemList.toArray(new Item[0]);
    }

    private ListAdapter createLoginAdapter(final Item[] authItems) {
        /*
          가능한 auth type들을 유저에게 보여주기 위한 준비.
         */
        return new ArrayAdapter<Item>(
                getContext(),
                android.R.layout.select_dialog_item,
                android.R.id.text1, authItems) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.layout_login_item, parent, false);
                }
                ImageView imageView = convertView.findViewById(R.id.login_method_icon);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setImageDrawable(getResources().getDrawable(authItems[position].icon, getContext().getTheme()));
                } else {
                    imageView.setImageDrawable(getResources().getDrawable(authItems[position].icon));
                }
                TextView textView = convertView.findViewById(R.id.login_method_text);
                textView.setText(authItems[position].textId);
                return convertView;
            }
        };
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /*
        로그인 요청 시 받은 systemUiVisibility, flags 및 displayCutoutMode 를 그대로 설정.
     */
    private void applyFullscreenOptions(final Dialog dialog) {
        Activity callerActivity = getActivity();
        if (callerActivity == null) return;
        Window activityWindow = callerActivity.getWindow();
        Window dialogWindow = dialog.getWindow();
        if (activityWindow == null || dialogWindow == null) return;

        int systemUiVisibility = activityWindow.getDecorView().getSystemUiVisibility();
        int flags = activityWindow.getAttributes().flags;

        int displayCutoutMode = Build.VERSION.SDK_INT < Build.VERSION_CODES.P ?
                0 : callerActivity.getWindow().getAttributes().layoutInDisplayCutoutMode;
        View decorView = dialogWindow.getDecorView();
        decorView.setSystemUiVisibility(systemUiVisibility);
        dialogWindow.addFlags(flags);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dialogWindow.getAttributes().layoutInDisplayCutoutMode = displayCutoutMode;
        }
    }

    /**
     * 실제로 유저에게 보여질 dialog 객체를 생성한다.
     *
     * @param authItems 가능한 AuthType들의 정보를 담고 있는 Item array
     * @param adapter   Dialog의 list view에 쓰일 adapter
     * @return 로그인 방법들을 팝업으로 보여줄 dialog
     */
    protected Dialog createLoginDialog(final Item[] authItems, final ListAdapter adapter) {
        final Dialog dialog = new Dialog(getContext(), R.style.LoginDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        applyFullscreenOptions(dialog);
        dialog.setContentView(R.layout.layout_login_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

//        TextView textView = (TextView) dialog.findViewById(R.id.login_title_text);
//        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/KakaoOTFRegular.otf");
//        if (customFont != null) {
//            textView.setTypeface(customFont);
//        }

        ListView listView = dialog.findViewById(R.id.login_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AuthType authType = authItems[position].authType;
                if (authType != null) {
                    openSession(authType);
                }
                dialog.dismiss();
            }
        });

        Button closeButton = dialog.findViewById(R.id.login_close_button);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void openSession(final AuthType authType) {
        if (getSupportFragment() != null) {
            Session.getCurrentSession().open(authType, getSupportFragment());
        } else {
            Session.getCurrentSession().open(authType, (Activity) getContext());
        }
    }

    /**
     * @deprecated Use {@link #setSupportFragment(Fragment)} instead
     */
    @Deprecated
    public void setSuportFragment(final Fragment fragment) {
        this.supportFragment = fragment;
    }

    public void setSupportFragment(final Fragment fragment) {
        this.supportFragment = fragment;
    }

    public Fragment getSupportFragment() {
        return this.supportFragment;
    }

    /**
     * 각 로그인 방법들의 text, icon, 실제 AuthType 들을 담고 있는 container class.
     */
    private static class Item {
        final int textId;
        public final int icon;
        final int contentDescId;
        final AuthType authType;

        Item(final int textId, final Integer icon, final int contentDescId, final AuthType authType) {
            this.textId = textId;
            this.icon = icon;
            this.contentDescId = contentDescId;
            this.authType = authType;
        }
    }
}
