package com.example.waiterlessfood.model;

public class UserDb {
    private String phone,password,fullname,email;

    public UserDb(){

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDb(String phone, String password, String fullname, String email) {
        this.phone = phone;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
    }
}
