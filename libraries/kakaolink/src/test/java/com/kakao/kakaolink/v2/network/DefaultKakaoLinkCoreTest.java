package com.kakao.kakaolink.v2.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;

import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.kakaolink.v2.KakaoLinkTestHelper;
import com.kakao.test.common.KakaoTestCase;
import com.kakao.test.common.TestAppConfiguration;
import com.kakao.test.common.TestKakaoUtilService;
import com.kakao.test.common.TestPhaseInfo;
import com.kakao.common.IConfiguration;
import com.kakao.common.KakaoContextService;
import com.kakao.util.KakaoUtilService;
import com.kakao.common.PhaseInfo;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.CommonProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author kevin.kang. Created on 2018. 1. 2..
 */
@Config(packageName = "com.kakao.kakaolink")
@RunWith(RobolectricTestRunner.class)
public class DefaultKakaoLinkCoreTest extends KakaoTestCase {
    private DefaultKakaoLinkCore core;
    private IConfiguration configuration;
    private PhaseInfo phaseInfo;
    private Context context;

    @Override
    public void setup() {
        super.setup();
        KakaoUtilService utilService = new TestKakaoUtilService();
        configuration = new TestAppConfiguration();
        phaseInfo = new TestPhaseInfo();
        context = ApplicationProvider.getApplicationContext();
        KakaoContextService contextService = spy(new KakaoContextService(configuration, phaseInfo));
        doNothing().when(contextService).initialize(context);
        core = new DefaultKakaoLinkCore(contextService, utilService);
    }

    @Test
    public void kakaoLinkIntentWithDelegateAppKey() {
        Intent intent = core.kakaoLinkIntent(context, "app_key", KakaoLinkTestHelper.createKakaoLinkResponse("1234"));
        Uri uri = intent.getData();
        assertNotNull(uri);
        assertEquals(KakaoTalkLinkProtocol.LINK_SCHEME, uri.getScheme());
        assertEquals(KakaoTalkLinkProtocol.LINK_AUTHORITY, uri.getHost());
        assertEquals(KakaoTalkLinkProtocol.LINK_VERSION_40, uri.getQueryParameter(KakaoTalkLinkProtocol.LINKVER));
        assertEquals("app_key", uri.getQueryParameter(KakaoTalkLinkProtocol.APP_KEY));
        assertEquals(configuration.getAppVer(), uri.getQueryParameter(KakaoTalkLinkProtocol.APP_VER));
        assertEquals("1234", uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID));
        assertNotNull(uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS));
        assertNotNull(uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_JSON));
    }

    @Test
    public void kakaoLinkIntent() throws JSONException {
        String userId = "1234+=?";
        String productId = "5678%20d+";
        Map<String, String> serverCallbackArgs = new HashMap<>();
        serverCallbackArgs.put("user_id", userId);
        serverCallbackArgs.put("product_id", productId);
        Intent intent = core.kakaoLinkIntent(context, null, KakaoLinkTestHelper.createKakaoLinkResponse("1234"), serverCallbackArgs);
        Uri uri = intent.getData();
        assertNotNull(uri);
        assertEquals(KakaoTalkLinkProtocol.LINK_SCHEME, uri.getScheme());
        assertEquals(KakaoTalkLinkProtocol.LINK_AUTHORITY, uri.getHost());
        assertEquals(KakaoTalkLinkProtocol.LINK_VERSION_40, uri.getQueryParameter(KakaoTalkLinkProtocol.LINKVER));
        assertEquals(phaseInfo.appKey(), uri.getQueryParameter(KakaoTalkLinkProtocol.APP_KEY));
        assertEquals(configuration.getAppVer(), uri.getQueryParameter(KakaoTalkLinkProtocol.APP_VER));
        assertEquals("1234", uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID));
        assertNotNull(uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS));
        assertNotNull(uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_JSON));

        // 링크 서버 콜백 값 lcba 체크
        JSONObject extras = new JSONObject(uri.getQueryParameter(KakaoTalkLinkProtocol.EXTRAS));
        JSONObject lcba = new JSONObject(extras.getString(KakaoTalkLinkProtocol.LCBA));
        assertEquals(userId, lcba.getString("user_id"));
        assertEquals(productId, lcba.getString("product_id"));
    }

    @Test
    public void kakaoLinkIntentWithNullServerCallbackArgs() throws JSONException {
        Intent intent = core.kakaoLinkIntent(context, null, KakaoLinkTestHelper.createKakaoLinkResponse("1234"), null);
        Uri uri = intent.getData();
        assertNotNull(uri);
        assertEquals(KakaoTalkLinkProtocol.LINK_SCHEME, uri.getScheme());
        assertEquals(KakaoTalkLinkProtocol.LINK_AUTHORITY, uri.getHost());
        assertEquals(KakaoTalkLinkProtocol.LINK_VERSION_40, uri.getQueryParameter(KakaoTalkLinkProtocol.LINKVER));
        assertEquals(phaseInfo.appKey(), uri.getQueryParameter(KakaoTalkLinkProtocol.APP_KEY));
        assertEquals(configuration.getAppVer(), uri.getQueryParameter(KakaoTalkLinkProtocol.APP_VER));
        assertEquals("1234", uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID));
        assertNotNull(uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS));
        assertNotNull(uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_JSON));

        // 링크 서버 콜백 값 lcba 체크
        JSONObject extras = new JSONObject(uri.getQueryParameter(KakaoTalkLinkProtocol.EXTRAS));
        assertFalse(extras.has(KakaoTalkLinkProtocol.LCBA));
    }

    @Test
    public void kakaoLinkIntentWithLongUri() {
        try {
            core.kakaoLinkIntent(context, "app_key", KakaoLinkTestHelper.createLongKakaoLinkResponse("1234"));
            fail("Long uri should throw KakaoException with URI_LENGTH_EXCEEDED error type.");
        } catch (KakaoException e) {
            assertEquals(KakaoException.ErrorType.URI_LENGTH_EXCEEDED, e.getErrorType());
        }
    }

    @Test
    public void marketIntent() {
        Intent intent = core.kakaoTalkMarketIntent(context);
        Uri uri = intent.getData();
        assertNotNull(uri);
        assertEquals("market", uri.getScheme());
        assertEquals("details", uri.getHost());
        assertEquals("com.kakao.talk", uri.getQueryParameter("id"));
        String referrer = uri.getQueryParameter("referrer");
        assertNotNull(referrer);
        try {
            JSONObject referrerJson = new JSONObject(referrer);
            assertEquals(configuration.getKAHeader(), referrerJson.get(CommonProtocol.KA_HEADER_KEY));
            assertEquals(phaseInfo.appKey(), referrerJson.get(KakaoTalkLinkProtocol.APP_KEY));
            assertEquals(configuration.getAppVer(), referrerJson.get(KakaoTalkLinkProtocol.APP_VER));
            assertEquals(configuration.getPackageName(), referrerJson.get(KakaoTalkLinkProtocol.APP_PACKAGE));
        } catch (JSONException e) {
            fail("There was an exception parsing market referrer.");
        }
    }
}
