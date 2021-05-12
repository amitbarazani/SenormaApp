package com.acebrico.royalcarribeanapp;

public class Message {
    private String content;
    private String seen;
    private String timestamp;
    private Sender user;

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
