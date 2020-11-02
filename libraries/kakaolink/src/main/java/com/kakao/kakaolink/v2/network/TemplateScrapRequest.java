/*
  Copyright 2017 Kakao Corp.

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

import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.network.ServerProtocol;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;

import java.util.Map;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 17..
 */

class TemplateScrapRequest extends KakaoLinkTemplateRequest {
    private final String url;

    TemplateScrapRequest(final PhaseInfo phaseInfo, IConfiguration configuration, final String url, final String templateId, final Map<String, String> tempalateArgs) {
        super(phaseInfo, configuration, null, templateId, tempalateArgs);
        this.url = url;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.LINK_TEMPLATE_SCRAP_PATH)
                .appendQueryParameter(KakaoTalkLinkProtocol.REQUEST_URL, url);
    }

    TemplateScrapRequest(final PhaseInfo phaseInfo, IConfiguration configuration, final String appKey, final String url, final String templateId, final Map<String, String> tempalateArgs) {
        super(phaseInfo, configuration, appKey, templateId, tempalateArgs);
        this.url = url;
    }

    @Override
    public String getMethod() {
        return GET;
    }
}
