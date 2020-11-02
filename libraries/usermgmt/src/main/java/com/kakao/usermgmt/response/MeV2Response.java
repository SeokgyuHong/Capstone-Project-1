/*
  Copyright 2018-2019 Kakao Corp.
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
package com.kakao.usermgmt.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.usermgmt.StringSet;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;

import org.json.JSONObject;

import java.util.Map;

/**
 * API response from /v2/user/me. /v2/user/me divides user-related data into three categories:
 * - kakaoAccount
 * - properties
 * - forPartners
 * <p>
 * UserAccount has all the data related to user's Kakao account. Properties contain app-scope
 * user data (including your custom ones). ForPartners have partner-specific data such as uuid and
 * remaining invite counts.
 *
 * @author kevin.kang. Created on 2018. 4. 4..
 */
public class MeV2Response extends JSONObjectResponse {
    /**
     * key for nickname in properties
     */
    public static final String KEY_NICKNAME = "nickname";
    /**
     * key for thumbnail_image in properties
     */
    public static final String KEY_THUMBNAIL_IMAGE = "thumbnail_image";
    /**
     * key for profile_image in properties
     */
    public static final String KEY_PROFILE_IMAGE = "profile_image";

    private Long id;
    private String groupUserToken;
    private OptionalBoolean hasSignedUp;

    private UserAccount kakaoAccount;
    private Map<String, String> properties;
    private JSONObject forPartners;

    private String synchedAt;
    private String connectedAt;

    private String nickname;
    private String thumbnailImagePath;
    private String profileImagePath;

    MeV2Response(final String stringData) {
        super(stringData);

        if (getBody().has(StringSet.id)) {
            this.id = getBody().getLong(StringSet.id);
        }

        this.groupUserToken = getBody().optString(StringSet.group_user_token, null);

        hasSignedUp = getBody().has(StringSet.has_signed_up) ?
                OptionalBoolean.getOptionalBoolean(getBody().getBoolean(StringSet.has_signed_up)) :
                OptionalBoolean.NONE;

        if (getBody().has(StringSet.properties)) {
            properties = ResponseBody.toMap(getBody().getBody(StringSet.properties));

            if (properties.containsKey(KEY_NICKNAME)) {
                nickname = properties.get(KEY_NICKNAME);
            }
            if (properties.containsKey(KEY_THUMBNAIL_IMAGE)) {
                thumbnailImagePath = properties.get(KEY_THUMBNAIL_IMAGE);
            }
            if (properties.containsKey(KEY_PROFILE_IMAGE)) {
                profileImagePath = properties.get(KEY_PROFILE_IMAGE);
            }
        }

        if (getBody().has(StringSet.kakao_account)) {
            kakaoAccount = new UserAccount(getBody().getBody(StringSet.kakao_account));
        }

        if (getBody().has(StringSet.for_partner)) {
            forPartners = getBody().getBody(StringSet.for_partner).getJson();
        }
        this.connectedAt = getBody().optString(StringSet.connected_at, null);
        this.synchedAt = getBody().optString(StringSet.synched_at, null);
    }

    public static final ResponseStringConverter<MeV2Response> CONVERTER = new ResponseStringConverter<MeV2Response>() {
        @Override
        public MeV2Response convert(String o) throws ResponseBody.ResponseBodyException {
            return new MeV2Response(o);
        }
    };

    /**
     * Returns data of user's kakao account
     *
     * @return data of user's Kakao account
     */
    public UserAccount getKakaoAccount() {
        return kakaoAccount;
    }

    /**
     * Returns app-scope user properties. They include pre-defined properties such as:
     * - nickname
     * - thumbnail_image
     * - profile_image
     * <p>
     * and other custom properties you defined in Kakao developer console.
     *
     * @return app-scope user properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Tells whether user has signed up or not.
     *
     * @return {@link OptionalBoolean#TRUE} if user already signed up,
     * {@link OptionalBoolean#FALSE} if user hasn't signed up yet,
     * {@link OptionalBoolean#NONE} if app uses automatic sign up functionality.
     */
    @NonNull
    public OptionalBoolean hasSignedUp() {
        return hasSignedUp;
    }

    /**
     * Returns app user id.
     * <p>
     * This id is app scope (different between apps for same user) and is kept same
     * even if user connects again after unlinking.
     *
     * @return app user id
     */
    public long getId() {
        return id;
    }

    /**
     * 해당앱이 그룹앱에 속한 경우 그룹에서 맵핑정보로 사용할수 있는 값.
     * 그룹정보가 변경되면 바뀔 수 있는 값이니 id 로 사용하면 안됨.
     */
    @Nullable
    public String getGroupUserToken() {
        return groupUserToken;
    }

    /**
     * Return values normally used by Kakao partner services such as:
     * - uuid
     * - remaining_invite_count
     * - remaining_group_msg_count
     *
     * @return values normally used by Kakao partner services
     */
    @Nullable
    public JSONObject forPartners() {
        return forPartners;
    }


    /**
     * 카카오싱크 간편가입창을 통해 카카오 로그인 한 시각. yyyy-mm-dd'T'HH:mm:ss'Z' 형식.
     */
    @Nullable
    public String getSynchedAt() {
        return synchedAt;
    }

    /**
     * 해당 서비스에 연결 완료된 시각. yyyy-mm-dd'T'HH:mm:ss'Z' 형식.
     */
    @Nullable
    public String getConnectedAt() {
        return connectedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return getBody().toString();
    }

    /**
     * 사용자 별명
     *
     * @return 사용자 별명
     * @deprecated Use {@link Profile#getNickname()} on {@link #getKakaoAccount()} instead
     */
    @Deprecated
    public String getNickname() {
        return nickname;
    }

    /**
     * 110px * 110px(톡에서 가지고 온 경우) 또는 160px * 160px(스토리에서 가지고 온 경우) 크기의 사용자의 썸네일 프로필 이미지 경로
     *
     * @return 사용자의 썸네일 프로필 이미지 경로
     * @deprecated Use {@link Profile#getThumbnailImageUrl()} ()} on {@link #getKakaoAccount()} instead
     */
    @Deprecated
    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }

    /**
     * 480px * 480px ~ 1024px * 1024px 크기의 사용자의 프로필 이미지 경로
     *
     * @return 사용자의 프로필 이미지 경로
     * @deprecated Use {@link Profile#getProfileImageUrl()} ()} ()} on {@link #getKakaoAccount()} instead
     */
    @Deprecated
    public String getProfileImagePath() {
        return profileImagePath;
    }
}
