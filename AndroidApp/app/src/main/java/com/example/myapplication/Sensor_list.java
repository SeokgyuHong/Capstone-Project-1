package com.example.myapplication;

public class Sensor_list{
    private String Sensor_macaddress;
    private String Sensor_name;
    private String Sensor_location;
    private String Sensor_phone;
    private String Sensor_status;

    public Sensor_list(String sensor_macaddress, String sensor_name, String sensor_location, String sensor_phone, String sensor_status){
        Sensor_macaddress = sensor_macaddress;
        Sensor_name = sensor_name;
        Sensor_location = sensor_location;
        Sensor_phone = sensor_phone;
        Sensor_status = sensor_status;
    }

    public void setSensor_macaddress(String sensor_macaddress) {
        Sensor_macaddress = sensor_macaddress;
    }
    public String getSensor_macaddress() {
        return Sensor_macaddress;
    }

    public void setSensor_name(String sensor_name) {
        Sensor_name = sensor_name;
    }

    public void setSensor_location(String sensor_location) {
        Sensor_location = sensor_location;
    }

    public void setSensor_phone(String sensor_phone) {
        Sensor_phone = sensor_phone;
    }

    public void setSensor_status(String sensor_status) {
        Sensor_status = sensor_status;
    }

    public String getSensor_name() {
        return Sensor_name;
    }

    public String getSensor_location() {
        return Sensor_location;
    }
    public String getSensor_phone() {
        return Sensor_phone;
    }
    public String getSensor_status() {
        return Sensor_status;
    }


}