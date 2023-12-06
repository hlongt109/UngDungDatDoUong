package com.duongnd.sipdrinkadmin.model;

public class Admin {
    private String Id, Email, Password, FullName, Img, Date;

    public Admin() {
    }

    public Admin(String id, String email, String password, String fullName, String img, String date) {
        Id = id;
        Email = email;
        Password = password;
        FullName = fullName;
        Img = img;
        Date = date;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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
