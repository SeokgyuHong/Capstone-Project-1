package com.kakao.sdk.sample;

import android.content.Intent;
import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.sdk.sample.common.BaseActivity;
import com.kakao.sdk.sample.common.RootLoginActivity;
import com.kakao.util.exception.KakaoException;

/**
 * @author leoshin
 * Created by leoshin on 15. 6. 18..
 */
public class SplashActivity extends BaseActivity {
    private ISessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        callback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                goToMainActivity();
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                redirectToLoginActivity();
            }
        };

        Session.getCurrentSession().addCallback(callback);
        findViewById(R.id.splash).postDelayed(() -> {
            if (!Session.getCurrentSession().checkAndImplicitOpen()) {
                redirectToLoginActivity();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, KakaoServiceListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, RootLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
