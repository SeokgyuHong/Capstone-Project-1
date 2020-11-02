package com.example.myapplication;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
    void onLeftClick(int position, RecyclerView.ViewHolder viewHolder);
    void onRightClick(int position, RecyclerView.ViewHolder viewHolder);
    public ArrayList<SampleData> getNotepadDataList();
}

