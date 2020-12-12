package com.kakao.kakaotalk.response.model;

import android.os.Parcel;

import com.kakao.test.common.KakaoTestCase;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 8. 9..
 */
public class ChatMemberTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void equals() {
        ChatMember member1 = createMember();
        ChatMember member2 = createMember();
        assertEquals(member1, member2);
    }

    @Test
    public void equalsWithNullFields() {
        ChatMember member1 = createMemberWithNullFields();
        ChatMember member2 = createMember();
        assertNotEquals(member1, member2);
        assertNotEquals(member2, member1);
    }

    @Test
    public void parcelable() {
        ChatMember member1 = createMember();
        testParcelable(member1);
        ChatMember member2 = createMemberWithNullFields();
        testParcelable(member2);
    }

    private ChatMember createMember() {
        return new ChatMember(1278457L,
                true,
                "닉네임1234",
                "http://alpha-th-p.talk.kakao.co.kr/th/talkp/wkaIzbfDmC/j3FmaOYQtHZp7kPmfkApkK/ux2keu_110x110_c.jpg",
                "soG2h7-IsJykkaSVpYm-i7mLvovb",
                false
        );
    }

    private ChatMember createMemberWithNullFields() {
        return new ChatMember(null,
                false,
                "닉네임1234",
                null,
                "soG2h7-IsJykkaSVpYm-i7mLvovb",
                false
        );
    }

    private void testParcelable(final ChatMember member) {
        Parcel parcel = Parcel.obtain();
        member.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ChatMember retrieved = ChatMember.CREATOR.createFromParcel(parcel);
        assertEquals(member, retrieved);
    }
}
