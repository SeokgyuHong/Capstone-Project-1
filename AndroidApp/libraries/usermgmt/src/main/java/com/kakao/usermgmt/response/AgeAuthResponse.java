/*
  Copyright 2014-2019 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.usermgmt.response;

import android.app.Activity;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.AuthService.AgeAuthLevel;
import com.kakao.network.response.ApiResponseStatusError;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.usermgmt.StringSet;
import com.kakao.util.OptionalBoolean;

import java.util.List;

/**
 * @author leo.shin
 */
public class AgeAuthResponse extends JSONObjectResponse {
    public enum AgeAuthLimitStatus {
        /**
         * App에 나이제한이 설정되어있지 않은경우, com.kakao.usermgmt.UserApi.requestAgeAuthInfo() param값으로 AgeLimit조차 요청으로 들어오지 않으면
         * 나이 기준을 알 수 없기 때문에 DONT_KNOW값이 들어간다.
         */
        DONT_KNOW,
        /**
         * 인증 받은 연령이 제한 나이 이상
         */
        BYPASS_AGE_LIMIT,
        /**
         * 인증 받은 연령이 제한 나이 미만
         */
        DONT_BYPASS_AGE_LIMIT
    }

    private final long userId;
    private final String authenticatedAt;
    private final OptionalBoolean hasCi;
    private final OptionalBoolean ciNeedsAgreement;
    private final String ci;
    private final AgeAuthLevel authLevel;
    private final int authLevelCode;
    private final AgeAuthLimitStatus ageAuthLimitStatus;

    public AgeAuthResponse(String stringData) {
        super(stringData);
        this.userId = getBody().getLong(StringSet.id);
        this.authLevel = AgeAuthLevel.convertByName(getBody().optString(StringSet.auth_level, ""));
        this.authLevelCode = getBody().optInt(StringSet.auth_level_code, 0);
        this.authenticatedAt = getBody().optString(StringSet.authenticated_at, null);
        this.hasCi = getBody().has(StringSet.has_ci) ?
                OptionalBoolean.getOptionalBoolean(getBody().getBoolean(StringSet.has_ci)) :
                OptionalBoolean.NONE;
        this.ciNeedsAgreement = getBody().has(StringSet.ci_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(getBody().getBoolean(StringSet.ci_needs_agreement)) :
                OptionalBoolean.NONE;
        this.ci = getBody().optString(StringSet.ci, null);

        if (getBody().has(StringSet.bypass_age_limit)) {
            this.ageAuthLimitStatus = getBody().getBoolean(StringSet.bypass_age_limit) ? AgeAuthLimitStatus.BYPASS_AGE_LIMIT : AgeAuthLimitStatus.DONT_BYPASS_AGE_LIMIT;
        } else {
            this.ageAuthLimitStatus = AgeAuthLimitStatus.DONT_KNOW;
        }
    }

    public static final ResponseStringConverter<AgeAuthResponse> CONVERTER = new ResponseStringConverter<AgeAuthResponse>() {
        @Override
        public AgeAuthResponse convert(String o) throws ResponseBody.ResponseBodyException, ApiResponseStatusError {
            return new AgeAuthResponse(o);
        }
    };

    /**
     * 인증 여부를 확인하는 user의 id
     * @return 인증 여부를 확인하는 user의 id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 인증 받은 시각. RFC3339 internet date/time format
     * @return 인증 받은 시각.
     */
    public String getAuthenticatedAt() {
        return authenticatedAt;
    }

    /**
     * 유저의 ci 값 소유 여부.
     * @return true if user has CI value, false otherwise
     */
    public OptionalBoolean hasCI() {
        return hasCi;
    }

    /**
     * CI 제공에 대한 사용자 동의 필요 여부
     *
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 account_ci 입니다.
     * 이미 CI가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     *
     * check if user's ci can be provided after scope update (account_ci)
     */
    public OptionalBoolean ciNeedsAgreement() {
        return ciNeedsAgreement;
    }

    /**
     * 인증후 받은 CI 값
     * @return 인증후 받은 CI 값
     */
    public String getCI() {
        return ci;
    }

    /**
     * 유저가 받은 인증레벨. AUTH_LEVEL_1, AUTH_LEVEL_2 중 하나.
     * @return AgeAuthLevel
     */
    public AgeAuthLevel getAuthLevel() {
        return authLevel;
    }

    /**
     * 1 (auth_level : AUTH_LEVEL1) / 2 (auth_level : AUTH_LEVEL2) (level을 비교 편의를 제공하기 위해 auth_level에 해당하는 code 제공)
     * @return AgeAuth level code.
     */
    public int getAuthLevelCode() {
        return authLevelCode;
    }

    /**
     * true : 인증 받은 연령이 제한 나이 이상 / false : 인증 받은 연령이 제한 나이 미만. age_limit(앱에 설정했거나 request param)이 주어지지 않으면 reponse에 미포함.
     * default null value.
     * @return 앱에 설정된 연령인증 정보에 미달되었는지 여부를 알 수 있는 값을 return.
     */
    public AgeAuthLimitStatus getAgeAuthLimitStatus() {
        return ageAuthLimitStatus;
    }

    @Override
    public String toString() {
        return "AgeAuthResponse{" +
                "userId=" + userId +
                ", authenticatedAt='" + authenticatedAt + '\'' +
                ", hasCi=" + hasCi + '\'' +
                ", ci='" + ci + '\'' +
                ", authLevel=" + authLevel +
                ", authLevelCode=" + authLevelCode +
                ", ageAuthLimitStatus=" + ageAuthLimitStatus +
                '}';
    }
}
