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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firechatbot.R;
import com.firechatbot.adapters.ChatPagerAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.fragments.ContactsFragment;
import com.firechatbot.interfaces.OnContactsReceived;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private TabLayout tabLayoutTL;
    private String mPhoneNumber;
    private List<ContactBean> mContactList;
    private OnContactsReceived mContactsReceived;
    private UserDetailBean mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        FireDatabase.getInstance().getContacts(this);
        setContentView(R.layout.activity_main);
        requestContactPermission();
    }

    /**
     * Method to initialize views.
     */
    private void initViews() {
        tabLayoutTL = (TabLayout) findViewById(R.id.tl_tabs);
        ViewPager pagerVP = (ViewPager) findViewById(R.id.vp_pages);
        tabLayoutTL.setupWithViewPager(pagerVP);
        mPhoneNumber = getIntent().getStringExtra(AppConstants.INTENT_PHONE_NUMBER);
        mCurrentUser = new UserDetailBean();
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
        //TextView text;
        for (int i = 0; i < tabIcons.length; i++) {
            TabLayout.Tab tab;
            if ((tab = tabLayoutTL.getTabAt(i)) != null) {
                tab.setIcon(tabIcons[i]);
                tab.setText(tabNames[i]);
            }
        }

        tabLayoutTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               /* if (tab.getCustomView() != null) {
                    TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                    textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.purple));
*//*
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_pages + ":" + tabLayoutTL.getSelectedTabPosition());
                    if (fragment instanceof ChatFragment)
                        toolbarForChat();
                    else if (fragment instanceof ContactsFragment)
                        toolbarForContacts();*//*
                }*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
               /* if (tab.getCustomView() != null) {
                    TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                    textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.silver));
                }*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(AppConstants.CONTACT_NAME, name).putExtra(AppConstants.CURRENT_USER,mCurrentUser));
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
        if (mContactsReceived!=null)
        {
            mCurrentUser.setuId(bean.getuId());
            mCurrentUser.setPhone(bean.getPhone());
            mCurrentUser.setFirstName(bean.getFirstName());
            mCurrentUser.setLastName(bean.getLastName());
            mCurrentUser.setLastSeen(bean.getLastSeen());
            mCurrentUser.setProfileUri(bean.getProfileUri());
            mCurrentUser.setStatus(bean.getStatus());
            mContactsReceived.getCurrentUser(bean);
        }
    }

    /**
     * Method to get contacts from database.
     */
    public void getContactsFromDatabase(List<UserDetailBean> list) {
        if (mContactsReceived!=null)
            mContactsReceived.getContacts(list);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ContactsFragment)
            mContactsReceived = (OnContactsReceived) fragment;
    }

}
