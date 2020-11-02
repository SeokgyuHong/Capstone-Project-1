package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        String title = Objects.requireNonNull(intent.getExtras()).getString("title");
        String content = intent.getExtras().getString("content");
        System.out.println("title : " + title + "content : " + content);

        TextView notepad_title = findViewById(R.id.notepad_content_title);
        TextView notepad_content = findViewById(R.id.notepad_content_content);

        notepad_title.setText(title);
        notepad_content.setText(content);
        //intent.putExtra("title", notepadDataList.get(position).getTitle());
        //intent.putExtra("content", notepadDataList.get(position).getContent());
    }



}