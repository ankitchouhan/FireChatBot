package com.firechatbot.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.adapters.ChatAdapter;
import com.firechatbot.beans.ChatContactBean;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.interfaces.OnAppUserReceived;
import com.firechatbot.interfaces.OnContactsReceived;
import com.firechatbot.utils.AuthenticationUtils;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment implements View.OnClickListener,OnAppUserReceived{

    private RecyclerView recyclerViewRV;
    private TextView addFriendSampleTV;
    private ImageView addFriendIV;
    private Button addFriendBtn;
    private ChatAdapter mChatAdapter;
    private TextView toolbarHeadingTv;
    private ImageView toolbarEditIv;
    private Activity mActivity;
    private List<ChatContactBean> mInboxList;
    private List<UserDetailBean> mAppUsersList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        AuthenticationUtils.getInstance().authStateListener(getActivity());
        initViews(view);
        initVariables();
        return view;
    }

    /**
     * Method to initialize views.
     * */
    private void initViews(View view)
    {
        recyclerViewRV = view.findViewById(R.id.rv_chats);
        addFriendIV = view.findViewById(R.id.iv_chat_image_sample);
        addFriendSampleTV = view.findViewById(R.id.tv_add_chat_sample);
        addFriendBtn = view.findViewById(R.id.b_add_friend);
        Toolbar toolbarTb = view.findViewById(R.id.tb_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarTb);
        if (toolbarTb!=null)
        {
            toolbarHeadingTv = toolbarTb.findViewById(R.id.tv_heading);
            toolbarEditIv = toolbarTb.findViewById(R.id.iv_edit);
        }
        toolbarForChat();
        addFriendBtn.setOnClickListener(this);
    }
    /**
     * Method to initialize variables.
     * */
    private void initVariables()
    {
        //mAppUsersList = new ArrayList<>();
        mActivity = getActivity();
        mInboxList = new ArrayList<>();
        mChatAdapter = new ChatAdapter(mInboxList,mActivity);
        recyclerViewRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewRV.setAdapter(mChatAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        AuthenticationUtils.getInstance().addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        AuthenticationUtils.getInstance().removeAuthStateListener();
    }

    @Override
    public void onClick(View view) {
        AuthenticationUtils.getInstance().signOut();
    }

    public void toolbarForChat()
    {
        toolbarHeadingTv.setText(getString(R.string.chat));
        toolbarEditIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void getAppUsers(List<UserDetailBean> list) {
        //mAppUsersList.addAll(list);
    }

    @Override
    public void getUser(UserDetailBean bean) {
        if (bean!=null) {
            UserDetailBean mCurrentUser = new UserDetailBean();
            mCurrentUser.setuId(bean.getuId());
            FireDatabase.getInstance().getUsersFromInbox(mActivity,bean.getuId());
        }
    }

    @Override
    public void getInboxList(List<ChatContactBean> list) {
        mInboxList.clear();
        mInboxList.addAll(list);
        if (mInboxList.size()>0)
        {
            addFriendBtn.setVisibility(View.GONE);
            addFriendSampleTV.setVisibility(View.GONE);
            addFriendIV.setVisibility(View.GONE);
            recyclerViewRV.setVisibility(View.VISIBLE);
        }
        else
        {
            addFriendBtn.setVisibility(View.VISIBLE);
            addFriendSampleTV.setVisibility(View.VISIBLE);
            addFriendIV.setVisibility(View.VISIBLE);
            recyclerViewRV.setVisibility(View.GONE);
        }
        mChatAdapter.notifyDataSetChanged();
    }

}
