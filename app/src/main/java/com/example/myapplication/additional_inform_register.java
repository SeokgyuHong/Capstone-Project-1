package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

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
import java.net.URL;

public class additional_inform_register extends AppCompatActivity {

    private String ip;
    private EditText Institution_name_view;
    private EditText Institution_phone_number_view;
    private EditText Institution_street_name_address_view;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_inform_register);

        ip = getResources().getString(R.string.server_ip);

        Institution_name_view = (EditText) findViewById(R.id.institution_name);
        Institution_phone_number_view = (EditText) findViewById(R.id.institution_phone_number);
        Institution_street_name_address_view = (EditText) findViewById(R.id.institution_street_name_address);
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
                    new ThreadTask<Object>() {
                        @Override
                        protected void onPreExecute() {// excute 전에
                            Log.e("ip_check", ip+"/account_create");
                        }

                        @Override
                        protected void doInBackground(String... urls) throws IOException, JSONException {//background로 돌아갈것
                            HttpURLConnection con = null;
                            JSONObject sendObject = new JSONObject();
                            BufferedReader reader = null;

                            if(login_type.equals("general")){
                                ip = urls[0]+"/general_account_create";
                            }
                            else{
                                ip = urls[0]+"/sns_account_create";
                            }
                            URL url = new URL(ip);
                            con = (HttpURLConnection) url.openConnection();
                            if(login_type.equals("general")){
                                sendObject.put("pw",intent.getStringExtra("pw"));
                            }
                            sendObject.put("email",email);
                            sendObject.put("institution_name", institution_name);
                            sendObject.put("institution_phone_number",institution_phone_number);
                            sendObject.put("address",address);

                            con.setRequestMethod("POST");//POST방식으로 보냄
                            con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                            con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

                            con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                            con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                            con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                            Log.e("tqeteqw", "2123123");
                            con.setConnectTimeout(15000);
                            con.connect();
                            Log.e("tqeteqw", "2345345345");
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
                            Account_create_result = buffer.toString();
                            Log.e("additional_inform_register",Account_create_result);

                        }

                        @Override
                        protected void onPostExecute() {
                            if(Account_create_result.equals("true")){
                                SharedPreferences pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("email" , email);
                                editor.putString("login_type" , login_type);
                                editor.commit();

                                Intent intent = new Intent(additional_inform_register.this, account_register_complete.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                                //남아있는 모든 activity 지우기
                                ActivityCompat.finishAffinity(additional_inform_register.this);
                                finish();
                            }
                        }
                    }.execute(ip);
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
}