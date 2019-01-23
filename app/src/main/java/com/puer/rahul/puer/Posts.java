package com.puer.rahul.puer;

public class Posts
{
    public String uid, time, date, postimage, description, profileimage, fullname,type,key,status;

    public Posts()
    {

    }

    public Posts(String uid, String time, String date, String postimage, String description, String profileimage, String fullname,String type,String key,String status) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.profileimage = profileimage;
        this.description = description;
        this.fullname = fullname;
        this.type = type;
        this.key =key;
        this.status = status;
    }

    public  String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public  String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public  String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.type = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getpostimage() {
        return postimage;
    }

    public void setpostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}