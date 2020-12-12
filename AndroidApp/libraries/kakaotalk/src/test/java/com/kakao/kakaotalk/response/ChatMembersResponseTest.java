package com.kakao.kakaotalk.response;

import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.response.model.ChatMember;
import com.kakao.test.common.KakaoTestCase;
import com.kakao.util.OptionalBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 8. 8..
 */
public class ChatMembersResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void convert() throws JSONException {
        String responseString = getResponseString();
        ChatMembersResponse response = ChatMembersResponse.CONVERTER.convert(responseString);
        assertEquals(4, (long) response.activeMembersCount());
        assertEquals(3, (long) response.activeFriendsCount());
        assertEquals("MultiChat", response.type());

        assertEquals(3, response.members().size());

        ChatMember member1 = response.members().get(0);
        assertSame(member1.appRegistered(), OptionalBoolean.FALSE);
        assertEquals("닉네임", member1.nickname());
        assertEquals("soOwibqIsISon6-fr52snqeXu4y5i7mMudI", member1.uuid());

        ChatMember member2 = response.members().get(1);
        assertSame(member2.appRegistered(), OptionalBoolean.FALSE);
        assertEquals("닉네임12", member2.nickname());
        assertEquals("http://alpha-th-p.talk.kakao.co.kr/th/talkp/wkaLcfXVXV/X6GJpmqkTwkPNgPY41S231/8vifdm_110x110_c.jpg", member2.thumbnailImage());
        assertEquals("soW9ir-Iu5egkKCQoJKrnqmFsoe1h7KH3A", member2.uuid());

        ChatMember member3 = response.members().get(2);
        assertSame(member3.appRegistered(), OptionalBoolean.TRUE);
        assertEquals(1278457, (long) member3.id());
        assertEquals("닉네임1234", member3.nickname());
        assertEquals("http://alpha-th-p.talk.kakao.co.kr/th/talkp/wkaIzbfDmC/j3FmaOYQtHZp7kPmfkApkK/ux2keu_110x110_c.jpg", member3.thumbnailImage());
        assertEquals("soG2h7-IsJykkaSVpYm-i7mLvovb", member3.uuid());
    }

    private String getResponseString() throws JSONException  {
        JSONObject response = new JSONObject();
        response.put(StringSet.active_members_count, 4);
        response.put(StringSet.active_friends_count, 3);
        response.put(StringSet.type, "MultiChat");

        JSONArray members = new JSONArray();

        JSONObject member1 = new JSONObject();
        member1.put(StringSet.app_registered, false)
                .put(StringSet.nickname, "닉네임")
                .put(StringSet.uuid, "soOwibqIsISon6-fr52snqeXu4y5i7mMudI");

        JSONObject member2 = new JSONObject();
        member2.put(StringSet.app_registered, false)
                .put(StringSet.nickname, "닉네임12")
                .put(StringSet.thumbnail_image, "http://alpha-th-p.talk.kakao.co.kr/th/talkp/wkaLcfXVXV/X6GJpmqkTwkPNgPY41S231/8vifdm_110x110_c.jpg")
                .put(StringSet.uuid, "soW9ir-Iu5egkKCQoJKrnqmFsoe1h7KH3A");

        JSONObject members3 = new JSONObject();
        members3.put(StringSet.id, 1278457)
                .put(StringSet.app_registered, true)
                .put(StringSet.nickname, "닉네임1234")
                .put(StringSet.thumbnail_image, "http://alpha-th-p.talk.kakao.co.kr/th/talkp/wkaIzbfDmC/j3FmaOYQtHZp7kPmfkApkK/ux2keu_110x110_c.jpg")
                .put(StringSet.uuid, "soG2h7-IsJykkaSVpYm-i7mLvovb");

        members.put(member1).put(member2).put(members3);
        response.put(StringSet.members, members);
        return response.toString();
    }
}
