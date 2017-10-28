package com.firechatbot.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firechatbot.R;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(view);
        return view;
    }

    /**
     * Method to initialize views.
     * */
    private void initViews(View view)
    {
        Toolbar toolbarTb = view.findViewById(R.id.tb_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarTb);
        if (toolbarTb!=null)
        {
            TextView toolbarHeadingTv = toolbarTb.findViewById(R.id.tv_heading);
            toolbarHeadingTv.setText(getString(R.string.settings));
        }

    }

}
