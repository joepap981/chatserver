package model;

import lombok.Data;

//@Data
public class Message {
    private int senderId;
    private int receiverId;

    private int length;
    private String text;

    public Message() {
    }

    public Message(int senderId, int receiverId, int length, String text) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.length = length;
        this.text = text;
    }


    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
