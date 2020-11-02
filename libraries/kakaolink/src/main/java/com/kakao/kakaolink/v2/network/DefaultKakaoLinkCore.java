/*
  Copyright 2017-2018 Kakao Corp.

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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kakao.kakaolink.R;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.IRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.common.IConfiguration;
import com.kakao.common.KakaoContextService;
import com.kakao.common.PhaseInfo;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.log.Logger;
import com.kakao.util.KakaoUtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 11. 13..
 */

class DefaultKakaoLinkCore implements KakaoLinkCore {
    private KakaoContextService contextService;
    private KakaoUtilService utilService;

    DefaultKakaoLinkCore(final KakaoContextService contextService, final KakaoUtilService utilService) {
        this.contextService = contextService;
        this.utilService = utilService;
    }

    @Override
    public IRequest defaultTemplateRequest(final Context context, final String appKey, final TemplateParams params) {
        // App key, key hash, KA header 등을 준비한다.
        contextService.initialize(context);
        PhaseInfo phaseInfo = contextService.phaseInfo();
        IConfiguration configuration = contextService.getAppConfiguration();
        return new TemplateDefaultRequest(phaseInfo, configuration, appKey, params);
    }

    @Override
    public IRequest customTemplateRequest(final Context context, final String appKey, String templateId, Map<String, String> templateArgs) {
        contextService.initialize(context);
        PhaseInfo phaseInfo = contextService.phaseInfo();
        IConfiguration configuration = contextService.getAppConfiguration();
        return new TemplateValidateRequest(phaseInfo, configuration, appKey, templateId, templateArgs);
    }

    @Override
    public IRequest scrapTemplateRequest(Context context, String appKey, String url) {
        return scrapTemplateRequest(context, appKey, url, null, null);
    }

    @Override
    public IRequest scrapTemplateRequest(final Context context, final String appKey, String url, final String templateId, final Map<String, String> templateArgs) {
        contextService.initialize(context);
        PhaseInfo phaseInfo = contextService.phaseInfo();
        IConfiguration configuration = contextService.getAppConfiguration();
        return new TemplateScrapRequest(phaseInfo, configuration, appKey, url, templateId, templateArgs);
    }

    @Override
    public boolean isAvailable(Context context) {
        Uri uri = new Uri.Builder().scheme(KakaoTalkLinkProtocol.LINK_SCHEME).authority(KakaoTalkLinkProtocol.LINK_AUTHORITY).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return utilService.resolveIntent(context, intent, KakaoTalkLinkProtocol.TALK_MIN_VERSION_SUPPORT_LINK_V2) != null;
    }

    @Override
    public Intent kakaoLinkIntent(final Context context, String appKey, final JSONObject linkResponse) throws KakaoException {
        return kakaoLinkIntent(context, appKey, linkResponse, null);
    }

    @Override
    public Intent kakaoLinkIntent(Context context, String appKey, JSONObject linkResponse, Map<String, String> serverCallbackArgs) throws KakaoException {
        contextService.initialize(context);
        PhaseInfo phaseInfo = contextService.phaseInfo();
        IConfiguration configuration = contextService.getAppConfiguration();
        try {
            int size = getAttachmentSize(phaseInfo, configuration, linkResponse);
            Logger.i("KakaoLink intent size is %d bytes.", size);
            if (size > KakaoTalkLinkProtocol.LINK_URI_MAX_SIZE) {
                throw new KakaoException(KakaoException.ErrorType.URI_LENGTH_EXCEEDED, context.getString(R.string.com_kakao_alert_uri_too_long));
            }
        } catch (JSONException e) {
            throw new KakaoException(KakaoException.ErrorType.JSON_PARSING_ERROR, e.toString());
        }

        String templateId = linkResponse.optString(KakaoTalkLinkProtocol.TEMPLATE_ID, null);
        JSONObject templateArgs = linkResponse.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_ARGS);
        JSONObject templateJson = linkResponse.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_MSG);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(KakaoTalkLinkProtocol.LINK_SCHEME).authority(KakaoTalkLinkProtocol.LINK_AUTHORITY);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.LINKVER, KakaoTalkLinkProtocol.LINK_VERSION_40);
        if (appKey == null) {
            appKey = phaseInfo.appKey();
        }
        builder.appendQueryParameter(KakaoTalkLinkProtocol.APP_KEY, appKey);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.APP_VER, configuration.getAppVer());

        if (templateId != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID, templateId);
        }
        if (templateArgs != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS, templateArgs.toString());
        }
        if (templateJson != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_JSON, templateJson.toString());
        }

        if (configuration.getExtrasJson() != null) {
            JSONObject mergedExtras = mergeExtrasAndServerCallbackArgs(configuration.getExtrasJson(), serverCallbackArgs);
            builder.appendQueryParameter(KakaoTalkLinkProtocol.EXTRAS, mergedExtras.toString());
        }
        Uri uri = builder.build();
        Intent intent = new Intent(Intent.ACTION_SEND, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return utilService.resolveIntent(context, intent, TALK_MIN_VERSION_SUPPORT_LINK_40);
    }

    @Override
    public Intent kakaoTalkMarketIntent(Context context) {
        contextService.initialize(context);
        PhaseInfo phaseInfo = contextService.phaseInfo();
        IConfiguration configuration = contextService.getAppConfiguration();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KakaoTalkLinkProtocol.TALK_MARKET_URL_PREFIX + getReferrer(phaseInfo, configuration)));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return marketIntent;
    }

    @Override
    public Uri sharerUri(Context context, TemplateParams params, final Map<String, String> serverCallbackArgs) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_OBJECT, params.toJSONObject());
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_DEFAULT, validationParams, serverCallbackArgs);
    }

    @Override
    public Uri sharerUri(Context context, String templateId, Map<String, String> templateArgs, final Map<String, String> serverCallbackArgs) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ID, Integer.parseInt(templateId));
            if (templateArgs != null) {
                validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ARGS, new JSONObject(templateArgs));
            }
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_CUSTOM, validationParams, serverCallbackArgs);
    }

    @Override
    public Uri sharerUri(Context context, String url, final Map<String, String> serverCallbackArgs) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.REQUEST_URL, url);
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_SCRAP, validationParams, serverCallbackArgs);
    }

    @Override
    public Uri sharerUri(Context context, String url, String templateId, Map<String, String> templateArgs, final Map<String, String> serverCallbackArgs) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.REQUEST_URL, url);
            if (templateId != null) {
                validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ID, Integer.parseInt(templateId));
            }
            if (templateArgs != null) {
                validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ARGS, new JSONObject(templateArgs));
            }
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_SCRAP, validationParams, serverCallbackArgs);
    }

    private Uri shareUri(final Context context, final String validationAction, JSONObject validationParams, final Map<String, String> serverCallbackArgs) {
        contextService.initialize(context);
        IConfiguration configuration = contextService.getAppConfiguration();
        PhaseInfo phaseInfo = contextService.phaseInfo();
        try {
            validationParams.put(KakaoTalkLinkProtocol.LINK_VER, KakaoTalkLinkProtocol.LINK_VERSION_40);
        } catch (JSONException e) {
            return null;
        }

        JSONObject paramsJson;
        try {
            paramsJson = new JSONObject(serverCallbackArgs);
        } catch (NullPointerException e) {
            paramsJson = null;
        }

        Uri.Builder builder = new Uri.Builder().authority(KakaoTalkLinkProtocol.sharerAuthority())
                .scheme(ServerProtocol.SCHEME)
                .path(KakaoTalkLinkProtocol.SHARE_URI)
                .appendQueryParameter(KakaoTalkLinkProtocol.SHARER_APP_KEY, phaseInfo.appKey())
                .appendQueryParameter(KakaoTalkLinkProtocol.VALIDATION_ACTION, validationAction)
                .appendQueryParameter(KakaoTalkLinkProtocol.VALIDATION_PARAMS, validationParams.toString())
                .appendQueryParameter(KakaoTalkLinkProtocol.SHARER_KA, configuration.getKAHeader());

        if (paramsJson != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.LCBA, paramsJson.toString());
        }
        return builder.build();
    }


    @SuppressWarnings("WeakerAccess")
    String getReferrer(final PhaseInfo phaseInfo, final IConfiguration configuration) {
        JSONObject json = new JSONObject();
        try {
            json.put(CommonProtocol.KA_HEADER_KEY, configuration.getKAHeader());
            json.put(KakaoTalkLinkProtocol.APP_KEY, phaseInfo.appKey());
            json.put(KakaoTalkLinkProtocol.APP_VER, configuration.getAppVer());
            json.put(KakaoTalkLinkProtocol.APP_PACKAGE, configuration.getPackageName());
        } catch (JSONException e) {
            Logger.w(e);
            return "";
        }
        return json.toString();
    }

    @SuppressWarnings("WeakerAccess")
    int getAttachmentSize(final PhaseInfo phaseInfo, final IConfiguration configuration, final JSONObject response) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KakaoTalkLinkProtocol.lv, KakaoTalkLinkProtocol.LINK_VERSION_40);
        jsonObject.put(KakaoTalkLinkProtocol.av, KakaoTalkLinkProtocol.LINK_VERSION_40);
        jsonObject.put(KakaoTalkLinkProtocol.ak, phaseInfo.appKey());

        JSONObject templateJson = response.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_MSG);
        jsonObject.put(KakaoTalkLinkProtocol.P, templateJson.get(KakaoTalkLinkProtocol.P));
        jsonObject.put(KakaoTalkLinkProtocol.C, templateJson.get(KakaoTalkLinkProtocol.C));
        jsonObject.put(KakaoTalkLinkProtocol.TEMPLATE_ID, response.optString(KakaoTalkLinkProtocol.TEMPLATE_ID, null));
        JSONObject templateArgs = response.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_ARGS);
        if (templateArgs != null) {
            jsonObject.put(KakaoTalkLinkProtocol.TEMPLATE_ARGS, templateArgs);
        }
        if (configuration.getExtrasJson() != null) {
            jsonObject.put(KakaoTalkLinkProtocol.EXTRAS, configuration.getExtrasJson().toString());
        }
        return jsonObject.toString().getBytes().length;
    }

    @SuppressWarnings("WeakerAccess")
    JSONObject mergeExtrasAndServerCallbackArgs(final JSONObject extras, final Map<String, String> serverCallbackArgs) {
        if (extras == null) return null;
        if (serverCallbackArgs == null) return extras;
        JSONObject paramsJson = new JSONObject(serverCallbackArgs);
        try {
            extras.put(KakaoTalkLinkProtocol.LCBA, paramsJson.toString());
        } catch (JSONException e) {
            Logger.w(String.format("failed to put Kakaolink callback parameters %s to extras.", serverCallbackArgs.toString()));
        }
        return extras;
    }


    private static final int TALK_MIN_VERSION_SUPPORT_LINK_40 = 1400255; // 6.0.0
}
