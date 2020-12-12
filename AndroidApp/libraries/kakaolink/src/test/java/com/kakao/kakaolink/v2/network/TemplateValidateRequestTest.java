package com.kakao.kakaolink.v2.network;

import android.net.Uri;

import com.kakao.test.common.TestAppConfiguration;
import com.kakao.test.common.TestPhaseInfo;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2016. 11. 28..
 */
public class TemplateValidateRequestTest extends KakaoTestCase {
    private IConfiguration configuration;
    private PhaseInfo phaseInfo;
    private String templateId = "12345";
    private Map<String, String> templateArgs;

    @Before
    public void setup() {
        super.setup();
        templateArgs = new HashMap<>();
        phaseInfo = new TestPhaseInfo();
        configuration = new TestAppConfiguration();
    }

    @Test
    public void testMethodIsGet() {
        TemplateValidateRequest request = new TemplateValidateRequest(phaseInfo, configuration, templateId, templateArgs);
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetHeaders() {
        TemplateValidateRequest request = new TemplateValidateRequest(phaseInfo, configuration, templateId, templateArgs);
        assertNotNull(request.getHeaders());
        assertEquals(5, request.getHeaders().size());
    }

    @Test
    public void testGetUrlWithNullTemplateArgs() {
        testGetParamsSize(2);
    }

    @Test
    public void testGetUrlWithEmptyTemplateArgs() {
        testGetParamsSize(2);
    }

    @Test
    public void testGetUrlWithOneTemplateArgs() {
        templateArgs.put("name", "Kevin Kang");
        testGetParamsSize(3);
    }

    @Test
    public void testGetUrlWithTwoTemplateArgs() {
        templateArgs.put("name", "Kevin Kang");
        templateArgs.put("age", "26");
        testGetParamsSize(3);
    }

//    @Test
//    public void getUrlWithSpecialCharacters() throws JSONException {
//        String expectedName = "Kevin%20%22%ED%85%8C%EC%8A%A4%ED%8A%B8%22.";
//        templateArgs.put("name", expectedName);
//        TemplateValidateRequest request = new TemplateValidateRequest(configuration, templateId, templateArgs);
//        String url = request.getUrl();
//
//        Uri uri = Uri.parse(url);
//        String ta = uri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS);
//
//        JSONObject jsonObject = new JSONObject(ta);
//        String name = jsonObject.getString("name");
//        assertEquals(expectedName, name);
//        Logger.e("url: " + url);
//        Logger.e("Uri.decode(url): " + Uri.decode(url));
//        Uri decodedUri = Uri.parse(Uri.decode(url));
//        jsonObject = new JSONObject(decodedUri.getQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS));
//        String decodedName = jsonObject.getString("name");
//
//        assertEquals(expectedName, decodedName);
//    }

    private void testGetParamsSize(final int paramsSize) {
        TemplateValidateRequest request = new TemplateValidateRequest(phaseInfo, configuration, templateId, templateArgs);

        String url = request.getUrl();
        assertNotNull(url);
        Uri uri = Uri.parse(url);
        assertNotNull(uri);

        assertEquals("https", uri.getScheme());
        assertEquals(ServerProtocol.apiAuthority(), uri.getAuthority());
        assertEquals("/" + ServerProtocol.LINK_TEMPLATE_VALIDATE_PATH, uri.getPath());

        assertEquals(paramsSize, uri.getQueryParameterNames().size());
    }
}
