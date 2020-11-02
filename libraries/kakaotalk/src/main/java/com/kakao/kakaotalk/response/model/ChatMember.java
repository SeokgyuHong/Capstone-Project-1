/*
  Copyright 2018 Kakao Corp.

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
import androidx.annotation.Nullable;

import com.kakao.auth.common.MessageSendable;
import com.kakao.kakaotalk.StringSet;
import com.kakao.network.response.JSONObjectConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.util.OptionalBoolean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 챗 멤버 클래스. 챗 멤버는 아래와 같은 특징을 가진다.
 *
 * <ul>
 *     <li>친구일수도, 아닐수도 있다.</li>
 *     <li>앱 가입자일수도, 아닐수도 있다.</li>
 * </ul>
 *
 *
 * Class representing chat members.
 *
 * @author kevin.kang. Created on 2018. 8. 8..
 */
public class ChatMember implements Parcelable, MessageSendable {

    /**
     * 사용자 ID. {@link #appRegistered()}가 {@link OptionalBoolean#TRUE} 인 경우에만 내려온다.
     *
     * @return App user id
     */
    public @Nullable Long id() {
        return id;
    }

    /**
     * 챗 멤버의 앱 가입 여부
     *
     * @return true is registered, false if otherwise.
     */
    public OptionalBoolean appRegistered() {
        return appRegistered;
    }

    /**
     * 카카오톡 닉네임.
     *
     * @return KakaoTalk nickname
     */
    public String nickname() {
        return nickname;
    }

    /**
     * 친구의 썸네일 이미지
     *
     * @return url of chat member's thumbnail image
     */
    public String thumbnailImage() {
        return thumbnailImage;
    }

    /**
     * 해당 앱에서 유일한 친구의 code
     * 가변적인 데이터.
     */
    public String uuid() {
        return uuid;
    }

    ChatMember(final Long id,
                      final Boolean appRegistered,
                      final String nickname,
                      final String thumbnailImage,
                      final String uuid,
               final Boolean msgBlocked
                      ) {
        this.id = id;
        this.appRegistered = OptionalBoolean.getOptionalBoolean(appRegistered);
        this.nickname = nickname;
        this.thumbnailImage = thumbnailImage;
        this.uuid = uuid;
        this.msgBlocked = OptionalBoolean.getOptionalBoolean(msgBlocked);
    }

    private Long id;
    private OptionalBoolean appRegistered;
    private String nickname;
    private String thumbnailImage;
    private String uuid;
    private OptionalBoolean msgBlocked;

    /*
        아래는 API 응답을 parsing하기 위한 컨버터.
    */
    public static final JSONObjectConverter<ChatMember> CONVERTER = new JSONObjectConverter<ChatMember>() {
        @Override
        public ChatMember convert(JSONObject data) throws ResponseBody.ResponseBodyException {
            try {
                Long id = data.has(StringSet.id) ? data.getLong(StringSet.id) : null;
                Boolean appRegistered = data.has(StringSet.app_registered) ?
                        data.getBoolean(StringSet.app_registered) : null;
                String nickname = data.optString(StringSet.nickname, null);
                String thumbnailImage = data.optString(StringSet.thumbnail_image, null);
                String uuid = data.optString(StringSet.uuid, null);
                Boolean msgBlocked = data.has(StringSet.msg_blocked) ?
                        data.getBoolean(StringSet.msg_blocked) : null;
                return new ChatMember(id, appRegistered, nickname, thumbnailImage, uuid, msgBlocked);
            } catch (JSONException e) {
                throw new ResponseBody.ResponseBodyException(e);
            }
        }
    };

    /*
        아래는 Parcelable 인터페이스에 따른 구현이다.
        Below are Parcelable implementation.
     */
    private ChatMember(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        byte tmpAppRegistered = in.readByte();
        appRegistered = tmpAppRegistered == 0 ?
                OptionalBoolean.NONE :
                tmpAppRegistered == 1 ?
                        OptionalBoolean.TRUE :
                        OptionalBoolean.FALSE;
        nickname = in.readString();
        thumbnailImage = in.readString();
        uuid = in.readString();
        byte tmpMsgblocked = in.readByte();
        msgBlocked = tmpMsgblocked == 0 ?
                OptionalBoolean.NONE :
                tmpMsgblocked == 1 ?
                        OptionalBoolean.TRUE :
                        OptionalBoolean.FALSE;
    }

    public static final Creator<ChatMember> CREATOR = new Creator<ChatMember>() {
        @Override
        public ChatMember createFromParcel(Parcel in) {
            return new ChatMember(in);
        }

        @Override
        public ChatMember[] newArray(int size) {
            return new ChatMember[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeByte((byte) (appRegistered == OptionalBoolean.NONE ? 0 : appRegistered == OptionalBoolean.TRUE ? 1 : 2));
        dest.writeString(nickname);
        dest.writeString(thumbnailImage);
        dest.writeString(uuid);
        dest.writeByte((byte) (msgBlocked == OptionalBoolean.NONE ? 0 : msgBlocked == OptionalBoolean.TRUE ? 1 : 2));
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChatMember)) return false;
        ChatMember cast = (ChatMember) obj;
        if (id == null ? cast.id != null : !id.equals(cast.id)) return false;
        if (appRegistered != cast.appRegistered) return false;
        if (nickname == null ? cast.nickname != null : !nickname.equals(cast.nickname)) return false;
        if (thumbnailImage == null ? cast.thumbnailImage != null : !thumbnailImage.equals(cast.thumbnailImage)) return false;
        if (uuid == null ? cast.uuid != null : !uuid.equals(cast.uuid)) return false;
        if (msgBlocked != cast.msgBlocked) return false;
        return true;
    }

    @Override
    public String getTargetId() {
        return uuid();
    }

    @Override
    public String getType() {
        return StringSet.uuid;
    }

    @Override
    public boolean isAllowedMsg() {
        return msgBlocked != OptionalBoolean.TRUE;
    }

    public OptionalBoolean msgBlocked() {
        return msgBlocked;
    }
}
