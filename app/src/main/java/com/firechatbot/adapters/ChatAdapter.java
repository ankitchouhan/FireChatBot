package com.firechatbot.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firechatbot.R;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.beans.ChatContactBean;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder> {

    private List<ChatContactBean> mList;
    private Context mContext;

    public ChatAdapter(List<ChatContactBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (mList.get(position).getName() != null)
            holder.contactNameTv.setText(mList.get(position).getName());
        if (mList.get(position).getProfileUri() != null)
        {
            holder.profileImageSdv.setVisibility(View.VISIBLE);
            holder.profileImageTv.setVisibility(View.GONE);
            holder.profileImageSdv.setImageURI(mList.get(position).getProfileUri());
        }
        else
        {
            holder.profileImageSdv.setVisibility(View.GONE);
            holder.profileImageTv.setVisibility(View.VISIBLE);
            holder.profileImageTv.setText(setContactImage(mList.get(position).getName()));
        }
        if (mList.get(position).getLastMessage()!=null)
        {
            if (mList.get(position).getMessageType()==0)
                holder.lastMessageTv.setText(mList.get(position).getLastMessage());
            else
                holder.lastMessageTv.setText(mContext.getString(R.string.image));
        }
        else
            holder.lastMessageTv.setText("");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contactNameTv, profileImageTv,lastMessageTv;
        private SimpleDraweeView profileImageSdv;

        CustomViewHolder(View itemView) {
            super(itemView);
            contactNameTv = itemView.findViewById(R.id.tv_chat_user_name);
            profileImageTv = itemView.findViewById(R.id.tv_profile_icon);
            profileImageSdv = itemView.findViewById(R.id.sdv_profile_icon);
            lastMessageTv = itemView.findViewById(R.id.tv_last_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ((MainActivity) mContext).startChat(mList.get(getAdapterPosition()));
        }
    }


    /**
     * Method to display contact image.
     */
    private String setContactImage(String name) {
        String[] contact = name.split(" ");
        String displayName = "";
        for (String data : contact) {
            displayName = displayName + data.charAt(0);
        }
        return displayName;
    }
}
