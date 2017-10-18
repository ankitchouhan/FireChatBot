package com.firechatbot.activities;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.adapters.BottomGalleryAdapter;
import com.firechatbot.adapters.MessagesAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.ImageBean;
import com.firechatbot.beans.MessageBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.database.FireStorage;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, View.OnTouchListener {

    private EditText messageEt;
    private TextView sendTv;
    private ImageView smileIv, locationIv, cameraIv, separatorIv;
    private TextView toolbarHeadingTv, toolbarStatusTv;
    private ImageView toolbarBackIv, toolbarCallIv;
    private ContactBean mContact;
    private UserDetailBean mCurrentUser;
    private UserDetailBean mReceiver;
    private RecyclerView messageRv, galleryRv;
    private LinearLayout bottomGalleryLL;
    private BottomGalleryAdapter mGalleryAdapter;
    private List<ImageBean> mImageList;
    private MessagesAdapter mMessageAdapter;
    private List<MessageBean> mMessageList;
    private List<Uri> mSendingImagesList;
    private boolean mCheckMessages = true;
    private String mMapUrl;
    private double mLatitude, mLongtitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        initVariables();
    }

    /**
     * method to initialize views.
     */
    private void initViews() {
        Toolbar toolbarTb = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbarTb);
        if (toolbarTb != null) {
            toolbarHeadingTv = toolbarTb.findViewById(R.id.tv_heading);
            toolbarStatusTv = toolbarTb.findViewById(R.id.tv_status_last_seen);
            toolbarBackIv = toolbarTb.findViewById(R.id.iv_back);
            toolbarCallIv = toolbarTb.findViewById(R.id.iv_call);
        }
        // LinearLayout parentLL = (LinearLayout) findViewById(R.id.ll_chat_activity);
        bottomGalleryLL = (LinearLayout) findViewById(R.id.ll_bottom_gallery);
        galleryRv = (RecyclerView) findViewById(R.id.rv_gallery);
        messageEt = (EditText) findViewById(R.id.et_message_input);
        sendTv = (TextView) findViewById(R.id.tv_send);
        smileIv = (ImageView) findViewById(R.id.iv_smile);
        locationIv = (ImageView) findViewById(R.id.iv_location);
        cameraIv = (ImageView) findViewById(R.id.iv_camera);
        messageRv = (RecyclerView) findViewById(R.id.rv_chat_message);
        sendTv.setOnClickListener(this);
        smileIv.setOnClickListener(this);
        locationIv.setOnClickListener(this);
        cameraIv.setOnClickListener(this);
        toolbarBackIv.setOnClickListener(this);
        separatorIv = (ImageView) findViewById(R.id.iv_separator);
        // sendTv.setOnTouchListener(this);
        // parentLL.setOnTouchListener(this);
        messageEt.setOnClickListener(this);
        messageEt.addTextChangedListener(this);
        messageEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (bottomGalleryLL.getVisibility() == View.VISIBLE) {
                        bottomGalleryLL.setVisibility(View.GONE);
                        mSendingImagesList.clear();
                        hideViews();
                    }
                }
            }
        });
    }

    /**
     * Method to initialize variables.
     */
    private void initVariables() {
        if (getIntent().getParcelableExtra(AppConstants.CONTACT_NAME) != null)
            mContact = getIntent().getParcelableExtra(AppConstants.CONTACT_NAME);
        if (getIntent().getParcelableExtra(AppConstants.CURRENT_USER) != null)
            mCurrentUser = getIntent().getParcelableExtra(AppConstants.CURRENT_USER);
        toolbarHeadingTv.setText(mContact.getName());
        toolbarCallIv.setVisibility(View.VISIBLE);
        toolbarBackIv.setVisibility(View.VISIBLE);
        mMessageList = new ArrayList<>();
        mImageList = new ArrayList<>();
        mSendingImagesList = new ArrayList<>();
        mGalleryAdapter = new BottomGalleryAdapter(this, mImageList);
        mMessageAdapter = new MessagesAdapter(mMessageList, mCurrentUser, this);
        galleryRv.setLayoutManager(new GridLayoutManager(this, 3));
        galleryRv.setAdapter(mGalleryAdapter);
        messageRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        messageRv.setAdapter(mMessageAdapter);
        messageRv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               /* getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                if (bottom<oldBottom)*/
                messageRv.scrollToPosition(mMessageAdapter.getItemCount() - 1);
            }
        });
        FireDatabase.getInstance().getReceiverDetails(this, filterContacts(mContact.getPhone()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_message_input:
                if (bottomGalleryLL.getVisibility() == View.VISIBLE) {
                    bottomGalleryLL.setVisibility(View.GONE);
                    mSendingImagesList.clear();
                    hideViews();
                }
                break;
            case R.id.tv_send:
                if (!messageEt.getText().toString().trim().isEmpty() || mSendingImagesList.size() > 0) {
                    FireDatabase.getInstance().getReceiverDetails(this, filterContacts(mContact.getPhone()));
                    //mImageList.get(0).setSelected(false);
                    // mGalleryAdapter.notifyDataSetChanged();
                    if (bottomGalleryLL.getVisibility() == View.VISIBLE) {
                        BottomGalleryAdapter.mSelectedImage = 0;
                        bottomGalleryLL.setVisibility(View.GONE);
                        hideViews();
                    }
                }
                break;

            case R.id.iv_back:
                AppUtils.hideKeyboard(toolbarBackIv, this);
                finish();
                break;

            case R.id.iv_location:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_location);
                Button yesBtn = dialog.findViewById(R.id.tv_yes);
                Button noBtn = dialog.findViewById(R.id.tv_no);
                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textView = dialog.findViewById(R.id.tv_share_status);
                textView.setText(getString(R.string.share_status) + " " + mContact.getName() + "?");
                curveCorners(yesBtn, R.color.purple, new float[]{0, 0, 0, 0, 25, 25, 0, 0});
                curveCorners(noBtn, R.color.light_gray, new float[]{0, 0, 0, 0, 0, 0, 25, 25});
                dialog.show();
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestLocationPermission();
                        dialog.cancel();
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                break;
            case R.id.iv_camera:
                AppUtils.hideKeyboard(cameraIv, this);
                requestStoragePermission();
                break;
        }
    }

    /**
     * Method to round corners of dialog.
     */
    public void curveCorners(View view, int color, float[] floats) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(ContextCompat.getColor(this, color));
        shape.setCornerRadii(floats);
        view.setBackground(shape);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            showViews();
        } else if (editable.toString().trim().isEmpty()) {
            hideViews();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      /* if (view.getId() == R.id.ll_chat_activity)
            AppUtils.hideKeyboard(view, this);*/
        return false;
    }


    /**
     * Method to filter contact.
     */
    private String filterContacts(String number) {
        if (number != null) {
            if (number.contains("-"))
                number = number.replaceAll("-", "");
            if (number.contains(" "))
                number = number.replaceAll(" ", "");
            if (number.contains("+91"))
                number = number.replace("+91", "");
            if (number.length() == 11)
                number = number.substring(1);
            return number;
        }
        return null;
    }

    /**
     * Methods to get receiver details.
     */
    public void setReceiverDetails(UserDetailBean bean) {
        mReceiver = bean;
        if (bean.getStatus() == 1)
            toolbarStatusTv.setText(getString(R.string.online));
        else
        {
            String text = getString(R.string.last_active)+" "+android.text.format.DateFormat.format("dd-MM-yy - hh:mm a", mReceiver.getLastSeen());
            toolbarStatusTv.setText(text);
        }
        if (mCheckMessages) {
            FireDatabase.getInstance().getChatRoomId(mCurrentUser.getuId(), bean.getuId(), this);
            mCheckMessages = false;
        } else
            checkReceiverInInbox(bean.getuId());
    }

    /**
     * Method to check receiver in inbox.
     */
    private void checkReceiverInInbox(String receiverId) {
        if (receiverId != null)
            FireDatabase.getInstance().checkUserInInbox(this, mCurrentUser.getuId(), receiverId);
    }

    /**
     * Method to get chatRoom id.
     */
    public void getChatRoomId(String chatRoomId) {
        if (chatRoomId != null) {
            if (mSendingImagesList.size() > 0) {
                for (Uri file : mSendingImagesList) {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setMessageType(1);
                    messageBean.setMessage(String.valueOf(file));
                    messageBean.setSender(mCurrentUser.getuId());
                    messageBean.setStatus(2);
                    messageBean.setTimestamp(System.currentTimeMillis());
                    setImageInMessageList(messageBean);
                    FireStorage.getInstance().uploadImageDuringChat(file, chatRoomId, this);
                }
                mSendingImagesList.clear();
            } else if (!messageEt.getText().toString().trim().isEmpty())
                FireDatabase.getInstance().createMessageNodeInDatabase(0, 0, messageEt.getText().toString().trim(), chatRoomId, mCurrentUser.getuId(), 0);
            else if (mMapUrl != null) {
                FireDatabase.getInstance().createMessageNodeInDatabase(mLatitude, mLongtitude, mMapUrl, chatRoomId, mCurrentUser.getuId(), 2);
                mLatitude = 0;
                mLongtitude = 0;
            }
        }
        messageEt.setText("");
    }

    /**
     * Method to set uploading image in message list.
     */
    private void setImageInMessageList(MessageBean bean) {
        mMessageList.add(bean);
        messageRv.scrollToPosition(mMessageAdapter.getItemCount() - 1);
        mMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Method to get imageUrl and upload Image.
     */
    public void getImageUrlToUpload(Uri url, String chatRoomId) {
        FireDatabase.getInstance().createMessageNodeInDatabase(0, 0, url.toString(), chatRoomId, mCurrentUser.getuId(), 1);
    }

    /**
     * Method to get all messages.
     */
    public void getAllMessages(List<MessageBean> list) {
        mMessageList.clear();
        mMessageList.addAll(list);
        messageRv.scrollToPosition(mMessageAdapter.getItemCount() - 1);
        mMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Method to notify adapter.
     */
    public void notifyAdapter(MessageBean bean) {
        for (int i = 0; i < mMessageList.size(); i++) {
            if (mMessageList.get(i).getMessageId().equals(bean.getMessageId())) {
                mMessageList.get(i).setStatus(bean.getStatus());
                break;
            }
        }
        mMessageAdapter.notifyDataSetChanged();
    }

    /**
     * Method to update receiver details.
     */
    public void updateReceiver(UserDetailBean bean) {
        if (bean != null) {
            if (bean.getStatus() == 1)
                toolbarStatusTv.setText(getString(R.string.online));
            else
                toolbarStatusTv.setText(getString(R.string.last_active) + " " + android.text.format.DateFormat.format("dd-MM-yy hh:mm a", bean.getLastSeen()));
        }
    }

    @Override
    protected void onDestroy() {
        //if (mChatRoomId != null)
        // FireDatabase.getInstance().removeListener(mChatRoomId);
        if (mReceiver != null)
            FireDatabase.getInstance().removeListenerFromUserNode(mReceiver.getPhone());
        FireDatabase.getInstance().removeChildListener(mCurrentUser.getuId(), mReceiver.getuId());
        super.onDestroy();
    }

    /**
     * Method to open bottom gallery.
     */
    private void openBottomGallery() {
        mImageList.clear();
        bottomGalleryLL.setVisibility(View.VISIBLE);
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        ContentResolver cr = getContentResolver();
        Cursor imagesCursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
        if (imagesCursor != null) {
            for (int i = 0; i < imagesCursor.getCount(); i++) {
                imagesCursor.moveToPosition(i);
                int dataColumnIndex = (imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                mImageList.add(new ImageBean(new File(imagesCursor.getString(dataColumnIndex)), false));
                // Uri baseUri = Uri.parse("content://media/external/images/media");
                //mImageList.add(String.valueOf(Uri.withAppendedPath(baseUri, "" + dataColumnIndex)));
            }
            imagesCursor.close();
        }
        if (mImageList.size() > 0)
            mGalleryAdapter.notifyDataSetChanged();
    }

    /**
     * Method to request storage permission.
     */
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.STORAGE_REQUEST_CODE);
        else
            openBottomGallery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openBottomGallery();
                } else
                    AppUtils.displayToast(this, getString(R.string.permission_denied));
                break;
            case AppConstants.ACCESS_FINE_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayPlacePicker();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomGalleryLL.getVisibility() == View.VISIBLE) {
            bottomGalleryLL.setVisibility(View.GONE);
            mSendingImagesList.clear();
            hideViews();
        } else
            super.onBackPressed();
    }

    /**
     * Method to get images list selected.
     */
    public void getSelectedImages(List<Uri> list) {
        if (list.size() > 0) {
            mSendingImagesList = list;
            //mSendingImagesList.addAll(list);
            //list.clear();
            showViews();
        } else {
            //mSendingImagesList.clear();
            hideViews();
        }
    }

    /**
     * Method to show views.
     */
    private void showViews() {
        sendTv.setVisibility(View.VISIBLE);
        separatorIv.setVisibility(View.GONE);
        locationIv.setVisibility(View.GONE);
        cameraIv.setVisibility(View.GONE);
    }

    /**
     * Method to hide views.
     */
    private void hideViews() {
        separatorIv.setVisibility(View.VISIBLE);
        locationIv.setVisibility(View.VISIBLE);
        cameraIv.setVisibility(View.VISIBLE);
        sendTv.setVisibility(View.GONE);
    }

    /**
     * Method to request location permission.
     */
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            displayPlacePicker();
        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.ACCESS_FINE_LOCATION_REQUEST_CODE);
    }


    /**
     * Method to display place picker.
     */
    private void displayPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), AppConstants.PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.PLACE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            Place mPlace = PlacePicker.getPlace(this, data);
            mLatitude = mPlace.getLatLng().latitude;
            mLongtitude = mPlace.getLatLng().longitude;
            mMapUrl = AppUtils.makeUrl(mLatitude, mLongtitude);
            FireDatabase.getInstance().getReceiverDetails(this, filterContacts(mContact.getPhone()));
        }
    }
}

