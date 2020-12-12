package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textview.MaterialTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

class RecycleAdaptors_hospital extends RecyclerView.Adapter<RecycleAdaptors_hospital.CustomViewHolder> implements ItemTouchHelperListener{

    private int position;
    private ViewGroup parent;
    ArrayList<Hospital> HospitalDataList = new ArrayList<>();
    private GoogleMap googleMap;

    public RecycleAdaptors_hospital(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void addItem(Hospital item){
        HospitalDataList.add(item);
    }

    public void setItems(ArrayList<Hospital> items){
        HospitalDataList = items;
    }

    public Hospital getItem(int position){
        return HospitalDataList.get(position);
    }
    public ArrayList<Hospital> getItems(){
        return HospitalDataList;
    }


    public void setItem(int position, Hospital item){
        HospitalDataList.set(position, item);
    }

    @Override
    public RecycleAdaptors_hospital.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_items, parent,false );

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("viewType : " + viewType);
//            }
//        });
        return new CustomViewHolder(view, googleMap);
    }

    @Override // 실제 추가될 때
    public void onBindViewHolder(@NonNull final RecycleAdaptors_hospital.CustomViewHolder holder, final int position) {
        //holder.Title.setText(notepadDataList.get(position).getTitle());
//        holder.Sensor_name.setText("Sensor1");
//        holder.Sensor_location.setText("303호 3층 화장실");
//        //holder.view.setOnClickListener();
//        this.position = position;

        //ImageView imageView = parent.findViewById(R.id.imageView2);

        Hospital item = HospitalDataList.get(position);
        GoogleMap googleMap_target = holder.getHolder_googleMap();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //googleMap_target.clear();
                LatLng cityHall = new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongtitude())); // 서울시청 위도와 경도

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(cityHall);
                markerOptions.title("경북대학교병원");
                markerOptions.snippet("대학 병원");

            }
        });

        holder.setItem(item);
       // holder.setSensor_switch(position);
    }

    @Override
    public int getItemCount() {
        return HospitalDataList.size();
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

    public ArrayList<Hospital> getHospitalDataList(){
        return HospitalDataList;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout view;

        protected TextView Hospital_name_text;
        protected TextView Hospital_location_text;
        protected TextView Hospital_time_text;
        protected TextView Hospital_category_text;
        protected TextView Hospital_phone_num_text;


        protected GoogleMap Holder_googleMap;

        public CustomViewHolder(@NonNull View itemView, GoogleMap googleMap) {
            super(itemView);
            view = (LinearLayout)itemView.findViewById(R.id.Hospital_list);
            Hospital_name_text  = (TextView)itemView.findViewById(R.id.Hospital_name_text);
            Hospital_location_text = (TextView)itemView.findViewById(R.id.Hospital_location_text);
            Hospital_category_text = (TextView)itemView.findViewById(R.id.Hospital_category_text);
            Hospital_time_text = (TextView)itemView.findViewById(R.id.Hospital_time_text);
            Hospital_phone_num_text = (TextView)itemView.findViewById(R.id.Hospital_phone_num_text);
            Holder_googleMap = googleMap;
        }

        public GoogleMap getHolder_googleMap() {
            return Holder_googleMap;
        }

        public void setItem(Hospital item){
            Hospital_name_text.setText(item.getHospital_name());
            Hospital_location_text.setText(item.getHospital_location());
            Hospital_time_text.setText(item.getHospital_time());
            Hospital_category_text.setText(item.getCategory());
            //Hospital_phone_num_text.setText(item.getHospital_phone_num());
            Hospital_phone_num_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tel="tel:"+item.getHospital_phone_num();
                    view.getContext().startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                }
            });
        }

    }
}
