package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Thread.ThreadTask;

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

class RecycleAdaptors extends RecyclerView.Adapter<RecycleAdaptors.CustomViewHolder> implements ItemTouchHelperListener{

    ArrayList<SampleData> notepadDataList;
    private int position;
    private ViewGroup parent;
    ArrayList<Sensor_list> sensorDataList = new ArrayList<>();
    private String Sensor_ip;

    private SharedPreferences sensor_status_pref;
    private SharedPreferences.Editor sensor_status_editor;

    public RecycleAdaptors(String sensor_ip) {
        Sensor_ip = sensor_ip;
    }

    public void addItem(Sensor_list item){
        sensorDataList.add(item);
    }

    public void setItems(ArrayList<Sensor_list> items){
        sensorDataList = items;
    }

    public void removeItems(int position){
        sensorDataList.remove(position);
    }
    public Sensor_list getItem(int position){
        return sensorDataList.get(position);
    }
    public ArrayList<Sensor_list> getItems(){
        return sensorDataList;
    }


    public void setItem(int position, Sensor_list item){
        sensorDataList.set(position, item);
    }

    @Override
    public RecycleAdaptors.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_item_list, parent,false );

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("viewType : " + viewType);
//            }
//        });
        return new CustomViewHolder(view, Sensor_ip);
    }

    @Override // 실제 추가될 때
    public void onBindViewHolder(@NonNull final RecycleAdaptors.CustomViewHolder holder, final int position) {
        //holder.Title.setText(notepadDataList.get(position).getTitle());
//        holder.Sensor_name.setText("Sensor1");
//        holder.Sensor_location.setText("303호 3층 화장실");
//        //holder.view.setOnClickListener();
//        this.position = position;

        //ImageView imageView = parent.findViewById(R.id.imageView2);

        Sensor_list item = sensorDataList.get(position);

        sensor_status_pref = parent.getContext().getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
        sensor_status_editor = sensor_status_pref.edit();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Sensor_inform.class);
                
                intent.putExtra("MAC", item.getSensor_macaddress());
                intent.putExtra("Sensor_name", item.getSensor_name());
                intent.putExtra("Sensor_location", item.getSensor_location());
                holder.view.getContext().startActivity(intent);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(v.getContext(), "길게눌렀다.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(parent.getContext())
                        .setTitle("알림")
                        .setMessage("센서를 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i)
                            {
                                ThreadTask<Object> sensor_deletion_result = getThreadTask_deleteSensor(item.getSensor_macaddress(), "/sensor_deletion");
                                sensor_deletion_result.execute(Sensor_ip);

                                if(sensor_deletion_result.getResult() == 1){
                                    /**
                                     * 센서 맥주소 양식 에러
                                     * */
                                }
                                else if(sensor_deletion_result.getResult()  == 2){
                                    /**
                                     * 삭제성공
                                     * */
                                    sensor_status_editor.remove("sensor"+position);
                                    sensor_status_editor.commit();

                                    sensorDataList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,sensorDataList.size());
                                    String text = String.format("%s 센서를 삭제했습니다.",item.getSensor_name()) ;
                                    Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();

                                    sensor_status_pref = parent.getContext().getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
                                    int sensor_total_count = Integer.parseInt(sensor_status_pref.getString("sensor_total_count", "0"));
                                    if(sensor_total_count != 0){
                                        sensor_total_count--;
                                    }
                                    sensor_status_editor = sensor_status_pref.edit();
                                    sensor_status_editor.putString("sensor_total_count" ,Integer.toString(sensor_total_count) );
                                    sensor_status_editor.commit();
                                }
                            } })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {

                            } });
                AlertDialog msgDlg = msgBuilder.create(); msgDlg.show();
                return false;
            }
        });

        holder.setItem(item);

        String sensor_status = sensor_status_pref.getString("sensor"+position, "false");
        if(sensor_status.equals("true")){
            holder.Sensor_switch.setChecked(true);
        }
        else{
            holder.Sensor_switch.setChecked(false);
        }


        //sensor_status_editor.putString("sensor" + position , "false");
        //sensor_status_editor.commit();

        holder.setSensor_switch(position, item.getSensor_macaddress(), item.getSensor_status(), parent.getContext());
    }

    private ThreadTask<Object> getThreadTask_deleteSensor(String MAC, String Router_name){

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

    @Override
    public int getItemCount() {
        return sensorDataList.size();
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
//        notepadDataList.remove(position);
//        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
//        notepadDataList.remove(position);
//        notifyItemRemoved(position);
    }

    public ArrayList<SampleData> getNotepadDataList(){
        return notepadDataList;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        protected String Sensor_ip;

        protected LinearLayout view;
        protected TextView Sensor_name;
        protected TextView Sensor_location;
        protected SwitchCompat Sensor_switch;

        private SharedPreferences sensor_status_pref;
        private SharedPreferences.Editor sensor_status_editor;

        public CustomViewHolder(@NonNull View itemView, String sensor_ip) {
            super(itemView);
            Sensor_ip = sensor_ip;
            view = (LinearLayout)itemView.findViewById(R.id.sensor_button);
            Sensor_name  = (TextView)itemView.findViewById(R.id.sensor_name);
            //Sensor_location = (TextView)itemView.findViewById(R.id.sensor_location);
            Sensor_switch = (SwitchCompat)itemView.findViewById(R.id.sensor_switch);
        }

        public void setItem(Sensor_list item){
            Sensor_name.setText(item.getSensor_name() + "의 센서");
//            String location = item.getSensor_location().substring(6,12);
//            Sensor_location.setText(location);
        }

        public void setSensor_switch(int position, String mac_address, String sensor_status, Context context){

            Sensor_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        //스위치 on
                        ThreadTask<Object> Sensor_list_result = getThreadTask_macCheck(mac_address, "/sensor_on");
                        Sensor_list_result.execute(Sensor_ip);

                        if( Sensor_list_result.getResult() == -1){
                            /**
                             * 이메일 양식에러
                             * */
                        }
                        else if (Sensor_list_result.getResult() == 1){
                            /**
                             * mac 양식 에러
                            */
                        }
                        else if(Sensor_list_result.getResult() == 2){
                            /**
                             * 센서 켜기 성공
                             * */
                            sensor_status_pref = context.getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
                            sensor_status_editor = sensor_status_pref.edit();
                            sensor_status_editor.putString("sensor" + position , "true");
                            sensor_status_editor.commit();

                            int sensor_on_count = Integer.parseInt(sensor_status_pref.getString("sensor_on_count", "0"));
                            sensor_on_count++;
                            sensor_status_editor = sensor_status_pref.edit();
                            sensor_status_editor.putString("sensor_on_count" ,Integer.toString(sensor_on_count) );
                            sensor_status_editor.commit();

                            Toast.makeText(context, "센서가 연결되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(Sensor_list_result.getResult() == 3){
                            /**
                             * 이미 기존에 센서가 켜져 있음(에러)
                             * */
                        }
                        else if(Sensor_list_result.getResult() == 0){
                            /**
                             * 센서 끄는 중 시스템 에러 발생
                             * */
                        }
                    }
                    else{
                        //스위치 off
                        ThreadTask<Object> Sensor_list_result = getThreadTask_macCheck(mac_address, "/sensor_off");
                        Sensor_list_result.execute(Sensor_ip);

                        if( Sensor_list_result.getResult() == -1){
                            /**
                             * 이메일 양식에러
                             * */
                        }
                        else if (Sensor_list_result.getResult() == 1){
                            /**
                             * mac 양식 에러
                             */
                        }
                        else if(Sensor_list_result.getResult() == 2){
                            /**
                             * 센서 끄기 성공
                             * */

                            sensor_status_pref = context.getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
                            sensor_status_editor = sensor_status_pref.edit();
                            sensor_status_editor.putString("sensor" + position , "false");
                            sensor_status_editor.commit();
                            Toast.makeText(context, "센서가 연결해제 되었습니다.", Toast.LENGTH_SHORT).show();

                            int sensor_on_count = Integer.parseInt(sensor_status_pref.getString("sensor_on_count", "0"));
                            sensor_on_count--;
                            sensor_status_editor = sensor_status_pref.edit();
                            sensor_status_editor.putString("sensor_on_count" ,Integer.toString(sensor_on_count) );
                            sensor_status_editor.commit();
                        }
                        else if(Sensor_list_result.getResult() == 3){
                            /**
                             * 이미 기존에 센서가 꺼저 있음(에러)
                             * */
                        }
                        else if(Sensor_list_result.getResult() == 0){
                            /**
                             * 센서 끄는 중 시스템 에러 발생
                             * */
                        }
                    }
                }
            });
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

    }

}
