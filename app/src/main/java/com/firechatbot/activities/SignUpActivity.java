package com.firechatbot.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;
import com.firechatbot.utils.AuthenticationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private LinearLayout layoutLL;
    private EditText phoneNumberEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        initSpinner();
        getUser();
    }

    /**
     * Method to initialize views.
     */
    private void initViews() {
        spinner = (Spinner) findViewById(R.id.s_country);
        TextView welcomeTv = (TextView) findViewById(R.id.tv_welcome);
        TextView termsTv = (TextView) findViewById(R.id.tv_terms);
        layoutLL = (LinearLayout) findViewById(R.id.ll_sign_up);
        phoneNumberEt = (EditText) findViewById(R.id.et_number);
        SpannableString welcomeSpan = SpannableString.valueOf(getString(R.string.welcome));
        welcomeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple)), 11, welcomeSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcomeTv.setText(welcomeSpan);
        SpannableString termsSpan = SpannableString.valueOf(getString(R.string.terms_of_services));
        termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple)), termsTv.getText().toString().indexOf("Terms"), termsTv.getText().toString().indexOf("and"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple)), termsTv.getText().toString().indexOf("Privacy"), termsTv.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTv.setText(termsSpan);
        findViewById(R.id.b_verify).setOnClickListener(this);
    }

    /**
     * Method to initialize spinner.
     */
    private void initSpinner() {
        String[] countryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countryCode);
        spinner.setAdapter(adapter);
    }


    /**
     * Method to check User Details.
     */
    private void getUser() {
        if (AuthenticationUtils.getInstance().getUser()!= null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_verify:
                if (AppUtils.validateNumber(phoneNumberEt.getText().toString().trim()))
                {
                    startActivity(new Intent(SignUpActivity.this, UserDetailActivity.class).putExtra(AppConstants.USER_PHONE_NUMBER,phoneNumberEt.getText().toString().trim()));
                    finish();
                }
                else
                    AppUtils.snackBar(layoutLL,getString(R.string.enter_valid_number));
                break;
        }
    }


}
