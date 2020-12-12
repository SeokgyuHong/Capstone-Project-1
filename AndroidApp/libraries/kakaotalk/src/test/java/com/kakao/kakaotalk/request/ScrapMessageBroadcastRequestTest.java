package com.kakao.kakaotalk.request;

import android.net.Uri;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author kevin.kang. Created on 2019-07-12..
 */
public class ScrapMessageBroadcastRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void normal() {
        String url = "https://developers.kakao.com";
        List<String> uuids = Arrays.asList("1234", "5678");
        ScrapMessageBroadcastRequest request = new ScrapMessageBroadcastRequest(
                uuids, url, null, null);
        Map<String, String> params = request.getParams();
        assertEquals("POST", request.getMethod());

        Uri uri = request.getUriBuilder().build();
        assertEquals(ServerProtocol.OPEN_TALK_MESSAGE_SCRAP_V1_PATH, Objects.requireNonNull(uri.getPath()).substring(1));
        assertEquals(new JSONArray(uuids).toString(), params.get(StringSet.receiver_uuids));
        assertEquals(url, params.get(StringSet.request_url));
        assertFalse(params.containsKey(StringSet.template_id));
        assertFalse(params.containsKey(StringSet.template_args));

    }
}
