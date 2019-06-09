package com.example.pikoproject.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Writeinfo implements Serializable {
    private String title;
    private ArrayList<String> contents;
    private String publicsher;
    private Date createdAt;
    private String id;
    private String likecount;
    private String userliked;
    private String likeid ;
    private String email;
    private ArrayList<String> chat;

    public  Writeinfo(){}


    public Writeinfo(String title, ArrayList<String> contents, String publicsher,Date createdAt,String id, String email, String likecount){
        this.title = title;
        this.contents = contents;
        this.publicsher = publicsher;
        this.createdAt = createdAt;
        this.id = id;
        this.email = email;
        this.likecount = likecount;
    }

    public Writeinfo(String title, ArrayList<String> contents, String publicsher,Date createdAt,String id, String email){
        this.title = title;
        this.contents = contents;
        this.publicsher = publicsher;
        this.createdAt = createdAt;
        this.id = id;
        this.email = email;
    }

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


    public Map<String , Object> getWriteinfo(){
        Map<String , Object> docdata = new HashMap<>();
        docdata.put("title",title);
        docdata.put("contents",contents);
        docdata.put("publicsher",publicsher);
        docdata.put("createdAt",createdAt);
        docdata.put("id",id);
        docdata.put("likecount",likecount);
        docdata.put("userliked",userliked);
        docdata.put("likeid",likeid);
        docdata.put("email",email);
        docdata.put("chat",chat);
        return docdata;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
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



    public String getLikeid() {
        return likeid;
    }

    public void setLikeid(String likeid) {
        this.likeid = likeid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getUserliked() {
        return userliked;
    }

    public void setUserliked(String userliked) {
        this.userliked = userliked;
    }
}
