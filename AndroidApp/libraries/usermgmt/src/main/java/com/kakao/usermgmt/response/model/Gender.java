package com.kakao.usermgmt.response.model;

/**
 * Enum class for gender.
 *
 * @author kevin.kang. Created on 2018. 5. 18..
 */
public enum Gender {
    /**
     * female
     */
    FEMALE("female"),
    /**
     * male
     */
    MALE("male"),
    /**
     * Other than female or male
     */
    OTHER("other"),
    /**
     * Gender was provided with an unknown value
     */
    UNKNOWN("N/A");

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender getGender(String value) {
        for (Gender gender : values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    private String value;
}
