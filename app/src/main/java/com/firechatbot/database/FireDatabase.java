package com.firechatbot.database;


import android.app.Activity;
import android.net.Uri;

import com.firechatbot.activities.ChatActivity;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.activities.SignUpActivity;
import com.firechatbot.beans.ChatRoomBean;
import com.firechatbot.beans.MessageBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.utils.AppConstants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireDatabase {


    private static FireDatabase mInstance;
    private DatabaseReference mReference;
    private ChildEventListener mChildListener;

    private FireDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.keepSynced(true);
    }

    /**
     * Method to generate single instance.
     */
    public static FireDatabase getInstance() {
        if (mInstance == null)
            mInstance = new FireDatabase();
        return mInstance;
    }

    /**
     * Method to set up user.
     */
    public void writeNewUser(String firstName, String lastName, String phone, String userId, Uri imageUri) {
        UserDetailBean bean = new UserDetailBean();
        bean.setFirstName(firstName);
        bean.setLastName(lastName);
        bean.setPhone(phone);
        if (imageUri != null)
            bean.setProfileUri(imageUri.toString());
        bean.setuId(userId);
        bean.setLastSeen(0);
        bean.setStatus(0);
        mReference.child(AppConstants.USER_NODE).child(userId).setValue(bean);
    }

    /**
     * Method to check user account in database.
     */
    public void checkUserProfile(final Activity activity, final String phone) {
        Query query = mReference.child(AppConstants.USER_NODE).orderByChild(AppConstants.USER_PHONE).equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    ((SignUpActivity) activity).signIn();
                else
                    ((SignUpActivity) activity).startUserDetailActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to get current user details from database.
     */
    public void getUserDetails(final Activity activity, String phone) {
        Query query = mReference.child(AppConstants.USER_NODE).orderByChild(AppConstants.USER_PHONE).equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserDetailBean bean = snapshot.getValue(UserDetailBean.class);
                    if (bean != null) {
                        UserDetailBean detailBean = new UserDetailBean();
                        detailBean.setFirstName(bean.getFirstName());
                        detailBean.setLastName(bean.getLastName());
                        detailBean.setPhone(bean.getPhone());
                        detailBean.setuId(bean.getuId());
                        detailBean.setStatus(bean.getStatus());
                        detailBean.setLastSeen(bean.getLastSeen());
                        detailBean.setProfileUri(bean.getProfileUri());
                        ((MainActivity) activity).setUserDetails(detailBean);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to get contacts from database.
     */
    public void getContacts(final Activity activity) {
        Query query = mReference.child(AppConstants.USER_NODE).orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserDetailBean> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserDetailBean bean = snapshot.getValue(UserDetailBean.class);
                    if (bean != null) {
                        bean.getFirstName();
                        bean.getLastName();
                        bean.getPhone();
                        bean.getProfileUri();
                        list.add(bean);
                    }
                }
                ((MainActivity) activity).getContactsFromDatabase(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     *Method to get receiver details.
     * */
    public void getReceiverDetails(final Activity activity, String phone)
    {
        mReference.child(AppConstants.USER_NODE).orderByChild(AppConstants.USER_PHONE).equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserDetailBean bean = snapshot.getValue(UserDetailBean.class);
                            if (bean != null) {
                                UserDetailBean detailBean = new UserDetailBean();
                                detailBean.setFirstName(bean.getFirstName());
                                detailBean.setLastName(bean.getLastName());
                                detailBean.setPhone(bean.getPhone());
                                detailBean.setuId(bean.getuId());
                                detailBean.setStatus(bean.getStatus());
                                detailBean.setLastSeen(bean.getLastSeen());
                                detailBean.setProfileUri(bean.getProfileUri());
                                ((ChatActivity)activity).setReceiverDetails(detailBean);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Method to check user in inbox.
     * */
    public void checkUserInInbox(final Activity activity, final String senderId, final String receiverId)
    {
        mReference.child(AppConstants.INBOX_NODE).child(senderId)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null)
                    checkReceiverExistence(activity,senderId,receiverId);
                else
                    createChatRoom(senderId,receiverId,activity,0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to check if receiver exist.
     * */
    private void checkReceiverExistence(final Activity activity, final String senderId, final String receiverId)
    {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            ((ChatActivity)activity).getChatRoomId((String) dataSnapshot.getValue());
                        }
                        else
                            createChatRoom(senderId,receiverId,activity,1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    /**
     * Method to create chatRoom.
     * */
    private void createChatRoom(String senderId,String receiverId,Activity activity,int status)
    {
        String chatRoomId = mReference.child(AppConstants.CHAT_ROOM_NODE).push().getKey();
        ChatRoomBean bean = new ChatRoomBean();
        bean.setUser1(senderId);
        bean.setUser2(receiverId);
        mReference.child(AppConstants.CHAT_ROOM_NODE).child(chatRoomId).setValue(bean);
        if (status==0)
        {
            createSenderNodeInInbox(chatRoomId,senderId,receiverId,activity);
            createReceiverNodeInInbox(chatRoomId,receiverId,senderId);
        }
        else
        {
            createReceiverNode(chatRoomId,senderId,receiverId);
            createReceiverNode(chatRoomId,receiverId,senderId);
            ((ChatActivity)activity).getChatRoomId(chatRoomId);
        }
    }

    /**
     * Method to create sender node in inbox node.
     * */
    private void createSenderNodeInInbox(String chatRoomId,String senderId,String receiverId, Activity activity)
    {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId).setValue(chatRoomId);
        ((ChatActivity)activity).getChatRoomId(chatRoomId);
    }

    /**
     * Method to create receiver node in inbox node.
     * */
    private void createReceiverNodeInInbox(String chatRoomId,String receiverId,String senderId)
    {
        mReference.child(AppConstants.INBOX_NODE).child(receiverId).child(senderId).setValue(chatRoomId);

    }

    /**
     * Method to create receiver node if only sender exist.
     * */
    private void createReceiverNode(String chatRoomId,String senderId,String receiverId)
    {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId).setValue(chatRoomId);
    }

    /**
     * Method to create message node.
     * */
    public void createMessageNodeInDatabase(String message,String chatRoomId,String senderId)
    {
        String mId = mReference.child(AppConstants.MESSAGE_NODE).push().getKey();
        mReference.child(AppConstants.MESSAGE_NODE).child(chatRoomId).child(mId).setValue(new MessageBean(senderId,message,mId,ServerValue.TIMESTAMP,0));
    }



    /**
     * Method to get chatRoomId from inbox.
     */
    public void getChatRoomId(String senderId, String receiverId, final Activity activity)
    {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            getAllMessages((String) dataSnapshot.getValue(),activity);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    /**
     * Method to get all messages.
     * */
    private void getAllMessages(String chatRoomId, final Activity activity) {
        final List<MessageBean> messagesList = new ArrayList<>();
        mChildListener = mReference.child(AppConstants.MESSAGE_NODE).child(chatRoomId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        messagesList.add(dataSnapshot.getValue(MessageBean.class));
                        ((ChatActivity)activity).getAllMessages(messagesList);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    /**
     * Method to remove child added listener.
     * */
    public void removeChildListener()
    {
       // mReference.child(AppConstants.MESSAGE_NODE).removeEventListener(mChildListener);
    }
}
