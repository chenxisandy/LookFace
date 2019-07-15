package com.example.lookface.model;

import org.litepal.crud.LitePalSupport;

public class Picture extends LitePalSupport {

    private String JSONString;

    private String picturePath;

    public String getJSONString() {
        return JSONString;
    }

    public void setJSONString(String JSONString) {
        this.JSONString = JSONString;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
