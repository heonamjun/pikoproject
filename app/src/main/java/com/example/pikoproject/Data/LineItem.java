package com.example.pikoproject.Data;

import java.io.Serializable;

public class LineItem implements Serializable {

    public String lineUrl;
    public LineItem() { //파이어에베이스와 연동하려면 매개변수없는걸로 필요?.ㄵ

    }


    public LineItem(String lineUrl/*,int img*/) {

        this.lineUrl = lineUrl;

    }
    public String getLineUrl(){return lineUrl;}
    public void setLineUrl(String lineUrl){this.lineUrl = lineUrl;}
}
