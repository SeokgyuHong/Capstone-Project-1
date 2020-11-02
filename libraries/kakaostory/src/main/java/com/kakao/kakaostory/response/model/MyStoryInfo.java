/**
 * Copyright 2014-2015 Kakao Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.kakaostory.response.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.kakao.kakaostory.StringSet;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leoshin, created at 15. 7. 31..
 */
public class MyStoryInfo extends JSONObjectResponse implements Parcelable {
    private final String id;
    private final String url;
    private final String mediaType;
    private final String createdAt;
    private final int commentCount;
    private final int likeCount;
    private final String content;
    private final String permission;
    private List<MyStoryImageInfo> imageInfoList;
    private List<StoryComment> commentList;
    private List<StoryLike> likeList;

    public MyStoryInfo(String stringData) {
        super(stringData);
        this.id = getBody().optString(StringSet.id, null);
        this.url = getBody().optString(StringSet.url, null);
        this.mediaType = getBody().optString(StringSet.media_type, null);
        this.createdAt = getBody().optString(StringSet.created_at, null);
        this.commentCount = getBody().optInt(StringSet.comment_count, 0);
        this.likeCount = getBody().optInt(StringSet.like_count, 0);
        this.content = getBody().optString(StringSet.content, null);
        this.permission = getBody().optString(StringSet.permission, null);
        this.imageInfoList = MyStoryImageInfo.CONVERTER.convertList(getBody().optJSONArray(StringSet.media, null));
        this.likeList = StoryLike.CONVERTER.convertList(getBody().optJSONArray(StringSet.likes, null));
        this.commentList = StoryComment.CONVERTER.convertList(getBody().optJSONArray(StringSet.comments, null));
    }

    MyStoryInfo(final Parcel parcel) {
        id = parcel.readString();
        url = parcel.readString();
        mediaType = parcel.readString();
        createdAt = parcel.readString();
        commentCount = parcel.readInt();
        likeCount = parcel.readInt();
        content = parcel.readString();
        permission = parcel.readString();

        imageInfoList = new ArrayList<>();
        commentList = new ArrayList<>();
        likeList = new ArrayList<>();

        parcel.readTypedList(imageInfoList, MyStoryImageInfo.CREATOR);
        parcel.readTypedList(commentList, StoryComment.CREATOR);
        parcel.readTypedList(likeList, StoryLike.CREATOR);
    }

    MyStoryInfo(String id, String url, String mediaType, String createdAt, int commentCount,
                       int likeCount, String content, String permission,
                       List<MyStoryImageInfo> imageInfoList, List<StoryComment> commentList,
                       List<StoryLike> likeList) {
        this.id = id;
        this.url = url;
        this.mediaType = mediaType;
        this.createdAt = createdAt;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.content = content;
        this.permission = permission;

        this.imageInfoList = imageInfoList;
        this.commentList = commentList;
        this.likeList = likeList;

        if (this.imageInfoList == null) {
            this.imageInfoList = new ArrayList<>();
        }
        if (this.commentList == null) {
            this.commentList = new ArrayList<>();
        }
        if (this.likeList == null) {
            this.likeList = new ArrayList<>();
        }
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<MyStoryImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public List<StoryComment> getCommentList() {
        return commentList;
    }

    public List<StoryLike> getLikeList() {
        return likeList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getContent() {
        return content;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "MyStoryInfo{" + "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", commentCount=" + commentCount +
                ", likeCount=" + likeCount +
                ", comments=" + commentList +
                ", likes=" + likeList +
                ", content='" + content + '\'' +
                ", medias=" + imageInfoList +
                ", permission='" + permission + '\'' +
                '}';
    }

    public static final Creator<MyStoryInfo> CREATOR = new Creator<MyStoryInfo>() {
        @Override
        public MyStoryInfo createFromParcel(Parcel parcel) {
            return new MyStoryInfo(parcel);
        }

        @Override
        public MyStoryInfo[] newArray(int i) {
            return new MyStoryInfo[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeString(mediaType);
        parcel.writeString(createdAt);
        parcel.writeInt(commentCount);
        parcel.writeInt(likeCount);
        parcel.writeString(content);
        parcel.writeString(permission);
        parcel.writeTypedList(imageInfoList);
        parcel.writeTypedList(commentList);
        parcel.writeTypedList(likeList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyStoryInfo)) return false;
        MyStoryInfo that = (MyStoryInfo) o;
        if (getCommentCount() != that.getCommentCount()) return false;
        if (getLikeCount() != that.getLikeCount()) return false;
        if (!TextUtils.equals(getId(), that.getId())) return false;
        if (!TextUtils.equals(getUrl(), that.getUrl())) return false;
        if (!TextUtils.equals(getMediaType(), that.getMediaType())) return false;
        if (!TextUtils.equals(getCreatedAt(), that.getCreatedAt())) return false;
        if (!TextUtils.equals(getContent(), that.getContent())) return false;
        if (!TextUtils.equals(getPermission(), that.getPermission())) return false;
        if (getImageInfoList() != null ? !getImageInfoList().equals(that.getImageInfoList()) :
                that.getImageInfoList() != null) return false;
        if (getCommentList() != null ? !getCommentList().equals(that.getCommentList()) :
                that.getCommentList() != null) return false;
        return getLikeList() != null ? getLikeList().equals(that.getLikeList()) :
                that.getLikeList() == null;
    }

    public static final ResponseStringConverter<MyStoryInfo> CONVERTER = new ResponseStringConverter<MyStoryInfo>() {
        @Override
        public MyStoryInfo convert(String o) throws ResponseBody.ResponseBodyException {
            return new MyStoryInfo(o);
        }
    };
}
