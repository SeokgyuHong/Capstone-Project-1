package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

enum ButtonsState{
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}


public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;
    private boolean swipeBack = false;
    private ButtonsState buttonsShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 115;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currenrtItemViewHolder = null;


    private String Title_filename;
    private String Content_filename;
    private File Title_file;
    private  File Content_file;
    private ArrayList<SampleData> notepadDatalist;
    private int target;
    private int fileSize;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener, String Title_filename, String Content_filename) {
        this.listener = listener;
        this.Title_filename = Title_filename;
        this.Content_filename = Content_filename;

        Title_file = new File("/data/data/com.example.myapplication/files", Title_filename);
        Content_file = new File("/data/data/com.example.myapplication/files", Content_filename);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(drag_flags,swipe_flags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //final int position = viewHolder.getAdapterPosition();
        target = viewHolder.getAdapterPosition();
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(buttonsShowedState != ButtonsState.GONE){
                if(buttonsShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else{
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            if(buttonsShowedState == ButtonsState.GONE){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        currenrtItemViewHolder = viewHolder;
        //버튼을 그려주는 함수 drawButtons(c, currenrtItemViewHolder);
        }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder){
        float buttonWidthWithOutPadding = buttonWidth - 10;
        float corners = 5;
        View itemView = viewHolder.itemView;

        Paint p = new Paint(); buttonInstance = null;
        //오른쪽으로 스와이프 했을때 (왼쪽에 버튼이 보여지게 될 경우)
        if(buttonsShowedState == ButtonsState.LEFT_VISIBLE){
            RectF leftButton = new RectF(itemView.getLeft() + 10, itemView.getTop() + 10,
                    itemView.getLeft() + buttonWidthWithOutPadding, itemView.getBottom() - 10);
            p.setColor(Color.BLUE); c.drawRoundRect(leftButton, corners, corners, p);
            drawText("수정", c, leftButton, p);
            buttonInstance = leftButton;
            //왼쪽으로 스와이프 했을때 (오른쪽에 버튼이 보여지게 될 경우)
            }
        else if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE){ RectF rightButton = new RectF(itemView.getRight()
                - buttonWidthWithOutPadding, itemView.getTop() + 10, itemView.getRight() -10, itemView.getBottom() - 10);
        p.setColor(Color.RED); c.drawRoundRect(rightButton, corners, corners, p);
        drawText("삭제", c, rightButton, p);
        buttonInstance = rightButton;
        }
    }

    //버튼의 텍스트 그려주기
    private void drawText(String text, Canvas c, RectF button, Paint p){
        float textSize = 25;
        p.setColor(Color.WHITE); p.setAntiAlias(true);
        p.setTextSize(textSize);
        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth/2), button.centerY() + (textSize/2), p);
    }

    @Override public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if(swipeBack){
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if(swipeBack){
                    if(dX < -buttonWidth) buttonsShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if(dX > buttonWidth) buttonsShowedState = ButtonsState.LEFT_VISIBLE;
                    if(buttonsShowedState != ButtonsState.GONE){ setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }
    private void setTouchDownListener(
            final Canvas c, final RecyclerView recyclerView , final RecyclerView.ViewHolder viewHolder,
            final float dX, final float dY , final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false; }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView ,
                                    final RecyclerView.ViewHolder viewHolder, final float dX, final float dY ,
                                    final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                ItemTouchHelperCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override public boolean onTouch(View v, MotionEvent event) {
                        return false; }
                });
                setItemsClickable(recyclerView, true);
                swipeBack = false;
                if(listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())){
                    if(buttonsShowedState == ButtonsState.LEFT_VISIBLE){ listener.onLeftClick(viewHolder.getAdapterPosition(), viewHolder);
                    }
                    else if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE){ listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder); }
                }
                buttonsShowedState = ButtonsState.GONE; currenrtItemViewHolder = null; return false; }
        });
    }
    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable){
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }


    public void editFile() throws IOException {
//        notepadDatalist = listener.getNotepadDataList();
//        fileSize = notepadDatalist.size();
//
//        BufferedWriter Title_writer = new BufferedWriter(new FileWriter(Title_file, false));
//        BufferedWriter Content_writer = new BufferedWriter(new FileWriter(Content_file, false));
//
//
//        for(int i = 0 ; i < fileSize ; i ++){
//
//        }
//
//        Title_writer.write(notepad_title);
//        Title_writer.write("\n");
//
//        Content_writer.write(notepad_content);
//        Content_writer.write("\n=============\n");
//
//        Title_writer.flush();
//        Content_writer.flush();
//
//        Title_writer.close();
//        Content_writer.close();
    }
}
