package com.example.myapplication.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * LoginRepository에서 검색된 로그인한 사용자의 사용자 정보를 캡처하는 데이터 클래스
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}