package com.kakao.kakaostory.response;

import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.kakaostory.response.model.MyStoryInfoTest;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONException;
import org.junit.Test;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class StoryPostResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void convert() throws JSONException {
        MyStoryInfo info = StoryPostResponse.CONVERTER.convert(getResponse());
        MyStoryInfoTest.validateMyStoryInfo(info);
    }

    String getResponse() throws JSONException {
        return MyStoryInfoTest.getMyStoryInfo().toString();
    }
}
