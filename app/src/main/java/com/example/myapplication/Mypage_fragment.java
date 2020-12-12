package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Thread.ThreadTask;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.android.material.textview.MaterialTextView;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mypage_fragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Mypage_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MaterialTextView logoutView;
    private MaterialTextView Account_delete_View;
    private MaterialTextView userinform;
    private MaterialTextView ChangPw;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String ip;

    private String Email;
    private String login_type;
    private String first_kakao_login;
    private String first_naver_login;
    private SharedPreferences login_information_pref;
    private SharedPreferences login_log_pref;

    private SharedPreferences.Editor login_infromation_editor;
    private SharedPreferences.Editor login_log_editor;

    private static Context mcontext;
    private static Activity mActivity;
    private static Mypage_fragment Instance;
    private boolean isSuccessDeleteToken = false;
    static OAuthLogin mOAuthLoginModule;

    private MaterialTextView EmailView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param activity Parameter 1.
     * @return A new instance of fragment Mypage_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Mypage_fragment newInstance(Activity activity) {
        Mypage_fragment fragment = new Mypage_fragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1);
        //args.putString(ARG_PARAM2);
        //fragment.setArguments(args);
        return fragment;
    }

    public Mypage_fragment() {
       // mActivity = activity;
        // Required empty public constructor
        //this.mcontext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity){
            mActivity = (Activity)context;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mypage_fragment, container, false);
        mOAuthLoginModule = OAuthLogin.getInstance();
        ip = getResources().getString(R.string.server_ip);
        //유저 정보가 담겨있는 shared preference
        login_information_pref = getActivity().getSharedPreferences("login_information", Context.MODE_PRIVATE);
        login_infromation_editor = login_information_pref.edit();


        login_type = login_information_pref.getString("login_type", "null");
        Email = login_information_pref.getString("email", Email);

        EmailView = v.findViewById(R.id.Email);
        EmailView.setText(Email);
        //SNS 로그인시에 첫 로그인인지 아닌지 확인하기 위한 preference
        login_log_pref = getActivity().getSharedPreferences("SNS_login_log", Context.MODE_PRIVATE);
        login_log_editor = login_log_pref.edit();

        //first_kakao_login = login_log_pref.getString("first_kakao_login","true");
        //first_naver_login = login_log_pref.getString("first_naver_login","true");

        //mcontext = container.getContext();

        userinform = (MaterialTextView)v.findViewById(R.id.userinform);
        userinform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_inform.class);
                startActivity(intent);
            }
        });

        ChangPw = (MaterialTextView)v.findViewById(R.id.ChangPw);
        ChangPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Change_PW.class);
                startActivity(intent);
            }
        });
        logoutView = (MaterialTextView)v.findViewById(R.id.logout_button);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadTask<Object> sign_out_result = getThreadTask(Email, "/sign_out");
                sign_out_result.execute(ip);

                if(sign_out_result.getResult() == 1){
                    //이메일 양식 에러
                }
                else if(sign_out_result.getResult() == 2){
                    //로그인이 안되어 있는 상태(오류)
                }
                else if(sign_out_result.getResult() == 3){
                    //로그아웃 성공
                    //카카오 로그아웃
                    if (login_type.equals("kakao")) {
                        Log.e("Main Logout", "로그아웃 성공");
                        //System.out.println("첫번째 menu");
                        //Toast.makeText(getContext(), "asdfasdfasdf", Toast.LENGTH_SHORT).show();
                        UserManagement.getInstance()
                                .requestLogout(new LogoutResponseCallback() {
                                    @Override
                                    public void onCompleteLogout() {
                                        login_infromation_editor.clear();
                                        //editor.putString("first_login", "false");
                                        login_infromation_editor.commit();

                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                    }
                    //네이버 로그아웃
                    else if (login_type.equals("naver")) {
                        //System.out.println("두번째 menu");
                        OAuthLogin mOAuthLogin = OAuthLogin.getInstance();
                        String loginState = mOAuthLogin.getState(getActivity()).toString();
                        if(!loginState.equals("NEED_LOGIN")){
                            Log.e("Main Logout", "로그아웃 성공");
                            mOAuthLogin.logout(getActivity());

                            login_infromation_editor.clear();
                            login_infromation_editor.commit();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        }
                        else{
                            Log.e("Main Logout", "로그아웃 실패");
                        }
                    }
                    else if (login_type.equals("general")){
                        login_infromation_editor.clear();
                        login_infromation_editor.commit();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
                else if(sign_out_result.getResult() == 0){
                    //시스템 에러
                }


            }
        });
        Account_delete_class account_delete_listener = new Account_delete_class();
        Account_delete_View = (MaterialTextView)v.findViewById(R.id.account_delete);
        Account_delete_View.setOnClickListener(account_delete_listener);

        return v;
    }
    // 이메일 주소를 보내서 받을 결과값 request
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
                URL url = new URL(urls[0] + Router_name);

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

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("알림")
                .setMessage("회원 탈퇴시 계정의 모든 정보가 손실됩니다.\n정말로 탈퇴하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    @Override public void onClick(DialogInterface dialogInterface, int i)
                    {

                        ThreadTask<Object> account_deletion_result = getThreadTask(Email, "/account_deletion");
                        account_deletion_result.execute(ip);

                        if(account_deletion_result.getResult() == 1){
                            //이메일 양식에러
                        }
                        else if(account_deletion_result.getResult() == 2){
                            //로그인이 안되어있는 상태
                        }
                        else if(account_deletion_result.getResult() == 3){
                            //계정 삭제 완료
                            if (login_type.equals("kakao")) { // 카카오 회원탈퇴
                                Log.e("Account_delete_class", "들어옴");
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
                                                login_infromation_editor.clear();
                                                login_infromation_editor.commit();

                                                login_log_editor.remove("first_kakao_login");
                                                login_log_editor.commit();
                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();

                                                Toast.makeText(getActivity(), "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            //네이버 회원탈퇴퇴
                            else if (login_type.equals("naver")) {

                                ThreadTask<Object> result = new ThreadTask<Object>() {

                                    @Override
                                    protected void onPreExecute() {// excute 전에

                                    }

                                    @Override
                                    protected void doInBackground(String... urls) throws IOException, JSONException {//background로 돌아갈것
                                        Log.e("delete_account", "log1");
                                        if(LoginActivity.mContext == null){
                                            //Toast.makeText(getActivity(), "null입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Log.e("delete_account", "log2");
                                            isSuccessDeleteToken = mOAuthLoginModule.logoutAndDeleteToken(LoginActivity.mContext);
                                            if(isSuccessDeleteToken) {
                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();

                                                login_infromation_editor.clear();
                                                login_infromation_editor.commit();

                                                login_log_editor.remove("first_naver_login");
                                                login_log_editor.commit();
                                            }
                                        }
                                    }

                                    @Override
                                    protected void onPostExecute() {
                                        if(isSuccessDeleteToken) {
                                            SharedPreferences pref = getActivity().getSharedPreferences("NaverOAuthLoginPreferenceData", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("first_login" , "true");
                                            editor.commit();
                                            Toast.makeText(getActivity(), "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public int getResult() {
                                        return 0;
                                    }

                                    @Override
                                    public String getErrorCode() {
                                        return null;
                                    }

                                };
                                result.execute("");

                            }
                            else if (login_type.equals("general")) {
                                login_infromation_editor.clear();
                                login_infromation_editor.commit();

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                                Toast.makeText(getActivity(), "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (account_deletion_result.getResult() == 0){
                            //시스템 에러
                        }
                    } })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    } });
        AlertDialog msgDlg = msgBuilder.create(); msgDlg.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflater.inflate(R.menu.);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class Account_delete_class implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            showDialog();
        }
    }
}