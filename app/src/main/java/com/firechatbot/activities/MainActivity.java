package com.firechatbot.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firechatbot.R;
import com.firechatbot.adapters.ChatPagerAdapter;
import com.firechatbot.utils.AuthenticationUtils;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayoutTL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initViews();
        setTabs();
    }

    /**
     * Method to initialize views.
     */
    private void initViews() {
        tabLayoutTL = (TabLayout) findViewById(R.id.tl_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.vp_pages);
        tabLayoutTL.setupWithViewPager(pager);
        ChatPagerAdapter pageAdapter = new ChatPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(1);
    }

    /**
     * Method to setup tabs.
     */
    private void setTabs() {
        int[] tabIcons = {R.drawable.selector_contact, R.drawable.selector_chat, R.drawable.selector_settings};
        String[] tabNames = getResources().getStringArray(R.array.tabs);
        TextView text;
        for (int i = 0; i < tabIcons.length; i++) {
            text = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            text.setText(tabNames[i]);
            text.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            tabLayoutTL.getTabAt(i).setCustomView(text);
        }

        tabLayoutTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView()!=null)
                {
                    TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                    textView.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.purple));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView()!=null)
                {
                    TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                    textView.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.silver));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /**
     * Method to start signUp activity after signOut.
     */
    public void startSignUpActivity() {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        finish();
    }

}
