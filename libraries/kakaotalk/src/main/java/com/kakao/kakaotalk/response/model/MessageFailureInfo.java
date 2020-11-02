package com.kakao.kakaotalk.response.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.response.JSONObjectConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 메시지 전송 실패 정보
 *
 * @author kevin.kang
 * @since 1.20.0
 */
public class MessageFailureInfo {
    @Nullable
    private final Integer code;
    @Nullable
    private final String msg;
    @Nullable
    private final List<String> receiverUuids;

    @SuppressWarnings("WeakerAccess")
    public MessageFailureInfo(@Nullable Integer code, @Nullable String msg, @Nullable List<String> receiverUuids) {
        this.code = code;
        this.msg = msg;
        this.receiverUuids = receiverUuids;
    }

    /**
     * 에러 코드
     *
     * @return error code
     */
    @Nullable
    public Integer code() {
        return code;
    }

    /**
     * 에러 메시지
     *
     * @return error message
     */
    @Nullable
    public String msg() {
        return msg;
    }

    /**
     * 해당 에러코드로 실패한 사용자 uuid 배열
     *
     * @return array of uuids of users who failed to receive the message
     */
    @Nullable
    public List<String> receiverUuids() {
        return receiverUuids;
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            return object.put(com.kakao.network.StringSet.code, code)
                    .put(com.kakao.network.StringSet.msg, msg)
                    .put(StringSet.receiver_ids, new JSONArray(receiverUuids)).toString();
        } catch (JSONException e) {
            return object.toString();
        }
    }

    public static final JSONObjectConverter<MessageFailureInfo> CONVERTER = new JSONObjectConverter<MessageFailureInfo>() {
        @Override
        public MessageFailureInfo convert(JSONObject data) {
            try {
                Integer code = data.has(com.kakao.network.StringSet.code) ? data.getInt(com.kakao.network.StringSet.code) : null;
                String msg = data.has(com.kakao.network.StringSet.msg) ? data.getString(com.kakao.network.StringSet.msg) : null;
                List<String> failedUuids = data.has(StringSet.receiver_uuids) ?
                        ResponseStringConverter.IDENTITY_CONVERTER.convertList(data.getJSONArray(StringSet.receiver_uuids)) :
                        null;
                return new MessageFailureInfo(code, msg, failedUuids);
            } catch (JSONException e) {
                throw new ResponseBody.ResponseBodyException(e);
            }
        }
    };
}
