package com.example.pikoproject.Data;

import java.io.Serializable;

public class item implements Serializable {
    public String gps;
    public String title;
    //public int img;

    public String picUrl;
    public item() { //파이어에베이스와 연동하려면 매개변수없는걸로 필요?.ㄵ

    }


    public item(String gps, String title, String picUrl/*,int img*/) {
        this.gps = gps;
        this.title = title;
        this.picUrl = picUrl;

    }
    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
