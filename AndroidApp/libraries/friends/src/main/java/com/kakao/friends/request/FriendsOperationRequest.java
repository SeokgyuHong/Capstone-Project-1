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
package com.kakao.friends.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.friends.FriendOperationContext;
import com.kakao.friends.StringSet;
import com.kakao.network.ServerProtocol;

/**
 * @author leo.shin
 */
public class FriendsOperationRequest extends AuthorizedApiRequest {
    public enum Operation {
        UNDEFINED("undefined", -1),
        INTERSECTION("i", 0),
        UNION("u", 1),
        SUBTRACTION("s", 2);

        final private String name;
        final private int value;
        Operation(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    private final String firstId;
    private final String secondId;
    private final Operation operation;
    private final boolean secureResource;
    private final int offset;
    private final int limit;
    private final String order;
    private final String url;

    public FriendsOperationRequest(FriendOperationContext context) {
        this.firstId = context.getFirstId();
        this.secondId = context.getSecondId();
        this.operation = context.getOperation();
        this.secureResource = context.isSecureResource();
        this.offset = context.getOffset();
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
        builder.path(ServerProtocol.GET_FRIENDS_OPERATION_PATH)
                .appendQueryParameter(StringSet.first_id, firstId)
                .appendQueryParameter(StringSet.second_id, secondId)
                .appendQueryParameter(StringSet.operator, operation.name)
                .appendQueryParameter(StringSet.secure_resource, String.valueOf(secureResource))
                .appendQueryParameter(StringSet.offset, String.valueOf(offset))
                .appendQueryParameter(StringSet.limit, String.valueOf(limit))
                .appendQueryParameter(StringSet.order, order);
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
