package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class additional_inform_register extends AppCompatActivity {

    private String ip;
    private EditText Institution_name_view;
    private EditText Institution_phone_number_view;
    private MaterialTextView Institution_street_name_address_view;
    private EditText Institution_detail_address_view;
    private Button confirm_button;

    private String login_type;
    private String email;
    private String pw;

    private String institution_name;
    private String institution_phone_number;
    private String institution_street_name_address;
    private String institution_detail_address;
    private String address;

    private String Account_create_result;

    private MaterialToolbar toolbar;
    private ActionBar actionBar;

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_inform_register);
        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        ip = getResources().getString(R.string.server_ip);

        Institution_name_view = (EditText) findViewById(R.id.institution_name);
        Institution_phone_number_view = (EditText) findViewById(R.id.institution_phone_number);
        Institution_phone_number_view.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        Institution_street_name_address_view = (MaterialTextView) findViewById(R.id.institution_street_name_address);

        Institution_street_name_address_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(additional_inform_register.this, DaumWebViewActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        Institution_detail_address_view = (EditText) findViewById(R.id.institution_detail_address);
        confirm_button = (Button)findViewById(R.id.confirm_button2);

        toolbar = (MaterialToolbar)findViewById(R.id.register_toolbar_required_information);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        login_type = intent.getStringExtra("login_type");
        email = intent.getStringExtra("email");
        pw = intent.getStringExtra("pw");

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                institution_name = Institution_name_view.getText().toString();
                institution_phone_number = Institution_phone_number_view.getText().toString();
                institution_street_name_address = Institution_street_name_address_view.getText().toString();
                institution_detail_address = Institution_detail_address_view.getText().toString();
                address = institution_street_name_address + " " + institution_detail_address;

                if(institution_name.length() == 0 || institution_phone_number.length() == 0 || institution_street_name_address.length() == 0 ||
                institution_detail_address.length() == 0){
                    Log.e("asdfasdf", "안됨안됨");
                }
                else{
                    //서버로 전체 정보 전송
                    if(login_type.equals("general")){ //일반 회원가입
                        ThreadTask<Object> result = getThreadTask_normal_sing_up(email, pw, institution_name, institution_phone_number, address, "/normal_sign_up");
                        result.execute(ip);
                        Log.e("asdfasdfasdf", Integer.toString(result.getResult()));
                        if(result.getResult() == 2){
                            //휴대폰 번호 양식 에러 (재입력 요구)
                        }
                        else if(result.getResult() == 3){
                            //회원 가입 성공
//                            SharedPreferences pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString("email" , email);
//                            editor.putString("login_type" , login_type);
//                            editor.commit();

                            Intent intent = new Intent(additional_inform_register.this, account_register_complete.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            //남아있는 모든 activity 지우기
                            ActivityCompat.finishAffinity(additional_inform_register.this);
                            finish();
                        }
                        else if(result.getResult() == 4){
                            //회원 가입 실패
                        }
                        else if(result.getResult() == 0){
                            //시스템 에러
                        }
                    }
                    else{// social 회원가입
                        ThreadTask<Object> result = getThreadTask_social_sign_up(email, institution_name, institution_phone_number, address, login_type, "/social_sign_up");
                        result.execute(ip);
                        if(result.getResult() == 1){
                            //이메일 양식 에러 (재입력 요구)
                        }
                        else if(result.getResult() == 2){
                            //휴대폰 번호 양식 에러 (재입력 요구)
                        }
                        else if(result.getResult() == 3){
                            //회원 가입 성공
//                            SharedPreferences pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString("email" , email);
//                            editor.putString("login_type" , login_type);
//                            editor.commit();

                            Intent intent = new Intent(additional_inform_register.this, account_register_complete.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            //남아있는 모든 activity 지우기
                            ActivityCompat.finishAffinity(additional_inform_register.this);
                            finish();
                        }
                        else if(result.getResult() == 4){
                            //회원 가입 실패
                        }
                        else if(result.getResult() == 0){
                            //시스템 에러
                        }
                    }

                }
            }
        });

    }

    private ThreadTask<Object> getThreadTask_normal_sing_up(String email, String pw, String institution_name, String institution_phone_number, String address, String Router_name){

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

                URL url = new URL(urls[0] +Router_name);

                con = (HttpURLConnection) url.openConnection();

                sendObject.put("email_address", email);
                sendObject.put("password", pw);
                sendObject.put("inst_name", institution_name);
                sendObject.put("phone_number", institution_phone_number);
                sendObject.put("inst_address", address);

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

    private ThreadTask<Object> getThreadTask_social_sign_up( String email, String institution_name, String institution_phone_number, String address, String user_type, String Router_name){

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

                URL url = new URL(urls[0] +Router_name);

                con = (HttpURLConnection) url.openConnection();

                sendObject.put("email_address", email);
                sendObject.put("inst_name", institution_name);
                sendObject.put("phone_number", institution_phone_number);
                sendObject.put("inst_address", address);
                sendObject.put("user_type", user_type);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Institution_street_name_address_view.setText(data);
                    }
                }
                break;
        }
    }
}