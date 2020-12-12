package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Thread.ThreadTask;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Change_PW extends AppCompatActivity {
    EditText CurrentPwView;
    EditText ChangedPWView;
    EditText ChangedPW2View;

    String currentPw;
    String changedPw;
    String changedPw2;
    String Email;
    private SharedPreferences login_information_pref;
    String ip;



    Button ConfirmButton;

    private ActionBar actionBar;
    private MaterialToolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__p_w);

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

        CurrentPwView = findViewById(R.id.current_pw);
        ChangedPWView = findViewById(R.id.changed_pw);
        ChangedPW2View = findViewById(R.id.changed2_pw);

        ConfirmButton = findViewById(R.id.pw_check);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPw = CurrentPwView.getText().toString();
                changedPw = ChangedPWView.getText().toString();
                changedPw2 = ChangedPW2View.getText().toString();

                if(!changedPw.equals(changedPw2)){
                    Toast.makeText(getApplication(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                if(changedPw.equals(changedPw2)){
                    ThreadTask<Object> change_pw_result = getThreadTask(Email,currentPw,changedPw, "/ChangePW");
                    change_pw_result.execute(ip);

                    if(change_pw_result.getResult() == 0){
                       /**
                        * 비밀번호 변경실패
                        * *
                        * */
                        Toast.makeText(getApplication(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplication(), "비밀번호 변경 성공!", Toast.LENGTH_SHORT).show();
                    }
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

    private ThreadTask<Object> getThreadTask(String email, String currentPw, String changedPw, String Router_name){

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

                sendObject.put("email_address", email);
                sendObject.put("currentPw", currentPw);
                sendObject.put("changedPw", changedPw);

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
}