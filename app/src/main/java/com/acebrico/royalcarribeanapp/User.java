package com.acebrico.royalcarribeanapp;

public class User {
    public String email;
    public String fullName;
    public String idNumber;
    public String password;
    public String role;
    public String Online;

    public User(String email, String fullname, String idNumber, String password, String role,String online) {
        this.email = email;
        this.fullName = fullname;
        this.idNumber = idNumber;
        this.password = password;
        this.role = role;
        this.Online = online;
    }
    public User() {}

}
