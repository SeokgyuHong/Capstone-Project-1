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

import com.kakao.auth.StringSet;
import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.network.ServerProtocol;

import org.json.JSONArray;

import java.util.List;

/**
 * 토큰으로 인증날짜와 CI값을 얻는다. 게임 사업부가 저장하고 있는 정보를 내려준다.
 * @author leo.shin
 */
public class AgeAuthRequest extends AuthorizedApiRequest {
    private final String ageLimit;
    private final List<String> propertyKeyList;

    public AgeAuthRequest(String ageLimit, List<String> propertyKeyList) {
        this.ageLimit = ageLimit;
        this.propertyKeyList = propertyKeyList;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.USER_AGE_AUTH);

        if (ageLimit != null && ageLimit.length() > 0) {
            builder.appendQueryParameter(StringSet.age_limit, ageLimit);
        }

        if (propertyKeyList != null && propertyKeyList.size() > 0) {
            builder.appendQueryParameter(StringSet.property_keys, new JSONArray(propertyKeyList).toString());
        }
        return builder;
    }
}
