package com.kakao.kakaostory.response;

import com.kakao.kakaostory.StringSet;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 10..
 */

public class CheckStoryUserResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void convert() throws JSONException {
        boolean user = CheckStoryUserResponse.CONVERTER.convert(getResponse(true));
        assertTrue(user);
        user = CheckStoryUserResponse.CONVERTER.convert(getResponse(false));
        assertFalse(user);
    }

    private String getResponse(boolean isUser) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringSet.isStoryUser, isUser);
        return jsonObject.toString();
    }
}
