/*
  Copyright 2019 Kakao Corp.
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.usermgmt.response.model;

import androidx.annotation.NonNull;

import com.kakao.network.response.ResponseBody;
import com.kakao.usermgmt.StringSet;

import org.json.JSONObject;

/**
 * 실시간 카카오 계정 프로필 정보.
 *
 * @author kevin.kang. Created on 2019-09-24..
 */
public class Profile {
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
    private JSONObject response;

    Profile(ResponseBody body) {
        nickname = body.optString(StringSet.nickname, null);
        thumbnailImageUrl = body.optString(StringSet.thumbnail_image_url, null);
        profileImageUrl = body.optString(StringSet.profile_image_url, null);
        response = body.getJson();
    }

    /**
     * 사용자 별명
     *
     * @return 사용자 별명
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 110px * 110px (일반 프로필 사진) 또는 100px * 100px (움직이는 프로필 사진) 크기의 사용자의 썸네일 프로필 이미지 경로
     *
     * @return 사용자의 썸네일 프로필 이미지 경로
     */
    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    /**
     * 640px * 640px (일반 프로필 사진) 또는 480px * 480px (움직이는 프로필 사진) 크기의 사용자의 프로필 이미지 경로
     *
     * @return 사용자의 프로필 이미지 경로
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return response.toString();
    }
}
