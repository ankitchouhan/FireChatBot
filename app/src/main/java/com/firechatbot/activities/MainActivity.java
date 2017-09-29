package com.firechatbot.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firechatbot.R;
import com.firechatbot.adapters.ChatPageAdapter;
import com.firechatbot.utils.AuthenticationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TabLayout tabLayoutTL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthenticationUtils.getInstance().authStateListener(this);
        initViews();
    }

    /**
     * Method to initialize views.
     * */
    private void initViews()
    {
        tabLayoutTL = (TabLayout) findViewById(R.id.tl_tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.vp_pages);
        pager.setCurrentItem(1);
        tabLayoutTL.setupWithViewPager(pager);
        ChatPageAdapter pageAdapter = new ChatPageAdapter(getSupportFragmentManager());
        pager.setAdapter(pageAdapter);
        //findViewById(R.id.b_sign_out).setOnClickListener(this);
    }


    /**
     * Method to start signUp activity after signOut.
     */
    public void startSignUpActivity() {
        startActivity(new Intent(MainActivity.this,SignUpActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuthenticationUtils.getInstance().addAuthStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AuthenticationUtils.getInstance().removeAuthStateListener();
    }

    @Override
    public void onClick(View view) {
        AuthenticationUtils.getInstance().signOut();
    }
}
