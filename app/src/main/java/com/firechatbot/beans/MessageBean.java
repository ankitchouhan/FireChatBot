package com.firechatbot.beans;




public class MessageBean {

    private String sender;
    private String message;
    private Object timestamp;
    private String messageId;
    private int status;
    private String receiver;
    private int messageType;
    private double lat;
    private double longt;

    public MessageBean()
    {

    }

    public MessageBean(double lat,double longt,int messageType,String sender,String message,String messageId,Object timestamp,int status)
    {
        this.lat = lat;
        this.longt = longt;
        this.messageType = messageType;
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

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public double getLat() {
        return lat;
    }

    public double getLongt() {
        return longt;
    }

    public int getMessageType() {
        return messageType;
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

    public Object getTimestamp() {
        return timestamp;
    }

    public String getMessageId() {
        return messageId;
    }
}
