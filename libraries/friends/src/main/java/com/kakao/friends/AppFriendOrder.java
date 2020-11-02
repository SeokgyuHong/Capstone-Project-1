package com.kakao.friends;

/**
 * @author kevin.kang. Created on 2019-10-07..
 */
public enum AppFriendOrder {
    /**
     * 닉네임 기준 정렬
     */
    NICKNAME("nickname"),
    /**
     * 즐겨찾기 먼저 정렬. 각 그룹 내에서는 가나다 순.
     */
    FAVORITE("favorite");

    final private String value;

    AppFriendOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
