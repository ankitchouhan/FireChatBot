package com.firechatbot.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.beans.MessageBean;
import com.firechatbot.beans.UserDetailBean;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.SenderViewHolder> {


    private final int SENDER = 0;
    private final int RECEIVER = 1;
    private List<MessageBean> mList;
    private UserDetailBean mCurrentUser;
    public MessagesAdapter(List<MessageBean> mList,UserDetailBean mCurrentUser) {
        this.mList = mList;
        this.mCurrentUser = mCurrentUser;

    }

    @Override
    public MessagesAdapter.SenderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SENDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sender_message, parent, false);
//                return new SenderViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receiver_message, parent, false);
//                return new SenderViewHolder(view);
                break;
        }
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sender_message, parent, false);
        return new SenderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.SenderViewHolder holder, int position) {
       /* switch (getItemViewType(position))
        {
            case SENDER:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                if (mList.get(position).getMessage()!=null)
                    senderViewHolder.sendMessageTv.setText(mList.get(position).getMessage());
                break;
            case RECEIVER:
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
                if (mList.get(position).getMessage()!=null)
                    receiverViewHolder.receiveMessageTv.setText(mList.get(position).getMessage());
                break;
        }*/
        if (mList.get(position).getMessage()!=null)
            holder.sendMessageTv.setText(mList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (mList.get(position).getSender().equals(mCurrentUser.getuId())) ? SENDER : RECEIVER;
    }


    /**
     * Sender view holder class.
     */
    class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView sendMessageTv;

        SenderViewHolder(View itemView) {
            super(itemView);
            sendMessageTv = itemView.findViewById(R.id.tv_sender_message);
        }
    }

    /**
     * Receiver View holder class.
     */
    /*private class ReceiverViewHolder extends RecyclerView.ViewHolder {
        private TextView receiveMessageTv;

        ReceiverViewHolder(View itemView) {
            super(itemView);
            receiveMessageTv = itemView.findViewById(R.id.tv_receiver_message);
        }
    }*/
}
