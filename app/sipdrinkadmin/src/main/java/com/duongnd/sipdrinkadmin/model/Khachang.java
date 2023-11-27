package com.duongnd.sipdrinkadmin.model;

public class Khachang {
    String Id, UserName, Password, FullName, email, img, date, phone;

    public Khachang() {
    }

    public Khachang(String id, String userName, String password, String fullName, String email, String img, String date, String phone) {
        Id = id;
        UserName = userName;
        Password = password;
        FullName = fullName;
        this.email = email;
        this.img = img;
        this.date = date;
        this.phone = phone;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
