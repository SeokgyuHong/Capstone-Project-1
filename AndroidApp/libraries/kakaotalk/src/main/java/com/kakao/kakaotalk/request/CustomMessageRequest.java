package com.kakao.kakaotalk.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.kakaotalk.StringSet;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 4. 26..
 */

public abstract class CustomMessageRequest extends AuthorizedApiRequest {

    private final String templateId;
    private final Map<String, String> templateArgs;

    public CustomMessageRequest(@NonNull String templateId, @Nullable Map<String, String> args) {
        this.templateId = templateId;
        this.templateArgs = args;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put(StringSet.template_id, templateId);

        if (templateArgs != null && !templateArgs.isEmpty()) {
            params.put(StringSet.template_args, new JSONObject(templateArgs).toString());
        }
        return params;
    }
}
