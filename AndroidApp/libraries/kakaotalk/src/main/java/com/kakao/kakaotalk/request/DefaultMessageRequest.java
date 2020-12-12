package com.kakao.kakaotalk.request;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.kakao.auth.common.MessageSendable;
import com.kakao.kakaotalk.StringSet;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ServerProtocol;

import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 5. 16..
 */
public class DefaultMessageRequest extends DefaultTemplateRequest {
    private MessageSendable receiverInfo;

    public DefaultMessageRequest(@NonNull final MessageSendable receiverInfo, @NonNull final TemplateParams templateParams) {
        super(templateParams);
        this.receiverInfo = receiverInfo;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put(StringSet.receiver_id, receiverInfo.getTargetId());
        params.put(StringSet.receiver_id_type, receiverInfo.getType());
        return params;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.TALK_MESSAGE_DEFAULT_V2_PATH);
        return builder;
    }
}
