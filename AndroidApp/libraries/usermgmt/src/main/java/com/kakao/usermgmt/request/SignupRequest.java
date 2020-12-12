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
package com.kakao.usermgmt.request;

import android.net.Uri;

import com.kakao.network.ServerProtocol;
import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.usermgmt.StringSet;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leoshin, created at 15. 8. 6..
 */
public class SignupRequest extends AuthorizedApiRequest {
    private final JSONObject properties;

    public SignupRequest(Map<String, String> properties) {
        this.properties = properties != null ? new JSONObject(properties) : null;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.USER_SIGNUP_PATH);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (properties != null) {
            params.put(StringSet.properties, properties.toString());
        }
        return params;
    }
}
