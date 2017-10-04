package com.firechatbot.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firechatbot.R;
import com.firechatbot.adapters.ChatPagerAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;
import com.firechatbot.utils.AuthenticationUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayoutTL;
    private boolean mContactCheck;
    private String mPhoneNumber;
    public UserDetailBean mUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        requestContactPermission();
        getCurrentUser();

    }

    /**
     * Method to initialize views.
     */
    private void initViews() {
        tabLayoutTL = (TabLayout) findViewById(R.id.tl_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.vp_pages);
        tabLayoutTL.setupWithViewPager(pager);
        mPhoneNumber = getIntent().getStringExtra(AppConstants.INTENT_PHONE_NUMBER);
        ChatPagerAdapter pageAdapter = new ChatPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pageAdapter);
        pager.setOffscreenPageLimit(3);
        pager.setCurrentItem(1);
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
            if ((tab = tabLayoutTL.getTabAt(i))!=null)
            {
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
            mContactCheck = true;
            initViews();
            setTabs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.CONTACTS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mContactCheck = true;
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
     * Method to return contact list.
     */
    public boolean contactDetails() {
        return mContactCheck;
    }


    /**
     * Method to start signUp activity after signOut.
     */
    public void startSignUpActivity() {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        finish();
    }


    /**
     * Method to get current user.
     * */
    private void getCurrentUser()
    {
        FireDatabase.getInstance().getUserDetails(this,mPhoneNumber);
    }

    /**
     * Method to set user details.
     * */
    public void setUserDetails(UserDetailBean bean)
    {
        mUserDetails = bean;
    }
}
