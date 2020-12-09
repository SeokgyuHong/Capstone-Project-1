package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.Thread.ThreadTask;
import com.example.myapplication.Thread.ThreadTask_getData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sensor_inform extends AppCompatActivity {
    private LineChart chart;
    private Thread thread;

    private ActionBar actionBar;
    private MaterialToolbar toolbar;
    private TextView text_MacAdress;
    private TextView text_SensorLocation;
    private TextView text_SensorStatus;
    private TextView text_Sensorbattery;
    private TextView text_Sensorbattery_remain;
    private TextView text_Sensor_name;

    private String MacAdress;
    private String SensorLocation;
    private String SensorStatus;
    private String SensorBattery;
    private String SensorBattery_remain;
    private String SensorName;

    private int arrIndex = 0;
    /**
     * 센서정보 수정시
     * 센서 맥주소 에러 key 1
     * 폰번호 양식 에러 key 2
     * 센서 정보 수정 성공 key 3
     * 센서 정보 수정시 에러 발생 key 0
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_inform);
        Utils.setStatusBarColor(this, Utils.StatusBarcolorType.BLACK_STATUS_BAR);

        init();
        initGraph();

        Intent intent = getIntent();

        MacAdress = intent.getStringExtra("MAC");
        SensorName = intent.getStringExtra("Sensor_name");
        SensorLocation = intent.getStringExtra("Sensor_location");

        text_MacAdress.setText(MacAdress);
        text_Sensor_name.setText(SensorName);
        text_SensorLocation.setText(SensorLocation);
        /**
         * fragment로 부터 intent로 정보 다 받아서 띄워주기
         * */
    }

    private void init(){
        toolbar = (MaterialToolbar)findViewById(R.id.Sensorinform_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        text_MacAdress = findViewById(R.id.text_MacAdress);
        text_SensorLocation = findViewById(R.id.text_SensorLocation);
        text_SensorStatus = findViewById(R.id.text_SensorStatus);
        text_Sensorbattery = findViewById(R.id.text_Sensorbattery);
        text_Sensorbattery_remain = findViewById(R.id.text_Sensorbattery_remain);
        text_Sensor_name = findViewById(R.id.text_Sensor_name);

    }
    private void initGraph(){

        chart = (LineChart) findViewById(R.id.Linechart_sensor);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextColor(Color.BLACK);
        chart.animateXY(1000, 1000);
        chart.invalidate();
        chart.getDescription().setText(" ");
        LineData data = new LineData();
        chart.setData(data);
        chart.getAxisLeft().setAxisMaximum((float)1.1);
        chart.getAxisLeft().setAxisMinimum((float)-0.3);
        feedMultiple();
    }
    private void addEntry() {
        LineData data = chart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            /**
            *
             * *통신으로 받은 데이터 넣기
             * Math.random에 넣으면 됨*/

            String ip = getString(R.string.sensor_ip);

            ThreadTask_getData<Object> getThreadTask_getData = getThreadTask_getData(MacAdress, "/sensor_getdata");
            getThreadTask_getData.execute(ip);

            //data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 2f), 0);
            data.addEntry(new Entry(set.getEntryCount(), (float) getThreadTask_getData.getResult()), 0);


            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(2);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "실시간데이터");
        set.setFillAlpha(110);
        set.setFillColor(Color.parseColor("#d7e7fa"));
        set.setColor(Color.parseColor("#0B80C9"));
        set.setCircleColor(Color.parseColor("#FFA1B4DC"));
        //set.setCircleColorHole(Color.BLUE);
        set.setValueTextColor(Color.BLACK);
        set.setDrawValues(false);
        set.setLineWidth(2);
        set.setCircleRadius(6);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        return set;
    }

    private void feedMultiple() {
        if (thread != null) thread.interrupt();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry();
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null)
            thread.interrupt();
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

    private ThreadTask_getData<Object> getThreadTask_getData(String Mac, String Router_name){

        return new ThreadTask_getData<Object>() {
            private double response_result;
            private String error_code;

            @Override
            protected void doInBackground(String... urls) throws IOException, JSONException {//background로 돌아갈것
                HttpURLConnection con = null;
                JSONObject sendObject = new JSONObject();
                BufferedReader reader = null;
                URL url = new URL(urls[0] + Router_name);

                con = (HttpURLConnection) url.openConnection();

                sendObject.put("wifi_mac_address", Mac);

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

                    this.response_result = (Double) responseJSON.get("key");
                    this.error_code = (String) responseJSON.get("err_code");
                }
            }

            @Override
            public double getResult() {
                return response_result;
            }
            @Override
            public String getErrorCode() {
                return error_code;
            }
        };
    }

    private ThreadTask<Object> getThreadTask_MacConfirm(String Mac, String Router_name){

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

                sendObject.put("wifi_mac_address", Mac);

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