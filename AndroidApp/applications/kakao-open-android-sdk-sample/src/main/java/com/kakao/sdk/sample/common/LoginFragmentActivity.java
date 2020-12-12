package com.kakao.sdk.sample.common;

import android.os.Bundle;

import com.kakao.sdk.sample.R;

/**
 * Container activity for LoginFragment.
 */
public class LoginFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
