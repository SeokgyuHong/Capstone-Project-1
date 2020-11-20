package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.ConstraintProperties.PARENT_ID;

public class MainActivity<pirvate> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback{

    private static final String CHANNEL_ID = "1000" ;

    private String [] notepad_title = new String[100];
    private String [] notepad_content = new String[100];
    private String[] temp = new String[2];
    private ConstraintLayout Parent_layout;
    private int i = 0;
    private String title_line = null;
    private String Content_line = null;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycle_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecycleAdaptors recycleAdaptors;

    private Home_fragment home_fragment;
    private Sensor_fragment sensor_fragment;
    private Mypage_fragment mypage_fragment;
    private String ip;
    DrawerLayout drawer;

    String Title_filename = "title.txt";
    String Content_filename = "content.txt";
    private ItemTouchHelper helper;
    ArrayList<SampleData> notepadDataList = new ArrayList<>();

    private MaterialToolbar toolbar;
    private ActionBar actionBar;
    private MaterialTextView toolbartext;

    private SharedPreferences login_information_pref;
    private String Email;
    private Class<LoginActivity> loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);
        home_fragment = new Home_fragment();
        sensor_fragment = new Sensor_fragment();
        mypage_fragment = new Mypage_fragment();

        login_information_pref = getSharedPreferences("login_information", Context.MODE_PRIVATE);
        Email = login_information_pref.getString("email", "");

        ip = getString(R.string.server_ip);

        /*홈 fragment로 내용 채워줌*/
        getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();

        toolbar = (MaterialToolbar)findViewById(R.id.MainActiviy_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        toolbartext = (MaterialTextView)findViewById(R.id.toolbar_textview);

        //actionBar.setDisplayHomeAsUpEnabled(true); 뒤로가기 버튼, 현재 흰색이라서 생성해도 보이지는 않음 필요는없

        //getSupportFragmentManager().beginTransaction().replace(R.id.container , home_fragment).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.tab1:
                                //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();
                                toolbartext.setText("Home");
                                return true;
                            case R.id.tab2 :
                                //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, sensor_fragment).commit();
                                toolbartext.setText("Sensor");
                                return true;
                            case R.id.tab3 :
                                //Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, mypage_fragment).commit();
                                toolbartext.setText("Mypage");
                                return true;
                        }
                        return false;
                    }
                }
        );
        Navinit(); // navigation drawer 초기화

        //getHashKey(); // fire base 해쉬 값 받아오기


        //createNotificationChannel();


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken(); //토큰 값이 바뀌면 new token call back
                Log.e("Fire base Token", newToken.toString());
                System.out.println("Fire base Token :" + newToken);
                //new JSONTask(newToken).execute("http://10.0.2.2:3000/post");//AsyncTask 시작시킴
                makeThread(newToken);
                int response = send_token_response(Email, newToken);

                //getHeadup();
                if(response == 1){
                    Log.e("토큰 전송 : ", "이메일 형식 에러");
                }
                else if(response == 2){
                    Log.e("토큰 전송 : ", "성공");
                }
                else if(response == 3){
                    Log.e("토큰 전송 : ", "실패3");
                }
                else if(response == 4){
                    Log.e("토큰 전송 : ", "실패4");
                }
                else if(response == 0){
                    Log.e("토큰 전송 : ", "시스템에러");
                }

            }

        });

    }
    private void getHeadup(){
        Intent snoozeIntent = new Intent(this, MainActivity.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setFullScreenIntent(snoozePendingIntent, true)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        int notificationId = 10;
        notificationManager.notify(notificationId, builder.build());
    }

    private int send_token_response(String request_email, String token){

        ThreadTask<Object> result = new ThreadTask<Object>() {

            String Request_email = request_email;
            String Token = token;

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

                URL url = new URL(urls[0] +"/firebase_token_save");
                con = (HttpURLConnection) url.openConnection();

                sendObject.put("email_address",Request_email);
                sendObject.put("fcm_token",Token);

                Log.e("토큰 전송", "토큰 전송 합니당.");
                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "application/json");//서버에 response 데이터를 html로 받음
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

                OutputStream outStream = con.getOutputStream();
                outStream.write(sendObject.toString().getBytes());
                outStream.flush();
                Log.e("토큰 전송", "토큰 전송 합니당.");
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

    public void makeThread(Object newToken){
        ThreadTask<Object> result = new ThreadTask<Object>() {
            Object NewToken = newToken;
            @Override
            protected void onPreExecute() {// excute 전에

            }

            @Override
            protected void doInBackground(String... urls) {//background로 돌아갈것
                try {
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    JSONObject jsonObject = new JSONObject();

                    Object jsonStr = "tlqkftlqkf";

                    Log.e("jsonTask", "실행됐다4");
                    //jsonObject.accumulate("name", "yun");
                    HttpURLConnection con = null;
                    BufferedReader reader = null;

                    jsonObject.accumulate("token", NewToken);

                    try{
                        //URL url = new URL("http://10.0.2.2:3000/post");

                        URL url = new URL(urls[0]+"/post");
                        Log.e("Mainactivity", urls[0]+"/post");
                        con = (HttpURLConnection) url.openConnection();

                        con.setRequestMethod("POST");//POST방식으로 보냄
                        con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                        con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

                        con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                        con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                        con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

                        con.setConnectTimeout(15000);
                        con.connect();
                        //서버로 보내기위해서 스트림 만듬

                        OutputStream outStream = con.getOutputStream();
                        //버퍼를 생성하고 넣음
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                        writer.write(jsonObject.toString());
                        writer.flush();
                        writer.close();//버퍼를 받아줌

                        //서버로 부터 데이터를 받음
                        InputStream stream = con.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(stream));
                        StringBuilder buffer = new StringBuilder();
                        String line = "";
                        while((line = reader.readLine()) != null){
                            buffer.append(line);
                        }

                        //return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(con != null){
                            con.disconnect();
                        }
                        try {
                            if(reader != null){
                                reader.close();//버퍼를 닫아줌
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();//
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

        };
        result.execute(ip);

    }
    public void wrtieToFile(){

        File Title_file = new File(MainActivity.this.getFilesDir(), Title_filename);
        File Content_file = new File(MainActivity.this.getFilesDir(), Content_filename);

        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader Title_reader = null;
        try {
            Title_reader = new BufferedReader(new FileReader(Title_file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (
                BufferedReader Content_reader =
                        new BufferedReader(new FileReader(Content_file))) {

            while (true) {
                assert Title_reader != null;
                title_line = Title_reader.readLine();
                if (title_line == null ) {
                    break;
                }

                while (!(Content_line = Content_reader.readLine()).equals("=============") && Content_line != null) {
                    stringBuffer.append(Content_line).append('\n');
                    System.out.println("gdgdgd " + stringBuffer.toString());;
                }

                Content_line = stringBuffer.toString();
                System.out.println("Content_line : " + Content_line);

                notepad_title[i] = title_line;
                notepad_content[i] = Content_line;
                System.out.println("읽어온 내용" + i + " : " + notepad_title[i] + " " + notepad_content[i]);

                notepadDataList.add(new SampleData(notepad_title[i] , notepad_content[i],i)) ;
                recycleAdaptors.notifyDataSetChanged();
                stringBuffer.delete(0, stringBuffer.length());
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    public void InitializeNotepadData() {
        notepadDataList = new ArrayList<>();

       // notepadDataList.add(new SampleData("노준석", 1));
    }
    public void Navinit(){
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu1) {
            Toast.makeText(this, "첫번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            onFragmentSelected(0, null);
        } else if (id == R.id.menu2) {
            Toast.makeText(this, "두번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            onFragmentSelected(1, null);
        } else if (id == R.id.menu3) {
            Toast.makeText(this, "세번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            onFragmentSelected(2, null);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment curFragment = null;

        if (position == 0) {
           System.out.println("첫번째 menu");
            UserManagement.getInstance()
                    .requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else if (position == 1) {
            System.out.println("두번째 menu");
            OAuthLogin mOAuthLogin = OAuthLogin.getInstance();
            String loginState = mOAuthLogin.getState(MainActivity.this).toString();
            if(!loginState.equals("NEED_LOGIN")){
                Log.e("Main Logout", "로그아웃 성공");
                mOAuthLogin.logout(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Log.e("Main Logout", "로그아웃 실패");
            }
        } else if (position == 2) {
            System.out.println("세번째 menu");
            /*회원탈퇴 할 때 필요한 부분*/
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
                        public void onSuccess(Long result) {
                            Log.i("KAKAO_API", "연결 끊기 성공. id: " + result);
                        }
                    });
        }

        /*
            //getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
            만약에 메뉴 클릭시 fragment 넣을거면 이걸로함.
         */

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}



