package com.firechatbot.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder>{

    public ChatAdapter()
    {

    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        CustomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
