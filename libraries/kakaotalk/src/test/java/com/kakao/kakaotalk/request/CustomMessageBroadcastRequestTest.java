package com.kakao.kakaotalk.request;

import android.net.Uri;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CustomMessageBroadcastRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void normal() {

        CustomMessageBroadcastRequest request = new CustomMessageBroadcastRequest(Collections.singletonList("1234"), "1234", null);
        Map<String, String> params = request.getParams();
        assertEquals("[\"1234\"]", params.get(StringSet.receiver_uuids));
        assertEquals("1234", params.get(StringSet.template_id));
        assertNull(params.get(StringSet.template_args));
    }

    @Test
    public void twoIds() throws JSONException {
        Map<String, String> args = new HashMap<>();
        args.put("key1", "value1");
        args.put("key2", "value2");
        List<String> uuids = Arrays.asList("1234", "5678");
        CustomMessageBroadcastRequest request = new CustomMessageBroadcastRequest(uuids, "1234", args);

        assertEquals("POST", request.getMethod());

        Uri uri = request.getUriBuilder().build();
        assertEquals(ServerProtocol.OPEN_TALK_MESSAGE_CUSTOM_V1_PATH, Objects.requireNonNull(uri.getPath()).substring(1));
        Map<String, String> params = request.getParams();
        assertEquals(new JSONArray(uuids).toString(), params.get(StringSet.receiver_uuids));
        assertEquals("1234", params.get(StringSet.template_id));
        assertEquals(new JSONObject().put("key1", "value1").put("key2", "value2").toString(),
                params.get(StringSet.template_args));
    }
}
