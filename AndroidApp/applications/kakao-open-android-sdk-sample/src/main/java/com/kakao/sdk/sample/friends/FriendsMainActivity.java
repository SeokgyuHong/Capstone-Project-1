package com.kakao.sdk.sample.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.FriendsService;
import com.kakao.friends.request.FriendsRequest.FriendType;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.ListTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.BaseActivity;
import com.kakao.sdk.sample.common.log.Logger;
import com.kakao.sdk.sample.common.widget.KakaoDialogSpinner;
import com.kakao.sdk.sample.common.widget.KakaoToast;
import com.kakao.sdk.sample.friends.FriendsListAdapter.IFriendListCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author leo.shin
 */
public class FriendsMainActivity extends BaseActivity implements OnClickListener, IFriendListCallback {
    public enum MSG_TYPE {
        FEED(0),
        LIST(1),
        SCRAP(2),
        DEFAULT(3);

        private final int value;

        MSG_TYPE(int value) {
            this.value = value;
        }

        public static MSG_TYPE valueOf(int i) {
            for (MSG_TYPE type : values()) {
                if (type.getValue() == i) {
                    return type;
                }
            }

            return FEED;
        }

        public int getValue() {
            return value;
        }
    }

    private static class AppFriendsInfo {
        private final List<AppFriendInfo> friendInfoList = new ArrayList<>();
        private int totalCount;
        private String id;

        AppFriendsInfo() {
        }

        List<AppFriendInfo> getFriendInfoList() {
            return friendInfoList;
        }

        void merge(AppFriendsResponse response) {
            this.id = response.getResultId();
            this.totalCount = response.getTotalCount();
            this.friendInfoList.addAll(response.getFriends());
        }

        public String getId() {
            return id;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public static final String EXTRA_KEY_SERVICE_TYPE = "KEY_FRIEND_TYPE";

    protected ListView list = null;
    private FriendsListAdapter adapter = null;
    private final List<FriendType> friendTypeList = new ArrayList<>();
    private AppFriendContext friendContext = null;
    private AppFriendsInfo appFriendsInfo = null;
    protected KakaoDialogSpinner msgType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_friends_main);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_KEY_SERVICE_TYPE)) {
            String[] serviceTypes = intent.getStringArrayExtra(EXTRA_KEY_SERVICE_TYPE);
            for (String serviceType : serviceTypes) {
                friendTypeList.add(FriendType.valueOf(serviceType));
            }
        } else {
            friendTypeList.add(FriendType.KAKAO_TALK);
            friendTypeList.add(FriendType.KAKAO_STORY);
            friendTypeList.add(FriendType.KAKAO_TALK_AND_STORY);
        }

        list = findViewById(R.id.friend_list);
        Button talkButton = findViewById(R.id.all_talk_friends);
        Button storyButton = findViewById(R.id.all_story_friends);
        Button talkStoryButton = findViewById(R.id.all_talk_and_story_friends);
        msgType = findViewById(R.id.message_type);

        talkButton.setVisibility(View.GONE);
        storyButton.setVisibility(View.GONE);
        talkStoryButton.setVisibility(View.GONE);

        for (FriendType friendType : friendTypeList) {
            switch (friendType) {
                case KAKAO_TALK:
                    talkButton.setVisibility(View.VISIBLE);
                    talkButton.setOnClickListener(this);
                    break;
                case KAKAO_STORY:
                    storyButton.setVisibility(View.VISIBLE);
                    storyButton.setOnClickListener(this);
                    break;
                case KAKAO_TALK_AND_STORY:
                    talkStoryButton.setVisibility(View.VISIBLE);
                    talkStoryButton.setOnClickListener(this);
                    break;
            }
        }

        if (friendTypeList.size() == 1) {
            appFriendsInfo = new AppFriendsInfo();
            requestFriends(friendTypeList.get(0));
        }

        findViewById(R.id.title_back).setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View v) {
        FriendType type = FriendType.KAKAO_TALK;
        switch (v.getId()) {
            case R.id.all_talk_friends:
                type = FriendType.KAKAO_TALK;
                break;
            case R.id.all_story_friends:
                type = FriendType.KAKAO_STORY;
                break;
            case R.id.all_talk_and_story_friends:
                type = FriendType.KAKAO_TALK_AND_STORY;
                break;
        }
        requestFriends(type);
    }

    private void requestFriends(FriendType type) {
        adapter = null;
        appFriendsInfo = new AppFriendsInfo();
        friendContext = new AppFriendContext(true, 0, 100, "asc");
        requestFriendsInner();
    }


    private void requestFriendsInner() {
        final IFriendListCallback callback = this;
        FriendsService.getInstance().requestAppFriends(friendContext, friendsResponseCallback);
    }

    private ListTemplate getListTemplate() {
        return ListTemplate.newBuilder("WEEKLY MAGAZINE",
                LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build())
                .addContent(ContentObject.newBuilder("취미의 특징, 탁구",
                        "http://mud-kage.kakao.co.kr/dn/bDPMIb/btqgeoTRQvd/49BuF1gNo6UXkdbKecx600/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("스포츠")
                        .build())
                .addContent(ContentObject.newBuilder("크림으로 이해하는 커피이야기",
                        "http://mud-kage.kakao.co.kr/dn/QPeNt/btqgeSfSsCR/0QJIRuWTtkg4cYc57n8H80/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("음식")
                        .build())
                .addContent(ContentObject.newBuilder("신메뉴 출시❤️ 체리블라썸라떼",
                        "http://mud-kage.kakao.co.kr/dn/c7MBX4/btqgeRgWhBy/ZMLnndJFAqyUAnqu4sQHS0/kakaolink40_original.png",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("사진").build())
                .addButton(new ButtonObject("웹으로 보기", LinkObject.newBuilder()
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build()))
                .addButton(new ButtonObject("앱으로 보기", LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();
    }

    protected TalkResponseCallback<Boolean> talkResponseCallback = new TalkResponseCallback<Boolean>() {
        @Override
        public void onNotKakaoTalkUser() {
            KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            KakaoToast.makeToast(getApplicationContext(), "failure : " + errorResult, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            KakaoToast.makeToast(getApplicationContext(), "onNotSignedUp : " + "User Not Registed App", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(Boolean result) {
            KakaoToast.makeToast(getApplicationContext(), "Send message success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDidStart() {
            showWaitingDialog();
        }

        @Override
        public void onDidEnd() {
            cancelWaitingDialog();
        }
    };

    private ApiResponseCallback<AppFriendsResponse> friendsResponseCallback = new TalkResponseCallback<AppFriendsResponse>() {
        @Override
        public void onNotKakaoTalkUser() {
            KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            redirectSignupActivity();
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            KakaoToast.makeToast(getApplicationContext(), errorResult.toString(), Toast.LENGTH_SHORT).show();
            Logger.e("onFailure: " + errorResult.toString());
        }

        @Override
        public void onSuccess(AppFriendsResponse result) {
            if (result != null) {
                appFriendsInfo.merge(result);
                if (adapter == null) {
                    adapter = new FriendsListAdapter(appFriendsInfo.getFriendInfoList(), FriendsMainActivity.this);
                    list.setAdapter(adapter);
                } else {
                    adapter.setItem(appFriendsInfo.getFriendInfoList());
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onDidStart() {
            showWaitingDialog();
        }

        @Override
        public void onDidEnd() {
            cancelWaitingDialog();
        }
    };

    protected TalkResponseCallback<MessageSendResponse> messageResponseCallback = new TalkResponseCallback<MessageSendResponse>() {
        @Override
        public void onNotKakaoTalkUser() {
            KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            KakaoToast.makeToast(getApplicationContext(), "failure : " + errorResult, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            KakaoToast.makeToast(getApplicationContext(), "onNotSignedUp : " + "User Not Registed App", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(MessageSendResponse result) {
            KakaoToast.makeToast(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDidStart() {
            showWaitingDialog();
        }

        @Override
        public void onDidEnd() {
            cancelWaitingDialog();
        }
    };

    protected void requestDefaultMemo() {
        KakaoTalkService.getInstance().requestSendMemo(talkResponseCallback, getListTemplate());
    }

    protected void requestScrapMemo() {
        KakaoTalkService.getInstance().requestSendMemo("https://developers.kakao.com", talkResponseCallback);
    }

    protected void requestDefaultMessage(final AppFriendInfo friendInfo) {
        KakaoTalkService.getInstance().sendMessageToFriends(Collections.singletonList(friendInfo.getUUID()), getListTemplate(), messageResponseCallback);
    }

    protected void requestScrapMessage(final AppFriendInfo friendInfo) {
        KakaoTalkService.getInstance().sendMessageToFriends(Collections.singletonList(friendInfo.getUUID()), "https://developers.kakao.com", messageResponseCallback);
    }

    @Override
    public void onItemSelected(int position, AppFriendInfo friendInfo) {
    }

    @Override
    public void onPreloadNext() {
        if (friendContext.hasNext()) {
            requestFriendsInner();
        }
    }
}
