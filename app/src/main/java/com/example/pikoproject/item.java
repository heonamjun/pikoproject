package com.example.pikoproject;

public class item {
    public String text;
    public String text2;
    public int img;

    public item(String text, String text2, int img) {
        this.text = text;
        this.text2 = text2;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}

   /* public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
*/