package com.example.myapplication.data;

import com.example.myapplication.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 로그인 자격 증명이 있는 인증을 처리하고 사용자 정보를 검색하는 클래스.
 */
public class LoginDataSource {
    private LoggedInUser User;
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            // TODO: 아이디 권한을 부여한다. 아이디 check는 Loginrepository에서

            LoggedInUser User =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jun Dol");

            return new Result.Success<>(User);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}