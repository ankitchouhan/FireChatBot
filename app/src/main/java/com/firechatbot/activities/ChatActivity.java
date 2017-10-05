package com.firechatbot.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.utils.AppConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText messageEt;
    private TextView sendTv;
    private ImageView smileIv,locationIv,cameraIv,separatorIv;
    private ScrollView scrollViewSv;
    private LinearLayout layoutLL;
    private RelativeLayout layoutRL;
    private DatabaseReference mReference;
    private TextView toolbarHeadingTv;
    private ImageView toolbarBackIv,toolbarCallIv;
    private ContactBean mContact;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        initVariables();
    }

    /**
     * method to initialize views.
     * */
    private void initViews()
    {
        Toolbar toolbarTb = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbarTb);
        if (toolbarTb!=null)
        {
            toolbarHeadingTv = toolbarTb.findViewById(R.id.tv_heading);
            toolbarBackIv = toolbarTb.findViewById(R.id.iv_back);
            toolbarCallIv = toolbarTb.findViewById(R.id.iv_call);
        }
        /*
        messageEt = (EditText) findViewById(R.id.et_message_input);
        sendTv = (TextView) findViewById(R.id.tv_send);
        smileIv = (ImageView) findViewById(R.id.iv_smile);
        locationIv = (ImageView) findViewById(R.id.iv_location);
        cameraIv = (ImageView) findViewById(R.id.iv_camera);
        scrollViewSv = (ScrollView) findViewById(R.id.scrollview);
        layoutLL = (LinearLayout) findViewById(R.id.ll_layout1);
        layoutRL = (RelativeLayout) findViewById(R.id.rl_layout2);
        messageEt.setOnClickListener(this);
        sendTv.setOnClickListener(this);
        smileIv.setOnClickListener(this);
        locationIv.setOnClickListener(this);
        cameraIv.setOnClickListener(this);
        separatorIv = (ImageView) findViewById(R.id.iv_separator);*/
    }

    /**
     * Method to initialize variables.
     * */
    private void initVariables()
    {
        if (getIntent().getParcelableExtra(AppConstants.CONTACT_NAME)!=null)
            mContact = getIntent().getParcelableExtra(AppConstants.CONTACT_NAME);
        mReference = FirebaseDatabase.getInstance().getReference();
        toolbarHeadingTv.setText(mContact.getName());
        toolbarCallIv.setVisibility(View.VISIBLE);
        toolbarBackIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
           /* case R.id.et_message_input:
                separatorIv.setVisibility(View.INVISIBLE);
                locationIv.setVisibility(View.INVISIBLE);
                cameraIv.setVisibility(View.INVISIBLE);
                sendTv.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_send:
                String messageText = messageEt.getText().toString().trim();
                if (!messageText.isEmpty())
                {
                    Map<String,String> map = new HashMap<>();
                    map.put(AppConstants.MESSAGE,messageText);
                    if (mContact!=null)
                        map.put(AppConstants.USER, mContact.getName());
                    //mReference.child(AppConstants.CHAT_NODE);

                }
                separatorIv.setVisibility(View.VISIBLE);
                locationIv.setVisibility(View.VISIBLE);
                cameraIv.setVisibility(View.VISIBLE);
                sendTv.setVisibility(View.INVISIBLE);
                break;*/
        }
    }
}
