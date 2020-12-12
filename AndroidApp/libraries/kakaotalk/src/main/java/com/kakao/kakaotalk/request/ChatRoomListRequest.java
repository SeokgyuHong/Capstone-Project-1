/*
  Copyright 2014-2017 Kakao Corp.

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
package com.kakao.kakaotalk.request;

import android.net.Uri;
import android.text.TextUtils;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;

/**
 * @author by leoshin on 15. 8. 25..
 */
public class ChatRoomListRequest extends AuthorizedApiRequest {

    // 가져올 챗방 리스트의 시작 offset
    private final int fromId;

    // 한 페이지에 가져올 챗방의 수. maximum 30개까지 가져올 수 있음
    private final int limit;

    // 챗방리스트 정렬방법. asc, desc 중 하나의 오더로 정렬
    private final String order;
    private final String url;
    private final String filter;

    public ChatRoomListRequest(ChatListContext context) {
        this.filter = context.getFilterString();
        this.fromId = context.getFromId();
        this.limit = context.getLimit();
        this.order = context.getOrder();
        this.url = context.getAfterUrl();
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();

        builder.path(ServerProtocol.TALK_CHATROOM_LIST_PATH)
                .appendQueryParameter(StringSet.from_id, String.valueOf(fromId))
                .appendQueryParameter(StringSet.limit, String.valueOf(limit))
                .appendQueryParameter(StringSet.order, order)
                .appendQueryParameter(StringSet.secure_resource, String.valueOf(true));
        if (!TextUtils.isEmpty(filter)) {
            builder.appendQueryParameter(StringSet.filter, filter);
        }
        return builder;
    }

    @Override
    public String getUrl() {
        if (url != null && url.length() > 0) {
            return url;
        }
        return super.getUrl();
    }
}
