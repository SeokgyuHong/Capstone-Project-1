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
package com.kakao.kakaostory.request;

import android.net.Uri;

import com.kakao.network.ServerProtocol;
import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.kakaostory.StringSet;

/**
 * @author leoshin, created at 15. 8. 4..
 */
public class DeleteMyStoryRequest extends AuthorizedApiRequest {

    private final String id;
    public DeleteMyStoryRequest(String id) {
        this.id = id;
    }
    @Override
    public String getMethod() {
        return DELETE;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.STORY_DELETE_ACTIVITY_PATH)
                .appendQueryParameter(StringSet.id, id);
    }
}
