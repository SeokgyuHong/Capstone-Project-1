package com.kakao.kakaostory.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedRequest;
import com.kakao.kakaostory.StringSet;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class PostPhotoRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void create() {
        List<String> images = new ArrayList<>();
        String image1 = "https://developers.kakao.com/image1.png";
        String image2 = "https://developers.kakao.com/image2.jpeg";
        images.add(image1);
        images.add(image2);
        AuthorizedRequest request = new PostPhotoRequest(images, "content", PostRequest.StoryPermission.ONLY_ME,
                true, getParams(), null, getParams(), getParams());

        assertEquals("POST", request.getMethod());
        Uri uri = Uri.parse(request.getUrl());
        assertEquals(ServerProtocol.STORY_POST_PHOTO_PATH, uri.getPath().substring(1));
        assertEquals("content", request.getParams().get(StringSet.content));
        assertEquals("M", request.getParams().get(StringSet.permission));
        assertEquals("true", request.getParams().get(StringSet.enable_share));
        JSONArray array = new JSONArray();
        array.put(image1);
        array.put(image2);
        assertEquals(array.toString(), request.getParams().get(StringSet.image_url_list));
        assertEquals(getParams(), request.getParams().get(StringSet.android_exec_param));
        assertNull(request.getParams().get(StringSet.ios_exec_param));
        assertEquals(getParams(), request.getParams().get(StringSet.android_market_param));
        assertEquals(getParams(), request.getParams().get(StringSet.ios_market_param));
    }

    private String getParams() {
        return "key1=value1&key2=value2";
    }
}
