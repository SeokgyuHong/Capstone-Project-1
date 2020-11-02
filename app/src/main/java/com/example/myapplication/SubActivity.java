package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubActivity extends AppCompatActivity {
    private EditText Notepad_title;
    private TextInputEditText Notepad_content ;
    private MaterialButton Save_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //Syst
        Notepad_title = (EditText) findViewById(R.id.notepad_title);
        Notepad_content = (TextInputEditText) findViewById(R.id.notepad_content);
        Save_button = (MaterialButton) findViewById(R.id.save_button);

        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notepad_title = notepad_title = Notepad_title.getText().toString();
                String notepad_content = notepad_content = Notepad_content.getText().toString();
                System.out.println("notepad_title : " + notepad_title + " notepad_content : " +notepad_content);
                if(notepad_title.equals("") && notepad_content.equals("")){
                    Toast.makeText(getApplicationContext(), "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(notepad_title.equals("")){
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(notepad_content.equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{

                    FileOutputStream outputStream;
                    System.out.println("type : " + notepad_content.getClass().getName());
                    System.out.printf("%s  %s  %n", notepad_content, notepad_title);
                    String directory = "/data/data/com.example.myapplication/files";
                    String Title_filename = "title.txt";
                    String Content_filename = "content.txt";

                    System.out.println(SubActivity.this.getFilesDir());
                    File Title_file = new File(SubActivity.this.getFilesDir(), Title_filename);
                    File Content_file = new File(SubActivity.this.getFilesDir(), Content_filename);

                    if(!Title_file.exists() && !Content_file.exists()){
                        System.out.println("파일 없음");

                        try {

                            // Write to a file.
                            BufferedWriter Title_writer = new BufferedWriter(new FileWriter(Title_file, true));
                            BufferedWriter Content_writer = new BufferedWriter(new FileWriter(Content_file, true));

                            Title_writer.write(notepad_title);
                            Title_writer.write("\n");

                            Content_writer.write(notepad_content);
                            Content_writer.write("\n=============\n");

                            Title_writer.close();
                            Content_writer.close();
                            // 나중에 새 글마다 txt 만들기
                        } catch (IOException ex) {
                            // Error occurred opening file for writing.
                        }
                    }
                    else {
                        System.out.println("기존 파일에서 읽어옴");
                        try {

                            // Write to a file.
                            BufferedWriter Title_writer = new BufferedWriter(new FileWriter(Title_file, true));
                            BufferedWriter Content_writer = new BufferedWriter(new FileWriter(Content_file, true));

                            Title_writer.write(notepad_title);
                            Title_writer.write("\n");

                            Content_writer.write(notepad_content);
                            Content_writer.write("\n=============\n");

                            Title_writer.flush();
                            Content_writer.flush();

                            Title_writer.close();
                            Content_writer.close();
                            // 나중에 새 글마다 txt 만들기
                        } catch (IOException ex) {
                            // Error occurred opening file for writing.
                        }

                    }
                    Intent intent = new Intent(SubActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "입력되었습니다.", Toast.LENGTH_SHORT).show();

                    finish();
                }
                //해야할 것 title과 content가 null이면 입력되지 않았다고 쓰기
                //                //title content 글자수 제한 두기




            }

        });



    }


}