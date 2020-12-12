package com.kakao.sdk.sample.kakaotalk;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.GlobalApplication;
import com.kakao.sdk.sample.friends.FriendsMainActivity;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Collections;
import java.util.List;

/**
 * @author leo.shin
 */
public class KakaoTalkFriendListActivity extends FriendsMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UserProfile userProfile = UserProfile.loadFromCache();
        if (userProfile != null) {
            View headerView = getLayoutInflater().inflate(R.layout.view_friend_item, list, false);

            NetworkImageView profileView = headerView.findViewById(R.id.profile_image);
            profileView.setDefaultImageResId(R.drawable.thumb_story);
            profileView.setErrorImageResId(R.drawable.thumb_story);
            TextView nickNameView = headerView.findViewById(R.id.nickname);

            String profileUrl = userProfile.getThumbnailImagePath();
            GlobalApplication app = GlobalApplication.getGlobalApplicationContext();
            if (profileUrl != null && profileUrl.length() > 0) {
                profileView.setImageUrl(profileUrl, app.getImageLoader());
            } else {
                profileView.setImageResource(R.drawable.thumb_story);
            }

            String nickName = getString(R.string.text_send_to_me) + " " + userProfile.getNickname();
            nickNameView.setText(nickName);

            list.addHeaderView(headerView);

            headerView.setOnClickListener(v -> {
                KakaoTalkMessageBuilder builder = new KakaoTalkMessageBuilder();
                builder.addParam("username", userProfile.getNickname());
                builder.addParam("labelMsg", "Hi " + userProfile.getNickname() + ". this is test message");
                requestSendMemo(builder);
            });
        }
    }

    @Override
    public void onItemSelected(final int position, final AppFriendInfo friendInfo) {
        TalkMessageHelper.showSendMessageDialog(this, (dialog, which) -> {
            MSG_TYPE type = MSG_TYPE.valueOf(msgType.getSelectedItemPosition());
            requestSendMessage(type, friendInfo, makeMessageBuilder(type, friendInfo.getProfileNickname()));
        });
    }

    private KakaoTalkMessageBuilder makeMessageBuilder(MSG_TYPE type, String nickName) {
        KakaoTalkMessageBuilder builder = new KakaoTalkMessageBuilder();

        if (type == MSG_TYPE.FEED) {
            builder.addParam("username", nickName);
            builder.addParam("labelMsg", "Hi " + nickName + ". this is test message");
        }

        return builder;
    }

    private void requestSendMessage(MSG_TYPE type, AppFriendInfo friendInfo, KakaoTalkMessageBuilder builder) {
        if (type == MSG_TYPE.DEFAULT) {
            requestDefaultMessage(friendInfo);
            return;
        }
        if (type == MSG_TYPE.SCRAP) {
            requestScrapMessage(friendInfo);
            return;
        }
        List<String> ids = Collections.singletonList(friendInfo.getUUID());
        KakaoTalkService.getInstance().sendMessageToFriends(ids, TalkMessageHelper.getSampleTemplateId(type), builder.build(), messageResponseCallback);
    }

    private void requestSendMemo(KakaoTalkMessageBuilder builder) {
        MSG_TYPE type = MSG_TYPE.valueOf(msgType.getSelectedItemPosition());
        if (type == MSG_TYPE.DEFAULT) {
            requestDefaultMemo();
            return;
        }
        if (type == MSG_TYPE.SCRAP) {
            requestScrapMemo();
            return;
        }
        KakaoTalkService.getInstance().requestSendMemo(talkResponseCallback, TalkMessageHelper.getSampleTemplateId(type), builder.build());
    }

}
