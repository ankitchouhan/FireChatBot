package com.firechatbot.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firechatbot.R;

public class SignUpActivity extends AppCompatActivity {

    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        initSpinner();
    }

    /**
     * Method to initialize views.
     * */
    private void initViews()
    {
        spinner = (Spinner) findViewById(R.id.s_country);
        TextView welcomeTv = (TextView) findViewById(R.id.tv_welcome);
        TextView termsTv = (TextView) findViewById(R.id.tv_terms);
        SpannableString welcomeSpan = SpannableString.valueOf(getString(R.string.welcome));
        welcomeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.purple)),11,welcomeSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcomeTv.setText(welcomeSpan);
        SpannableString termsSpan = SpannableString.valueOf(getString(R.string.terms_of_services));
        termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.purple)),termsTv.getText().toString().indexOf("Terms"),termsTv.getText().toString().indexOf("and"),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.purple)),termsTv.getText().toString().indexOf("Privacy"),termsTv.getText().toString().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTv.setText(termsSpan);
    }

    /**
     * Method to initialize spinner.
     * */
    private void initSpinner()
    {
        String[] countryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,countryCode);
        spinner.setAdapter(adapter);
    }

}
