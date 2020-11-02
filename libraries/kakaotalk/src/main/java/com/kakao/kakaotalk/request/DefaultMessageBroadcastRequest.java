package com.kakao.kakaotalk.request;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.kakao.kakaotalk.StringSet;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ServerProtocol;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * 디폴트 템플릿을 사용한 동보 메시지 전송
 *
 * @author kevinkang
 */
public class DefaultMessageBroadcastRequest extends DefaultTemplateRequest {
    private List<String> receiverUuids;

    public DefaultMessageBroadcastRequest(@NonNull List<String> receiverUuids, @NonNull TemplateParams templateParams) {
        super(templateParams);
        this.receiverUuids = receiverUuids;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.OPEN_TALK_MESSAGE_DEFAULT_V1_PATH);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put(StringSet.receiver_uuids, new JSONArray(receiverUuids).toString());
        return params;
    }
}
