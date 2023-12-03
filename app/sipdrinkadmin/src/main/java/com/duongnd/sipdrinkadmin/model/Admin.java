package com.duongnd.sipdrinkadmin.Model;

import java.util.HashMap;

public class Admin {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String email;

    public Admin() {
    }

    public Admin(String id, String username, String password, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public Admin setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Admin setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Admin setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Admin setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Admin setEmail(String email) {
        this.email = email;
        return this;
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
