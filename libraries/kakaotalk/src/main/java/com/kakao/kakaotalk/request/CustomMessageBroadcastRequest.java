package com.kakao.kakaotalk.request;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * 커스텀 템플릿을 이용한 동보 메시지 전송
 *
 * @author kevinkang
 */
public class CustomMessageBroadcastRequest extends CustomMessageRequest {
    private List<String> receiverUuids;

    public CustomMessageBroadcastRequest(@NonNull List<String> receiverUuids, @NonNull String templateId, @Nullable Map<String, String> args) {
        super(templateId, args);
        this.receiverUuids = receiverUuids;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.OPEN_TALK_MESSAGE_CUSTOM_V1_PATH);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put(StringSet.receiver_uuids, new JSONArray(receiverUuids).toString());
        return params;
    }
}
