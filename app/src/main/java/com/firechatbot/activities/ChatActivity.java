package com.firechatbot.activities;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.adapters.BottomGalleryAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;



public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, View.OnTouchListener, SlidingUpPanelLayout.PanelSlideListener {

    private EditText messageEt;
    private TextView sendTv;
    private ImageView smileIv, locationIv, cameraIv, separatorIv;
    private LinearLayout parentLL;
    private TextView toolbarHeadingTv;
    private ImageView toolbarBackIv, toolbarCallIv;
    private ContactBean mContact;
    private UserDetailBean mCurrentUser;
    private String mReceiverId;
    private RecyclerView bottomSheetRv;
    private BottomGalleryAdapter mGalleryAdapter;

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
            toolbarBackIv = toolbarTb.findViewById(R.id.iv_back);
            toolbarCallIv = toolbarTb.findViewById(R.id.iv_call);
        }
        parentLL = (LinearLayout) findViewById(R.id.ll_chat_activity);
        messageEt = (EditText) findViewById(R.id.et_message_input);
        sendTv = (TextView) findViewById(R.id.tv_send);
        smileIv = (ImageView) findViewById(R.id.iv_smile);
        locationIv = (ImageView) findViewById(R.id.iv_location);
        cameraIv = (ImageView) findViewById(R.id.iv_camera);
        // bottomSheetRv = (RecyclerView) findViewById(R.id.rv_bottom_gallery);
        //SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        messageEt.setOnClickListener(this);
        sendTv.setOnClickListener(this);
        smileIv.setOnClickListener(this);
        locationIv.setOnClickListener(this);
        cameraIv.setOnClickListener(this);
        toolbarBackIv.setOnClickListener(this);
        separatorIv = (ImageView) findViewById(R.id.iv_separator);
        messageEt.addTextChangedListener(this);
        sendTv.setOnTouchListener(this);
        parentLL.setOnTouchListener(this);
        // slidingUpPanelLayout.addPanelSlideListener(this);
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
        //bottomSheetRv.setLayoutManager(new GridLayoutManager(this, GridLayoutManager.DEFAULT_SPAN_COUNT));
        // mGalleryAdapter = new BottomGalleryAdapter();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_message_input:
                break;
            case R.id.tv_send:
                if (!messageEt.getText().toString().trim().isEmpty()) {
                    FireDatabase.getInstance().getReceiverDetails(this, filterContacts(mContact.getPhone()));
                }
                break;

            case R.id.iv_back:
                finish();

            case R.id.iv_location:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_location);
                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textView = dialog.findViewById(R.id.tv_share_status);
                textView.setText(getString(R.string.share_status) + " " + mContact.getName() + "?");
                dialog.show();
                break;
        }
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
            separatorIv.setVisibility(View.GONE);
            locationIv.setVisibility(View.GONE);
            cameraIv.setVisibility(View.GONE);
            sendTv.setVisibility(View.VISIBLE);
        } else if (editable.toString().trim().isEmpty()) {
            separatorIv.setVisibility(View.VISIBLE);
            locationIv.setVisibility(View.VISIBLE);
            cameraIv.setVisibility(View.VISIBLE);
            sendTv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.tv_send)
            AppUtils.hideKeyboard(view, this);
        if (view.getId() == R.id.ll_chat_activity)
            AppUtils.hideKeyboard(view, this);
        return false;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (newState.name().equalsIgnoreCase(getString(R.string.collapsed))) {
            //action when collapsed

        } else if (newState.name().equalsIgnoreCase(getString(R.string.expanded))) {
            //action when expanded
        }
    }


    /**
     * Method to filter contact.
     */
    private String filterContacts(String number) {
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

    /**
     * Methods to get receiver details.
     */
    public void setReceiverDetails(UserDetailBean bean) {
        mReceiverId = bean.getuId();
        checkReceiverInInbox(mReceiverId);
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
        if (chatRoomId != null)
            FireDatabase.getInstance().createMessageNodeInDatabase(messageEt.getText().toString().trim(), chatRoomId, mCurrentUser.getuId());
        messageEt.setText("");
    }

}
