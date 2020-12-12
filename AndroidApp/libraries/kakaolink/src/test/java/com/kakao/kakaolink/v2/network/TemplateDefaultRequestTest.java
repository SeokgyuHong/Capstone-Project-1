package com.kakao.kakaolink.v2.network;

import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.test.common.TestAppConfiguration;
import com.kakao.test.common.TestPhaseInfo;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;
import com.kakao.test.common.KakaoTestCase;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevin.kang. Created on 2017. 3. 14..
 */
public class TemplateDefaultRequestTest extends KakaoTestCase {
    private IConfiguration configuration;
    private PhaseInfo phaseInfo;

    @Before
    public void setup() {
        super.setup();
        configuration = new TestAppConfiguration();
        phaseInfo = new TestPhaseInfo();
    }
    @Test
    public void testMethodIsGet() {
        TemplateDefaultRequest request = new TemplateDefaultRequest(phaseInfo, configuration,
                FeedTemplate.newBuilder(ContentObject.newBuilder("title", "imageUrl",
                        LinkObject.newBuilder().build()).build()).build());
        assertEquals("GET", request.getMethod());
    }
}
