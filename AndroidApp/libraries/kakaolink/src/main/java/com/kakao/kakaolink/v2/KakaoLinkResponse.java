/*
  Copyright 2016-2017 Kakao Corp.

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
package com.kakao.kakaolink.v2;

import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseStringConverter;

import org.json.JSONObject;

/**
 * Template validation에 성공하였을 때 반환되는 response.
 * Created by kevin.kang on 2016. 11. 25..
 */

public class KakaoLinkResponse extends JSONObjectResponse {
    private final String templateId;
    private JSONObject templateArgs;
    private final JSONObject templateMsg;
    private final JSONObject warningMsg;
    private final JSONObject argumentMsg;

    public KakaoLinkResponse(final String stringData) {
        super(stringData);
        templateId = getBody().optString(KakaoTalkLinkProtocol.TEMPLATE_ID, null);
        templateArgs = getBody().optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_ARGS, null);
        templateMsg = getBody().optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_MSG, null);
        warningMsg = getBody().optJSONObject(KakaoTalkLinkProtocol.WARNING_MSG, null);
        argumentMsg = getBody().optJSONObject(KakaoTalkLinkProtocol.ARGUMENT_MSG, null);
    }

    public KakaoLinkResponse(final JSONObject linkResponse) {
        this(linkResponse.toString());
    }

    KakaoLinkResponse(final String tempplateId, final JSONObject templateArgs, final JSONObject templateMsg, final JSONObject warningMsg, final JSONObject argumentMsg) {
        this.templateId = tempplateId;
        this.templateArgs = templateArgs;
        this.templateMsg = templateMsg;
        this.warningMsg = warningMsg;
        this.argumentMsg = argumentMsg;
    }

    public String getTemplateId() {
        return templateId;
    }

    @SuppressWarnings("unused")
    public JSONObject getTemplateArgs() {
        return templateArgs;
    }

    @SuppressWarnings("unused") // 써드 앱들이 사용하는 메소드.
    public JSONObject getTemplateMsg() {
        return templateMsg;
    }

    @SuppressWarnings("unused") // 써드 앱들이 사용하는 메소드.
    public JSONObject getWarningMsg() {
        return warningMsg;
    }

    @SuppressWarnings("unused") // 써드 앱들이 사용하는 메소드.
    public JSONObject getArgumentMsg() {
        return argumentMsg;
    }


    public static final ResponseStringConverter<KakaoLinkResponse> CONVERTER = new ResponseStringConverter<KakaoLinkResponse>() {
        @Override
        public KakaoLinkResponse convert(String data) {
            return new KakaoLinkResponse(data);
        }
    };
}
