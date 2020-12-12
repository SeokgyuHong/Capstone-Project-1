package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.ui.login.LoginActivity;

public class account_register_complete extends AppCompatActivity {

    private Button go_to_login_activity_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register_complete);
        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        go_to_login_activity_button = (Button)findViewById(R.id.go_to_login_activity_button);
        go_to_login_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account_register_complete.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}