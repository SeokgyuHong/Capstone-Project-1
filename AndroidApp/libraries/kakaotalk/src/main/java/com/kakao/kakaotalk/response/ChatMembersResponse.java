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
package com.kakao.kakaotalk.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.response.model.ChatMember;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import java.util.List;

/**
 * 챗멤버 API의 응답 클래스.
 *
 * Response model for /v1/api/talk/members API.
 *
 * @author kevin.kang. Created on 2018. 8. 7..
 */
public class ChatMembersResponse extends JSONObjectResponse {

    /**
     * 챗방 내 톡친구의 수. 500의 최대값을 가진다. Friend_only 옵션을 true로 준 경우에만 내려옴.
     *
     * @return number of friends in this chat
     */
    public @Nullable Long activeFriendsCount() {
        return activeFriendsCount;
    }

    /**
     * 챗방 내 멤버의 수. 500의 최대값을 가진다. Friend_only 옵션을 true로 준 경우에만 내려옴.
     *
     * @return number of active members in this chat
     */
    public @Nullable Long activeMembersCount() {
        return activeMembersCount;
    }

    /**
     * 해당 챗방에 참여중인 멤버의 {@link ChatMember} 리스트. 멤버수가 500명을 초과할 경우 500명만 내려오게 된다.
     * 이 값은 추후 바뀔 수 있다.
     *
     * @return list of members in this chat
     */
    public @Nullable List<ChatMember> members() {
        return members;
    }

    /**
     * 해당 챗방의 타입. "DirectChat" 또는 "MultiChat"
     *
     * @return chat type (DirectChat / MultiChat)
     */
    public @NonNull String type() {
        return type;
    }


    @SuppressWarnings("WeakerAccess")
    ChatMembersResponse(final String stringData) {
        super(stringData);
        if (getBody().has(StringSet.active_members_count)) {
            activeMembersCount = getBody().getLong(StringSet.active_members_count);
        }
        if (getBody().has(StringSet.active_friends_count)) {
            activeFriendsCount = getBody().getLong(StringSet.active_friends_count);
        }
        if (getBody().has(StringSet.members)) {
            members = ChatMember.CONVERTER.convertList(getBody().getJSONArray(StringSet.members));
        }
        type = getBody().optString(StringSet.type, null);
    }

    private Long activeMembersCount;
    private Long activeFriendsCount;
    private List<ChatMember> members;
    private String type;

    public static final ResponseStringConverter<ChatMembersResponse> CONVERTER = new ResponseStringConverter<ChatMembersResponse>() {
        @Override
        public ChatMembersResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new ChatMembersResponse(o);
        }
    };
}
