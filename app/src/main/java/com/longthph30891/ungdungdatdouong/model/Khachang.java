package com.longthph30891.ungdungdatdouong.model;

public class Khachang {
  private String Id, Password, FullName, email, img, date, phone;

    public Khachang() {
    }

    public Khachang(String id, String password, String fullName, String email, String img, String date, String phone) {
        Id = id;
        Password = password;
        FullName = fullName;
        this.email = email;
        this.img = img;
        this.date = date;
        this.phone = phone;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
}

