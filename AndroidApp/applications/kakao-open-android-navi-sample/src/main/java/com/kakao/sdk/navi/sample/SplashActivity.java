package com.kakao.sdk.navi.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        findViewById(R.id.splash).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, KakaoNaviActivity.class));
            finish();
        }, 500);
    }
}
