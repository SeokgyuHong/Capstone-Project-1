/*
  Copyright 2014-2018 Kakao Corp.

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
package com.kakao.kakaolink.internal;

import com.kakao.common.KakaoContextService;
import com.kakao.common.PhaseInfo;

import java.nio.charset.Charset;

/**
 * @author MJ
 */
public final class KakaoTalkLinkProtocol {
    public static final String LINK_VERSION_40 = "4.0";
    public static final String ENCODING = Charset.forName("UTF-8").name();
    public static final String TALK_MARKET_URL_PREFIX = "market://details?id=com.kakao.talk&referrer=";
    public static final String TALK_MARKET_URL_PREFIX_2 = "https://play.google.com/store/apps/details?id=com.kakao.talk&referrer=";
    public static final String APP_PACKAGE = "appPkg";
    public static final String APP_KEY_HASH = "keyHash";

    // main key
    public static final String APP_KEY = "appkey";
    public static final String APP_VER = "appver";
    public static final String API_VER = "apiver";
    public static final String LINKVER = "linkver";
    public static final String OBJS = "objs";
    public static final String EXTRAS = "extras";
    public static final String FORWARDABLE = "forwardable";
    public static final String LCBA = "lcba"; // Link Callback Arguments

    /// keys in KakaoTalk
    public static final String lv = "lv";
    public static final String av = "av";
    public static final String ak = "ak";
    public static final String P = "P";
    public static final String C = "C";

    // kakaolink
    public static final String LINK_SCHEME = "kakaolink";
    public static final String LINK_AUTHORITY = "send";

    // 4.0 parameters
    public static final String LINK_VER = "link_ver";
    public static final String TEMPLATE_ID = "template_id";
    public static final String TEMPLATE_ARGS = "template_args";
    public static final String TEMPLATE_JSON = "template_json";
    public static final String TEMPLATE_MSG = "template_msg";
    public static final String ARGUMENT_MSG = "argument_msg";
    public static final String WARNING_MSG = "warning_msg";
    public static final String TEMPLATE_OBJECT = "template_object";
    public static final String REQUEST_URL = "request_url";
    public static final String TARGET_APP_KEY = "target_app_key";


    public static final String LINK_40 = "4.0";
    public static final int LINK_URI_MAX_SIZE = 10 * 1024;

    // keys for web sharer
    public static final String SHARER_APP_KEY = "app_key";
    public static final String VALIDATION_ACTION = "validation_action";
    public static final String VALIDATION_PARAMS = "validation_params";
    public static final String VALIDATION_DEFAULT = "default";
    public static final String VALIDATION_CUSTOM = "custom";
    public static final String VALIDATION_SCRAP = "scrap";
    public static final String SHARER_KA = "ka";


    public static final int TALK_MIN_VERSION_SUPPORT_LINK_V2 = 1400255; // 6.0.0

    public static final String SHARE_URI = "talk/friends/picker/easylink";

    public static String sharerAuthority() {
        PhaseInfo phaseInfo = KakaoContextService.getInstance() == null ? null : KakaoContextService.getInstance().phaseInfo();
        if (phaseInfo != null) {
            switch (phaseInfo.phase()) {
                case DEV:
                    return "alpha-sharer.devel.kakao.com";
                case SANDBOX:
                    return "sandbox-sharer.devel.kakao.com";
                case CBT:
                    return "beta-sharer.kakao.com";
                case PRODUCTION:
                default:
                    return "sharer.kakao.com";
            }
        }
        return initShareAuthority();
    }
    private static String initShareAuthority() {
        return "sharer.kakao.com";
    }
}
