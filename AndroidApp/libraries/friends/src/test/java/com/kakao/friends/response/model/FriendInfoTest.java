package com.kakao.friends.response.model;

import android.os.Parcel;

import com.kakao.test.common.KakaoTestCase;
import com.kakao.util.OptionalBoolean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author kevin.kang. Created on 2017. 8. 23..
 */

public class FriendInfoTest extends KakaoTestCase {
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void parcelable() {
        FriendInfo.FriendRelation friendRelation = new FriendInfo.FriendRelation(FriendInfo.Relation.FRIEND, FriendInfo.Relation.FRIEND);
        testParcelableWithInfo(new FriendInfo("1", 1, 1, true,
                "kevin", null, OptionalBoolean.TRUE, "android", true, friendRelation));

        FriendInfo.FriendRelation relation2 = new FriendInfo.FriendRelation(null, null);
        testParcelableWithInfo(new FriendInfo("1", 1, 0, false,
                "kevin", null, OptionalBoolean.FALSE, null, true, relation2));

    }

    private void testParcelableWithInfo(final FriendInfo friendInfo) {
        Parcel parcel = Parcel.obtain();
        friendInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        FriendInfo retrieved = FriendInfo.CREATOR.createFromParcel(parcel);
        assertEquals(friendInfo, retrieved);
    }
}
