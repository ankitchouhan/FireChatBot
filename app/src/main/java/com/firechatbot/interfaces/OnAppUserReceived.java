package com.firechatbot.interfaces;


import com.firechatbot.beans.ChatContactBean;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;

import java.util.List;

public interface OnAppUserReceived {
    void getAppUsers(List<UserDetailBean> list);
    void getUser(UserDetailBean bean);
    void getInboxList(List<ChatContactBean> list);

}
