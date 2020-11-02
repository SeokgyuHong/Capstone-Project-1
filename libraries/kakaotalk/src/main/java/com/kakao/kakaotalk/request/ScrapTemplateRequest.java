package com.kakao.kakaotalk.request;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang. Created on 2019-07-12..
 */
public class ScrapTemplateRequest extends AuthorizedApiRequest {
    private final String url;
    private final String templateId;
    private final Map<String, String> templateArgs;

    public ScrapTemplateRequest(
            @NonNull final String url,
            @Nullable final String templateId,
            @Nullable final Map<String, String> templateArgs) {
        this.url = url;
        this.templateId = templateId;
        this.templateArgs = templateArgs;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.TALK_MEMO_SCRAP_V2_PATH);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put(StringSet.request_url, url);
        if (templateId != null) {
            params.put(StringSet.template_id, templateId);
        }
        if (templateArgs != null && !templateArgs.isEmpty()) {
            params.put(StringSet.template_args, new JSONObject(templateArgs).toString());
        }
        return params;
    }
}
