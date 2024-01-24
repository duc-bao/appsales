package com.manager.myapplication.model;

public class User {
    String id;
    String email;
    String pass;
    String username;
    String mobile;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User(String id, String email, String pass, String username, String mobile) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.username = username;
        this.mobile = mobile;
    }

    public User() {
    }
}
