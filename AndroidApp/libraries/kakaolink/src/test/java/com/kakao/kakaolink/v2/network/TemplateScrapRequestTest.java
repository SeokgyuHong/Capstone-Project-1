package com.kakao.kakaolink.v2.network;

import com.kakao.test.common.KakaoTestCase;
import com.kakao.test.common.TestAppConfiguration;
import com.kakao.test.common.TestPhaseInfo;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevin.kang. Created on 2017. 3. 14..
 */
public class TemplateScrapRequestTest extends KakaoTestCase {
    private IConfiguration configuration;
    private PhaseInfo phaseInfo;

    @Before
    public void setup() {
        configuration = new TestAppConfiguration();
        phaseInfo = new TestPhaseInfo();
    }

    @Test
    public void testMethodIsGet() {
        TemplateScrapRequest request = new TemplateScrapRequest(phaseInfo, configuration, "url", null, null);
        assertEquals("GET", request.getMethod());
    }
}
