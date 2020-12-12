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
package com.kakao.kakaotalk.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.response.ResponseBody;

/**
 * * 카카오톡 프로필 요청의 결과 객체로
 * 카카오톡의 닉네임 정보(nickName), 카카오톡의 프로필 이미지 URL(profileImageURL), 카카오톡의 프로필 이미지의 썸네일 URL(thumbnailURL), 카카오톡의 국가코드(countryISO)로 구성되어 있다.
 * @author leoshin, created at 15. 7. 27..
 */
public class KakaoTalkProfile implements Parcelable {
    final private String nickName;
    final private String profileImageUrl;
    final private String thumbnailUrl;
    final private String countryISO;


    KakaoTalkProfile(ResponseBody body) {
        this.nickName = body.optString(StringSet.nickName, null);
        this.profileImageUrl = body.optString(StringSet.profileImageURL, null);
        this.thumbnailUrl = body.optString(StringSet.thumbnailURL, null);
        this.countryISO = body.optString(StringSet.countryISO, null);
    }

    KakaoTalkProfile(String nickName, String profileImageUrl, String thumbnailUrl, String countryISO) {
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.countryISO = countryISO;
    }

    KakaoTalkProfile(final Parcel parcel) {
        nickName = parcel.readString();
        profileImageUrl = parcel.readString();
        thumbnailUrl = parcel.readString();
        countryISO = parcel.readString();
    }

    /**
     * 카카오톡 별명
     * @return 카카오톡 별명
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 640px * 640px 크기의 카카오톡 프로필 이미지 URL
     * @return 카카오톡 프로필 이미지 URL
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * 110px * 110px 크기의 카카오톡 썸네일 프로필 이미지 URL
     * @return 카카오톡 썸네일 프로필 이미지 URL
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * 카카오톡 국가
     * @return 카카오톡 국가
     */
    public String getCountryISO() {
        return countryISO;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KakaoTalkProfile)) return false;
        KakaoTalkProfile compare = (KakaoTalkProfile) obj;
        if (!TextUtils.equals(nickName, compare.getNickName())) return false;
        if (!TextUtils.equals(profileImageUrl, compare.getProfileImageUrl())) return false;
        if (!TextUtils.equals(thumbnailUrl, compare.getThumbnailUrl())) return false;
        if (!TextUtils.equals(countryISO, compare.getCountryISO())) return false;
        return true;
    }

    /**
     * 각 정보를 string으로 표현하여 반환한다.
     * @return 각 정보를 포함한 string
     */
    @Override
    public String toString() {
        return "KakaoTalkProfile{" + "nickName='" + nickName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", countryISO='" + countryISO + '\'' +
                '}';
    }

    public static final Creator<KakaoTalkProfile> CREATOR = new Creator<KakaoTalkProfile>() {
        @Override
        public KakaoTalkProfile createFromParcel(Parcel parcel) {
            return new KakaoTalkProfile(parcel);
        }

        @Override
        public KakaoTalkProfile[] newArray(int i) {
            return new KakaoTalkProfile[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nickName);
        parcel.writeString(profileImageUrl);
        parcel.writeString(thumbnailUrl);
        parcel.writeString(countryISO);
    }
}
