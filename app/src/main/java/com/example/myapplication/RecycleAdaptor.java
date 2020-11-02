package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecycleAdaptors extends RecyclerView.Adapter<RecycleAdaptors.CustomViewHolder> implements ItemTouchHelperListener{

    ArrayList<SampleData> notepadDataList;
    private int position;
    private ViewGroup parent;

    public RecycleAdaptors(ArrayList<SampleData> notepadDataList) {
        this.notepadDataList = notepadDataList;

    }

    @Override
    public RecycleAdaptors.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent,false );
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("viewType : " + viewType);
//            }
//        });

        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override // 실제 추가될 때
    public void onBindViewHolder(@NonNull final RecycleAdaptors.CustomViewHolder holder, final int position) {
        holder.Title.setText(notepadDataList.get(position).getTitle());
        //holder.view.setOnClickListener();
        this.position = position;


        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("position : " + position);

                Intent intent = new Intent(parent.getContext(), ContentActivity.class);
                intent.putExtra("title", notepadDataList.get(position).getTitle());
                intent.putExtra("content", notepadDataList.get(position).getContent());
                holder.Title.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != notepadDataList ? notepadDataList.size() : 0);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        notepadDataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        notepadDataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ArrayList<SampleData> getNotepadDataList(){
        return notepadDataList;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        protected View view;
        protected TextView Title;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            Title  = (TextView)itemView.findViewById(R.id.Title);
        }

    }

}
