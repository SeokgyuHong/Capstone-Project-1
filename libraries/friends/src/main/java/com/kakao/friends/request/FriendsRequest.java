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
package com.kakao.friends.request;

import android.net.Uri;

import com.kakao.friends.FriendContext;
import com.kakao.friends.StringSet;
import com.kakao.network.ServerProtocol;

/**
 * 카카오 친구를 얻어오기 위해 사용되는 Request Model.a
 *
 * @author leo.shin
 */
public class FriendsRequest extends AppFriendsRequest {
    /**
     * 가져올 친구의 타입
     * KAKAO_TALK 카카오톡 친구
     * KAKAO_STORY 카카오스토리 친구
     * KAKAO_TALK_AND_STORY 카카오톡과 카카오스토리의 친구
     */
    public enum FriendType {
        UNDEFINED("undefined", -1),
        KAKAO_TALK("talk", 0),
        KAKAO_STORY("story", 1),
        KAKAO_TALK_AND_STORY("talkstory", 2);

        final private String name;
        final private int value;

        FriendType(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    /**
     * 친구리스트 필터 방법
     * NONE 전체친구
     * REGISTERED 앱 가입친구
     * INVITABLE 앱미가입친구
     */
    public enum FriendFilter {
        NONE("none", 0),
        REGISTERED("registered", 1),
        INVITABLE("invitable", 2),
        ;

        final private String name;
        final private int value;

        FriendFilter(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    /**
     * 친구리스트 정렬 대상.
     */
    public enum FriendOrder {
        UNDEFINED("undefined"),

        /**
         * 이름으로 정렬
         */
        NICKNAME("nickname"),
        /**
         * 19세 이상 먼저 정렬
         */
        AGE("age"),
        /**
         * 즐겨찾기 먼저 정렬
         */
        FAVORITE("favorite"),

        /**
         * @deprecated default value (nickname) will be used if this is chosen.
         */
        @Deprecated
        LAST_CHAT_TIME("last_chat_time"),

        /**
         * @deprecated default value (nickname) will be used if this is chosen.
         */
        @Deprecated
        TALK_CREATED_AT("talk_created_at"),

        /**
         * @deprecated default value (nickname) will be used if this is chosen.
         */
        @Deprecated
        AFFINITY("affinity");

        final private String value;

        FriendOrder(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final FriendType friendType;
    private final FriendFilter friendFilter;
    private final FriendOrder friendOrder;

    public FriendsRequest(FriendContext context) {
        super(context);
        this.friendType = context.getFriendType();
        this.friendFilter = context.getFriendFilter();
        this.friendOrder = context.getFriendOrder();
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().path(ServerProtocol.GET_FRIENDS_PATH);
        if (friendType != null)
            builder.appendQueryParameter(StringSet.friend_type, friendType.name);
        if (friendFilter != null)
            builder.appendQueryParameter(StringSet.friend_filter, friendFilter.name);
        if (friendOrder != null)
            builder.appendQueryParameter(StringSet.friend_order, friendOrder.value);
        return builder;
    }
}
