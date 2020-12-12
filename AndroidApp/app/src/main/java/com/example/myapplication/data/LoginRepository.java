package com.example.myapplication.data;

import com.example.myapplication.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

/**
*
 *원격 데이터 원본에서 인증 및 사용자 정보를 요청하고 로그인 상태 및 사용자 자격 증명 정보의 메모리 내 캐시를 유지하는 클래스./
*/
public class LoginRepository {

    private static volatile LoginRepository instance;
    private  Result<LoggedInUser> result;
    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        if ( username.equals("ljs3271@naver.com") && password.equals("123456") ) {
            result = dataSource.login(username, password); // 로그인된 정보를 저장한다
        }
        if (result instanceof Result.Success) { /**result가 result.succes의 instance이면 */
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}