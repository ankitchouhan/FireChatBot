package com.firechatbot.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firechatbot.R;
import com.firechatbot.adapters.ContactAdapter;
import com.firechatbot.utils.AppConstants;

public class ContactsFragment extends Fragment {

    private SimpleDraweeView profileImageSdv;
    private TextView nameTv,phoneTv;
    private RecyclerView recyclerViewRv;
    private ContactAdapter mContactAdapter;
    private Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        mActivity = getActivity();
        mContactAdapter = new ContactAdapter();
        recyclerViewRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewRv.setAdapter(mContactAdapter);
    }

    /**
     * Method to request contact permission.
     * */
    private void requestContactPermission()
    {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.CONTACTS_REQUEST_CODE);
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.CONTACTS_REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)

        }
    }*/

    /**
     * Method to display contacts.
     * */
    private void displayContacts()
    {

    }

}
