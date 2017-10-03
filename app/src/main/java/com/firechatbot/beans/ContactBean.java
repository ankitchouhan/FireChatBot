package com.firechatbot.beans;



public class ContactBean {

    private String name;
    private String phone;

    public ContactBean()
    {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
