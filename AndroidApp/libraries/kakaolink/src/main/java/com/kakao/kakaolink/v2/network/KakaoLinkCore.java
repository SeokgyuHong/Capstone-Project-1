package com.kakao.kakaolink.v2.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kakao.message.template.TemplateParams;
import com.kakao.network.IRequest;
import com.kakao.common.KakaoContextService;
import com.kakao.util.KakaoUtilService;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import java.util.Map;

/**
 * This is an interface that provides {@link IRequest} instance for using KakaoLink v2 validation API.
 * This also provides a helper that returns KakaoLink v2 intent.
 *
 * 이 인터페이스는 내부적 용도로 사용되며 언제든 변경될 수 있습니다.
 *
 * @author kevin.kang. Created on 2017. 11. 13..
 */

public interface KakaoLinkCore {
    /**
     * Checks whether KakaoLink v2 messages can be sent with the KakaoTalk installed on the device.
     *
     * @param context Context instance to be used in resolving intent
     * @return true if KakaoLink v2 can be sent with the installed KakaoTalk
     */
    boolean isAvailable(final Context context);

    /**
     *
     * Returns an {@link IRequest} object for default template validation API.
     *
     * @param context Current application context
     * @param appKey App key of the app that will be displayed in KakaoLink v2 message
     * @param params {@link TemplateParams}
     * @return {@link IRequest} instance
     */
    IRequest defaultTemplateRequest(final Context context, final String appKey, final TemplateParams params);

    /**
     * Returns an {@link IRequest} object for custom template validation API.
     *
     * @param context Current application context
     * @param appKey App key of the app that will be displayed in KakaoLink v2 message
     * @param templateId custom template id
     * @param templateArgs template arguments to be used in the custom template
     * @return {@link IRequest} object
     */
    IRequest customTemplateRequest(final Context context, final String appKey, final String templateId, final Map<String, String> templateArgs);

    /**
     *
     * Returns an {@link IRequest} object for simple scrap template validation API.
     *
     * @param context Current application context
     * @param appKey App key of the app that will be displayed in KakaoLink v2 message
     * @param url URL to be scrapped
     * @return {@link IRequest} instance
     */
    @SuppressWarnings("unused")
    IRequest scrapTemplateRequest(final Context context, final String appKey, final String url);

    /**
     * Returns an {@link IRequest} for scrap template validation API with custom template and
     * template arguments.
     *
     * @param context Current application context
     * @param url URL to be scrapped
     * @param templateId custom template id
     * @param templateArgs template arguments to be used in the custom template
     * @return {@link IRequest} instance
     */
    IRequest scrapTemplateRequest(final Context context, final String appKey, final String url, final String templateId, final Map<String, String> templateArgs);

    /**
     * Create an intent (kakaolink://send...) that will be used to send KakaoLink v2 message.
     *
     * @param context Current application context
     * @param appKey App key of the app that will be displayed in KakaoLink v2 message
     * @param linkResponse {@link JSONObject} response from validation API
     * @return {@link Intent} instance
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#URI_LENGTH_EXCEEDED}
     * if
     */
    Intent kakaoLinkIntent(final Context context, final String appKey, final JSONObject linkResponse) throws KakaoException;

    Intent kakaoLinkIntent(final Context context, final String appKey, final JSONObject linkResponse, final Map<String, String> serverCallbackArgs) throws KakaoException;
    /**
     * Create a market install intent for KakaoTalk. This will also include referrer parameter
     * for the referrer application.
     *
     * @param context Current context
     * @return Market install intent for KakaoTalk
     */
    Intent kakaoTalkMarketIntent(final Context context);

    /**
     * create a KakaoLink sharer URL for default template messages.
     * @param context Current application context
     * @param params {@link TemplateParams}
     * @return {@link Uri} instance representing the sharer URL
     */
    Uri sharerUri(final Context context, final TemplateParams params, final Map<String, String> serverCallbackArgs);

    /**
     * create a KakaoLink sharer URL for custom template messages.
     * @param context Current application context
     * @param templateId custom template id
     * @param templateArgs template arguments to be used in the custom template
     * @return {@link Uri} instance representing the sharer URL
     */
    Uri sharerUri(final Context context, final String templateId, final Map<String, String> templateArgs, final Map<String, String> serverCallbackArgs);

    /**
     *  create a KakaoLink sharer URL for simple scrap template messages.
     * @param context Current application context
     * @param url URL to be scrapped
     * @return {@link Uri} instance representing the sharer URL
     */
    @SuppressWarnings("unused")
    Uri sharerUri(final Context context, final String url, final Map<String, String> serverCallbackArgs);

    /**
     * creates a KakaoLink sharer URL for scrap template messages with custom template and
     * template arguments.
     * @param context Current application context
     * @param url URL to be scrapped
     * @param templateId custom template id
     * @param templateArgs template arguments to be used in the custom template
     * @return {@link Uri} instance representing the sharer URL
     */
    Uri sharerUri(final Context context, final String url, final String templateId, final Map<String, String> templateArgs, final Map<String, String> serverCallbackArgs);

    /**
     *
     */
    class Factory {
        /**
         * Retrieves  a singleton instance that sends KakaoLink v2 messages with the app key defined in
         * AndroidManifest.xml.
         *
         * @return IKakaoLinkSerice instance
         */
        public static KakaoLinkCore getInstance() {
            return service;
        }
        private static KakaoLinkCore service = new DefaultKakaoLinkCore(KakaoContextService.getInstance(), KakaoUtilService.Factory.getInstance());
    }
}
