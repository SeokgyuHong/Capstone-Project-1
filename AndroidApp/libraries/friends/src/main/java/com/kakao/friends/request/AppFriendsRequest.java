/*
  Copyright 2019 Kakao Corp.

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

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.StringSet;
import com.kakao.network.ServerProtocol;

/**
 * @author kevin.kang. Created on 2019-08-01..
 */
public class AppFriendsRequest extends AuthorizedApiRequest {
    public AppFriendsRequest(AppFriendContext context) {
        this.offset = context.getOffset();
        this.limit = context.getLimit();
        this.order = context.getOrder();
        this.appFriendOrder = context.getAppFriendOrder();
        this.url = context.getAfterUrl();

    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().path(ServerProtocol.OPEN_FRIENDS_V1_PATH)
                .appendQueryParameter(StringSet.offset, String.valueOf(offset))
                .appendQueryParameter(StringSet.limit, String.valueOf(limit))
                .appendQueryParameter(StringSet.secure_resource, String.valueOf(true));
        if (appFriendOrder != null)
            builder.appendQueryParameter(StringSet.friend_order, appFriendOrder.getValue());
        if (order != null) builder.appendQueryParameter(StringSet.order, order);
        return builder;
    }

    @Override
    public String getUrl() {
        if (url != null && url.length() > 0) {
            return url;
        }
        return super.getUrl();
    }

    private final int offset;
    private final int limit;
    private final AppFriendOrder appFriendOrder;
    private final String order;
    private final String url;
}
