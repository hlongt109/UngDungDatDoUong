package com.duongnd.sipdrinkadmin.model;

import java.util.HashMap;

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

    public static class Admin {
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
}
