package com.kakao.kakaotalk.response;

import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.response.model.PlusFriendInfo;
import com.kakao.kakaotalk.response.model.PlusFriendRelation;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2019-03-26..
 */
public class PlusFriendsResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void nullFriends() throws JSONException {
        JSONObject response = new JSONObject()
                .put(StringSet.user_id, 1376016924426879191L)
                .put(StringSet.plus_friends, null);
        PlusFriendsResponse parsed = PlusFriendsResponse.CONVERTER.convert(response.toString());
        assertEquals(parsed.getUserId().longValue(), 1376016924426879191L);
        assertNull(parsed.getPlusFriends());
    }

    @Test
    public void emptyFriends() throws JSONException {
        JSONObject response = new JSONObject()
                .put(StringSet.user_id, 1376016924426879191L)
                .put(StringSet.plus_friends, new JSONArray());
        PlusFriendsResponse parsed = PlusFriendsResponse.CONVERTER.convert(response.toString());
        assertEquals(parsed.getUserId().longValue(), 1376016924426879191L);
        assertNotNull(parsed.getPlusFriends());
        assertEquals(0, parsed.getPlusFriends().size());
    }

    @Test
    public void twoFriends() throws JSONException {
        JSONObject first = new JSONObject()
                .put(StringSet.plus_friend_uuid, "@카카오톡채널")
                .put(StringSet.plus_friend_public_id, "")
                .put(StringSet.relation, "ADDED")
                .put(StringSet.updated_at, "2018-03-14T05:25:01Z");
        JSONObject second = new JSONObject()
                .put(StringSet.plus_friend_uuid, "@알파카카오톡채널")
                .put(StringSet.plus_friend_public_id, "_Brxjem")
                .put(StringSet.relation, "NONE");
        JSONArray friends = new JSONArray().put(first).put(second);
        JSONObject response = new JSONObject()
                .put(StringSet.user_id, 1376016924426879191L)
                .put(StringSet.plus_friends, friends);
        PlusFriendsResponse parsed = PlusFriendsResponse.CONVERTER.convert(response.toString());

        assertNotNull(parsed.getPlusFriends());
        assertEquals(2, parsed.getPlusFriends().size());

        PlusFriendInfo firstInfo = parsed.getPlusFriends().get(0);
        PlusFriendInfo secondInfo = parsed.getPlusFriends().get(1);

        assertEquals(PlusFriendRelation.ADDED, firstInfo.getRelation());
        assertNotNull(firstInfo.getUpdatedAt());
        assertEquals(PlusFriendRelation.NONE, secondInfo.getRelation());
        assertNull(secondInfo.getUpdatedAt());
    }
}
