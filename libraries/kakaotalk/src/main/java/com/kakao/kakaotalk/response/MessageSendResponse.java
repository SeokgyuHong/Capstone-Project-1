package com.kakao.kakaotalk.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 메시지 오픈 API 응답 모델
 *
 * @author kevin.kang
 * @since 1.20.0
 */
public class MessageSendResponse {
    @Nullable
    private List<String> successfulReceiverUuids;
    @Nullable
    private List<MessageFailureInfo> failureInfo;

    @SuppressWarnings("WeakerAccess")
    MessageSendResponse(@Nullable List<String> successfulUuids, @Nullable List<MessageFailureInfo> failureInfo) {
        this.successfulReceiverUuids = successfulUuids;
        this.failureInfo = failureInfo;
    }

    /**
     * 성공한 사용자 아이디 배열
     *
     * @return array of ids of users who succeeded to receive the message
     */
    @Nullable
    @SuppressWarnings("WeakerAccess")
    public List<String> successfulReceiverUuids() {
        return successfulReceiverUuids;
    }

    /**
     * 실패 정보
     * <p>
     * - 요청 수신자가 1명일 때 실패시 failure_info 없이 에러 코드로 전달
     * - 요청 수신자가 2명이상일 때 실패시 'http status code: 200' 과 함께 실패한 사용자 정보를 failure_info 로 전달
     */
    @Nullable
    @SuppressWarnings({"unused", "WeakerAccess"})
    public List<MessageFailureInfo> failureInfo() {
        return failureInfo;
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            if (successfulReceiverUuids != null)
                object.put(StringSet.successful_receiver_uuids, new JSONArray(successfulReceiverUuids));
            if (failureInfo != null) {
                JSONArray array = new JSONArray();
                for (MessageFailureInfo info : failureInfo) {
                    array.put(new JSONObject(info.toString()));
                }
                object.put(StringSet.failure_info, array);
            }
            return object.toString();
        } catch (JSONException e) {
            Logger.w("There was an error parsing MessageSendResponse: %s", e.toString());
            return object.toString();
        }
    }


    public static final ResponseStringConverter<MessageSendResponse> CONVERTER = new ResponseStringConverter<MessageSendResponse>() {
        @Override
        public MessageSendResponse convert(String o) throws ResponseBody.ResponseBodyException {
            ResponseBody body = new ResponseBody(o);
            List<String> successfulUuids = body.has(StringSet.successful_receiver_uuids) ?
                    IDENTITY_CONVERTER.convertList(body.getJSONArray(StringSet.successful_receiver_uuids)) :
                    null;
            List<MessageFailureInfo> failureInfos = body.has(StringSet.failure_info) ?
                    MessageFailureInfo.CONVERTER.convertList(body.getJSONArray(StringSet.failure_info)) :
                    null;
            return new MessageSendResponse(successfulUuids, failureInfos);
        }
    };
}
