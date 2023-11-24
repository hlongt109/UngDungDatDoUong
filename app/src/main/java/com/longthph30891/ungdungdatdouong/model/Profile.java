package com.longthph30891.ungdungdatdouong.model;

public class Profile {
    String fullName, userName, date, phone, image;

    public Profile(String fullName, String userName, String date, String phone, String image) {
        this.fullName = fullName;
        this.userName = userName;
        this.date = date;
        this.phone = phone;
        this.image = image;
    }

    public Profile() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
