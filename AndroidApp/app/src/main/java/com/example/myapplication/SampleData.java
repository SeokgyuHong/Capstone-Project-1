package com.example.myapplication;

public class SampleData {
    private int iv_profile;
    private String Title;
    private String Content;

    public SampleData(String Title, String Content, int iv_profile){
        this.Title = Title;
        this.Content = Content;
        this.iv_profile = iv_profile;
    }

    public String getTitle()
    {
        return this.Title;
    }
    public String getContent(){
        return this.Content;
    }

    public int getiv_profile() {
        return iv_profile;
    }
}