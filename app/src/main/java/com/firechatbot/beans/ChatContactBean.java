package com.firechatbot.beans;



public class ChatContactBean {

    private String name;
    private String lastMessage;
    private String profileUri;
    private String phone;
    private String chatRoomId;
    private int messageType;

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfileUri() {
        return profileUri;
    }
}
