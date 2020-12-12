package com.example.myapplication;

public class Hospital {

    private String Hospital_name;
    private String Hospital_location;
    private String Hospital_phone_num;
    private String Category;
    private String Hospital_time; //응급실 수
    private String Latitude; // 위도
    private String Longtitude; // 경도

    public Hospital(String hospital_name, String hospital_location, String hospital_phone_num, String category, String hospital_time, String latitude, String longtitude){
        Hospital_name = hospital_name;
        Hospital_location = hospital_location;
        Hospital_phone_num = hospital_phone_num;
        Category = category;
        Hospital_time = hospital_time;
        Latitude = latitude;
        Longtitude = longtitude;
    }

    public String getHospital_name() {
        return Hospital_name;
    }

    public String getHospital_location() {
        return Hospital_location;
    }

    public String getHospital_phone_num() {
        return Hospital_phone_num;
    }

    public String getHospital_time() {
        return Hospital_time;
    }

    public String getCategory() {
        return Category;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongtitude(){
        return Longtitude;
    }
}
