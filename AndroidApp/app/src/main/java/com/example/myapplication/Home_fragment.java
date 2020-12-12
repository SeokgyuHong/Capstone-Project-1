package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CircleProgressBar circleProgressBar;
    private CircleProgressBar Fall_down_bar;

    private SharedPreferences sensor_status_pref;
    private SharedPreferences.Editor sensor_status_editor;
    private Thread thread;

    public Home_fragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_fragment newInstance(String param1, String param2) {
        Home_fragment fragment = new Home_fragment();
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
        View v = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        feedMultiple(v);


        return v;
    }
    private void setCircleProgressBar(View v){
        sensor_status_pref = getActivity().getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
        sensor_status_editor = sensor_status_pref.edit();

//        int sensor_total_count = Integer.parseInt(sensor_status_pref.getString("sensor_total_count", "0"));
//        int sensor_on_count = Integer.parseInt(sensor_status_pref.getString("sensor_on_count", "0"));
       // int falldown_count = Integer.parseInt(sensor_status_pref.getString("falldown_count", "0"));
        int sensor_total_count = 4;
        int sensor_on_count = 3 ;
        int falldown_count = 3;

        float sensor_rate = (float) sensor_on_count /(float)sensor_total_count;
        float falldown_reate = (float) ((float)falldown_count / 5.0);
        Log.e("sensor_rate", Float.toString(sensor_rate));
//        sensor_status_editor = sensor_status_pref.edit();
//        sensor_status_editor.putString("sensor_on_count" ,Integer.toString(sensor_on_count) );
//        sensor_status_editor.commit();

        circleProgressBar = v.findViewById(R.id.contected_sensor);
        circleProgressBar.setProgress((int) (sensor_rate * 100));
        circleProgressBar.setProgressFormatter((progress, max) -> {
            final String DEFAULT_PATTERN = "%d/%d개"   ;
            //return String.format(DEFAULT_PATTERN, (int) ((float) progress /(float) max * 100));
            return String.format(DEFAULT_PATTERN, sensor_on_count, sensor_total_count);
        });

        Fall_down_bar = v.findViewById(R.id.fall_down_count);
        Fall_down_bar.setProgress((int) (falldown_reate * 100));
        Fall_down_bar.setProgressFormatter((progress, max) -> {
            final String DEFAULT_PATTERN = "%d번"   ;
            //return String.format(DEFAULT_PATTERN, (int) ((float) progress /(float) max * 100));
            return String.format(DEFAULT_PATTERN, falldown_count);
        });
    }

    private void feedMultiple(View v) {

        if (thread != null) thread.interrupt();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setCircleProgressBar(v);
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(runnable);
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

}