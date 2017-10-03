package com.firechatbot.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.adapters.ChatAdapter;
import com.firechatbot.utils.AuthenticationUtils;


public class ChatFragment extends Fragment implements View.OnClickListener{

    RecyclerView recyclerViewRV;
    TextView addFriendSampleTV;
    ImageView addFriendIV;
    Button addFriendBtn;
    ChatAdapter mChatAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        addFriendBtn.setOnClickListener(this);
    }
    /**
     * Method to initialize variables.
     * */
    private void initVariables()
    {
        mChatAdapter = new ChatAdapter();
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
}
