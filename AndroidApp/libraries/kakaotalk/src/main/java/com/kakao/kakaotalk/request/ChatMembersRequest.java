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
package com.kakao.kakaotalk.request;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;

/**
 * @author kevin.kang. Created on 2018. 8. 7..
 */
public class ChatMembersRequest extends AuthorizedApiRequest {
    private final @NonNull
    Long chatId;
    private final Boolean friendsOnly;

    public ChatMembersRequest(final @NonNull Long chatId, Boolean friendOnly) {
        this.chatId = chatId;
        this.friendsOnly = friendOnly;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().path(ServerProtocol.TALK_CHAT_MEMBERS_PATH);
        builder.appendQueryParameter(StringSet.chat_id, chatId.toString());
        if (friendsOnly != null) {
            builder.appendQueryParameter(StringSet.friends_only, friendsOnly.toString());
        }
        return builder;
    }
}
