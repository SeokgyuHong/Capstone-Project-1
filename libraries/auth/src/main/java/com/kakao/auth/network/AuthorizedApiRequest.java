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
package com.kakao.auth.network;

import android.net.Uri;

import com.kakao.network.ApiRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.network.multipart.Part;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This is an abstract class for network requests with access token authentication.
 *
 * @author leo.shin
 */
public abstract class AuthorizedApiRequest extends ApiRequest implements AuthorizedRequest {
    private String accessToken;

    protected AuthorizedApiRequest() {
    }

    public abstract String getMethod();

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().authority(ServerProtocol.apiAuthority());
    }

    public Map<String, String> getParams() {
        return super.getParams();
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = super.getHeaders();
        headers.put(ServerProtocol.AUTHORIZATION_HEADER_KEY, ServerProtocol.AUTHORIZATION_BEARER + ServerProtocol.AUTHORIZATION_HEADER_DELIMITER + accessToken);
        return headers;
    }

    @Override
    public List<Part> getMultiPartList() {
        return Collections.emptyList();
    }

    public String getBodyEncoding() {
        return "UTF-8";
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
}
