package com.acebrico.royalcarribeanapp;

public class Message {
    public String content;
    public String seen;
    public String timestamp;
    public Sender user;

    public Message(String content, String seen, String timestamp, Sender user) {
        this.content = content;
        this.seen = seen;
        this.timestamp = timestamp;
        this.user = user;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", seen='" + seen + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", user=" + user +
                '}';
    }
}
