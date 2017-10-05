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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firechatbot.R;
import com.firechatbot.adapters.ChatPagerAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.fragments.ChatFragment;
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
    private TextView toolbarHeadingTv;
    private ImageView toolbarEditIv,toolbarAddIv;

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
        Toolbar toolbarTb = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbarTb);
        tabLayoutTL.setupWithViewPager(pagerVP);
        if (toolbarTb!=null)
        {
            toolbarHeadingTv = toolbarTb.findViewById(R.id.tv_heading);
            toolbarAddIv = toolbarTb.findViewById(R.id.iv_add);
            toolbarEditIv = toolbarTb.findViewById(R.id.iv_edit);
        }
        mPhoneNumber = getIntent().getStringExtra(AppConstants.INTENT_PHONE_NUMBER);
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
            //tabLayoutTL.addTab(tabLayoutTL.newTab());
            if ((tab = tabLayoutTL.getTabAt(i)) != null) {
                tab.setIcon(tabIcons[i]);
                tab.setText(tabNames[i]);
            }
            /*text = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            text.setText(tabNames[i]);
            text.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            tabLayoutTL.getTabAt(i).setCustomView(text);*/
        }

        tabLayoutTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                    textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.purple));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                    textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.silver));
                }
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
        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(AppConstants.CONTACT_NAME, name));
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
            mContactsReceived.getCurrentUser(bean);
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
        {
            mContactsReceived = (OnContactsReceived) fragment;
            toolbarHeadingTv.setText(getString(R.string.contacts));
            toolbarAddIv.setVisibility(View.VISIBLE);
            toolbarEditIv.setVisibility(View.GONE);
        }
        else if (fragment instanceof ChatFragment)
            {
                toolbarHeadingTv.setText(getString(R.string.chat));
                toolbarAddIv.setVisibility(View.GONE);
                toolbarEditIv.setVisibility(View.VISIBLE);
            }
    }

}
