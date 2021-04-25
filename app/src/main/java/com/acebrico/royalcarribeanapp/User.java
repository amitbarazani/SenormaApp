package com.acebrico.royalcarribeanapp;

public class User {
    public String email;
    public String fullname;
    public String ID;
    public String password;
    public String role;

    public User(String email, String fullname, String ID, String password, String role) {
        this.email = email;
        this.fullname = fullname;
        this.ID = ID;
        this.password = password;
        this.role = role;
    }
    public User() {}

}
