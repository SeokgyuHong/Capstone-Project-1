package com.example.myapplication.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.myapplication.Thread.ThreadTask;
import com.example.myapplication.data.LoginRepository;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String ip) { // 로그인 버튼을 누르고 나서 결과
        // can be launched in a separate asynchronous job

        //Result<LoggedInUser> result = loginRepository.login(username, password); // 로그인 결과
        ThreadTask<Object> request = getThreadTask_normal_sign_in(username, password, "/normal_sign_in");
        request.execute(ip);
//        if (result instanceof Result.Success && username.equals("ljs3271@naver.com") && isUserNameValid(username)
//                && password.equals("123456") && isPasswordValid(password)) {
        //username, password를 전송하고 , 결과값을 보고 true이면 mainactivity로 false이면 올바르지 않은 계정이라고
        // 이 때 로그인 성공시 email, name 을 sharedpreference에 저장. login type 은 general로


        if (request.getResult() == 1) {
            //이메일 양식 에러
            loginResult.setValue(new LoginResult(R.string.give_me_right_email_form));
        }
        else if(request.getResult() == 2){
             //등록되지 않은 이메일
            loginResult.setValue(new LoginResult(R.string.No_register_emailorpassword));
        }
        else if(request.getResult() == 3){
            //로그인성공
            Log.e("LoginViewModel", "asdfasdfasdf");
            //LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView("")));
        }
        else if(request.getResult() == 4){
            //등록되지 않은 이메일
            loginResult.setValue(new LoginResult(R.string.No_register_emailorpassword));
        }
        else {
            //시스템 에러
            loginResult.setValue(new LoginResult(R.string.system_error));
        }

//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }

    }

    private ThreadTask<Object> getThreadTask_normal_sign_in(String email, String pw, String Router_name){

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

    public void loginDataChanged(String username, String password) { // 로그인을 치고있을 때
        if (!isUserNameValid(username)) {
            //loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            //loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));

        } else {
            loginFormState.setValue(new LoginFormState(true));

        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) { // id가 유효한지
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) { // pw가 유효한지
        return password != null && password.trim().length() > 5;
    }
}