package com.kakao.kakaostory.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedRequest;
import com.kakao.kakaostory.StringSet;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class PostNoteRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void create() {
        AuthorizedRequest request = new PostNoteRequest("content", PostRequest.StoryPermission.FRIEND,
                false, getParams(), getParams(), getParams(), null);
        assertEquals("POST", request.getMethod());
        Uri uri = Uri.parse(request.getUrl());
        assertEquals(ServerProtocol.STORY_POST_NOTE_PATH, uri.getPath().substring(1));
        assertEquals("content", request.getParams().get(StringSet.content));
        assertEquals("F", request.getParams().get(StringSet.permission));
        assertEquals("false", request.getParams().get(StringSet.enable_share));
        assertEquals(getParams(), request.getParams().get(StringSet.android_exec_param));
        assertEquals(getParams(), request.getParams().get(StringSet.android_market_param));
        assertEquals(getParams(), request.getParams().get(StringSet.ios_exec_param));
        assertNull(request.getParams().get(StringSet.ios_market_param));
    }

    private String getParams() {
        return "key1=value1&key2=value2";
    }
}
