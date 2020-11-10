package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText EmailText;
    private EditText PasswordText;
    private EditText PasswordConfirmText;
    private Button EmailcheckButton;
    private Button Confirm_button;
    private String Email;
    private String duplication_check_result ="";
    private boolean Pw_match_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailText = (EditText)findViewById(R.id.email);
        PasswordText = (EditText)findViewById(R.id.pw);
        PasswordConfirmText = (EditText)findViewById(R.id.pw_confirm);
        EmailcheckButton = (Button)findViewById(R.id.EmailCheck);
        Confirm_button = (Button)findViewById(R.id.confirm_button);

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
                    startActivity(intent);
                }
            }
        });

    }
}