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
package com.kakao.network;

import com.kakao.common.KakaoContextService;
import com.kakao.common.PhaseInfo;

/**
 * Provides string constants necessary to constitute API requests, including server hosts, and
 * api endpoint paths.
 *
 * @author MJ
 */
public final class ServerProtocol {
    @Deprecated
    public static final String AUTH_AUTHORITY = initAuthAuthority();
    @Deprecated
    public static final String AGE_AUTH_AUTHORITY = initAccountAuthority();
    @Deprecated
    public static final String ACCOUNT_AUTHORITY = initAccountAuthority();
    @Deprecated
    public static final String API_AUTHORITY = initAPIAuthority();
    @Deprecated
    public static final String NAVI_AUTHORITY = initNaviAuthority();

    //Authorization: Bearer
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_BEARER = "Bearer";
    public static final String KAKAO_AK_HEADER_KEY = "KakaoAK";
    public static final String AUTHORIZATION_HEADER_DELIMITER = " ";

    public static final String SCHEME = "https";
    // oauth url
    public static final String AUTHORIZE_CODE_PATH = "oauth/authorize";
    public static final String ACCESS_TOKEN_PATH = "oauth/token";
    public static final String ACCESS_AGE_AUTH_PATH = "ageauths/main.html";

    // api url
    private static final String API_VERSION = "v1";
    private static final String V2_API_VERSION = "v2";

    // usermgmt
    public static final String USER_ME_V2_PATH = V2_API_VERSION + "/user/me";
    public static final String USER_LOGOUT_PATH = API_VERSION + "/user/logout";
    public static final String USER_SIGNUP_PATH = API_VERSION + "/user/signup";
    public static final String USER_UNLINK_PATH = API_VERSION + "/user/unlink";
    public static final String USER_UPDATE_PROFILE_PATH = API_VERSION + "/user/update_profile";
    public static final String USER_ACCESS_TOKEN_INFO_PATH = API_VERSION + "/user/access_token_info";
    public static final String USER_AGE_AUTH = API_VERSION + "/user/age_auth";
    public static final String USER_SERVICE_TERMS_PATH = API_VERSION + "/user/service/terms";
    public static final String USER_SHIPPING_ADDRESS_PATH = API_VERSION + "/user/shipping_address";

    public static final String ACCOUNT_LOGIN_PATH = "kakao_accounts/view/login";
    public static final String ACCOUNT_CREATE_PATH = "sdks/signup";
    // push
    public static final String PUSH_REGISTER_PATH = API_VERSION + "/push/register";
    public static final String PUSH_TOKENS_PATH = API_VERSION + "/push/tokens";
    public static final String PUSH_DEREGISTER_PATH = API_VERSION + "/push/deregister";
    public static final String PUSH_SEND_PATH = API_VERSION + "/push/send";

    //POST
    public static final String STORY_MULTI_UPLOAD_PATH = API_VERSION + "/api/story/upload/multi";
    public static final String STORY_POST_NOTE_PATH = API_VERSION + "/api/story/post/note";
    public static final String STORY_POST_PHOTO_PATH = API_VERSION + "/api/story/post/photo";
    public static final String STORY_POST_LINK_PATH = API_VERSION + "/api/story/post/link";
    //GET
    public static final String STORY_PROFILE_PATH = API_VERSION + "/api/story/profile";
    public static final String STORY_ACTIVITIES_PATH = API_VERSION + "/api/story/mystories";
    public static final String STORY_ACTIVITY_PATH = API_VERSION + "/api/story/mystory";
    public static final String STORY_LINK_SCRAPPER_PATH = API_VERSION + "/api/story/linkinfo";
    public static final String IS_STORY_USER_PATH = API_VERSION + "/api/story/isstoryuser";
    //DELETE
    public static final String STORY_DELETE_ACTIVITY_PATH = API_VERSION + "/api/story/delete/mystory";

    //talk
    public static final String TALK_PROFILE_PATH = API_VERSION + "/api/talk/profile";
    public static final String TALK_CHATROOM_LIST_PATH = API_VERSION + "/api/talk/chat/list";
    public static final String TALK_CHAT_MEMBERS_PATH = API_VERSION + "/api/talk/members";

    public static final String TALK_MESSAGE_SEND_V2_PATH = V2_API_VERSION + "/api/talk/message/send";
    public static final String TALK_MESSAGE_SCRAP_V2_PATH = V2_API_VERSION + "/api/talk/message/scrap/send";
    public static final String TALK_MESSAGE_DEFAULT_V2_PATH = V2_API_VERSION + "/api/talk/message/default/send";

    public static final String OPEN_TALK_MESSAGE_CUSTOM_V1_PATH = API_VERSION + "/api/talk/friends/message/send";
    public static final String OPEN_TALK_MESSAGE_SCRAP_V1_PATH = API_VERSION + "/api/talk/friends/message/scrap/send";
    public static final String OPEN_TALK_MESSAGE_DEFAULT_V1_PATH = API_VERSION + "/api/talk/friends/message/default/send";

    public static final String TALK_MEMO_SEND_V2_PATH = V2_API_VERSION + "/api/talk/memo/send";
    public static final String TALK_MEMO_SCRAP_V2_PATH = V2_API_VERSION + "/api/talk/memo/scrap/send";
    public static final String TALK_MEMO_DEFAULT_V2_PATH = V2_API_VERSION + "/api/talk/memo/default/send";

    public static final String TALK_PLUS_FRIENDS_PATH = API_VERSION + "/api/talk/plusfriends";

    @Deprecated
    public static final String TALK_CHAT_LIST_PATH = API_VERSION + "/api/talk/chats";

    // link
    public static final String LINK_TEMPLATE_PATH = "/api/kakaolink/talk/template";
    public static final String LINK_TEMPLATE_VALIDATE_PATH = V2_API_VERSION + LINK_TEMPLATE_PATH + "/validate";
    public static final String LINK_TEMPLATE_DEFAULT_PATH = V2_API_VERSION + LINK_TEMPLATE_PATH + "/default";
    public static final String LINK_TEMPLATE_SCRAP_PATH = V2_API_VERSION + LINK_TEMPLATE_PATH + "/scrap";

    public static final String TALK_MESSAGE_IMAGE_PATH = "/api/talk/message/image";
    public static final String LINK_IMAGE_UPLOAD_PATH = V2_API_VERSION + TALK_MESSAGE_IMAGE_PATH + "/upload";
    public static final String LINK_IMAGE_SCRAP_PATH = V2_API_VERSION + TALK_MESSAGE_IMAGE_PATH + "/scrap";
    public static final String LINK_IMAGE_DELETE_PATH = V2_API_VERSION + TALK_MESSAGE_IMAGE_PATH + "/delete";

    // friends and operation
    public static final String GET_FRIENDS_PATH = API_VERSION + "/friends";
    public static final String OPEN_FRIENDS_V1_PATH = API_VERSION + "/api/talk/friends";
    public static final String GET_FRIENDS_OPERATION_PATH = API_VERSION + "/friends/operation";

    // storage
    public static final String STORAGE_UPLOAD_IMAGE = API_VERSION + "/storage/image/upload";

    public static String apiAuthority() {
        PhaseInfo phaseInfo = KakaoContextService.getInstance() == null ? null : KakaoContextService.getInstance().phaseInfo();
        if (phaseInfo != null && phaseInfo.phase() != null) {
            switch (phaseInfo.phase()) {
                case DEV:
                    return "alpha-kapi.kakao.com";
                case SANDBOX:
                    return "sandbox-kapi.kakao.com";
                case CBT:
                    return "beta-kapi.kakao.com";
                case PRODUCTION:
                default:
                    return "kapi.kakao.com";
            }
        }
        return initAPIAuthority();
    }

    public static String authAuthority() {
        PhaseInfo phaseInfo = KakaoContextService.getInstance() == null ? null : KakaoContextService.getInstance().phaseInfo();
        if (phaseInfo != null && phaseInfo.phase() != null) {
            switch (phaseInfo.phase()) {
                case DEV:
                    return "alpha-kauth.kakao.com";
                case SANDBOX:
                    return "sandbox-kauth.kakao.com";
                case CBT:
                    return "beta-kauth.kakao.com";
                case PRODUCTION:
                default:
                    return "kauth.kakao.com";
            }
        }
        return initAuthAuthority();
    }

    public static String accountAuthority() {
        PhaseInfo phaseInfo = KakaoContextService.getInstance() == null ? null : KakaoContextService.getInstance().phaseInfo();
        if (phaseInfo != null && phaseInfo.phase() != null) {
            switch (phaseInfo.phase()) {
                case DEV:
                    return "alpha-auth.kakao.com";
                case SANDBOX:
                    return "sandbox-auth.kakao.com";
                case CBT:
                    return "beta-auth.kakao.com";
                case PRODUCTION:
                default:
                    return "auth.kakao.com";
            }
        }
        return initAccountAuthority();
    }

    public static String accountsAuthority() {
        PhaseInfo phaseInfo = KakaoContextService.getInstance() == null ? null : KakaoContextService.getInstance().phaseInfo();
        if (phaseInfo != null && phaseInfo.phase() != null) {
            switch (phaseInfo.phase()) {
                case DEV:
                    return "alpha-accounts.kakao.com";
                case SANDBOX:
                    return "sandbox-accounts.kakao.com";
                case CBT:
                    return "beta-accounts.kakao.com";
                case PRODUCTION:
                default:
                    return "accounts.kakao.com";
            }
        }
        return "accounts.kakao.com";
    }

    private static String initAuthAuthority() {
        return "kauth.kakao.com";
    }

    private static String initAPIAuthority() {
        return "kapi.kakao.com";
    }

    private static String initAccountAuthority() {
        return "auth.kakao.com";
    }

    private static String initNaviAuthority() {
        return "kakaonavi-wguide.kakao.com";
    }
}
