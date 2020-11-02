package com.kakao.kakaostory.response.model;

import android.os.Parcel;

import com.kakao.test.common.KakaoTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author kevin.kang. Created on 2017. 8. 23..
 */

public class StoryCommentTest extends KakaoTestCase {
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void parcelable() {
        testParcelableWithComment(new StoryComment("comment", new StoryActor("kevin", "profile_url")));
        testParcelableWithComment(new StoryComment(null, null));

    }

    private void testParcelableWithComment(final StoryComment comment) {
        Parcel parcel = Parcel.obtain();
        comment.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        StoryComment retrieved = StoryComment.CREATOR.createFromParcel(parcel);
        assertEquals(comment, retrieved);
    }
}
