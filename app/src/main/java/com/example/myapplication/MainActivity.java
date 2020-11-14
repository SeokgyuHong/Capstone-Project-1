package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
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

    DrawerLayout drawer;

    String Title_filename = "title.txt";
    String Content_filename = "content.txt";
    private ItemTouchHelper helper;
    ArrayList<SampleData> notepadDataList = new ArrayList<>();

    private MaterialToolbar toolbar;
    private ActionBar actionBar;
    private MaterialTextView toolbartext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_fragment = new Home_fragment();
        sensor_fragment = new Sensor_fragment();
        mypage_fragment = new Mypage_fragment();

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
                                toolbartext.setText("홈");
                                return true;
                            case R.id.tab2 :
                                //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, sensor_fragment).commit();
                                toolbartext.setText("센서");
                                return true;
                            case R.id.tab3 :
                                //Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, mypage_fragment).commit();
                                toolbartext.setText("마이 페이지");
                                return true;
                        }
                        return false;
                    }
                }
        );
        Navinit(); // navigation drawer 초기화


        //getHashKey(); // fire base 해쉬 값 받아오기

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Object newToken = instanceIdResult.getToken(); //토큰 값이 바뀌면 new token call back
                Log.e("Fire base Token", newToken.toString());
                System.out.println("Fire base Token :" + newToken);
                //new JSONTask(newToken).execute("http://10.0.2.2:3000/post");//AsyncTask 시작시킴
                makeThread(newToken);
            }

        });

//        //this.InitializeNotepadData();
//        recyclerView = (RecyclerView) findViewById(R.id.rv);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        recycleAdaptors = new RecycleAdaptors(notepadDataList);
//
//        recyclerView.setAdapter(recycleAdaptors);
//
//        SwipeHelper swipeHelper = new SwipeHelper(recycleAdaptors, this, recyclerView) {
//
//            @SuppressLint("UseCompatLoadingForDrawables")
//            @Override
//            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "삭제",
//                        getResources().getDrawable(R.drawable.ic_delete_24px, null),
//                        R.drawable.ic_delete_24px,
//                        Color.parseColor("#FF3C30"),
//                        new SwipeHelper.UnderlayButtonClickListener() {
//                            @Override
//                            public void onClick(int pos) {
//                                // TODO: onDelete
//                            }
//                        }
//                )
//                );
//                wrtieToFile();
//            }
//
//        };
//
//       // ItemTouchHelper 생성
//        //helper = new ItemTouchHelper(new ItemTouchHelperCallback(recycleAdaptors, Title_filename, Content_filename ));
//        //RecyclerView에 ItemTouchHelper 붙이기
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//
//        //swipeHelper.attachToRecyclerView(recyclerView);
//
//        MaterialButton button = (MaterialButton) findViewById(R.id.create_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (i >= 100) { // 수용가능한 메모 개소
//                    Toast.makeText(MainActivity.this, "메모는 최대 8개 입니다.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//        });

        // 뒤로가기 누르면 세션이 그대로 남아있음 리다이렉트 해야함
        //Recycle view 써서 swipe edit delete 만들기

    }

    public void makeThread(Object newToken){
        new ThreadTask<String>() {
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

                        URL url = new URL(urls[0]);
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
        }.execute("http://10.0.2.2:3000/post");
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.action_save:
                Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
            default :
                return super.onOptionsItemSelected(item);
        }
    }*/

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

}



