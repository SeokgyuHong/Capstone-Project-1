package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.Thread.ThreadTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class User_inform extends AppCompatActivity {

    private MaterialTextView EmailView;
    private MaterialTextView PhoneNumView;
    private MaterialTextView AddressView;
    private MaterialButton goBack_Button;

    private ActionBar actionBar;
    private MaterialToolbar toolbar;

    private SharedPreferences login_information_pref;
    private String Email;
    private String Phonenumber;
    private String Address;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inform);
        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        toolbar = (MaterialToolbar)findViewById(R.id.Sensorinform_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ip = getString(R.string.server_ip);

        login_information_pref = getSharedPreferences("login_information", Context.MODE_PRIVATE);
        Email = login_information_pref.getString("email", Email);

        EmailView = findViewById(R.id.Email);
        PhoneNumView = findViewById(R.id.Phonenumber);
        AddressView = findViewById(R.id.Address);
        goBack_Button = findViewById(R.id.goBack_Button);

        ThreadTask<Object> getThreadTask_userInform = getThreadTask_userInform(Email, "/user_inform");
        getThreadTask_userInform.execute(ip);

        EmailView.setText(Email);
        PhoneNumView.setText(Phonenumber);
        AddressView.setText(Address);

        goBack_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ThreadTask<Object> getThreadTask_userInform(String Mac, String Router_name){

        return new ThreadTask<Object>() {
            private int response_result;
            private String error_code;
            @Override
            protected void onPreExecute() {// excute 전에

            }

            @Override
            protected void doInBackground(String... urls) throws IOException, JSONException {//background로 돌아갈것
                HttpURLConnection con = null;
                JSONObject sendObject = new JSONObject();
                BufferedReader reader = null;
                URL url = new URL(urls[0] + Router_name);

                con = (HttpURLConnection) url.openConnection();

                sendObject.put("wifi_mac_address", Mac);

                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "application/json");//서버에 response 데이터를 html로 받음
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

                OutputStream outStream = con.getOutputStream();
                outStream.write(sendObject.toString().getBytes());
                outStream.flush();

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream stream = con.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = stream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    String response = new String(byteData);
                    JSONObject responseJSON = new JSONObject(response);

                    this.response_result = (Integer) responseJSON.get("key");
                    this.error_code = (String) responseJSON.get("err_code");

                    Phonenumber = (String) responseJSON.get("email");
                    Address = (String) responseJSON.get("address");
                }
            }

            @Override
            protected void onPostExecute() {

            }

            @Override
            public int getResult() {
                return response_result;
            }

            @Override
            public String getErrorCode() {
                return error_code;
            }
        };
    }
}