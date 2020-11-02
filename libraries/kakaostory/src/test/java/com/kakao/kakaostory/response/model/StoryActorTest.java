package com.kakao.kakaostory.response.model;

import android.os.Parcel;

import com.kakao.test.common.KakaoTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author kevin.kang. Created on 2017. 8. 23..
 */

public class StoryActorTest extends KakaoTestCase {
    private StoryActor actor;
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testParcelable() {
        actor = new StoryActor("kevin", "profile_url");
        Parcel parcel = Parcel.obtain();
        actor.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        StoryActor retrieved = StoryActor.CREATOR.createFromParcel(parcel);
        assertEquals(actor, retrieved);
    }
}
