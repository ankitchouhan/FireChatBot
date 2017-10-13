package com.firechatbot.beans;



public class InboxBean {

    private String receiverId;
    private String chatRoomId;

    public InboxBean(String receiverId,String chatRoomId)
    {
        this.receiverId = receiverId;
        this.chatRoomId = chatRoomId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }
}
