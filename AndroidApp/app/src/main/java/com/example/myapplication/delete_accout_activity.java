package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.ui.login.LoginActivity;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONException;

import java.io.IOException;

import static com.example.myapplication.Mypage_fragment.mOAuthLoginModule;

public class delete_accout_activity extends AppCompatActivity {
    static OAuthLogin mOAuthLoginModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_accout_activity);
        mOAuthLoginModule = OAuthLogin.getInstance();



    }
}