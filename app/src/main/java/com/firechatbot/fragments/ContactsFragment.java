package com.firechatbot.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firechatbot.R;
import com.firechatbot.adapters.ContactAdapter;

public class ContactsFragment extends Fragment {

    private SimpleDraweeView profileImageSdv;
    private TextView nameTv,phoneTv;
    private RecyclerView recyclerViewRv;
    private ContactAdapter mContactAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initViews(view);
        initVariables();
        return view;
    }

    /**
     * Method to initialize views.
     * */
    private void initViews(View view) {
        nameTv = view.findViewById(R.id.tv_name);
        phoneTv = view.findViewById(R.id.tv_phone);
        profileImageSdv = view.findViewById(R.id.sdv_profile_icon);
        recyclerViewRv = view.findViewById(R.id.rv_contacts);
    }

    /**
     * Method to initialize variables.
     * */
    private void initVariables()
    {
        mContactAdapter = new ContactAdapter();
        recyclerViewRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewRv.setAdapter(mContactAdapter);
    }

}
