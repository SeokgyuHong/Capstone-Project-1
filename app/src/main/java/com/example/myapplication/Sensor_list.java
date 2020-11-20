package com.example.myapplication;

public class Sensor_list{
    private String Sensor_name;
    private String Sensor_location;

    public Sensor_list(String sensor_name, String sensor_location){
        Sensor_name = sensor_name;
        Sensor_location = sensor_location;
    }

    public void setSensor_name(String sensor_name) {
        Sensor_name = sensor_name;
    }

    public void setSensor_location(String sensor_location) {
        Sensor_location = sensor_location;
    }

    public String getSensor_name() {
        return Sensor_name;
    }

    public String getSensor_location() {
        return Sensor_location;
    }


}