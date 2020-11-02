package com.kakao.kakaotalk.request;

import android.net.Uri;

import com.kakao.message.template.TemplateParams;
import com.kakao.network.ServerProtocol;

/**
 * @author kevin.kang. Created on 2017. 5. 16..
 */

public class DefaultMemoRequest extends DefaultTemplateRequest {
    public DefaultMemoRequest(final TemplateParams templateParams) {
        super(templateParams);
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.TALK_MEMO_DEFAULT_V2_PATH);
        return builder;
    }
}
