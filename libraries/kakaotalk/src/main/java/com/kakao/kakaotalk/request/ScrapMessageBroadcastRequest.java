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
 * @author kevin.kang. Created on 2019-07-12..
 */
public class ScrapMessageBroadcastRequest extends ScrapTemplateRequest {

    private final List<String> receiverUuids;

    public ScrapMessageBroadcastRequest(
            @NonNull List<String> receiverUuids,
            @NonNull String url,
            @Nullable String templateId,
            @Nullable Map<String, String> templateArgs
    ) {
        super(url, templateId, templateArgs);
        this.receiverUuids = receiverUuids;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.OPEN_TALK_MESSAGE_SCRAP_V1_PATH);
        return builder;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put(StringSet.receiver_uuids, new JSONArray(receiverUuids).toString());
        return params;
    }
}
