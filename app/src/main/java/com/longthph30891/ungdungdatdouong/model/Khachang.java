package com.longthph30891.ungdungdatdouong.model;

public class Khachang {
    String Id, UserName, Password, FullName, email;

    public Khachang() {
    }

    public Khachang(String id, String userName, String password, String fullName, String email) {
        Id = id;
        UserName = userName;
        Password = password;
        FullName = fullName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
