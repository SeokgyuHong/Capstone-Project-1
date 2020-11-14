package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {

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

    TimerTask timerTask;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        toolbar = (MaterialToolbar)findViewById(R.id.register_toolbar_required_information);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //1. 이메일 형식인지 확인
        //2. 중복확인 누르면 서버로 전송
        //3. true이면 등록가능한 상태
        //4. 비밀번호 입력, 재확인 둘이 같은지 확인해야함
        //5. 이메일과 비밀번호, 재확인이 다 입력되어ㅣㅆ지 않으면 alert창 띄우기
        //6. 전부다 완료시 다음 창으로 넘어감.

        EmailcheckButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Email = EmailText.getText().toString();

                //서버로 Email 전송하기
                if (Email.contains("@")) {
                    if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                        new ThreadTask<Object>() {

                            @Override
                            protected void onPreExecute() {// excute 전에

                            }

                            @Override
                            protected void doInBackground(String... urls) throws IOException, JSONException {//background로 돌아갈것
                                HttpURLConnection con = null;
                                JSONObject sendObject = new JSONObject();
                                BufferedReader reader = null;

                                URL url = new URL(urls[0]);
                                con = (HttpURLConnection) url.openConnection();

                                sendObject.put("email",Email);

                                con.setRequestMethod("POST");//POST방식으로 보냄
                                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

                                con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                                Log.e("tqeteqw", "1");
                                con.setConnectTimeout(15000);
                                con.connect();
                                Log.e("tqeteqw", "2");
                                OutputStream outStream = con.getOutputStream();
                                //버퍼를 생성하고 넣음
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                                writer.write(sendObject.toString());
                                writer.flush();
                                writer.close();//버퍼를 받아줌
                                //서버로 부터 데이터받음
                                InputStream stream = con.getInputStream();
                                reader = new BufferedReader(new InputStreamReader(stream));
                                StringBuilder buffer = new StringBuilder();
                                String line = "";
                                while((line = reader.readLine()) != null){
                                    buffer.append(line);
                                }
                                duplication_check_result = buffer.toString();
                            }

                            @Override
                            protected void onPostExecute() {

                            }
                        }.execute("http://10.0.2.2:3000/email_duplication_check");

                        if(duplication_check_result.equals("false")){
                            Toast.makeText(getApplication(), "이미 등록된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(duplication_check_result.equals("true")){
                            Log.e("duplication Check", duplication_check_result);
                            Toast.makeText(getApplication(), "등록된 계정이 없습니다.", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.email_security_code_layout).setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Toast.makeText(getApplication(), "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Confirm_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pw_match_check = PasswordText.getText().toString().equals(PasswordConfirmText.getText().toString());
                if(duplication_check_result.equals("")){
                    Toast.makeText(getApplication(), "이메일 중복검사를 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(duplication_check_result.equals("false")) {
                    Toast.makeText(getApplication(), "이미 등록된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!Pw_match_check){
                    Toast.makeText(getApplication(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(PasswordText.getText().length() == 0 || PasswordConfirmText.getText().length() == 0){
                    Toast.makeText(getApplication(), "입력하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(Pw_match_check && duplication_check_result.equals("true") && PasswordText.getText().length() > 0 && PasswordConfirmText.getText().length() > 0 ){
                    Intent intent = new Intent(RegisterActivity.this, additional_inform_register.class);
                    intent.putExtra("email", Email);
                    intent.putExtra("pw", PasswordConfirmText.getText().toString());
                    intent.putExtra("login_type", "general");
                    startActivity(intent);
                }
            }
        });

        EmailSecurityConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String Security_code = EmailSecurityText.getText().toString();
                //security_Code를 서버로 전송하고
                //Security_code_check true or false
                //true이면 EmailSecurityInformText.setText(인증되었씁니다)로 바꿔주기
                //true이면 EmailSecurityInformText.setText(인증번호가 일치하지않습니다)로 바꿔주기
            }
        });

        EmailSecurityCodeSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //인증번호 서버에 요철하기.
                //요철하고 나면 EmailSecurityCodeSendButton.setText("재전송") 으로 바꿔주기
                //요청하면 타이머 시작
                startTimerTask();
            }
        });
    }

    private void startTimerTask(){

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