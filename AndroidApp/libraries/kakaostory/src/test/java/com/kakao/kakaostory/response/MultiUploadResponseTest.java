package com.kakao.kakaostory.response;

import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class MultiUploadResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void convert() {
        List<String> images = MultiUploadResponse.CONVERTER.convertList(getResponse());
        assertEquals("image1", images.get(0));
        assertEquals("image2", images.get(1));
        assertEquals("image3", images.get(2));
    }

    private String getResponse() {
        JSONArray array = new JSONArray();
        array.put("image1");
        array.put("image2");
        array.put("image3");
        return array.toString();
    }
}
