package com.firechatbot.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.firechatbot.R;
import com.firechatbot.utils.AppConstants;


public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
                finish();
            }
        }, AppConstants.SPLASH_TIME_OUT);
    }
}
