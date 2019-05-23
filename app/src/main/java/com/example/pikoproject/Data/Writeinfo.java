package com.example.pikoproject.Data;

public class Writeinfo {
    private String title;
    private String contents;
    private String publicsher;

    public Writeinfo(String title, String contents, String publicsher){
        this.title = title;
        this.contents = contents;
        this.publicsher = publicsher;
    }

    public String getPublicsher() {
        return publicsher;
    }

    public void setPublicsher(String publicsher) {
        this.publicsher = publicsher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
