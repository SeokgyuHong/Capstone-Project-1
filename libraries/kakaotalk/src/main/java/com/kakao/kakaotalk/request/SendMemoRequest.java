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
package com.kakao.kakaotalk.request;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.kakao.network.ServerProtocol;

import java.util.Map;

/**
 * @author leoshin, created at 15. 7. 29..
 */
public class SendMemoRequest extends CustomMessageRequest {

    public SendMemoRequest(@NonNull final String templateId, Map<String, String> args) {
        super(templateId, args);
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.TALK_MEMO_SEND_V2_PATH);
        return builder;
    }
}
