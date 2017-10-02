package com.firechatbot.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class UserDetailBean implements Parcelable{

    private String phone;
    private String firstName;
    private String lastName;
    private String profileUri;

    public UserDetailBean() {
    }

    public UserDetailBean(Parcel in) {
        phone = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        profileUri = in.readString();
    }

    public static final Creator<UserDetailBean> CREATOR = new Creator<UserDetailBean>() {
        @Override
        public UserDetailBean createFromParcel(Parcel in) {
            return new UserDetailBean(in);
        }

        @Override
        public UserDetailBean[] newArray(int size) {
            return new UserDetailBean[size];
        }
    };

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getProfileUri() {
        return profileUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phone);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(profileUri);
    }
}
