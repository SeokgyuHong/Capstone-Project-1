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

public class GetMyStoryListRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void create() {
        AuthorizedRequest request = new GetMyStoryListRequest("1234");
        assertEquals("GET", request.getMethod());
        Uri uri = Uri.parse(request.getUrl());
        assertEquals(ServerProtocol.STORY_ACTIVITIES_PATH, uri.getPath().substring(1));
        assertEquals("1234", uri.getQueryParameter(StringSet.last_id));

    }
}
