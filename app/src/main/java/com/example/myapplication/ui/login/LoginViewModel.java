package com.example.myapplication.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.myapplication.data.LoginRepository;
import com.example.myapplication.data.Result;
import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.R;

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

    public void login(String username, String password) { // 로그인 버튼을 누르고 나서 결과
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password); // 로그인 결과

//        if (result instanceof Result.Success && username.equals("ljs3271@naver.com") && isUserNameValid(username)
//                && password.equals("123456") && isPasswordValid(password)) {
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) { // 로그인을 치고있을 때
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));

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