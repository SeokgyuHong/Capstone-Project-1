package com.kakao.kakaotalk.response.model;

import androidx.annotation.NonNull;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.response.JSONObjectConverter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 유저와 카카오톡 채널의 관계 정보
 *
 * @author kevin.kang. Created on 2019-03-19..
 * @since 1.17.0
 */
public class PlusFriendInfo {

    private final String uuid;
    private final String encodedId;
    private final PlusFriendRelation relation;
    private final String updatedAt;

    PlusFriendInfo(final @NonNull JSONObject body) {
        uuid = body.optString(StringSet.plus_friend_uuid, null);
        encodedId = body.optString(StringSet.plus_friend_public_id, null);
        relation = body.has(StringSet.relation) ?
                PlusFriendRelation.fromName(body.optString(StringSet.relation, null)) : null;
        updatedAt = body.optString(StringSet.updated_at, null);
    }

    public static final JSONObjectConverter<PlusFriendInfo> CONVERTER = new JSONObjectConverter<PlusFriendInfo>() {
        @Override
        public PlusFriendInfo convert(JSONObject data) {
            return new PlusFriendInfo(data);
        }
    };

    /**
     * plus friend uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * encoded plus friend id (ex. https://pf.kakao.com/${plusFriendId})
     */
    public String getEncodedId() {
        return encodedId;
    }

    /**
     * 카카오톡 채널과의 관계. 추가된 상태 / 관계 없음.
     *
     * @see PlusFriendRelation
     */
    public PlusFriendRelation getRelation() {
        return relation;
    }

    /**
     * relation 변경 시각 (현재는 ADDED 상태의 친구 추가시각만 의미)
     * RFC3339 internet date/time format
     * (yyyy-mm-dd'T'HH:mm:ss'Z', yyyy-mm-dd'T'HH:mm:ss'+'HH:mm, yyyy-mm-dd'T'HH:mm:ss'-'HH:mm 가능)
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        try {
            return new JSONObject().put(StringSet.plus_friend_uuid, uuid)
                    .put(StringSet.relation, relation.getName())
                    .put(StringSet.updated_at, updatedAt).toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
