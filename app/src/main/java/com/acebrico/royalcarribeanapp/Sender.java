package com.acebrico.royalcarribeanapp;

public class Sender {
    public String avatar;
    public String id;
    public String name;

    public Sender(String avatar, String id, String name) {
        this.avatar = avatar;
        this.id = id;
        this.name = name;
    }

    public Sender() {
    }

    @Override
    public String toString() {
        return "Sender{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
