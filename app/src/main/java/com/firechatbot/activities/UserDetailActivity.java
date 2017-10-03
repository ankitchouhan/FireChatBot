package com.firechatbot.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firechatbot.R;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.database.FireStorage;
import com.firechatbot.pojo.UserDetailBean;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AuthenticationUtils;


public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layoutLL;
    private ProgressBar progressBarPb;
    private EditText firstNameEt, lastNameEt;
    private String mPhoneNumber;
    private SimpleDraweeView profileImageSdv;
    private String mUserId;
    private Uri mFile, mFbPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_user_detail);
        initViews();
        getIntentData();
    }

    /**
     * Method to initialize views.
     */
    private void initViews() {
        progressBarPb = (ProgressBar) findViewById(R.id.pb_progress);
        layoutLL = (LinearLayout) findViewById(R.id.ll_sign_up);
        firstNameEt = (EditText) findViewById(R.id.et_first_name);
        lastNameEt = (EditText) findViewById(R.id.et_last_name);
        profileImageSdv = (SimpleDraweeView) findViewById(R.id.sdv_profile_image);
        findViewById(R.id.iv_profile_image).setOnClickListener(this);
        findViewById(R.id.b_done).setOnClickListener(this);
    }

    /**
     * Method to get intent extras.
     */
    private void getIntentData() {
        mPhoneNumber = getIntent().getStringExtra(AppConstants.INTENT_PHONE_NUMBER);
        //mUserId = getIntent().getStringExtra(AppConstants.USER_ID);
        if (getIntent().getParcelableExtra(AppConstants.USER_DETAIL_BEAN) != null) {
            UserDetailBean mBean = getIntent().getParcelableExtra(AppConstants.USER_DETAIL_BEAN);
            firstNameEt.setText(mBean.getFirstName());
            lastNameEt.setText(mBean.getLastName());
            profileImageSdv.setImageURI(mBean.getProfileUri());
            mFbPic = Uri.parse(mBean.getProfileUri());
        }
    }


    /**
     * Method to request permission.
     */
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.STORAGE_REQUEST_CODE);
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, AppConstants.GALLERY_IMAGE_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, AppConstants.GALLERY_IMAGE_REQUEST_CODE);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                mFile = data.getData();
                profileImageSdv.setImageURI(data.getData());
            }
        }
    }

    /**
     * Method to start main activity if user exist in database.
     */
    private void checkUserInDatabase() {
        FireDatabase.getInstance().getUserProfile(this, mPhoneNumber);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_done:
                if (validateName()) {
                    showViews();
                    //checkUserInDatabase();
                    AuthenticationUtils.getInstance().signInAnonymously(this);
                }
                break;
            case R.id.iv_profile_image:
                requestStoragePermission();
        }
    }

    /**
     * Method to upload user profile.
     */
    public void uploadUserProfile(String userId) {
        mUserId = userId;
        if (mFile != null) {
            FireStorage.getInstance().uploadUserImage(mFile, mPhoneNumber, this);
            return;
        } else if (mFbPic != null) {
            uploadData(mFbPic);
            return;
        }
        uploadData(null);
    }

    /**
     * Method to upload user info to database.
     */
    public void uploadData(Uri imageUrl) {
        FireDatabase.getInstance().writeNewUser(firstNameEt.getText().toString().trim(),
                lastNameEt.getText().toString().trim(),
                mPhoneNumber,
                mUserId,
                imageUrl);
        startMainActivity();
    }

    /**
     * Method to validate name.
     */
    private boolean validateName() {
        if (TextUtils.isEmpty(firstNameEt.getText().toString().trim())) {
            firstNameEt.setError(getString(R.string.enter_first_name));
            return false;
        }
        if (TextUtils.isEmpty(lastNameEt.getText().toString().trim())) {
            lastNameEt.setError(getString(R.string.enter_last_name));
            return false;
        }
        return true;
    }

    /**
     * Method to start main activity.
     */
    public void startMainActivity() {
        startActivity(new Intent(UserDetailActivity.this, MainActivity.class));
        finish();
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

}
