package com.kakao.sdk.sample.common.log;

/**
 * @author leoshin
 * Created by leoshin on 15. 4. 6..
 */
public enum Tag {
    DEFAULT("kakao.sdk");

    private final String tag;

    Tag(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }
}
