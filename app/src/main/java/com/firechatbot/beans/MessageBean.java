package com.firechatbot.beans;


import java.util.Map;

public class MessageBean {

    private String sender;
    private String message;
    private Map<String,String> timestamp;
    private String messageId;
    private int status;
    private String receiver;

    public MessageBean()
    {

    }

    public MessageBean(String sender,String message,String messageId,Map<String,String> timestamp,int status)
    {
        this.sender = sender;
        this.message = message;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(Map<String,String> timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public String getMessageId() {
        return messageId;
    }
}
