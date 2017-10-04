package com.firechatbot.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.beans.ContactBean;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    private final int INVITE_VIEW = 0;
    private final int CONTACT_VIEW = 1;
    private List<ContactBean> mList;
    private Filter mFilter;
    private List<ContactBean> mFilteredList;

    public ContactAdapter(List<ContactBean> mList)
    {
        this.mList = mList;
        mFilteredList = new ArrayList<>();
        mFilteredList.addAll(mList);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType)
        {
            case INVITE_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invite,parent,false);
                return new InviteViewHolder(view);
            case CONTACT_VIEW:
                view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts,parent,false);
                return new ContactViewHolder(view);
        }
        view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contacts,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position))
        {
            case INVITE_VIEW:
                break;
            case CONTACT_VIEW:
                ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
                int pos = position-1;
                if (mList.get(pos).getName()!=null)
                    contactViewHolder.nameTv.setText(mList.get(pos).getName());
                else
                    contactViewHolder.nameTv.setVisibility(View.GONE);
                if (mList.get(pos).getPhone()!=null)
                    contactViewHolder.lastSeenTv.setText(mList.get(pos).getPhone());
                else
                    contactViewHolder.lastSeenTv.setVisibility(View.GONE);
                contactViewHolder.contactIconTv.setText(setContactImage(mList.get(pos).getName()));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position)
        {
            case 0:
                return INVITE_VIEW;
            case 1:
                return CONTACT_VIEW;
        }
        return CONTACT_VIEW;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new ContactFilter();
        return mFilter;
    }

    /**
     * Class to filter Contacts and return result.
     * */
    private class ContactFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            mList.clear();
            if (charSequence == null || charSequence.length()==0)
            {
                mList.addAll(mFilteredList);
            }
            else {
                for (ContactBean bean: mFilteredList)
                {
                    if (bean.getName().toLowerCase().contains(charSequence))
                        mList.add(bean);
                }
            }
            results.values = mList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mList = (List<ContactBean>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    /**
     * Contact ViewHolder Class.
     * */
    private class ContactViewHolder extends RecyclerView.ViewHolder{

        private TextView contactIconTv;
        private TextView nameTv,lastSeenTv;
        ContactViewHolder(View itemView) {
            super(itemView);
            contactIconTv = itemView.findViewById(R.id.tv_contact_icon);
            nameTv = itemView.findViewById(R.id.tv_contact_name);
            lastSeenTv = itemView.findViewById(R.id.tv_contact_last_seen);
        }

    }

    /**
     * Invite ViewHolder class.
     * */
    private class InviteViewHolder extends RecyclerView.ViewHolder {
        InviteViewHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * Method to display contact image.
     * */
    private String setContactImage(String name)
    {
        String[] contact = name.split(" ");
        String displayName="";
        for (String data : contact)
        {
            displayName = displayName+data.charAt(0);
        }
        return displayName;
    }


}
