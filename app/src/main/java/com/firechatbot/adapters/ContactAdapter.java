package com.firechatbot.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firechatbot.R;
import com.firechatbot.beans.ContactBean;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.CustomViewHolder>{

    private List<ContactBean> mList;

    public ContactAdapter(List<ContactBean> mList)
    {
        this.mList = mList;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (mList.get(position).getName()!=null)
            holder.nameTv.setText(mList.get(position).getName());
        else
            holder.nameTv.setVisibility(View.GONE);
        if (mList.get(position).getPhone()!=null)
            holder.lastSeenTv.setText(mList.get(position).getPhone());
        else
            holder.lastSeenTv.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
