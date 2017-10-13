package com.firechatbot.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firechatbot.FireChatApplication;
import com.firechatbot.R;
import com.firechatbot.adapters.ChatPagerAdapter;
import com.firechatbot.beans.ChatContactBean;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.InboxBean;
import com.firechatbot.beans.MessageBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.fragments.ChatFragment;
import com.firechatbot.fragments.ContactsFragment;
import com.firechatbot.interfaces.OnAppUserReceived;
import com.firechatbot.interfaces.OnContactsReceived;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppSharedPreferences;
import com.firechatbot.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayoutTL;
    private String mPhoneNumber;
    private List<ContactBean> mContactList;
    private OnContactsReceived mContactsReceived;
    private OnAppUserReceived mAppUserReceived;
    private UserDetailBean mCurrentUser;
    private List<UserDetailBean> mAppUserList;
    private List<InboxBean> mInboxList;
    private List<ContactBean> mAppUserContactList;
    private List<ChatContactBean> mChatContactBeanList;
    private long mBackPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        FireDatabase.getInstance().getContacts(this);
        setContentView(R.layout.activity_main);
        requestContactPermission();
    }

    /**
     * Method to initialize views and variables.
     */
    private void initViews() {
        tabLayoutTL = (TabLayout) findViewById(R.id.tl_tabs);
        ViewPager pagerVP = (ViewPager) findViewById(R.id.vp_pages);
        tabLayoutTL.setupWithViewPager(pagerVP);
        mPhoneNumber = getIntent().getStringExtra(AppConstants.INTENT_PHONE_NUMBER);
        mCurrentUser = new UserDetailBean();
        mAppUserList = new ArrayList<>();
        mInboxList = new ArrayList<>();
        mChatContactBeanList = new ArrayList<>();
        ChatPagerAdapter pageAdapter = new ChatPagerAdapter(getSupportFragmentManager());
        pagerVP.setAdapter(pageAdapter);
        pagerVP.setOffscreenPageLimit(3);
        pagerVP.setCurrentItem(1);
        getCurrentUser();

    }

    /**
     * Method to setup tabs.
     */
    private void setTabs() {
        int[] tabIcons = {R.drawable.selector_contact, R.drawable.selector_chat, R.drawable.selector_settings};
        String[] tabNames = getResources().getStringArray(R.array.tabs);
        for (int i = 0; i < tabIcons.length; i++) {
            TabLayout.Tab tab;
            if ((tab = tabLayoutTL.getTabAt(i)) != null) {
                tab.setIcon(tabIcons[i]);
                tab.setText(tabNames[i]);
            }
        }
    }


    /**
     * Method to request contact permission.
     */
    private void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.CONTACTS_REQUEST_CODE);
        else {
            mContactList = getContacts();
            initViews();
            setTabs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.CONTACTS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mContactList = getContacts();
                initViews();
                setTabs();
            } else {
                initViews();
                setTabs();
                AppUtils.displayToast(this, getString(R.string.permission_denied));
            }
        }
    }


    /**
     * Method to get contacts details
     */
    private List<ContactBean> getContacts() {
        List<ContactBean> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ContactBean contactsBean = new ContactBean();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    contactsBean.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Cursor cr = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?", new String[]{id}, null);
                    if (cr != null && cr.moveToFirst()) {
                        contactsBean.setPhone(cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        cr.close();
                    }
                }
                if (contactsBean.getName() != null && contactsBean.getPhone() != null)
                    list.add(contactsBean);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Method to return contact list.
     */
    public List<ContactBean> contactDetails() {
        return mContactList;
    }


    /**
     * Method to start signUp activity after signOut.
     */
    public void startSignUpActivity() {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        finish();
    }

    /**
     * Method to start chat activity.
     */
    public void startChatActivity(ContactBean name) {
        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(AppConstants.CONTACT_NAME, name).putExtra(AppConstants.CURRENT_USER, mCurrentUser));
    }

    /**
     * Method to start chat activity with userDetailBean.
     */
    public void startChat(ChatContactBean bean) {
        ContactBean bean1 = new ContactBean();
        bean1.setName(bean.getName());
        bean1.setPhone(bean.getPhone());
        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(AppConstants.CONTACT_NAME, bean1).putExtra(AppConstants.CURRENT_USER, mCurrentUser));
    }

    /**
     * Method to get current user.
     */
    private void getCurrentUser() {
        FireDatabase.getInstance().getUserDetails(this, mPhoneNumber);
    }

    /**
     * Method to set user details.
     */
    public void setUserDetails(UserDetailBean bean) {
        if (mContactsReceived != null && bean != null) {
            mCurrentUser.setuId(bean.getuId());
            mCurrentUser.setPhone(bean.getPhone());
            mCurrentUser.setFirstName(bean.getFirstName());
            mCurrentUser.setLastName(bean.getLastName());
            mCurrentUser.setLastSeen(bean.getLastSeen());
            mCurrentUser.setProfileUri(bean.getProfileUri());
            mCurrentUser.setStatus(bean.getStatus());
            mContactsReceived.getCurrentUser(bean);
            new AppSharedPreferences(getApplicationContext()).createLoginSession(mCurrentUser.getPhone(),mCurrentUser.getuId());
            FireDatabase.getInstance().updateOnlineStatus(bean.getuId());
        }
        if (mAppUserReceived != null)
            mAppUserReceived.getUser(mCurrentUser);
    }

    /**
     * Method to get contacts from database.
     */
    public void getContactsFromDatabase(List<UserDetailBean> list) {
        if (mContactsReceived != null)
            mContactsReceived.getContacts(list);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ContactsFragment)
            mContactsReceived = (OnContactsReceived) fragment;
        if (fragment instanceof ChatFragment)
            mAppUserReceived = (OnAppUserReceived) fragment;
    }


    /**
     * Method to get app users list from contact fragment.
     */
    public void getAppUsersList(List<UserDetailBean> list) {
        mAppUserList.clear();
        mAppUserList.addAll(list);
       /* if (mAppUserReceived != null) {
            mAppUserReceived.getAppUsers(list);
        }*/
    }

    /**
     * Method to get app users list in different bean type.
     */
    public void getAppUser(List<ContactBean> list) {
        mAppUserContactList = list;
    }

    /**
     * Method to get inbox users list.
     */
    public void setInboxUsersList(String receiverId, String chatRoomId) {
        mInboxList.add(new InboxBean(receiverId, chatRoomId));
        for (UserDetailBean bean : mAppUserList) {
            if (bean.getuId().equals(receiverId)) {
                for (ContactBean contactBean : mAppUserContactList) {
                    if (bean.getPhone().equals(contactBean.getPhone())) {
                        {
                            FireDatabase.getInstance().getLastMessages(this, chatRoomId, contactBean.getName(), bean.getProfileUri(), bean.getPhone());
                            break;
                        }
                    }
                }
            }
        }
        filterInboxUsers();
    }

    /**
     * Method to update last message.
     */
    public void updateLastMessage(String chatRoomId, MessageBean bean) {
        for (int i = 0; i < mChatContactBeanList.size(); i++) {
            if (mChatContactBeanList.get(i).getChatRoomId().equals(chatRoomId)) {
                mChatContactBeanList.get(i).setLastMessage(bean.getMessage());
                break;
            }
        }
        if (mAppUserReceived != null) {
            mAppUserReceived.getInboxList(mChatContactBeanList);
        }
    }

    /**
     * Method to get inbox list.
     */
    public void getInboxList(ChatContactBean bean) {
        if (mAppUserReceived != null) {
            for (int i = 0; i < mChatContactBeanList.size(); i++) {
                if (mChatContactBeanList.get(i).getChatRoomId().equals(bean.getChatRoomId()))
                    mChatContactBeanList.remove(i);
            }
            mChatContactBeanList.add(0, bean);
            mAppUserReceived.getInboxList(mChatContactBeanList);
        }
    }

    /**
     * Method to filter inbox users.
     */
    public void filterInboxUsers() {

        for (InboxBean inbox : mInboxList) {
            int count = 0;
            for (UserDetailBean bean : mAppUserList) {
                if (bean.getuId().equals(inbox.getReceiverId())) {
                    count = 1;
                    break;
                }
            }
            if (count == 0) {
                FireDatabase.getInstance().getUnknownInboxUser(this, inbox.getReceiverId(), inbox.getChatRoomId());
            }
        }
    }

    /**
     * Method to get unknown users from inbox.
     */
    public void getUnknownUsers(UserDetailBean bean, String chatRoomId) {
      /*  mChatContactBean.setName(bean.getFirstName() + " " + bean.getLastName());
        mChatContactBean.setPhone(bean.getPhone());
        if (bean.getProfileUri() != null)
            mChatContactBean.setProfileUri(bean.getProfileUri());*/
        FireDatabase.getInstance().getLastMessages(this, chatRoomId, bean.getPhone(), bean.getProfileUri(), bean.getPhone());
/*
        if (mAppUserReceived != null) {
            mChatContactBeanList.add(mChatContactBean);
            mAppUserReceived.getInboxList(mChatContactBeanList);
        }*/
    }

    @Override
    public void onBackPressed() {
        if (mBackPressedTime + AppConstants.PERIOD > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else
            AppUtils.displayToast(this, getString(R.string.exit));
        mBackPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        FireDatabase.getInstance().updateLastSeen(mCurrentUser.getuId());
        FireDatabase.getInstance().removeListenerFromInbox(mCurrentUser.getuId());
        FireDatabase.getInstance().removeLastMessageNodeListener();
        super.onDestroy();
    }
}
