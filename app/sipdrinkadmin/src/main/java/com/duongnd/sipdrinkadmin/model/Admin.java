package com.duongnd.sipdrinkadmin.model;

public class Admin {
    String Id, UserName, Password, FullName;
    public Admin() {
    }
    public Admin(String id, String userName, String password, String fullName) {
        Id = id;
        UserName = userName;
        Password = password;
        FullName = fullName;
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
