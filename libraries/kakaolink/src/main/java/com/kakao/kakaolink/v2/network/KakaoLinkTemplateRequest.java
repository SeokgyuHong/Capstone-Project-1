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
package com.kakao.kakaolink.v2.network;

import android.net.Uri;

import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.network.ApiRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 17..
 */

public abstract class KakaoLinkTemplateRequest extends ApiRequest {
    private final String templateId;
    private final String targetAppKey;
    private final Map<String, String> templateArgs;

    KakaoLinkTemplateRequest(final PhaseInfo phaseInfo, final IConfiguration configuration, final String appKey, final String templateId, final Map<String, String> templateArgs) {
        super(phaseInfo, configuration);
        this.targetAppKey = appKey;
        this.templateId = templateId;
        this.templateArgs = templateArgs;
    }

    KakaoLinkTemplateRequest(final PhaseInfo phaseInfo, IConfiguration configuration, final String appKey) {
        super(phaseInfo, configuration);
        this.targetAppKey = appKey;
        this.templateId = null;
        this.templateArgs = null;
    }

    public String getTemplateId() {
        return templateId;
    }

    public Map getTemplateArgs() {
        return templateArgs;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.authority(ServerProtocol.apiAuthority());
        builder.appendQueryParameter(KakaoTalkLinkProtocol.LINK_VER, KakaoTalkLinkProtocol.LINK_VERSION_40);
        if (templateId != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID, templateId);
        }
        if (templateArgs != null && !templateArgs.isEmpty()) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS, getTemplateArgsString());
        }
        if (targetAppKey != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TARGET_APP_KEY, targetAppKey);
        }
        return builder;
    }

    public String getTemplateArgsString() {
        if (templateArgs == null || templateArgs.isEmpty())
            return null;
        return new JSONObject(templateArgs).toString();
    }
}
