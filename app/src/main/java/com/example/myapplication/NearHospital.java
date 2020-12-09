package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NearHospital extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MaterialToolbar toolbar;
    private ActionBar actionBar;
    private MaterialTextView toolbartext;
    private MaterialTextView PhoneCall;

    private GoogleMap GMap;

    private ArrayList<Hospital> HospitalDataList = new ArrayList<>();
    private RecycleAdaptors_hospital recycleAdaptors;
    private RecyclerView Hospital_recycler_view;
    private TextView Phone_call;

    private LinearLayoutManager layoutManager;

    private Double start_latitude;
    private Double start_longitude;
    private String target_phone;
    private String data_array;
    private JSONArray Hospital_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_hospital);

        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        toolbar = (MaterialToolbar)findViewById(R.id.MainActiviy_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        toolbartext = (MaterialTextView)findViewById(R.id.toolbar_textview);
        toolbartext.setText("피보호자 근처의 병원입니다");

//        String input = "{" +
//                "latitude: '35.8883990450972'," +
//                "longitude: '128.6104001454858'," +
//                "phone_number: '010-5555-5553'," +
//                "data_array: '[{\"hospital_name\":\"경남연합정형외과의원\",\"hospital_location\":\"대구광역시 북고 대현동 대현남로6길 11\",\"hospital_phone_num\":\"053-242-0119\",\"category\":\"외과\",\"time\":\"09:00~18:00\",\"latitude\":\"35.88122783453655\",\"longitude\":\"128.608410057474\"}" +
//                ",{\"hospital_name\":\"든든한 병원\",\"hospital_location\":\"대구광역시 동구 신암동 신암로 120\",\"hospital_phone_num\":\"053-940-3000\",\"category\":\"종합병원\",\"time\":\"09:00~18:00\",\"latitude\":\"35.88103054660772\",\"longitude\":\"128.61258938252828\"}" +
//                ",{\"hospital_name\":\"대구 파티마 병원\",\"hospital_location\":\"대구 광역시 동구 아양로 99\",\"hospital_phone_num\":\"053-940-7114\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.88422977202024\",\"longitude\":\"128.6242076358278\"}" +
//                ",{\"hospital_name\":\"대구 시티 병원\",\"hospital_location\":\"대구 광역시 북구 복현동 동북로 270\",\"hospital_phone_num\":\"053-959-7114\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.893712463929305\",\"longitude\":\"128.61971185982367\"}]'" +
//                "}";


        String input = "{" +
                "latitude: '35.86464630089416'," +
                "longitude: '128.59296105844146'," +
                "phone_number: '010-2999-6313'," +
                "data_array: '[{\"hospital_name\":\"광개토병원\",\"hospital_location\":\"대구광역시 중구 성내1동 중앙대로 366\",\"hospital_phone_num\":\"053-242-0119\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.866152697316956\",\"longitude\":\" 128.59380216598595\"}" +
                ",{\"hospital_name\":\"다정 연합병원\",\"hospital_location\":\"대구광역시 중구 봉산동 52-31\",\"hospital_phone_num\":\"053-425-4604\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.86487021645115\",\"longitude\":\"128.5957011691545\"}" +
                ",{\"hospital_name\":\"라파엘 병원\",\"hospital_location\":\"대구광역시 중구 남산동 700-11\",\"hospital_phone_num\":\"053-762-8228\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.86085586470934\",\"longitude\":\"128.59192351545732\"}" +
                ",{\"hospital_name\":\"곽병원\",\"hospital_location\":\"대구광역시 중구 성내2동 국채보상로 531\",\"hospital_phone_num\":\"053-252-2401\",\"category\":\"종합병원\",\"time\":\"08:40~17:00\",\"latitude\":\"35.871080801029485\",\"longitude\":\"128.58861867553006\"}" +
                ",{\"hospital_name\":\"으뜸병원\",\"hospital_location\":\"대구광역시 중구 수동 30\",\"hospital_phone_num\":\"053-423-0100\",\"category\":\"종합병원\",\"time\":\"09:00~19:00\",\"latitude\":\"35.87045479251812\",\"longitude\":\"128.5892410281009\"}" +
                ",{\"hospital_name\":\"든든한 병원\",\"hospital_location\":\"대구광역시 동구 신암동 신암로 120\",\"hospital_phone_num\":\"053-940-3000\",\"category\":\"종합병원\",\"time\":\"09:00~18:00\",\"latitude\":\"35.88103054660772\",\"longitude\":\"128.61258938252828\"}"+
                ",{\"hospital_name\":\"대구 파티마 병원\",\"hospital_location\":\"대구 광역시 동구 아양로 99\",\"hospital_phone_num\":\"053-940-7114\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.88422977202024\",\"longitude\":\"128.6242076358278\"}"+
                ",{\"hospital_name\":\"경남연합정형외과의원\",\"hospital_location\":\"대구광역시 북고 대현동 대현남로6길 11\",\"hospital_phone_num\":\"053-242-0119\",\"category\":\"외과\",\"time\":\"09:00~18:00\",\"latitude\":\"35.88122783453655\",\"longitude\":\"128.608410057474\"}"+
                ",{\"hospital_name\":\"대구 시티 병원\",\"hospital_location\":\"대구 광역시 북구 복현동 동북로 270\",\"hospital_phone_num\":\"053-959-7114\",\"category\":\"종합병원\",\"time\":\"24시간 영업\",\"latitude\":\"35.893712463929305\",\"longitude\":\"128.61971185982367\"}]'"+
                "}";


        //{\"hospital_name\":\"신세계여성병원\",\"hospital_location\":\"대구광역시 북구 산격3동 1287-4\",\"hospital_phone_num\":\"+82539547771\",\"category\":\"여성병원\",\"time\":\"09:00~18:00\",\"latitude\":\"35.897941271089294\",\"longitude\":\"128.613653306951\"}" +
        //                ",
        //Log.e("NearHospital", input);
        Hospital_recycler_view = findViewById(R.id.Hospital_Recycler_view);
        layoutManager =  new LinearLayoutManager(NearHospital.this);
        if(layoutManager != null){

            Hospital_recycler_view.setLayoutManager(layoutManager);
        }
        else{
            Log.e("SensorFragment", "Error");
        }
//        JsonParser parser = new JsonParser();
//        Object obj = parser.parse(input);
        JSONObject input_object = null;
        try {
            input_object = new JSONObject(input);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Intent intent = getIntent();
        try {
            start_latitude = Double.parseDouble(input_object.getString("latitude"));
            start_longitude = Double.parseDouble(input_object.getString("longitude"));
            Log.e("NearHospital", Double.toString(start_latitude));
            Log.e("NearHospital", Double.toString(start_longitude));
            target_phone = input_object.getString("phone_number");
            data_array = input_object.getString("data_array");
            Hospital_array = null;
            Hospital_array = new JSONArray(data_array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0 ; i < Hospital_array.length() ; i++) {
            try {
                JSONObject hospital_data = Hospital_array.getJSONObject(i);
                String hospital_name = hospital_data.getString("hospital_name");
                String hospital_location = hospital_data.getString("hospital_location");
                String hospital_phone_num = hospital_data.getString("hospital_phone_num");
                String category = hospital_data.getString("category");
                String hospital_time = hospital_data.getString("time");
                String latitude = hospital_data.getString("latitude");
                String longitude = hospital_data.getString("longitude");
                Log.e("tqtqtqtqtqtq", "asdfsdgfsdgfsdfgsdfgd");
                HospitalDataList.add(new Hospital(hospital_name, hospital_location, hospital_phone_num
                        ,category,hospital_time, latitude, longitude));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        // 맵이 실행되면 onMapReady 실행
        mapFragment.getMapAsync(this);


        /**
         * 근처 병원 위치 다 받아오기
         * */

    }

    public com.google.android.gms.maps.GoogleMap getGoogleMap() {
        return GMap;
    }

    public void setGoogleMap(com.google.android.gms.maps.GoogleMap googleMap) {
        GMap = googleMap;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        setGoogleMap(googleMap);
        GMap.setOnMarkerClickListener(this);
        // 구글에서 등록한 api와 엮어주기
        // 시작위치를 서울 시청으로 변경
        /**
         * 센서 위치 시작 지점 받아오기
         * */

        LatLng Starting_Point = new LatLng(start_latitude, start_longitude); // 스타팅 지점
        GMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Starting_Point, 14));
        //GMap.animateCamera(CameraUpdateFactory.zoomTo(14));// 키우면 더 확대

        // 시작시 마커 생성하기 누르면 title과 snippet이 뜬다.
        MarkerOptions markerOptions = new MarkerOptions();
        double Latitude;
        double Longtitude;

//        HospitalDataList.add(new Hospital("대구 파티마 병원",
//                "대구광역시 동구 아양로 99",
//                "053-940-7114", "24시간 영업", true, "35.88422977202024", "128.6242076358278"));
//
//        HospitalDataList.add(new Hospital("대구 시티 병원",
//                "대구광역시 북구 복현동 동북로 270",
//                "053-959-7114", "24시간 영업", true, "35.893712463929305", "128.61971185982367"));
//
//        HospitalDataList.add(new Hospital("든든한 병원",
//                "대구광역시 동구 신암동 신암로 120",
//                "053-940-3000", "09:00~18:00", true, "35.88103054660772", "128.61258938252828"));
//
//        HospitalDataList.add(new Hospital("경남연합정형외과의원",
//                "대구광역시 북구 대현동 대현남로6길 11",
//                "053-242-0119", "09:00~18:00", true, "35.88122783453655", "128.608410057474"));
//
//        HospitalDataList.add(new Hospital("신세계여성병원",
//                "대구광역시 북구 산격3동 1287-4",
//                "+82539547771", "09:00~18:00", true, "35.897941271089294", "128.613653306951"));

        Marker marker = null;
        for(int i = 0 ; i < HospitalDataList.size() ; i++){
            Latitude = Double.parseDouble(HospitalDataList.get(i).getLatitude());
            Longtitude = Double.parseDouble(HospitalDataList.get(i).getLongtitude());

//            markerOptions.position(new LatLng(Latitude,Longtitude));
//            markerOptions.title(HospitalDataList.get(i).getHospital_name());
            //markerOptions.snippet(Hos);
            // 생성된 마커 옵션을 지도에 표시

            //marker = GMap.addMarker(markerOptions);
            //marker.showInfoWindow();
            marker = GMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Latitude,Longtitude))
                    .title(HospitalDataList.get(i).getHospital_name() ));
        }
        marker.showInfoWindow();
        // 서울광장마커

        // 회사 DB에 데이터를 가지고 있어야 된다.
//        LatLng plaza = new LatLng(37.565785, 126.978056);
//        markerOptions.position(plaza);
//        markerOptions.title("광장");
//        markerOptions.snippet("서울 광장");
//        GMap.addMarker(markerOptions);

        //맵 로드 된 이후
        GMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //Toast.makeText(NearHospital.this, "Map로딩성공", Toast.LENGTH_SHORT).show();
            }
        });

        //카메라 이동 시작
        GMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                //Log.d("set>>","start");
            }
        });

        // 카메라 이동 중
        GMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //Log.d("set>>","move");
            }
        });

        // 지도를 클릭하면 호출되는 이벤트
        GMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 기존 마커 정리
                //googleMap.clear();
                // 클릭한 위치로 지도 이동하기
                GMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // 신규 마커 추가
//                MarkerOptions newMarker=new MarkerOptions();
//                newMarker.position(latLng);
//                googleMap.addMarker(newMarker);
            }
        });

        recycleAdaptors = new RecycleAdaptors_hospital(getGoogleMap());
        //HospitalDataList = recycleAdaptors.getItems();


//        HospitalDataList.add(new Hospital("동산병원",
//                "대구 광역시 중구삼덕동4가 동덕로 130-11",
//                "053-986-153", "내과", true, latitude, longtitude));
//        HospitalDataList.add(new Hospital("영남대학교 병원",
//                "대구 광역시 중구삼덕동99가 동덕로 130-123",
//                "053-941-823", "신경과", true, latitude, longtitude));
        //ArrayList<Sensor_list> sensorDataList = recycleAdaptors.getItems();
        //sensorDataList.add(new Sensor_list("12:34:56:78", "준덕이 센서", "휴하우스" ));
        recycleAdaptors.setItems(HospitalDataList);
        Hospital_recycler_view.setAdapter(recycleAdaptors);

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
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //System.out.println(layoutManager.canScrollVertically());
                int position = 0;
                for( Hospital hospital : HospitalDataList ){
                    if(hospital.getHospital_name().equals(marker.getTitle())){
                        System.out.println(hospital.getHospital_name());
                        Hospital_recycler_view.smoothScrollToPosition(position);
                    }
                    position++;
                }
            }
        }, 200);
        return false;
    }
}