package com.kakao.kakaostory.response.model;

import android.os.Parcel;

import com.kakao.kakaostory.StringSet;
import com.kakao.test.common.KakaoTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests MyStoryInfo creation and conversion from JSON. This is not a unit test and tests creation
 * of all child elements such as {@link MyStoryImageInfo}, {@link StoryLike}, and {@link StoryComment}.
 *
 * @author kevin.kang. Created on 2017. 8. 23..
 */

public class MyStoryInfoTest extends KakaoTestCase {
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void parcelable() {
        List<MyStoryImageInfo> imageInfoList =
                Collections.singletonList(new MyStoryImageInfo("xLarge", "large", "medium",
                        "small", "original"));
        List<StoryComment> commentList =
                Collections.singletonList(new StoryComment("comment",
                        new StoryActor("kevin", "profile_url")));
        List<StoryLike> likeList =
                Collections.singletonList(new StoryLike(StoryLike.Emotion.HAPPY,
                        new StoryActor("kevin", "profile_url")));
        testParcelableWithStoryInfo(new MyStoryInfo("id", "url", "type", "date", 1, 1, "content", "permission",
                imageInfoList, commentList, likeList));
        testParcelableWithStoryInfo(new MyStoryInfo("id", "url", "type", "date", 1, 1, "content", "permission",
                null, null, likeList));

    }

    @Test
    public void converter() throws JSONException {
        String stringData = getMyStoryInfo().toString();
        MyStoryInfo info = MyStoryInfo.CONVERTER.convert(stringData);
        validateMyStoryInfo(info);
    }

    @Test
    public void convertList() throws JSONException {
        JSONArray storyInfos = getMyStoryInfos();
        List<MyStoryInfo> infos = MyStoryInfo.CONVERTER.convertList(storyInfos.toString());
        assertEquals(3, infos.size());

        for (MyStoryInfo info : infos) {
            validateMyStoryInfo(info);
        }
    }

    public static void validateMyStoryInfo(MyStoryInfo info) {
        assertEquals("1234", info.getId());
        assertEquals("https://sample.com", info.getUrl());
        assertEquals("PHOTO", info.getMediaType());
        assertEquals("created_at", info.getCreatedAt());
        assertEquals(2, info.getCommentCount());
        assertEquals(3, info.getLikeCount());
        assertEquals("content", info.getContent());

        List<MyStoryImageInfo> imageInfos = info.getImageInfoList();
        assertEquals(1, imageInfos.size());
        MyStoryImageInfo imageInfo = imageInfos.get(0);

        assertEquals("xlarge", imageInfo.getXlarge());
        assertEquals("large", imageInfo.getLarge());
        assertEquals("medium", imageInfo.getMedium());
        assertEquals("small", imageInfo.getSmall());
        assertEquals("original", imageInfo.getOriginal());

        List<StoryLike> likes = info.getLikeList();
        assertEquals(1, likes.size());
        StoryLike like = likes.get(0);
        assertEquals(StoryLike.Emotion.CHEER_UP, like.getEmoticon());
        assertEquals("display_name", like.getActor().getDisplayName());
        assertEquals("url", like.getActor().getProfileThumbnailUrl());

        List<StoryComment> comments = info.getCommentList();
        assertEquals(1, comments.size());
        StoryComment comment = comments.get(0);
        assertEquals("text", comment.getText());
        assertEquals("display_name", comment.getWriter().getDisplayName());
        assertEquals("url", comment.getWriter().getProfileThumbnailUrl());
    }

    private void testParcelableWithStoryInfo(final MyStoryInfo storyInfo) {
        Parcel parcel = Parcel.obtain();
        storyInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        MyStoryInfo retrieved = MyStoryInfo.CREATOR.createFromParcel(parcel);
        assertEquals(storyInfo, retrieved);
    }

    public static JSONObject getMyStoryInfo() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringSet.id, "1234");
        jsonObject.put(StringSet.url, "https://sample.com");
        jsonObject.put(StringSet.media_type, "PHOTO");
        jsonObject.put(StringSet.created_at, "created_at");
        jsonObject.put(StringSet.comment_count, 2);
        jsonObject.put(StringSet.like_count, 3);
        jsonObject.put(StringSet.content, "content");
        jsonObject.put(StringSet.media, getMyStoryImageInfoList());
        jsonObject.put(StringSet.likes, getStoryLikeList());
        jsonObject.put(StringSet.comments, getStoryCommentList());
        return jsonObject;
    }

    public static JSONArray getMyStoryInfos() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(getMyStoryInfo());
        array.put(getMyStoryInfo());
        array.put(getMyStoryInfo());
        return array;
    }

    public static JSONObject getWrongJSONObject() {
        return null;
    }

    public static JSONArray getMyStoryImageInfoList() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringSet.xlarge, "xlarge");
        jsonObject.put(StringSet.large, "large");
        jsonObject.put(StringSet.medium, "medium");
        jsonObject.put(StringSet.small, "small");
        jsonObject.put(StringSet.original, "original");

        JSONArray array = new JSONArray();
        array.put(jsonObject);
        return array;
    }

    public static JSONArray getStoryLikeList() throws JSONException {
        JSONObject likeJson = new JSONObject();
        likeJson.put(StringSet.emotion, StoryLike.Emotion.CHEER_UP.papiEmotion);
        likeJson.put(StringSet.actor, getStoryActor());
        JSONArray array = new JSONArray();
        array.put(likeJson);
        return array;
    }

    public static JSONArray getStoryCommentList() throws JSONException {
        JSONObject commentJson = new JSONObject();
        commentJson.put(StringSet.text, "text");
        commentJson.put(StringSet.writer, getStoryActor());
        JSONArray array = new JSONArray();
        array.put(commentJson);
        return array;
    }

    public static JSONObject getStoryActor() throws JSONException {
        JSONObject actorJson = new JSONObject();
        actorJson.put(StringSet.display_name, "display_name");
        actorJson.put(StringSet.profile_thumbnail_url, "url");
        return actorJson;
    }
}
