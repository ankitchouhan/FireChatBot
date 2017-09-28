package com.firechatbot.activities;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Spinner;
import android.widget.TextView;

import com.firechatbot.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
    }

    /**
     * Method to initialize views.
     * */
    private void initViews()
    {
        Spinner spinner = (Spinner) findViewById(R.id.s_country);
        TextView welcomeTv = (TextView) findViewById(R.id.tv_welcome);
        TextView welcomeMessageTv = (TextView) findViewById(R.id.tv_welcome_message);
        SpannableString welcomeSpan = SpannableString.valueOf(getString(R.string.welcome));
        welcomeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.purple)),11,welcomeSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcomeTv.setText(welcomeSpan);
        SpannableString welcomeMessageSpan = SpannableString.valueOf(getString(R.string.welcome_message));
        welcomeMessageSpan.setSpan(new StyleSpan(Typeface.BOLD),0,23,0);
        welcomeMessageTv.setText(welcomeMessageSpan);
    }

}
