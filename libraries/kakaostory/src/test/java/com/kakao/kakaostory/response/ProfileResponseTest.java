package com.kakao.kakaostory.response;

import com.kakao.kakaostory.StringSet;
import com.kakao.kakaostory.response.model.StoryProfile;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 10..
 */

public class ProfileResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void convert() throws JSONException {
        ProfileResponse response = ProfileResponse.CONVERTER.convert(getProfile().toString());
        StoryProfile profile = response.getProfile();
        assertNotNull(profile);
        assertEquals("nickname", profile.getNickName());
        assertEquals("url", profile.getProfileImageURL());
        assertEquals("thumb_url", profile.getThumbnailURL());
        assertEquals("bg_url", profile.getBgImageURL());
        assertEquals("permalink", profile.getPermalink());
        assertEquals("0115", profile.getBirthday());
        assertEquals(ProfileResponse.BirthdayType.SOLAR, profile.getBirthdayType());
    }

    @Test
    public void parcelable() {
    }

    public JSONObject getProfile() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringSet.nickName, "nickname");
        jsonObject.put(StringSet.profileImageURL, "url");
        jsonObject.put(StringSet.thumbnailURL, "thumb_url");
        jsonObject.put(StringSet.bgImageURL, "bg_url");
        jsonObject.put(StringSet.permalink, "permalink");
        jsonObject.put(StringSet.birthday, "0115");
        jsonObject.put(StringSet.birthdayType, "SOLAR");
        return jsonObject;
    }
}
