package com.kakao.sdk.link.sample.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kakao.sdk.link.sample.R;
import com.kakao.sdk.link.sample.common.widget.KakaoSpinnerAdapter.ISpinnerListener;
import com.kakao.sdk.link.sample.common.widget.KakaoSpinnerAdapter.KakaoSpinnerItems;

import java.util.Arrays;
import java.util.List;

/**
 * @author leo.shin
 */
public class KakaoDialogSpinner extends LinearLayout {

    private List<String> entryList = null;
    private String title = null;
    private Dialog dialog = null;
    private ListView listView = null;
    private KakaoSpinnerAdapter adapter = null;
    private int iconResId = 0;
    private TextView spinner;
    private int selectedItemPosition = 0;
    private boolean showTitleDivider = false;
    private int titleBgResId = 0;
    private int titleTextColor = 0;

    public KakaoDialogSpinner(Context context) {
        super(context);
        initView(context, null);
    }

    public KakaoDialogSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.view_spinner, this, false);
        addView(layout);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KakaoDialogSpinner);
            title = a.getString(R.styleable.KakaoDialogSpinner_kakao_prompt);
            iconResId = a.getResourceId(R.styleable.KakaoDialogSpinner_kakao_icon, 0);
            titleBgResId = a.getResourceId(R.styleable.KakaoDialogSpinner_kakao_dialogTitle, 0);
            titleTextColor = a.getResourceId(R.styleable.KakaoDialogSpinner_kakao_titleTextColor, 0);
            showTitleDivider = a.getBoolean(R.styleable.KakaoDialogSpinner_kakao_showTitleDivider, false);

            final int entriesResId = a.getResourceId(R.styleable.KakaoDialogSpinner_kakao_entries, 0);
            if (entriesResId > 0) {
                entryList = Arrays.asList(getResources().getStringArray(entriesResId));
            }

            a.recycle();
        }

        ImageView icon = layout.findViewById(R.id.menu_icon);
        if (iconResId > 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setBackgroundResource(iconResId);
        } else {
            icon.setVisibility(View.GONE);
        }

        spinner = layout.findViewById(R.id.menu_title);
        if (entryList != null && entryList.size() > 0) {
            spinner.setText(entryList.get(0));
        }

        DialogBuilder builder = new DialogBuilder(context);
        if (title != null) {
            builder = builder.setTitle(title);
        }

        builder.setTitleBgResId(titleBgResId);
        builder.setTitleTextColor(titleTextColor);
        builder.setShowTitleDivider(showTitleDivider);

        if (entryList != null) {
            listView = (ListView)inflater.inflate(R.layout.view_custom_list, null, false);
            builder.setView(listView);
            dialog = builder.create();

            adapter = new KakaoSpinnerAdapter(new KakaoSpinnerItems(iconResId, entryList), new ISpinnerListener() {
                @Override
                public int getSelectedItemPosition() {
                    return selectedItemPosition;
                }

                @Override
                public void onItemSelected(BaseAdapter adapter, int position) {
                    selectedItemPosition = position;
                    spinner.setText(entryList.get(selectedItemPosition));
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            listView.setAdapter(adapter);
        }

        setOnClickListener(v -> showDialog());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public Object getSelectedItem() {
        if (adapter != null && listView != null) {
            return adapter.getItem(selectedItemPosition);
        }

        return null;
    }

    public void setSelection(int position) {
        this.selectedItemPosition = position;
        spinner.setText(entryList.get(selectedItemPosition));
    }
}
