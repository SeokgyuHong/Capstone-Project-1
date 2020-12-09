package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.Thread.ThreadTask_temp_sensor_list;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sensor_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sensor_fragment extends Fragment {

    private String sensor_ip;
    private ArrayList<Sensor_list> SensorDataList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private RecyclerView recyclerView;
    //private RecyclerView.Adapter recycle_adapter;
    private RecycleAdaptors recycleAdaptors;
    private ImageView Sensor_create_button;
    private MaterialTextView toolbartext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences login_information_pref;
    private String Email;

    public Sensor_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sensor_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Sensor_fragment newInstance(String param1, String param2) {
        Sensor_fragment fragment = new Sensor_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sensor_fragment, container, false);
        RecyclerView.LayoutManager layoutManager;

        sensor_ip = getResources().getString(R.string.sensor_ip);
        recyclerView = (RecyclerView)v.findViewById(R.id.Recycler_view);

        layoutManager =  new LinearLayoutManager(getActivity());
        if(layoutManager != null){
            recyclerView.setLayoutManager(layoutManager);
        }
        else{
            Log.e("SensorFragment", "Error");
        }

        /*1. 등록된 센서 목록을 서버로 부터 전부다 전달 받는다.
        * 2. 센서 등록시 센서 등록 page에서 정보를 입력한 뒤에 서버로 전달.
        * 정보입력하고 확인 버튼을 누르면 다시 센서 목록으로 오고 센서 목록은 다시 update된 서버 목록으로 부터 불러옴
        * 3. 센서 연결 끊기는 서버로 부터 요청하고 응답 되면 센서가 꺼짐.
        * 4. 각 센서들을 누르면 센서들의 정보들이 나옴 이 때 서버로 부터 요청함
        * 5. 센서 삭제는 각 센서들을 꾹 누르면 삭제 되거나 센서 정보page에서 삭제 버튼을 누름
        * 6. 센서 정보 수정은 센서 정보 page에서 수정할 수 있음
         */

        Sensor_create_button = (ImageView) v.findViewById(R.id.sensor_create);
        toolbartext = (MaterialTextView)getActivity().findViewById(R.id.toolbar_textview);

        /**
         * 1. 이메일 주소로 요청하기
         * 2. 센서list를 서버로 부터 요청. seonsor_list 불러오기,
         * */

        login_information_pref = getActivity().getSharedPreferences("login_information", Context.MODE_PRIVATE);
        Email = login_information_pref.getString("email", Email);

        ThreadTask_temp_sensor_list<Object> Sensor_list_result = getThreadTask_sensor_list(Email, "/sensor_list_request");
        Sensor_list_result.execute(sensor_ip);

        if(Sensor_list_result.getResult() == 1){
            /**
             * Mac address가 없을 때
             * */
            SensorDataList = Sensor_list_result.getSensor_list();
            recycleAdaptors = new RecycleAdaptors(sensor_ip);
            ArrayList<Sensor_list> sensorDataList = recycleAdaptors.getItems();
//            sensorDataList.add(new Sensor_list("12:34:56:78", "준덕이 센서",
//                    "휴하우스","010-2999-6313","on"));
            recycleAdaptors.setItems(SensorDataList);
            recyclerView.setAdapter(recycleAdaptors);

            Log.e("등록된 센서가 있음", "등록된 센서가 있음");
        }
        else if(Sensor_list_result.getResult() == 2){
            /**
             * 등록된 센서가 없을 때
             * */
        }
        else if(Sensor_list_result.getResult() == 3){
            /**
             * 등록된 센서가 있을 때
             * */

        }

        Sensor_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Sensor_register.newInstance("1","2")).commit();
                toolbartext.setText("센서 등록");
            }
        });

        //recycleAdaptors.addItem(new Sensor_list("sensor2", "3층 302호 화장실"));
       // recycleAdaptors.addItem(new Sensor_list("sensor3", "3층 303호 화장실"));
        //recycleAdaptors.addItem(new Sensor_list("sensor4", "3층 304호 화장실"));


        // Inflate the layout for this fragment
        return v;
    }

    private ThreadTask_temp_sensor_list<Object> getThreadTask_sensor_list(String email, String Router_name){

        return new ThreadTask_temp_sensor_list<Object>() {
            private int response_result;
            private String error_code;
            private ArrayList<Sensor_list> sensorDataList = new ArrayList<>();
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
                    JSONArray sensorListJSON = new JSONObject(response).getJSONArray("list");
                    JSONObject responseJSON = new JSONObject(response);

                    this.response_result = (Integer) responseJSON.get("key");
                    //this.error_code = (String) responseJSON.get("err_code");
                   // Log.e("sensor_fragment", sensorListJSON.toString());
                    for(int i = 0 ; i < sensorListJSON.length() ; i++){
                        JSONObject sensor = sensorListJSON.getJSONObject(i);
                        String MAC = sensor.getString("wifi_mac_address");
                        String Board_nickname = sensor.getString("board_nickname");
                        String Location = sensor.getString("address");
                        String Phone_number = sensor.getString("phone_number");
                        String Sensor_status = "false";
                        sensorDataList.add(new Sensor_list(MAC,Board_nickname, Location, Phone_number, Sensor_status));
                    }
                }
            }

            @Override
            public int getResult() {
                return response_result;
            }

            @Override
            public String getErrorCode() {
                return error_code;
            }

            @Override
            public ArrayList<Sensor_list> getSensor_list() {
                return sensorDataList;
            }
        };
    }
}