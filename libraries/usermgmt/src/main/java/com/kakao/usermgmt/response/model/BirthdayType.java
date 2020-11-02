package com.kakao.usermgmt.response.model;

import androidx.annotation.NonNull;

/**
 * 생일의 양력,음력 정보
 *
 * @author kevin.kang. Created on 2020-01-07..
 */
public enum BirthdayType {
    /**
     * 양력
     */
    SOLAR,
    /**
     * 음력
     */
    LUNAR,
    /**
     *
     */
    UNKNOWN;

    public static BirthdayType getType(@NonNull final String type) {
        for (BirthdayType birthdayType : values()) {
            if (birthdayType.name().equalsIgnoreCase(type)) {
                return birthdayType;
            }
        }
        return UNKNOWN;
    }

    @NonNull
    @Override
    public String toString() {
        return name();
    }
}
