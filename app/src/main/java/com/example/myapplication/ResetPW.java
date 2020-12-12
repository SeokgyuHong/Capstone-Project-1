package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Thread.ThreadTask;
import com.example.myapplication.Thread.ThreadTask_temp_pw_check;
import com.google.android.material.appbar.MaterialToolbar;
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
import java.util.Timer;
import java.util.TimerTask;

public class ResetPW extends AppCompatActivity {

    private EditText EmailText;
    private EditText PasswordText;
    private EditText PasswordConfirmText;
    private EditText EmailSecurityText;
    private MaterialTextView EmailSecurityTimer;
    private MaterialTextView EmailSecurityInformText;
    private MaterialTextView EmailSecurityCodeSendButton;

    private Button EmailcheckButton; // 중복확인 버튼
    private Button Confirm_button; // 회원가입 확인 버튼
    private Button EmailSecurityConfirmButton; // 인증번호 확인 버튼

    private String Email;
    private String duplication_check_result ="";

    private MaterialToolbar toolbar;
    private ActionBar actionBar;

    private boolean Pw_match_check;
    private boolean Security_code_check;
    private boolean IsValid_security_code = true;

    private String ip;

    TimerTask timerTask;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_p_w);
        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        EmailText = (EditText)findViewById(R.id.email);
        PasswordText = (EditText)findViewById(R.id.pw);
        PasswordConfirmText = (EditText)findViewById(R.id.pw_confirm);
        EmailcheckButton = (Button)findViewById(R.id.EmailCheck);
        Confirm_button = (Button)findViewById(R.id.confirm_button);

        EmailSecurityText = (EditText)findViewById(R.id.email_security_code);
        EmailSecurityConfirmButton = (Button)findViewById(R.id.email_security_code_confirm_button);
        EmailSecurityInformText = (MaterialTextView)findViewById(R.id.email_security_inform_text);
        EmailSecurityCodeSendButton = (MaterialTextView)findViewById(R.id.email_security_code_send);
        EmailSecurityTimer = (MaterialTextView)findViewById(R.id.email_security_timer);

        toolbar = (MaterialToolbar)findViewById(R.id.Sensorinform_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ip = getString(R.string.server_ip);

        //1. 이메일 형식인지 확인
        //2. 중복확인 누르면 서버로 전송
        //3. true이면 등록가능한 상태
        //4. 비밀번호 입력, 재확인 둘이 같은지 확인해야함
        //5. 이메일과 비밀번호, 재확인이 다 입력되어ㅣㅆ지 않으면 alert창 띄우기
        //6. 전부다 완료시 다음 창으로 넘어감.

        /**이메일 중복체크*/
        EmailcheckButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Email = EmailText.getText().toString();

                /**서버로 Email 전송하기*/
                if (Email.contains("@")) {
                    if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                        ThreadTask<Object> result = getThreadTask(Email, "/id_duplication_check");
                        result.execute(ip);
                        if(result.getResult() == 3){
                            duplication_check_result = "true";
                            findViewById(R.id.email_security_code_layout).setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Toast.makeText(getApplication(), "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPW.this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**회원가입 확인*/
        Confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pw_match_check = PasswordText.getText().toString().equals(PasswordConfirmText.getText().toString());
                if(duplication_check_result.equals("")){
                    Toast.makeText(getApplication(), "이메일 인증을 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(duplication_check_result.equals("false")) {
                    Toast.makeText(getApplication(), "등록된 이메일이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!Pw_match_check){
                    Toast.makeText(getApplication(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!Security_code_check){
                    Toast.makeText(getApplication(), "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(PasswordText.getText().length() == 0 || PasswordConfirmText.getText().length() == 0){
                    Toast.makeText(getApplication(), "입력하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(Security_code_check && Pw_match_check && duplication_check_result.equals("true") && PasswordText.getText().length() > 0 && PasswordConfirmText.getText().length() > 0 ){
                    ThreadTask_temp_pw_check<Object> result = getThreadTask_pw_check(Email, PasswordConfirmText.getText().toString(),"/ResetPW");
                    //security_Code를 서버로 전송하고
                    result.execute(ip);
                    if(result.getResult() == 0){
                        Toast.makeText(getApplication(), "비밀번호 재설정 실패", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        finish();
                    }
                }
            }
        });
        /**인증번호 확인*/
        EmailSecurityConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String Security_code = EmailSecurityText.getText().toString();
                ThreadTask_temp_pw_check<Object> result = getThreadTask_pw_check(Email, Security_code,"/temp_pw_check");
                //security_Code를 서버로 전송하고
                result.execute(ip);

                if(result.getResult() == 1){ //Security_code_check true or false
                    //임시 비밀번호 일치 추가 진행 가능
                    //true이면 EmailSecurityInformText.setText(인증되었씁니다)로 바꿔주기
                    Security_code_check = true;
                    Toast.makeText(ResetPW.this, "인증되었습니다.", Toast.LENGTH_SHORT).show();
                    //EmailSecurityInformText.setText("인증되었습니다.");
                    findViewById(R.id.email_security_code_layout).setVisibility(View.GONE);
                    //true이면 EmailSecurityInformText.setText(인증번호가 일치하지않습니다)로 바꿔주기
                }
                else if(result.getResult() == 2){
                    //시스템 에러
                    Security_code_check = false;
                    EmailSecurityInformText.setText("인증번호가 일치하지않습니다.");
                }
                else if(result.getResult() == 0){
                    //시스템 에러
                }
            }
        });
        //인증번호 전송
        EmailSecurityCodeSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //인증번호 서버에 요청하기.


                ThreadTask<Object> result = getThreadTask(Email, "/temp_pw_create");
                result.execute(ip);

                if(result.getResult() == 1){
                    //이메일양식에러
                    Log.e("EmailSecurityCodeSendButton", Integer.toString(result.getResult()));
                }
                else if(result.getResult() == 2){
                    //임시 비밀 번호 생성 및 이메일 처리상 오류
                    Log.e("EmailSecurityCodeSendButton", Integer.toString(result.getResult()));
                }
                else if(result.getResult() == 3){
                    //임시 비밀번호 생성 완료()
                    //요철하고 나면 EmailSecurityCodeSendButton.setText("재전송") 으로 바꿔주기
                    //요청하면 타이머 시작
                    //Toast.makeText(RegisterActivity.this, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ResetPW.this, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                    startTimerTask();
                    EmailSecurityCodeSendButton.setText("인증번호 재전송");

                }
                else if(result.getResult() == 0){
                    //시스템 에러
                    Log.e("EmailSecurityCodeSendButton", Integer.toString(result.getResult()));
                }
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

    private ThreadTask<Object> getThreadTask(String email, String Router_name){

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

    private ThreadTask_temp_pw_check<Object>getThreadTask_pw_check(String email, String security_code, String Router_name){

        return new ThreadTask_temp_pw_check<Object>() {
            private int response_result;
            private String error_code;
            //private String security_code;

            @Override
            protected void doInBackground(String... urls) throws IOException, JSONException {//background로 돌아갈것
                HttpURLConnection con = null;
                JSONObject sendObject = new JSONObject();
                BufferedReader reader = null;

                URL url = new URL(urls[0] +Router_name);

                con = (HttpURLConnection) url.openConnection();
                //this.security_code = security_code;
                Log.e("Security_code", security_code);
                sendObject.put("email_address", email);
                sendObject.put("temp_password", security_code);

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
            public int getResult() {
                return response_result;
            }

            @Override
            public String getErrorCode() {
                return error_code;
            }
        };
    }

    private void startTimerTask(){
        stopTimerTask();
        timerTask = new TimerTask() {
            int timer = 180;
            int minutes;
            int seconds;
            String seconds_string;
            @Override
            public void run() {
                timer--;
                minutes = timer / 60;
                seconds = timer % 60;
                if(seconds < 10){
                    seconds_string = "0" + Integer.toString(seconds);
                }
                else{
                    seconds_string = Integer.toString(seconds);
                }
                EmailSecurityTimer.post(new Runnable() {
                    @Override
                    public void run() {
                        EmailSecurityTimer.setText(Integer.toString(minutes)+":"+seconds_string);
                    }
                });
                if(timer <= 0){
                    stopTimerTask();
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void stopTimerTask() {
        if (timerTask != null) {
            //.setText("3:00");
            timerTask.cancel();
            timerTask = null;
            IsValid_security_code = false;
        }
    }
}