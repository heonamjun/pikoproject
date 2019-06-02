package com.example.pikoproject.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Writeinfo implements Serializable {
    private String title;
    private ArrayList<String> contents;
    private String publicsher;
    private Date createdAt;
    private String id;

    public Writeinfo(String title, ArrayList<String> contents, String publicsher,Date createdAt,String id){
        this.title = title;
        this.contents = contents;
        this.publicsher = publicsher;
        this.createdAt = createdAt;
        this.id = id;
    }

    public Writeinfo(String title, ArrayList<String> contents, String publicsher,Date createdAt){
        this.title = title;
        this.contents = contents;
        this.publicsher = publicsher;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
