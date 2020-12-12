package com.example.myapplication.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.FindEmail;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.ResetPW;
import com.example.myapplication.Thread.ThreadTask;
import com.example.myapplication.Tutorial;
import com.example.myapplication.Utils;
import com.example.myapplication.additional_inform_register;
import com.example.myapplication.data.LoginRepository;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;

public class LoginActivity extends AppCompatActivity  {
    /*
    * sns 연동시 check 해야할 사항 :
    * 1. naver : mOAuthLoginModule.getState(mContext).toString() -> OK이면 로그인 session이 있다.
    * 2. kakao : Kakao_session.checkAndImplicitOpen() -> true이면 로그인 session이 있다.
    *
    * 회원의 네이버아이디는 출력결과에 포함되지 않습니다.
    *  대신 프로필조회 api 호출 결과에 포함되는 'id'라는 값을 이용해서 회원을 구분하시길 바랍니다.
    * 'id'값은 각 애플리케이션마다 회원 별로 유니크한 값으로,
    * 같은 네이버 회원이라도 네아로를 적용한 애플리케이션이 다르면 id값이 다른 점 유념하시길 바랍니다
    */

    private LoginViewModel loginViewModel;
    private Button btn_custom_login;
    private Button btn_custom_login_out;
    private SessionCallback sessionCallback;
    public static Context loginContext;
    private static Activity loginActivity;
    static OAuthLogin mOAuthLoginModule;
    private static String OAUTH_CLIENT_ID = "Tu7qkulSTOAWYaEYnMyJ";
    private static String OAUTH_CLIENT_SECRET = "swpY8wkE0P";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";
    public static Context mContext;
    private String apiURL;
    private String Naver_profile_ReadBody;
    private static String Email;
    private String first_naver_login;
    private String naver_access_token_check;
    private String ip;
    private SharedPreferences login_information_pref;
    private SharedPreferences login_log_pref;

    private SharedPreferences.Editor login_infromation_editor;
    private SharedPreferences.Editor login_log_editor;

    private Button find_Email_button;
    private Button find_pw_button;

    private String kakao_Email;
    Session Kakao_session;
    private LoginRepository KakaoTalkService;
    /*
    * sns 로그인 구현시 필독
    * 로그인시 email을 서버에 전송해서 이게 db에 이미 있는지 check, 없으면 추가정보 page로 넘아가고
    * 아니면 바로 로그인.
    *
    *
    * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        mContext = this;
        ip = getString(R.string.server_ip);
        //Toast.makeText(getApplication(),ip+"/naver", Toast.LENGTH_SHORT).show();
        //session 값에서 어떤 로그인할지 정하기.. ?

        find_Email_button = findViewById(R.id.find_id);
        find_pw_button = findViewById(R.id.find_pw);

        find_Email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.myapplication", "com.example.myapplication.FindEmail"));
                startActivity(intent);
            }
        });

        find_pw_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.myapplication", "com.example.myapplication.ResetPW"));
                startActivity(intent);
            }
        });

        getHashKey();
        btn_custom_login = (Button) findViewById(R.id.btn_kakao_login_custom);

        sessionCallback = new SessionCallback(mContext);
        Kakao_session = Session.getCurrentSession();
        Kakao_session.addCallback(sessionCallback);
        Kakao_session.checkAndImplicitOpen();// 카카오 자동로그인

        login_log_pref = getSharedPreferences("SNS_login_log", Activity.MODE_PRIVATE);
        first_naver_login = login_log_pref.getString("first_naver_login", "true");

        /*네이버 로그인 구현*/
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.showDevelopersLog(true);
        mOAuthLoginModule.init(LoginActivity.this
                ,OAUTH_CLIENT_ID
                ,OAUTH_CLIENT_SECRET
                ,OAUTH_CLIENT_NAME
        );
        OAuthLoginButton mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        @SuppressLint("HandlerLeak") OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                    if (success) {// naver access이 있다면
                        String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                        String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                        Log.e("naver Token accessToken", accessToken);
                        Log.e("naver Token refreshToken", refreshToken);

                        if(!Kakao_session.checkAndImplicitOpen()){ // 카카오 세션이 없다면 page가 넘어감
                            //로그인 성공
                            Log.e("Login Check", "kakao Session이 없고 naver로그인 성공");
                            // 네이버 로그인 접근 토큰;
                            String header = "Bearer " + accessToken; // Bearer 다음에 공백 추가
                            apiURL = "https://openapi.naver.com/v1/nid/me";

                            Map<String, String> requestHeaders = new HashMap<>();
                            requestHeaders.put("Authorization", header);

                            makeThread(apiURL, requestHeaders);
                            String responseBody = null;
                            responseBody = Naver_profile_ReadBody;

                            ThreadTask<Object> result = getThreadTask_social_sign_in(Email, "naver","/social_sign_in");
                            result.execute(ip);

                            if(result.getResult() == 1){
                                //이메일 양식 에러
                            }
                            else if(result.getResult() == 2){
                                //등록되지 않은 이메일 // 회원가입 페이지로
                                //실제 구현 다 되고나면 ㅇ여기 넣기
                                if(first_naver_login.equals("true")){
                                    Intent intent = new Intent(LoginActivity.this, additional_inform_register.class);
                                    intent.putExtra("email", Email);
                                    intent.putExtra("login_type","naver");

                                    login_log_pref = getSharedPreferences("SNS_login_log", Activity.MODE_PRIVATE);
                                    login_log_editor = login_log_pref.edit();
                                    login_log_editor.putString("first_naver_login" , "false");
                                    login_log_editor.commit();
                                    Log.e("Naver_login 성공 ", Email);
                                    //intent.putExtra("pw", PasswordConfirmText.getText().toString());
                                    startActivity(intent);
                                }
                            }
                            else if(result.getResult() == 3){ // 회원가입 실패 중복된 이메일이 있ㅅ음
                                Toast.makeText(LoginActivity.this , "이미 등록된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                                mOAuthLoginModule.logoutAndDeleteToken(LoginActivity.mContext);
                                //                                login_information_pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
//                                login_infromation_editor = login_information_pref.edit();

//                                if(first_naver_login.equals("true")){
//                                    Intent intent = new Intent(LoginActivity.this, additional_inform_register.class);
//                                    intent.putExtra("email", Email);
//                                    intent.putExtra("login_type","naver");
//
//                                    login_log_pref = getSharedPreferences("SNS_login_log", Activity.MODE_PRIVATE);
//                                    login_log_editor = login_log_pref.edit();
//                                    login_log_editor.putString("first_naver_login" , "false");
//                                    login_log_editor.commit();
//                                    Log.e("Naver_login 성공 ", Email);
//                                    //intent.putExtra("pw", PasswordConfirmText.getText().toString());
//                                    startActivity(intent);
//                                }
//                                else if(first_naver_login.equals("false")){
//                                    //가져온 Email을 서버에 전송, Email이 user table에 있으면 true를 받아옴
//                                    //true이면 main activity로 넘어감
//                                    //sns_login_request(Email, "naver");
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    login_infromation_editor.putString("login_type" , "naver");
//                                    login_infromation_editor.putString("email" , Email);
//                                    login_infromation_editor.commit();
//                                    startActivity(intent);
//                                    finish();
//
//                                }
                            }
                            else if(result.getResult() == 4) { // 로그인 성공!
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                login_information_pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
                                login_infromation_editor = login_information_pref.edit();
                                login_infromation_editor.putString("login_type" , "naver");
                                login_infromation_editor.putString("email" , Email);
                                login_infromation_editor.commit();
                                startActivity(intent);
                                finish();
                            }
                            else if(result.getResult() == 0) {
                                //시스템 에러
                            }
                    }
                } else { //로그인 실패시 핸들러
                    /*String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                    String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();*/
                    Log.e("naver_login", "로그인 실패");
                }
            };
        };
        mOAuthLoginButton.setOnClickListener( //버튼 클릭시 로그인 시작, 네이버 최초 로그인
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOAuthLoginModule.startOauthLoginActivity((Activity) mContext, mOAuthLoginHandler); //로그인 시작.Intent intent = new Intent(LoginActivity.this, additional_inform_register.class);
                    }
                }
        );

        //자동으로 로그인 넘어가는거 방지
        if(!OAuthLoginState.NEED_LOGIN.equals(OAuthLogin.getInstance().getState(mContext)) && !Kakao_session.checkAndImplicitOpen()){
            mOAuthLoginModule.startOauthLoginActivity((Activity) mContext, mOAuthLoginHandler); //로그인 시작.
            Log.e("Naver Login", "자동 로그인");
        }

        //네이버 로그인 커스텀
        mOAuthLoginButton.setBgResourceId(R.drawable.ic_anyconv__naver_login_button);

        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kakao_session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);

             }
        });

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final Button RegisterButton = findViewById(R.id.register);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) { //로그인 form에 변화가 생긴다면.
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid()); // data가 valid하면 버튼의 색이 바뀐다.
                if (loginFormState.getUsernameError() != null) { //username에 에러가 발생하면
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) { //username에 에러가 발생하면
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) { //로그인 에러가 발생하면
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) { // 성공하면
                    updateUiWithUser(loginResult.getSuccess());
                    Email = usernameEditText.getText().toString();
                    login_information_pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
                    login_infromation_editor = login_information_pref.edit();
                    login_infromation_editor.putString("login_type" , "general");
                    login_infromation_editor.putString("email" , Email);
                    login_infromation_editor.commit();

                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent intent = new Intent(LoginActivity.this, Tutorial.class);
                    startActivity(intent);
                    finish();
                }
                setResult(Activity.RESULT_OK);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), ip); // 로그인
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), ip); // 로그인 시작
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private ThreadTask<Object> getThreadTask_social_sign_in(String email,String user_type, String Router_name){
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

    //네이버 사용자 정보 받아오기
    public void makeThread(String apiURL, Map<String, String> requestHeaders){
        new ThreadTask<Object>() {
            String ApiURL = apiURL;
            Map<String, String> RequestHeaders = requestHeaders;

            @Override
            protected void onPreExecute() {// excute 전에

            }

            @Override
            protected void doInBackground(String... urls) {//background로 돌아갈것
                try {
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    try{
                        //URL url = new URL("http://10.0.2.2:3000/post");
                        URL url = new URL(apiURL);
                        con = (HttpURLConnection) url.openConnection();

                        con.setRequestMethod("GET");//받기
                        for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                            con.setRequestProperty(header.getKey(), header.getValue());
                        }

                        int responseCode = con.getResponseCode();
                        //성공하면 서버에 전송함.
                        if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                            Log.e("Naver_login","정상 호출됨");
                            Naver_profile_ReadBody =  readBody(con.getInputStream());
                            JSONObject jsonObject = new JSONObject(Naver_profile_ReadBody);
                            JSONObject naver_object = jsonObject.getJSONObject("response");
                            // resonse 부분만 따로 빼낼것임 굳이 이렇게 안해도됨
                            Email = naver_object.getString("email");
                            sendProfile("naver", naver_object.getString("name"),
                                    naver_object.getString("age"), naver_object.getString("email"),
                                    naver_object.getString("gender"),naver_object.getString("birthday"));
                        } else { // 에러 발생
                            Naver_profile_ReadBody = readBody(con.getErrorStream());
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    } finally {
                        if(con != null){
                            con.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute() {

            }

            @Override
            public int getResult() {
                return 0;
            }

            @Override
            public String getErrorCode() {
                return null;
            }
        }.execute(apiURL);
    }

    /* naver 회원정보 조회*/
    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private void sendProfile(String social_type, String name, String age, String email, String gender, String birthday) throws IOException, JSONException {
        /*카카오 네이버 서버 보내기 합치기*/
        HttpURLConnection con = null;
        JSONObject sendObject = new JSONObject();
        BufferedReader reader = null;

        URL url = new URL(ip+"/"+social_type);
        Log.e("SendProfile : ", social_type);
        con = (HttpURLConnection) url.openConnection();

        sendObject.put("name", name);
        sendObject.put("age", age);
        sendObject.put("email",email);
        sendObject.put("gender", gender);
        sendObject.put("birthday", birthday);

        con.setRequestMethod("POST");//POST방식으로 보냄
        con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
        con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

        con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
        con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
        con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

        con.setConnectTimeout(3000);
        con.connect();
        Log.e("naver profile", "profile 정보 서버 전송중");

        OutputStream outStream = con.getOutputStream();
        //버퍼를 생성하고 넣음
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
        writer.write(sendObject.toString());
        writer.flush();
        writer.close();//버퍼를 받아줌
        Log.e("naver profile", "profile 정보 서버 전송중");
        //서버로 부터 데이터를 받음
        InputStream stream = con.getInputStream();;
        ByteArrayOutputStream baos = null;

        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buffer = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null){
            buffer.append(line);
        }
        Log.e("sns_받은 데이터", buffer.toString());
        Log.e("naver profile", "profile 정보 서버 전송 완료");
    }

    //카카오톡, 네이버 social_sign_in
    private int sns_login_request(String request_email, String social_type){

        ThreadTask<Object> result = new ThreadTask<Object>() {
            String Request_email = request_email;
            String Social_type = social_type;
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

                URL url = new URL(urls[0] +"/social_sign_in");
                con = (HttpURLConnection) url.openConnection();

                sendObject.put("email",Request_email);

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
                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream stream = con.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;

                    while ((nLength = stream.read(byteBuffer, 0, byteBuffer.length)) != -1){
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    String response = new String(byteData);

                    JSONObject responseJSON = new JSONObject(response);
                    this.response_result = (Integer) responseJSON.get("result");
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

        result.execute(ip);
        return result.getResult();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        //String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
      //  Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
    //동의를 해야 사용자 정보를 얻어올 수 있따.
    private void getAgree(){
        List<String> scopes = Arrays.asList("account_email", "profile", "gender", "age_range", "birthday");

        Session.getCurrentSession()
                .updateScopes(this, scopes, new AccessTokenCallback() {
                    @Override
                    public void onAccessTokenReceived(AccessToken accessToken) {
                        Log.e("KAKAO_SESSION", "새로운 동의항목 추가 완료");

                        // 요청한 scope이 추가되어 토큰이 재발급 됨

                        // TODO: 사용자 동의 획득 이후 프로세스
                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_SESSION", "사용자 동의 실패: " + errorResult);
                    }
                });
    }

    public class SessionCallback implements ISessionCallback { // 카카오 로그인
        private Context mContext;
        //private MeV2ResponseCallback responseCallback;

        private UserAccount kakaoAccount;
        private KakaoResponseCallback kakaoResponseCallback;

        public SessionCallback(Context mContext){
            this.mContext = mContext;
        }
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            //System.out.println("tlqkfktkqtkqktqk");
                if(OAuthLoginState.NEED_LOGIN.equals(OAuthLogin.getInstance().getState(mContext))) {
                    Log.e("Login Activitiy", "Naver login 안되어있음.");
                    getAgree();
                    kakao_user_information_request();
            }
            //((LoginActivity) LoginActivity.loginContext).method1();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 카카오 사용자 정보 요청
        public void kakao_user_information_request() {
            kakaoResponseCallback = new KakaoResponseCallback();
            UserManagement.getInstance().me(kakaoResponseCallback);
        }

        public class KakaoResponseCallback extends MeV2ResponseCallback{
           // public String kakao_Email;
           // private UserAccount kakaoAccount;

            public UserAccount getKakaoAccount(){
                return kakaoAccount;
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Log.e("KAKAO_API", "사용자 아이디: " + result.getId());

                kakaoAccount = result.getKakaoAccount();
                System.out.println("kakaoAccount.emailNeedsAgreement()" + kakaoAccount.emailNeedsAgreement());

                if (kakaoAccount != null) {
                    kakao_Email = kakaoAccount.getEmail();
                    //setEmail(kakaoAccount.getEmail());
                    Log.e("KAKAO_API", "kakaAccount null 아님");
                    Log.e("KAKAO_API", kakao_Email);
                    if (kakao_Email != null) {
                        Log.e("KAKAO_API", "email: " + kakao_Email);
                    } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                        Log.e("kakao Profile", "email 획득 가능");
                        // 동의 요청 후 이메일 획득 가능
                        // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                    } else {
                        // 이메일 획득 불가
                        Log.e("kakao Profile", "email 획득 불가");
                    }
                    // 프로필
                    Profile profile = kakaoAccount.getProfile();
                    if (profile != null) {
                        String age = kakaoAccount.getAgeRange().getValue();
                        String birthday = kakaoAccount.getBirthday();
                        String gender =kakaoAccount.getGender().getValue();
                        String name = kakaoAccount.getProfile().getNickname();

                        login_log_pref = getSharedPreferences("SNS_login_log", Activity.MODE_PRIVATE);
                        String first_kakao_login = login_log_pref.getString("first_kakao_login", "true");

                        login_log_editor = login_log_pref.edit();

                        ThreadTask<Object> social_sign_in_result = getThreadTask_social_sign_in(kakao_Email, "kakao","/social_sign_in");
                        social_sign_in_result.execute(ip);

                        if(social_sign_in_result.getResult() == 1){
                            //이메일 형식 에러
                        }
                        else if(social_sign_in_result.getResult() == 2){
                            //등록되지 않은 이메일
                            //나중에 주석 풀기

//                         Intent intent = new Intent(LoginActivity.this, additional_inform_register.class);
//                            intent.putExtra("email", kakao_Email);
//                            intent.putExtra("login_type","kakao");
//
//                            login_log_editor = login_log_pref.edit();
//                            login_log_editor.putString("first_kakao_login" , "false");
//                            login_log_editor.commit();
//                            Log.e("Kakao_login 성공 ", "tqtqtqtqtqtqtqtq");
//                            //intent.putExtra("pw", PasswordConfirmText.getText().toString());
//                            startActivity(intent);
//
                        if(first_kakao_login.equals("true")){
                            Intent intent = new Intent(LoginActivity.this, additional_inform_register.class);
                            intent.putExtra("email", Email);
                            intent.putExtra("login_type","kakao");

                            login_log_editor = login_log_pref.edit();
                            login_log_editor.putString("first_kakao_login" , "false");
                            login_log_editor.commit();
                            Log.e("Kakao_login 성공 ", "tqtqtqtqtqtqtqtq");
                            //intent.putExtra("pw", PasswordConfirmText.getText().toString());
                            startActivity(intent);
                         }
                        }
                        else if(social_sign_in_result.getResult() == 3){
                            //등록된 이메일이 있음
                            Toast.makeText(LoginActivity.this , "이미 등록된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();

                            UserManagement.getInstance()
                                    .requestUnlink(new UnLinkResponseCallback() {
                                        @Override
                                        public void onSessionClosed(ErrorResult errorResult) {
                                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                                        }

                                        @Override
                                        public void onFailure(ErrorResult errorResult) {
                                            Log.e("KAKAO_API", "연결 끊기 실패: " + errorResult);

                                        }
                                        @Override
                                        public void onSuccess(Long result) {// 카카오 회원탈퇴 성공시
                                            Log.i("KAKAO_API", "연결 끊기 성공. id: " + result);
                                        }
                                    });

//                            if(first_kakao_login.equals("true")){
//                                Intent intent = new Intent(LoginActivity.this, additional_inform_register.class);
//                                intent.putExtra("email",kakao_Email);
//                                intent.putExtra("login_type","kakao");
//
//                                login_log_editor = login_log_pref.edit();
//                                login_log_editor.putString("first_kakao_login" , "false");
//                                login_log_editor.commit();
//                                Log.e("Kakao_login 성공 ", "tqtqtqtqtqtqtqtq");
//                                //intent.putExtra("pw", PasswordConfirmText.getText().toString());
//                                startActivity(intent);
//                            }
//                            else{
//                                login_information_pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
//                                login_infromation_editor = login_information_pref.edit();
//                                login_infromation_editor.putString("login_type" , "kakao");
//                                login_infromation_editor.putString("email" , kakao_Email);
//                                login_infromation_editor.commit();
//
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);//  성공하고 다음페이지로 넘어감
//                                finish();
//                            }
                        }
                        else if(social_sign_in_result.getResult() == 4){
                            //로그인 성공!
                        login_information_pref = getSharedPreferences("login_information", Activity.MODE_PRIVATE);
                        login_infromation_editor = login_information_pref.edit();
                        login_infromation_editor.putString("login_type" , "kakao");
                        login_infromation_editor.putString("email" , kakao_Email);
                        login_infromation_editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);//  성공하고 다음페이지로 넘어감
                        finish();
                        }
                        else if(social_sign_in_result.getResult() == 0){
                            //시스템 에러
                        }

                    }
                    else{
                        Log.e("Login Activitiy", "Naver login 되어있음.");
                    }

                }
                else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                        // 동의 요청 후 프로필 정보 획득 가능

                    }
                else {
                        // 프로필 획득 불가
                        Log.e("kakao Profile", "프로필 획득 불가");
                    }
                }


            public String getEmail() {
                return kakao_Email;
            }

        }
    }



}