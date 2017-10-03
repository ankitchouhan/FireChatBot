package com.firechatbot.activities;

import android.content.Intent;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firechatbot.R;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;
import com.firechatbot.utils.AuthenticationUtils;

import org.json.JSONObject;

import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private LinearLayout layoutLL;
    private EditText phoneNumberEt;
    private CallbackManager mCallbackManager;
    private UserDetailBean mBean;
    private ProgressBar progressBarPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbLoginManager();
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
        progressBarPb = (ProgressBar) findViewById(R.id.pb_progressbar);
        SpannableString welcomeSpan = SpannableString.valueOf(getString(R.string.welcome));
        welcomeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple)), 11, welcomeSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcomeTv.setText(welcomeSpan);
        SpannableString termsSpan = SpannableString.valueOf(getString(R.string.terms_of_services));
        termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple)), termsTv.getText().toString().indexOf("Terms"), termsTv.getText().toString().indexOf("and"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple)), termsTv.getText().toString().indexOf("Privacy"), termsTv.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTv.setText(termsSpan);
        findViewById(R.id.b_verify).setOnClickListener(this);
        findViewById(R.id.f_sign_up_facebook).setOnClickListener(this);
    }

    /**
     * Method to setup facebook login.
     */
    private void fbLoginManager() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                AppUtils.snackBar(layoutLL, getString(R.string.fail_authenticate));
            }

            @Override
            public void onError(FacebookException error) {
                AppUtils.snackBar(layoutLL, getString(R.string.fail_authenticate));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Method to set facebook data.
     */
    private void setFacebookData(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (Profile.getCurrentProfile() != null) {
                            mBean = new UserDetailBean();
                            mBean.setFirstName(Profile.getCurrentProfile().getFirstName());
                            mBean.setLastName(Profile.getCurrentProfile().getLastName());
                            mBean.setProfileUri(Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString());
                        }
                    }
                });
        request.executeAsync();
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
        if (AuthenticationUtils.getInstance().getUser() != null)
            startMainActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_verify:
                if (AppUtils.validateNumber(phoneNumberEt.getText().toString().trim())) {
                    showViews();
                    checkUserInDatabase();
                    // AuthenticationUtils.getInstance().signInAnonymously(this, layoutLL);
                    //startUserDetailActivity();
                } else
                    AppUtils.snackBar(layoutLL, getString(R.string.enter_valid_number));
                break;
            case R.id.f_sign_up_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
        }
    }

    /**
     * Method to start user detail activity.
     */
    public void startUserDetailActivity() {
        Intent intent = new Intent(SignUpActivity.this, UserDetailActivity.class);
        intent.putExtra(AppConstants.INTENT_PHONE_NUMBER, phoneNumberEt.getText().toString().trim());
        //intent.putExtra(AppConstants.USER_ID, userId);
        if (mBean != null)
            intent.putExtra(AppConstants.USER_DETAIL_BEAN, mBean);
        hideViews();
        startActivity(intent);
    }

    /**
     * Method to start main activity if user exist in database.
     */
    private void checkUserInDatabase() {
        FireDatabase.getInstance().getUserProfile(this, phoneNumberEt.getText().toString().trim());
    }

    /**
     * Method to start main activity.
     */
    public void startMainActivity() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Method to sign in.
     * */
    public void signIn()
    {
        AuthenticationUtils.getInstance().signInAnonymously(this);
    }

    /**
     * Method to show progress bar.
     */
    public void showViews() {
        progressBarPb.setVisibility(View.VISIBLE);
    }

    /**
     * Method to hide progress bar.
     */
    public void hideViews() {
        progressBarPb.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
