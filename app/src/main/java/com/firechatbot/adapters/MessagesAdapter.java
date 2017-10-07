package com.firechatbot.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.beans.MessageBean;
import com.firechatbot.beans.UserDetailBean;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int SENDER = 0;
    private final int RECEIVER = 1;
    private List<MessageBean> mList;
    private Context mContext;
    private UserDetailBean mCurrentUser;

    public MessagesAdapter(List<MessageBean> mList, UserDetailBean mCurrentUser,Context mContext) {
        this.mList = mList;
        this.mCurrentUser = mCurrentUser;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SENDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sender_message, parent, false);
                return new SenderViewHolder(view);
            case RECEIVER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receiver_message, parent, false);
                return new ReceiverViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sender_message, parent, false);
        return new SenderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case SENDER:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                senderViewHolder.sendMessageTv.setText(mList.get(position).getMessage());
                senderViewHolder.timeStampTv.setText(convertTimestampIntoDates((Long) mList.get(position).getTimestamp()));
                break;
            case RECEIVER:
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
                receiverViewHolder.timeStampTv.setText(convertTimestampIntoDates((Long) mList.get(position).getTimestamp()));
                receiverViewHolder.receiveMessageTv.setText(mList.get(position).getMessage());
                break;
        }
        /*if (mList.get(position).getMessage() != null)
            holder.sendMessageTv.setText(mList.get(position).getMessage());*/
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
    private class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView sendMessageTv, timeStampTv;

        SenderViewHolder(View itemView) {
            super(itemView);
            sendMessageTv = itemView.findViewById(R.id.tv_sender_message);
            timeStampTv = itemView.findViewById(R.id.tv_message_timestamp);
        }
    }

    /**
     * Receiver View holder class.
     */
    private class ReceiverViewHolder extends RecyclerView.ViewHolder {
        private TextView receiveMessageTv, timeStampTv;

        ReceiverViewHolder(View itemView) {
            super(itemView);
            receiveMessageTv = itemView.findViewById(R.id.tv_receiver_message);
            timeStampTv = itemView.findViewById(R.id.tv_message_timestamp);
        }
    }

    /**
     * Method to convert timestamp to current time.
     */
    private String convertTimestampIntoDates(long createdDateInMS) {

        return (String) android.text.format.DateFormat.format("hh:mm a",createdDateInMS);
/*
        long currentDateInMS = System.currentTimeMillis();
        long difference = currentDateInMS - createdDateInMS;
        if (difference == 0)
            return null;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthsInMilli = daysInMilli * 30;

        String result = "";

        long elapsedMonths = difference / monthsInMilli;
        difference = difference % monthsInMilli;

        long elapsedWeeks = difference / weeksInMilli;
        difference = difference % weeksInMilli;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;


        long elapsedSeconds = difference / secondsInMilli;
        if (elapsedMonths == 1) {
            result = String.valueOf(elapsedMonths) + mContext.getString(R.string.month_ago);

        } else if (elapsedMonths > 1 && elapsedMonths < 12) {
            result = String.valueOf(elapsedMonths) + mContext.getString(R.string.months_ago);

        } else if (elapsedMonths == 12) {
            result = 1 + mContext.getString(R.string.year_ago);

        } else if (elapsedMonths > 12) {
            result = null;

        } else if (elapsedWeeks == 1) {
            result = String.valueOf(elapsedWeeks) + mContext.getString(R.string.week_ago);

        } else if (elapsedWeeks > 1) {
            result = String.valueOf(elapsedWeeks) + mContext.getString(R.string.weeks_ago);

        } else if (elapsedDays == 1) {
            result = String.valueOf(elapsedDays) + mContext.getString(R.string.day_ago);

        } else if (elapsedDays > 1) {
            result = String.valueOf(elapsedDays) + mContext.getString(R.string.days_ago);

        } else if (elapsedHours == 1) {
            result = String.valueOf(elapsedHours) +mContext.getString(R.string.hour_ago);
        } else if (elapsedHours > 1) {
            result = String.valueOf(elapsedHours) + mContext.getString(R.string.hours_ago);
        } else if (elapsedMinutes == 1) {
            result = String.valueOf(elapsedMinutes) + mContext.getString(R.string.min_ago);
        } else if (elapsedMinutes > 1) {
            result = String.valueOf(elapsedMinutes) + mContext.getString(R.string.mins_ago);
        } else if (elapsedSeconds <= 15) {
            result = mContext.getString(R.string.just_now);
        } else if (elapsedSeconds > 15) {
            result = String.valueOf(elapsedSeconds) + mContext.getString(R.string.secs_ago);
        } else {
            result = null;
        }

        return result;*/
    }
}
