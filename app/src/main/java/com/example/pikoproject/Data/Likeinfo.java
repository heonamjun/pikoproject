package com.example.pikoproject.Data;

import java.io.Serializable;

public class Likeinfo implements Serializable {

    private int count;

    public Likeinfo(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
