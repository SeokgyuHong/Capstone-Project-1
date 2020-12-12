package com.example.myapplication;

import android.app.Activity;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

public class Utils {
    public enum StatusBarcolorType{
        BLACK_STATUS_BAR(R.color.white);

        private int backgroundColorId;

        StatusBarcolorType(int backgroundColorId){
            this.backgroundColorId = backgroundColorId;
        }

        public int getBackgroundColorId(){
            return backgroundColorId;
        }
    }

    public static void setStatusBarColor(Activity activity, StatusBarcolorType colorType){
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
    }
}
