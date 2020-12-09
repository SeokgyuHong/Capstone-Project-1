package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Thread.ThreadTask;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sensor_register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sensor_register extends Fragment {

    private String sensor_ip;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText Mac_address;
    private EditText Sensor_name;
    private MaterialTextView Sensor_location;
    private Button Mac_address_check_button;
    private Button Sensor_register_confirm_button;
    private EditText Guardian_phone_number;
    private EditText Sensor_location_detail;

    private String mac_address;
    private String sesor_name;
    private String guardian_phone_number;
    private String sensor_location;
    private String sensor_location_detail;

    private SharedPreferences login_information_pref;
    private String Email;

    private SharedPreferences sensor_status_pref;
    private SharedPreferences.Editor sensor_status_editor;

    private String guardian_street_name_address;

    private static final int SEARCH_ADDRESS_ACTIVITY = 20000;

    private boolean mac_check = false;





    public Sensor_register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sensor_register.
     */
    // TODO: Rename and change types and number of parameters
    public static Sensor_register newInstance(String param1, String param2) {
        Sensor_register fragment = new Sensor_register();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sensor_register, container, false);

        login_information_pref = getActivity().getSharedPreferences("login_information", Context.MODE_PRIVATE);
        Email = login_information_pref.getString("email", Email);

        sensor_ip = getString(R.string.sensor_ip);

        Mac_address = v.findViewById(R.id.mac_address);
        Sensor_name = v.findViewById(R.id.sensor_name_text);
        Sensor_location = (MaterialTextView)v.findViewById(R.id.sensor_location_text);
        Sensor_location_detail = v.findViewById(R.id.sensor_location_detail_text);

        Sensor_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DaumWebViewActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });
        Mac_address_check_button = v.findViewById(R.id.mac_address_check);

        Guardian_phone_number = v.findViewById(R.id.guardian_phone_number);
        Guardian_phone_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Sensor_register_confirm_button = v.findViewById(R.id.sensor_register_confirm_button);

        Mac_address_check_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //센서에 맥 주소 요청함.
                mac_address = Mac_address.getText().toString();

                ThreadTask<Object> Sensor_list_result = getThreadTask_macCheck(mac_address, "/sensor_duplication_check");
                Sensor_list_result.execute(sensor_ip);
                if(mac_address.equals("")){
                    Toast.makeText( getContext(), "MAC 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if(Sensor_list_result.getResult() == 1){
                    /**
                     * 양식에러
                     * */
                    Toast.makeText( getContext(), "MAC 주소 양식이 잘못됐습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(Sensor_list_result.getResult() == 2){
                    /**
                     * 중복없음
                     * */
                    Toast.makeText( getContext(), "사용가능한 MAC 주소입니다.", Toast.LENGTH_SHORT).show();
                    mac_check = true;
                }
                else if(Sensor_list_result.getResult() == 3){
                    Toast.makeText( getContext(), "이미 등록된 MAC 주소가 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(Sensor_list_result.getResult() == 0){
                    /**
                     * 시스템에러
                     * */
                }
                // 중복 이면 false 중복 아니면 true
            }
        });
        Sensor_register_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mac_address = Mac_address.getText().toString();
                sesor_name = Sensor_name.getText().toString();
                sensor_location = Sensor_location.getText().toString();
                sensor_location_detail = Sensor_location_detail.getText().toString();
                sensor_location = sensor_location + " " + sensor_location_detail;

                guardian_phone_number = Guardian_phone_number.getText().toString();
                List<Address> list = null;
                if(!mac_check){
                    Toast.makeText( getContext(), "중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(sesor_name.equals("")|sensor_location.equals("")|guardian_phone_number.equals("")){
                    Toast.makeText( getContext(), "입력하지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(mac_check && !mac_address.equals("") && !sesor_name.equals("") && !sensor_location.equals("")
                         && !guardian_phone_number.equals("")){
                    /**
                     * 등록 성공, 입력한 값을 서버로 전달하기
                     * */
                    final Geocoder geocoder = new Geocoder(getContext());
                    try {
                        list = geocoder.getFromLocationName(sensor_location, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address addr = list.get(0);
                    double latitude = addr.getLatitude();
                    double longitude = addr.getLongitude();

                    System.out.println("lat = " +Double.toString(latitude) + " lon = " + Double.toString(longitude));

                    ThreadTask<Object> Sensor_list_register = getThreadTask_sensorRegister(Email, mac_address, sesor_name, sensor_location,
                            guardian_phone_number, latitude,longitude, "/sensor_registration");
                    Sensor_list_register.execute(sensor_ip);
                    if(Sensor_list_register.getResult() == 1){
                        /**
                         * 센서 등록 실패
                        */
                        Toast.makeText( getContext(), "센서 등록을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(Sensor_list_register.getResult() == 2){
                        /**
                         * 센서 등록 성공
                         * */
                        /** 성공시 fragment 바꾸기*/

                        sensor_status_pref = getActivity().getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
                        int sensor_total_count = Integer.parseInt(sensor_status_pref.getString("sensor_total_count", "0"));
                        sensor_total_count++;
                        sensor_status_editor = sensor_status_pref.edit();
                        sensor_status_editor.putString("sensor_total_count" ,Integer.toString(sensor_total_count) );
                        sensor_status_editor.commit();

                        Toast.makeText( getContext(), "센서가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        //Toast.makeText( getContext(), list(0), Toast.LENGTH_SHORT).show();
                        //Toast.makeText( getContext(), "센서 등록 성공!", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Sensor_fragment.newInstance("1","2")).commit();
                    }
                    else if(Sensor_list_register.getResult() == 0) {
                        /**
                         * 시스템에러
                         * */
                    }
                }

            }
        });


        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Sensor_location.setText(data);
                    }
                }
                break;
        }
    }

    private ThreadTask<Object> getThreadTask_macCheck(String MAC, String Router_name){

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

                sendObject.put("wifi_mac_address", MAC);

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

    private ThreadTask<Object> getThreadTask_sensorRegister(String Email, String MAC, String Sensor_name, String Sensor_location,
                                                            String guardian_phone_number,double latitude, double longitude, String Router_name){
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

                sendObject.put("email_address", Email);
                sendObject.put("wifi_mac_address", MAC);
                sendObject.put("board_nickname", Sensor_name);
                /**
                 * 키값 변경
                 * */
                sendObject.put("address", Sensor_location); // 센서 위치
                sendObject.put("phone_number", guardian_phone_number);// 센서 사용자 연락처
                sendObject.put("latitude", latitude); // 위도
                sendObject.put("longitude",longitude); // 경도

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
}