package com.acebrico.royalcarribeanapp;

public class User {
    public String email;
    public String fullname;
    public String idNumber;
    public String password;
    public String role;
    public String online;

    public User(String email, String fullname, String idNumber, String password, String role,String online) {
        this.email = email;
        this.fullname = fullname;
        this.idNumber = idNumber;
        this.password = password;
        this.role = role;
        this.online = online;
    }
    public User() {}

}
