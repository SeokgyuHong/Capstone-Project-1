package com.kakao.kakaotalk.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;

import org.json.JSONArray;

import java.util.List;

/**
 * @since 1.17.0
 *
 * @author kevin.kang. Created on 2019-03-19..
 */
public class PlusFriendsRequest extends AuthorizedApiRequest {
    private List<String> publicIds;

    public PlusFriendsRequest(List<String> publicIds) {
        this.publicIds = publicIds;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().path(ServerProtocol.TALK_PLUS_FRIENDS_PATH);
        if (publicIds != null && !publicIds.isEmpty()) {
            builder.appendQueryParameter(StringSet.plus_friend_public_ids, new JSONArray(publicIds).toString());
        }
        return builder;
    }
}
