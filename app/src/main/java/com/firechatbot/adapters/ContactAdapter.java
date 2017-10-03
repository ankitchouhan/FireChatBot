package com.firechatbot.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firechatbot.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.CustomViewHolder>{

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView contactIconSdv;
        private TextView nameTv,lastSeenTv;
        CustomViewHolder(View itemView) {
            super(itemView);
            contactIconSdv = itemView.findViewById(R.id.sdv_contact_icon);
            nameTv = itemView.findViewById(R.id.tv_contact_name);
            lastSeenTv = itemView.findViewById(R.id.tv_contact_last_seen);
        }
    }
}
