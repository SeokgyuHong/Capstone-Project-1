package com.kakao.usermgmt.response.model;

/**
 * Enum class for age range.
 *
 * @author kevin.kang. Created on 2018. 5. 18..
 */
public enum AgeRange {
    AGE_0_9("0~9"),
    AGE_10_14("10~14"),
    AGE_15_19("15~19"),
    AGE_20_29("20~29"),
    AGE_30_39("30~39"),
    AGE_40_49("40~49"),
    AGE_50_59("50~59"),
    AGE_60_69("60~69"),
    AGE_70_79("70~79"),
    AGE_80_89("80~89"),
    AGE_90_ABOVE("90~"),

    /**
     * Used when wrong age range is provided.
     */
    AGE_RANGE_UNKNOWN("N/A");

    AgeRange(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AgeRange getRange(String value) {
        for (AgeRange range : values()) {
            if (range.value.equalsIgnoreCase(value)) {
                return range;
            }
        }
        return AGE_RANGE_UNKNOWN;
    }

    private String value;

}
