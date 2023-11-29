package com.duongnd.sipdrinkadmin.model;

public class Admin {
    String AdminId, UserName, Password, FullName, email, img, date, phone;

    public Admin(String adminId, String userName, String password, String fullName, String email, String img, String date, String phone) {
        AdminId = adminId;
        UserName = userName;
        Password = password;
        FullName = fullName;
        this.email = email;
        this.img = img;
        this.date = date;
        this.phone = phone;
    }

    public Admin() {
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

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
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
}
