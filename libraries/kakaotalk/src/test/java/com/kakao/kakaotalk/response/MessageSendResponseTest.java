package com.kakao.kakaotalk.response;

import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MessageSendResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void withoutFailureInfos() throws JSONException {

        JSONObject jsonObject = new JSONObject().put(StringSet.successful_receiver_uuids,
                new JSONArray().put("3651")).put("result_code", 0);

        MessageSendResponse response = MessageSendResponse.CONVERTER.convert(jsonObject.toString());
        assertNotNull(response.successfulReceiverUuids());
        assertArrayEquals(Collections.singletonList("3651").toArray(), Objects.requireNonNull(response.successfulReceiverUuids()).toArray());
    }

    @Test
    public void withFailureInfos() throws JSONException {
        Integer code = -532;
        String msg = "daily message limit per sender has been exceeded.";
        String id1 = "26490";
        String id2 = "10050";
        JSONObject jsonObject = new JSONObject().put(
                StringSet.failure_info,
                new JSONArray().put(
                        new JSONObject().put(com.kakao.network.StringSet.code, code)
                                .put(com.kakao.network.StringSet.msg, msg)
                                .put(StringSet.receiver_uuids, new JSONArray().put(id2))
                )
        ).put(StringSet.successful_receiver_uuids, new JSONArray().put(id1)).put(StringSet.result_code, -500);

        MessageSendResponse response = MessageSendResponse.CONVERTER.convert(jsonObject.toString());
        assertNotNull(response.successfulReceiverUuids());
        assertNotNull(response.failureInfo());
        assertEquals(1, Objects.requireNonNull(response.failureInfo()).size());
        MessageFailureInfo failureInfo = Objects.requireNonNull(response.failureInfo()).get(0);
        assertEquals(code.intValue(), failureInfo.code().intValue());
        assertEquals(msg, failureInfo.msg());
        List<String> ids = failureInfo.receiverUuids();
        assertEquals(id2, ids.get(0));

//        assertEquals(id2, ids.get(1));
    }

}
