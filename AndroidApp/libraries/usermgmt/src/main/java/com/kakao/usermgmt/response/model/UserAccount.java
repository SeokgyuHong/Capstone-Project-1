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
package com.kakao.usermgmt.response.model;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.network.response.ResponseBody;
import com.kakao.usermgmt.StringSet;
import com.kakao.util.OptionalBoolean;

import org.json.JSONObject;

import java.util.List;

/**
 * Class for Kakao account user data, meaning these data are account-scoped, not app-scoped.
 *
 * @author kevin.kang. Created on 2018. 4. 4..
 */
public class UserAccount {
    private OptionalBoolean profileNeedsAgreement;
    private Profile profile;
    private OptionalBoolean hasEmail;
    private OptionalBoolean emailNeedsAgreement;
    private OptionalBoolean isEmailVerified;
    private OptionalBoolean isEmailValid;
    private String email;

    private OptionalBoolean hasPhoneNumber;
    private OptionalBoolean phoneNumberNeedsAgreement;
    private String phoneNumber;

    private OptionalBoolean hasAgeRange;
    private OptionalBoolean ageRangeNeedsAgreement;
    private AgeRange ageRange;

    private OptionalBoolean hasBirthday;
    private OptionalBoolean birthdayNeedsAgreement;
    private BirthdayType birthdayType;
    private String birthday;

    private OptionalBoolean hasBirthyear;
    private OptionalBoolean birthyearNeedsAgreement;
    private String birthyear;

    private OptionalBoolean hasGender;
    private OptionalBoolean genderNeedsAgreement;
    private Gender gender;

    private OptionalBoolean hasCi;
    private OptionalBoolean ciNeedsAgreement;
    private String ci;
    private String ciAuthenticatedAt;

    private OptionalBoolean legalNameNeedsAgreement;
    private String legalName;
    private OptionalBoolean legalBirthDateNeedsAgreement;
    private String legalBirthDate;
    private OptionalBoolean legalGenderNeedsAgreement;
    private Gender legalGender;
    private OptionalBoolean isKoreanNeedsAgreement;
    private OptionalBoolean isKorean;

    private OptionalBoolean isKakaoTalkUser;
    private String displayId;

    private JSONObject response;

    public UserAccount(ResponseBody body) {
        profileNeedsAgreement = body.has(StringSet.profile_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.profile_needs_agreement)) :
                OptionalBoolean.NONE;
        hasEmail = body.has(StringSet.has_email) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_email)) :
                OptionalBoolean.NONE;
        emailNeedsAgreement = body.has(StringSet.email_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.email_needs_agreement)) :
                OptionalBoolean.NONE;
        isEmailVerified = body.has(StringSet.is_email_verified) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.is_email_verified)) :
                OptionalBoolean.NONE;
        isEmailValid = body.has(StringSet.is_email_valid) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.is_email_valid)) :
                OptionalBoolean.NONE;
        hasPhoneNumber = body.has(StringSet.has_phone_number) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_phone_number)) :
                OptionalBoolean.NONE;
        phoneNumberNeedsAgreement = body.has(StringSet.phone_number_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.phone_number_needs_agreement)) :
                OptionalBoolean.NONE;
        hasAgeRange = body.has(StringSet.has_age_range) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_age_range)) :
                OptionalBoolean.NONE;
        ageRangeNeedsAgreement = body.has(StringSet.age_range_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.age_range_needs_agreement)) :
                OptionalBoolean.NONE;
        hasBirthday = body.has(StringSet.has_birthday) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_birthday)) :
                OptionalBoolean.NONE;
        birthdayNeedsAgreement = body.has(StringSet.birthday_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.birthday_needs_agreement)) :
                OptionalBoolean.NONE;
        hasBirthyear = body.has(StringSet.has_birthyear) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_birthyear)) :
                OptionalBoolean.NONE;
        birthyearNeedsAgreement = body.has(StringSet.birthyear_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.birthyear_needs_agreement)) :
                OptionalBoolean.NONE;
        hasGender = body.has(StringSet.has_gender) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_gender)) :
                OptionalBoolean.NONE;
        genderNeedsAgreement = body.has(StringSet.gender_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.gender_needs_agreement)) :
                OptionalBoolean.NONE;
        hasCi = body.has(StringSet.has_ci) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.has_ci)) :
                OptionalBoolean.NONE;
        ciNeedsAgreement = body.has(StringSet.ci_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.ci_needs_agreement)) :
                OptionalBoolean.NONE;

        legalNameNeedsAgreement = body.has(StringSet.legal_name_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.legal_name_needs_agreement)) :
                OptionalBoolean.NONE;
        legalBirthDateNeedsAgreement = body.has(StringSet.legal_birth_date_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.legal_birth_date_needs_agreement)) :
                OptionalBoolean.NONE;
        legalGenderNeedsAgreement = body.has(StringSet.legal_gender_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.legal_gender_needs_agreement)) :
                OptionalBoolean.NONE;
        isKoreanNeedsAgreement = body.has(StringSet.is_korean_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.is_korean_needs_agreement)) :
                OptionalBoolean.NONE;

        isKakaoTalkUser = body.has(StringSet.is_kakaotalk_user) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.is_kakaotalk_user)) :
                OptionalBoolean.NONE;
        isKorean = body.has(StringSet.is_korean) ?
                OptionalBoolean.getOptionalBoolean(body.getBoolean(StringSet.is_korean)) :
                OptionalBoolean.NONE;

        if (body.has(StringSet.profile)) profile = new Profile(body.getBody(StringSet.profile));
        if (body.has(StringSet.email)) email = body.getString(StringSet.email);
        if (body.has(StringSet.phone_number)) phoneNumber = body.getString(StringSet.phone_number);
        if (body.has(StringSet.age_range))
            ageRange = AgeRange.getRange(body.getString(StringSet.age_range));
        if (body.has(StringSet.birthday_type))
            birthdayType = BirthdayType.getType(body.getString(StringSet.birthday_type));
        if (body.has(StringSet.birthday)) birthday = body.getString(StringSet.birthday);
        if (body.has(StringSet.birthyear)) birthyear = body.getString(StringSet.birthyear);
        if (body.has(StringSet.gender)) gender = Gender.getGender(body.getString(StringSet.gender));
        if (body.has(StringSet.ci)) ci = body.getString(StringSet.ci);
        if (body.has(StringSet.ci_authenticated_at))
            ciAuthenticatedAt = body.getString(StringSet.ci_authenticated_at);
        if (body.has(StringSet.legal_name)) legalName = body.getString(StringSet.legal_name);
        if (body.has(StringSet.legal_birth_date))
            legalBirthDate = body.getString(StringSet.legal_birth_date);
        if (body.has(StringSet.legal_gender))
            legalGender = Gender.getGender(body.getString(StringSet.legal_gender));

        if (body.has(StringSet.display_id)) displayId = body.getString(StringSet.display_id);

        response = body.getJson();
    }

    /**
     * 프로필 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 profile 입니다.
     * 이미 프로필이 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's profile can be provided after scope update (profile)
     */
    public OptionalBoolean profileNeedsAgreement() {
        return profileNeedsAgreement;
    }

    /**
     * 카카오계정의 프로필
     *
     * @return Kakao account's profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * 이메일 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE }인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 account_email 입니다.
     * 이미 이메일이 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's email can be provided after scope update (account_email)
     */
    public OptionalBoolean emailNeedsAgreement() {
        return emailNeedsAgreement;
    }

    /**
     * @return {@link OptionalBoolean#TRUE} if email is verified,
     * {@link OptionalBoolean#FALSE} if not verified,
     * {@link OptionalBoolean#NONE} if this info cannot be provided.
     */
    @SuppressWarnings("unused")
    public OptionalBoolean isEmailVerified() {
        return isEmailVerified;
    }

    public OptionalBoolean isEmailValid() {
        return isEmailValid;
    }

    /**
     * Returns email of user's Kakao account. This method returns null under following cases:
     * - when user does not have email (she/he registered with phone number)
     * - when user did not agree to provide email to this service
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 전화번호 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 phone_number 입니다.
     * 이미 전화번호가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's phone number can be provided after scope update (phone_number)
     */
    public OptionalBoolean phoneNumberNeedsAgreement() {
        return phoneNumberNeedsAgreement;
    }

    /**
     * Returns phone number of user's Kakao account. This method returns null under the following
     * cases:
     * - when the account has no phone number
     * - when user did not agree to provide phone number
     *
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns user's display id. This will be either email address or phone number.
     *
     * @return displayId
     */
    @SuppressWarnings("unused")
    public String getDisplayId() {
        return displayId;
    }

    /**
     * 연령대 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 age_range 입니다.
     * 이미 연령대가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's age range can be provided after scope update (age_range)
     */
    public OptionalBoolean ageRangeNeedsAgreement() {
        return ageRangeNeedsAgreement;
    }

    /**
     * Returns user's age range. This getter returns null if:
     * - when the account has no age range info
     * - when the user did not agree to provide age range info to this app
     *
     * @return {@link AgeRange} enum, null if not included in the API response.
     */
    public AgeRange getAgeRange() {
        return ageRange;
    }

    /**
     * 생일 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 birthday 입니다.
     * 이미 전화번호가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's birthday can be provided after scope update (birthday)
     */
    public OptionalBoolean birthdayNeedsAgreement() {
        return birthdayNeedsAgreement;
    }

    /**
     * Returns user's birthday in mmdd format (0115, 0427, etc).
     *
     * @return birthday in mmdd format, null if not included in the API response.
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 생일의 양력,음력
     */
    public BirthdayType getBirthdayType() {
        return birthdayType;
    }

    /**
     * 생년 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 birthyear 입니다.
     * 이미 전화번호가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's birthyear can be provided after scope update (birthyear)
     */
    public OptionalBoolean birthyearNeedsAgreement() {
        return birthyearNeedsAgreement;
    }

    /**
     * 유저의 출생 연도를 반환한다. (ex. 1990)
     *
     * @return user's birthyear
     */
    public String getBirthyear() {
        return birthyear;
    }

    /**
     * 성별 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 gender 입니다.
     * 이미 전화번호가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's gender can be provided after scope update (gender)
     */
    public OptionalBoolean genderNeedsAgreement() {
        return genderNeedsAgreement;
    }

    /**
     * Return user's gender.
     *
     * @return {@link Gender} enum, null if not included in the API response.
     */
    @Nullable
    public Gender getGender() {
        return gender;
    }

    /**
     * CI 제공에 대한 사용자 동의 필요 여부
     * <p>
     * {@link OptionalBoolean#TRUE} 인 경우 새로운 동의 요청이 가능한 상태이며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 account_ci 입니다.
     * 이미 CI가 제공되고 있는 경우에는 {@link OptionalBoolean#FALSE} 가 반환됩니다.
     * <p>
     * check if user's ci can be provided after scope update (account_ci)
     */
    public OptionalBoolean ciNeedsAgreement() {
        return ciNeedsAgreement;
    }

    /**
     * 유저의 CI 값
     *
     * @return 유저가 본인 인증후 받은 CI 값
     */
    public String getCI() {
        return ci;
    }

    /**
     * 사용자가 ci를 획득한 시간. 카카오계정에 등록된 사용자의 ci 획득시간이 제공됩니다.
     */
    public String ciAuthenticatedAt() {
        return ciAuthenticatedAt;
    }

    public boolean isKakaotalkUserNeedsAgreement() {
        return isKakaoTalkUser == OptionalBoolean.NONE;
    }

    /**
     * 카카오톡 가입 여부
     * <p>
     * 제휴를 통해 권한이 부여된 특정 앱에서만 획득할 수 있습니다. 제휴되어 있지 않은 경우 {@link OptionalBoolean#NONE} 이 반환됩니다.
     * 카카오톡 카카오계정 설정에 연결되어 있는 경우 {@link OptionalBoolean#TRUE} 가 반환됩니다.
     * 사용자 동의가 필요한 경우 {@link OptionalBoolean#NONE} 이 반환되며
     * {@link com.kakao.auth.Session#updateScopes(Activity, List, AccessTokenCallback)} 메소드를 이용하여 동의를 받을 수 있습니다.
     * 파라미터로 전달할 scope ID 는 is_kakaotalk_user 입니다.
     */
    public OptionalBoolean isKakaoTalkUser() {
        return isKakaoTalkUser;
    }

    /**
     * Returns a raw json API response. If there is any data not defined as field in this class,
     * you can query with appropriate keys.
     *
     * @return raw API response
     */
    public JSONObject getResponse() {
        return response;
    }

    /**
     * @return {@link OptionalBoolean#TRUE} if user has email registered in her/his Kakao account.
     * @deprecated Use {@link #emailNeedsAgreement()} instead
     */
    @Deprecated
    public OptionalBoolean hasEmail() {
        return hasEmail;
    }

    /**
     * Returns whether user's Kakao account has phone number registered or not.
     *
     * @return {@link OptionalBoolean#TRUE} if user hash phone number,
     * {@link OptionalBoolean#FALSE} if she/he doesn't,
     * {@link OptionalBoolean#NONE} if this info cannot be provided.
     * @deprecated Use {@link #phoneNumberNeedsAgreement()} instead
     */
    @Deprecated
    public OptionalBoolean hasPhoneNumber() {
        return hasPhoneNumber;
    }

    /**
     * @return {@link OptionalBoolean#TRUE}
     * @deprecated Use {@link #ageRangeNeedsAgreement()} instead
     */
    @Deprecated
    public OptionalBoolean hasAgeRange() {
        return hasAgeRange;
    }

    /**
     * Returns whether user's Kakao account has her/his birthday info.
     *
     * @return {@link OptionalBoolean#TRUE} if the account has birthday info,
     * {@link OptionalBoolean#FALSE} if not.
     * @deprecated Use {@link #birthdayNeedsAgreement()} instead
     */
    @Deprecated
    public OptionalBoolean hasBirthday() {
        return hasBirthday;
    }

    /**
     * Returns whether user's Kakao account has gender info.
     *
     * @return {@link OptionalBoolean#TRUE} if user's Kakao account has gender info,
     * {@link OptionalBoolean#FALSE} if not,
     * {@link OptionalBoolean#NONE} if app does not have permission to use this info.
     * @deprecated Use {@link #genderNeedsAgreement()} instead
     */
    @Deprecated
    public OptionalBoolean hasGender() {
        return hasGender;
    }

    /**
     * 유저의 출생 연도 값 소유 여부. 유저의 카카오 계정에 출생 연도 값이 있는지를 나타냄.
     *
     * @return {@link OptionalBoolean#TRUE} if user's Kakao account has birthyear info,
     * {@link OptionalBoolean#FALSE} if not,
     * {@link OptionalBoolean#NONE} if app does not have permission.
     * @deprecated Use {@link #birthyearNeedsAgreement()}} instead
     */
    @Deprecated
    public OptionalBoolean hasBirthyear() {
        return hasBirthyear;
    }

    /**
     * 유저의 ci 값 소유 여부.
     *
     * @return true if user has CI value, false otherwise
     * @deprecated Use {@link #ciNeedsAgreement()} instead
     */
    @Deprecated
    public OptionalBoolean hasCI() {
        return hasCi;
    }

    /**
     * check if user's email can be provided after scope update (account_email)
     *
     * @return true if user's email can be provided after scope update, false otherwise
     * @deprecated Use {@link #emailNeedsAgreement()} instead
     */
    @Deprecated
    public boolean needsScopeAccountEmail() {
        return hasEmail == OptionalBoolean.TRUE && email == null;
    }

    /**
     * check if user's phone number can be provided after scope update (phone_number)
     *
     * @return true if user's phone number can be provided after scope update, false otherwise
     * @deprecated Use {@link #phoneNumberNeedsAgreement()} instead
     */
    @Deprecated
    public boolean needsScopePhoneNumber() {
        return hasPhoneNumber == OptionalBoolean.TRUE && phoneNumber == null;
    }

    /**
     * @return true if user's age range can be provided after scope update, false otherwise
     * @deprecated Use {@link #ageRangeNeedsAgreement()} instead
     */
    @Deprecated
    public boolean needsScopeAgeRange() {
        return hasAgeRange == OptionalBoolean.TRUE && ageRange == null;
    }

    /**
     * check if user's birthday (without year) can be provided after scope update (birthday)
     *
     * @return true if user's birthday (without year) can be provided after scope update, false otherwise
     * @deprecated Use {@link #birthdayNeedsAgreement()} instead
     */
    @Deprecated
    public boolean needsScopeBirthday() {
        return hasBirthday == OptionalBoolean.TRUE && birthday == null;
    }

    /**
     * check if user's gender can be provided after scope update (gender)
     *
     * @return true if user's gender info can be provided after scope update, false otherwise
     * @deprecated Use {@link #genderNeedsAgreement()} instead
     */
    @Deprecated
    public boolean needsScopeGender() {
        return hasGender == OptionalBoolean.TRUE && gender == null;
    }

    @Deprecated
    public boolean needsScopeIsKakaotalkUser() {
        return isKakaoTalkUser == OptionalBoolean.NONE;
    }

    @NonNull
    @Override
    public String toString() {
        return response.toString();
    }

    /**
     * 사용자 동의를 받으면 본인인증된 실명(legal_name)을 가지고 갈 수 있는지 여부.
     */
    public OptionalBoolean legalNameNeedsAgreement() {
        return legalNameNeedsAgreement;
    }

    /**
     * 2차 본인인증으로 수집된 사용자의 실명.
     */
    public String getLegalName() {
        return legalName;
    }

    /**
     * 사용자 동의를 받으면 본인인증된 생년월일(legal_birth_date)를 가지고 갈 수 있는지 여부.
     */
    public OptionalBoolean legalBirthDateNeedsAgreement() {
        return legalBirthDateNeedsAgreement;
    }

    /**
     * 법정 생년월일. 2차 본인인증으로 수집된 사용자의 생년월일. yyyyMMDD 형식.
     */
    public String getLegalBirthDate() {
        return legalBirthDate;
    }

    /**
     * 사용자 동의를 받으면 본인인증된 성별(legal_gender)를 가지고 갈 수 있는지 여부.
     */
    public OptionalBoolean legalGenderNeedsAgreement() {
        return legalGenderNeedsAgreement;
    }

    /**
     * 법정 성별. 2차 본인인증으로 수집된 사용자의 성별. female(여)/male(남)
     */
    public Gender getLegalGender() {
        return legalGender;
    }

    /**
     * 내국인 여부. 2차 본인인증으로 수집된 내/외국인 정보. true(내국인)
     */
    public OptionalBoolean isKorean() {
        return isKorean;
    }

    /**
     * 사용자의 동의를 받으면 본인인증된 내국인 여부(is_korean)를 가지고 갈 수 있는지 여부.
     */
    public OptionalBoolean isKoreanNeedsAgreement() {
        return isKoreanNeedsAgreement;
    }
}
