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
package com.kakao.kakaolink.v2.network;

import android.net.Uri;

import com.kakao.network.ServerProtocol;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;

import java.util.Map;

/**
 * Template validation request class.
 * Created by kevin.kang on 2016. 11. 25..
 */

public class TemplateValidateRequest extends KakaoLinkTemplateRequest {
    TemplateValidateRequest(final PhaseInfo phaseInfo, final IConfiguration configuration, final String templateId, final Map<String, String> tempalteArgs) {
        super(phaseInfo, configuration, null, templateId, tempalteArgs);
    }

    TemplateValidateRequest(final PhaseInfo phaseInfo, final IConfiguration configuration, final String appKey, final String templateId, final Map<String, String> tempalteArgs) {
        super(phaseInfo, configuration, appKey, templateId, tempalteArgs);
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.LINK_TEMPLATE_VALIDATE_PATH);
        return builder;
    }
}
