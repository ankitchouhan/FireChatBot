package com.firechatbot.beans;



public class ChatRoomBean {

    private String senderId;
    private String receiverId;
    public ChatRoomBean()
    {

    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
