package com.kakao.kakaostory.response;

import com.kakao.kakaostory.StringSet;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class LinkInfoResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void convert() throws JSONException {
        LinkInfoResponse response = LinkInfoResponse.CONVERTER.convert(getLinkInfo());
        validateLinkInfo(response);

        String linkInfoString = ResponseStringConverter.IDENTITY_CONVERTER.convert(getLinkInfo());
        response = LinkInfoResponse.CONVERTER.convert(linkInfoString);
        validateLinkInfo(response);
    }

    private void validateLinkInfo(LinkInfoResponse response) {
        assertEquals("url", response.getUrl());
        assertEquals("requested_url", response.getRequestedUrl());
        assertEquals("host", response.getHost());
        assertEquals("title", response.getTitle());

        List<String> images = response.getImageList();
        assertEquals(2, images.size());
        assertEquals("image1", images.get(0));
        assertEquals("image2", images.get(1));

        assertEquals("description", response.getDescription());
        assertEquals("section", response.getSection());
        assertEquals("type", response.getType());
    }

    String getLinkInfo() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringSet.url, "url");
        jsonObject.put(StringSet.requested_url, "requested_url");
        jsonObject.put(StringSet.host, "host");
        jsonObject.put(StringSet.title, "title");

        JSONArray images = new JSONArray();
        images.put("image1");
        images.put("image2");
        jsonObject.put(StringSet.image, images);
        jsonObject.put(StringSet.description, "description");
        jsonObject.put(StringSet.section, "section");
        jsonObject.put(StringSet.type, "type");
        return jsonObject.toString();
    }
}
