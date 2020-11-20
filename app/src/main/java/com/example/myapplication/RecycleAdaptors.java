package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecycleAdaptors extends RecyclerView.Adapter<RecycleAdaptors.CustomViewHolder> implements ItemTouchHelperListener{

    ArrayList<SampleData> notepadDataList;
    private int position;
    private ViewGroup parent;
    ArrayList<Sensor_list> sensorDataList = new ArrayList<>();

    public RecycleAdaptors() {


    }

    public void addItem(Sensor_list item){
        sensorDataList.add(item);
    }

    public void setItems(ArrayList<Sensor_list> items){
        sensorDataList = items;
    }

    public Sensor_list getItem(int position){
        return sensorDataList.get(position);
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
        return new CustomViewHolder(view);
    }

    @Override // 실제 추가될 때
    public void onBindViewHolder(@NonNull final RecycleAdaptors.CustomViewHolder holder, final int position) {
        //holder.Title.setText(notepadDataList.get(position).getTitle());
//        holder.Sensor_name.setText("Sensor1");
//        holder.Sensor_location.setText("303호 3층 화장실");
//        //holder.view.setOnClickListener();
//        this.position = position;

        //ImageView imageView = parent.findViewById(R.id.imageView2);
        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Sensor_inform.class);
                holder.view.getContext().startActivity(intent);
            }
        });
        Sensor_list item = sensorDataList.get(position);
        holder.setItem(item);
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

    @Override
    public ArrayList<SampleData> getNotepadDataList(){
        return notepadDataList;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout view;
        protected TextView Sensor_name;
        protected TextView Sensor_location;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            view = (LinearLayout)itemView.findViewById(R.id.sensor_button);
            Sensor_name  = (TextView)itemView.findViewById(R.id.sensor_name);
            Sensor_location = (TextView)itemView.findViewById(R.id.sensor_location);
        }

        public void setItem(Sensor_list item){
            Sensor_name.setText(item.getSensor_name());
            Sensor_location.setText(item.getSensor_location());
        }
    }
}
