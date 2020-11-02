package com.kakao.kakaostory.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class PostLinkRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void create() {
        AuthorizedRequest request = new PostLinkRequest("content", "{\"url\":\"https://developers.kakao.com/\"}",
                PostRequest.StoryPermission.PUBLIC, true, getParams(), getParams(), getParams(), getParams());

        assertEquals("POST", request.getMethod());
        Uri uri = Uri.parse(request.getUrl());
        assertEquals(ServerProtocol.STORY_POST_LINK_PATH, uri.getPath().substring(1));

    }


    private String getParams() {
        return "key1=value1&key2=value2";
    }
}
