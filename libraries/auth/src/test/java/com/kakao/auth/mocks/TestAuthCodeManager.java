package com.kakao.auth.mocks;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.kakao.auth.AuthCodeCallback;
import com.kakao.auth.authorization.authcode.AuthCodeManager;
import com.kakao.auth.AuthType;
import com.kakao.auth.helper.StartActivityWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 5. 25..
 */

public class TestAuthCodeManager implements AuthCodeManager {
    private String authCode = "auth_code";

    @Override
    public void requestAuthCode(AuthType authType, Activity activity, AuthCodeCallback authCodeCallback) {
        authCodeCallback.onAuthCodeReceived(authCode);
    }

    @Override
    public void requestAuthCode(AuthType authType, Fragment fragment, AuthCodeCallback authCodeCallback) {
        authCodeCallback.onAuthCodeReceived(authCode);
    }

    @Override
    public void requestAuthCode(AuthType authType, StartActivityWrapper wrapper, AuthCodeCallback authCodeCallback) {
        authCodeCallback.onAuthCodeReceived(authCode);
    }

    @Override
    public void requestAuthCode(AuthType authType, StartActivityWrapper wrapper, Map<String, String> extraParams, AuthCodeCallback authCodeCallback) {
        authCodeCallback.onAuthCodeReceived(authCode);
    }

    @Override
    public void requestAuthCodeWithScopes(AuthType authType, StartActivityWrapper wrapper, List<String> scopes, AuthCodeCallback authCodeCallback) {
        authCodeCallback.onAuthCodeReceived(authCode);
    }

    @Override
    public void requestAuthCodeWithCustomAccountsUrl(StartActivityWrapper wrapper, Map<String, String> extraParams, String path, Map<String, String> accountParams, AuthCodeCallback authCodeCallback) {
        authCodeCallback.onAuthCodeReceived(authCode);
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return true;
    }

    @Override
    public boolean isTalkLoginAvailable() {
        return true;
    }

    @Override
    public boolean isStoryLoginAvailable() {
        return true;
    }
}
