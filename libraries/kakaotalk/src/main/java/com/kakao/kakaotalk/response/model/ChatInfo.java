/*
  Copyright 2014-2018 Kakao Corp.

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
package com.kakao.kakaotalk.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kakao.auth.common.MessageSendable;
import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.network.response.JSONObjectConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import org.json.JSONObject;

import java.util.List;

/**
 * 챗방 클래스.
 * <p>
 * Class for Chat room.
 *
 * @author by leoshin on 15. 8. 25..
 * @see com.kakao.kakaotalk.v2.KakaoTalkService#requestChatRoomList(TalkResponseCallback, ChatListContext)
 */
public class ChatInfo implements Parcelable, MessageSendable {
    private final long chatId;
    private final String title;
    private final String imageUrl;
    private final int memberCount;
    private final String chatType;
    private final List<String> displayMemberImageList;
    private final List<String> memberImageUrlList;

    ChatInfo(JSONObject body) throws ResponseBody.ResponseBodyException {
        this.chatId = body.optLong(StringSet.id, 0L);
        this.title = body.optString(StringSet.title, null);
        this.imageUrl = body.optString(StringSet.image_url, null);
        this.memberCount = body.optInt(StringSet.member_count, 0);
        this.chatType = body.optString(StringSet.chat_type, null);
        this.memberImageUrlList = ResponseStringConverter.IDENTITY_CONVERTER.convertList(body.optJSONArray(StringSet.member_image_url_list));
        this.displayMemberImageList = ResponseStringConverter.IDENTITY_CONVERTER.convertList(body.optJSONArray(StringSet.display_member_images));
    }

    ChatInfo(Parcel in) {
        this.chatId = in.readLong();
        this.title = in.readString();
        this.imageUrl = in.readString();
        this.memberCount = in.readInt();
        this.chatType = in.readString();
        this.memberImageUrlList = in.createStringArrayList();
        this.displayMemberImageList = in.createStringArrayList();
    }

    /**
     * 채팅방 고유의 id.
     *
     * @return 채팅방 고유의 id
     */
    public long getChatId() {
        return chatId;
    }

    /**
     * 채팅방의 이름.
     *
     * @return 채팅방의 이름
     */
    public String getTitle() {
        return title;
    }

    /**
     * 그룹채팅방에 대표이미지가 설정된 경우 받을 수 있다.
     *
     * @return 그룹 채팅방 대표 이미지의 url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    public int getMemberCount() {
        return memberCount;
    }

    /**
     * "regular" 일반 채팅방
     * "open" 오픈채팅방
     *
     * @return "regular", "open"
     */
    public String getChatType() {
        return chatType;
    }

    public List<String> getDisplayMemberImageList() {
        return displayMemberImageList;
    }

    @Override
    public String toString() {
        return "++ id : " + chatId +
                ", title : " + title +
                ", imageUrl : " + imageUrl +
                ", chatType : " + chatType +
                ", memberCount : " + memberCount;
    }

    public static final JSONObjectConverter<ChatInfo> CONVERTER = new JSONObjectConverter<ChatInfo>() {
        @Override
        public ChatInfo convert(JSONObject data) throws ResponseBody.ResponseBodyException {
            return new ChatInfo(data);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(chatId);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeInt(memberCount);
        dest.writeString(chatType);
        dest.writeStringList(memberImageUrlList);
        dest.writeStringList(displayMemberImageList);
    }

    public static final Parcelable.Creator<ChatInfo> CREATOR = new Parcelable.Creator<ChatInfo>() {
        public ChatInfo createFromParcel(Parcel in) {
            return new ChatInfo(in);
        }

        public ChatInfo[] newArray(int size) {
            return new ChatInfo[size];
        }
    };

    @Override
    public String getTargetId() {
        return String.valueOf(chatId);
    }

    @Override
    public String getType() {
        return "chat_id";
    }

    @Override
    public boolean isAllowedMsg() {
        return true;
    }
}
