package com.duongnd.sipdrinkadmin.model;

import java.util.HashMap;

public class Admin {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String email;

    private String image;

    public Admin() {
    }

    public Admin(String id, String username, String password, String fullName, String email, String image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HashMap<String, Object> convertHashMap(){
        HashMap<String,Object> Admin = new HashMap<>();
        Admin.put("id",id);
        Admin.put("username",username);
        Admin.put("password",password);
        Admin.put("fullName",fullName);
        Admin.put("email",email);
        return Admin;
    }
}
