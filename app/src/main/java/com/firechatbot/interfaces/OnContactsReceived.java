package com.firechatbot.interfaces;


import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;

import java.util.List;

public interface OnContactsReceived {
    void getContacts(List<UserDetailBean> list);
    void getCurrentUser(UserDetailBean bean);
    void syncContacts(List<ContactBean> list);
}
