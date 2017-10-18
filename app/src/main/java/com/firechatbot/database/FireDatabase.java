package com.firechatbot.database;


import android.app.Activity;
import android.net.Uri;

import com.firechatbot.activities.ChatActivity;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.activities.SignUpActivity;
import com.firechatbot.beans.ChatContactBean;
import com.firechatbot.beans.ChatRoomBean;
import com.firechatbot.beans.MessageBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.interfaces.OnAppUserReceived;
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
import java.util.HashMap;
import java.util.List;

public class FireDatabase {


    private static FireDatabase mInstance;
    private DatabaseReference mReference;
    private ChildEventListener mChildListener;
    private ChildEventListener mUserInboxListener;
    private ChildEventListener mOnlineStatusListener;
    private ChildEventListener mLastMessageListener;

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
                        ((MainActivity) activity).setUserDetails(bean);
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
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
     * Method to get receiver details.
     */
    public void getReceiverDetails(final Activity activity, String phone) {
        mOnlineStatusListener = mReference.child(AppConstants.USER_NODE).orderByChild(AppConstants.USER_PHONE).equalTo(phone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() != null) {
                            UserDetailBean bean = dataSnapshot.getValue(UserDetailBean.class);
                            if (bean != null) {
                                ((ChatActivity) activity).setReceiverDetails(bean);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        ((ChatActivity) activity).updateReceiver(dataSnapshot.getValue(UserDetailBean.class));
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
        /*mReference.child(AppConstants.USER_NODE).orderByChild(AppConstants.USER_PHONE).equalTo(phone)
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
                });*/
    }

    /**
     * Method to remove listener from user node.
     */
    public void removeListenerFromUserNode(String phone) {
        mReference.child(AppConstants.USER_NODE).orderByChild(AppConstants.USER_PHONE).equalTo(phone).removeEventListener(mOnlineStatusListener);
    }

    /**
     * Method to check user in inbox.
     */
    public void checkUserInInbox(final Activity activity, final String senderId, final String receiverId) {
        mReference.child(AppConstants.INBOX_NODE).child(senderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                            checkReceiverExistence(activity, senderId, receiverId);
                        else
                            createChatRoom(senderId, receiverId, activity, 0);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Method to check if receiver exist.
     */
    private void checkReceiverExistence(final Activity activity, final String senderId, final String receiverId) {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            ((ChatActivity) activity).getChatRoomId((String) dataSnapshot.getValue());
                        } else
                            createChatRoom(senderId, receiverId, activity, 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    /**
     * Method to create chatRoom.
     */
    private void createChatRoom(String senderId, String receiverId, Activity activity, int status) {
        String chatRoomId = mReference.child(AppConstants.CHAT_ROOM_NODE).push().getKey();
        ChatRoomBean bean = new ChatRoomBean();
        bean.setUser1(senderId);
        bean.setUser2(receiverId);
        mReference.child(AppConstants.CHAT_ROOM_NODE).child(chatRoomId).setValue(bean);
        if (status == 0) {
            createSenderNodeInInbox(chatRoomId, senderId, receiverId, activity);
            createReceiverNodeInInbox(chatRoomId, receiverId, senderId);
            getAllMessage(chatRoomId, activity, senderId);
        } else {
            createReceiverNode(chatRoomId, senderId, receiverId);
            createReceiverNode(chatRoomId, receiverId, senderId);
            ((ChatActivity) activity).getChatRoomId(chatRoomId);
            getAllMessage(chatRoomId, activity, senderId);
        }
    }

    /**
     * Method to create sender node in inbox node.
     */
    private void createSenderNodeInInbox(String chatRoomId, String senderId, String receiverId, Activity activity) {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId).setValue(chatRoomId);
        ((ChatActivity) activity).getChatRoomId(chatRoomId);
    }

    /**
     * Method to create receiver node in inbox node.
     */
    private void createReceiverNodeInInbox(String chatRoomId, String receiverId, String senderId) {
        mReference.child(AppConstants.INBOX_NODE).child(receiverId).child(senderId).setValue(chatRoomId);

    }

    /**
     * Method to create receiver node if only sender exist.
     */
    private void createReceiverNode(String chatRoomId, String senderId, String receiverId) {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId).setValue(chatRoomId);
    }

    /**
     * Method to create message node.
     */
    public void createMessageNodeInDatabase(double lat,double longt,String message, String chatRoomId, String senderId, int messageType) {
        String mId = mReference.child(AppConstants.MESSAGE_NODE).push().getKey();
        mReference.child(AppConstants.MESSAGE_NODE).child(chatRoomId).child(mId).setValue(new MessageBean(lat,longt,messageType, senderId, message, mId, ServerValue.TIMESTAMP, 0));
        setLastMessage(chatRoomId, new MessageBean(lat,longt,messageType, senderId, message, mId, ServerValue.TIMESTAMP, 0));
    }

    /**
     * Method to get chatRoomId from inbox.
     */
    public void getChatRoomId(final String senderId, final String receiverId, final Activity activity) {
        mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            getAllMessage((String) dataSnapshot.getValue(), activity, senderId);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * Method to get all messages.
     */
    private void getAllMessage(final String chatRoomId, final Activity activity, final String senderId) {
        final List<MessageBean> messagesList = new ArrayList<>();
        mChildListener = mReference.child(AppConstants.MESSAGE_NODE).child(chatRoomId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        MessageBean bean = dataSnapshot.getValue(MessageBean.class);
                        messagesList.add(bean);
                        ((ChatActivity) activity).getAllMessages(messagesList);
                        if (bean != null) {
                            if (!bean.getSender().equals(senderId))
                                mReference.child(AppConstants.MESSAGE_NODE).child(chatRoomId).child(bean.getMessageId()).child("status").setValue(1);
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        ((ChatActivity) activity).notifyAdapter(dataSnapshot.getValue(MessageBean.class));
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
     */
    public void removeChildListener(String senderId, String receiverId) {

        if (receiverId != null)
            mReference.child(AppConstants.INBOX_NODE).child(senderId).child(receiverId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            removeListener((String) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
    }

    /**
     * method to remove listener.
     */
    private void removeListener(String chatRoomId) {
        if (mChildListener != null && chatRoomId != null)
            mReference.child(AppConstants.MESSAGE_NODE).child(chatRoomId).removeEventListener(mChildListener);
    }

    /**
     * Method to get inbox users with whom user chat.
     */
    public void getUsersFromInbox(final Activity activity, String senderId) {
        mUserInboxListener = mReference.child(AppConstants.INBOX_NODE).child(senderId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() != null) {
                            ((MainActivity) activity).setInboxUsersList(dataSnapshot.getKey(), (String) dataSnapshot.getValue());
                            // ((MainActivity)activity).filterInboxUsers();
                        }
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
     * Method to remove child listener from user inbox.
     */
    public void removeListenerFromInbox(String senderId) {
        if (mUserInboxListener != null && senderId != null)
            mReference.child(AppConstants.INBOX_NODE).child(senderId).removeEventListener(mUserInboxListener);
    }


    /**
     * Method to get inbox users.
     */
    public void getUnknownInboxUser(final Activity activity, String uId, final String chatRoomId) {
        mReference.child(AppConstants.USER_NODE).child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((MainActivity) activity).getUnknownUsers(dataSnapshot.getValue(UserDetailBean.class), chatRoomId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to update user online status.
     */
    public void updateOnlineStatus(String senderId) {
        if (senderId != null)
            mReference.child(AppConstants.USER_NODE).child(senderId).child(AppConstants.ONLINE_STATUS).setValue(1);
    }

    /**
     * Method to update user offline status.
     */
    public void updateOfflineStatus(String senderId) {
        if (senderId != null)
            mReference.child(AppConstants.USER_NODE).child(senderId).child(AppConstants.ONLINE_STATUS).setValue(0);

    }

    /**
     * Method to update last seen.
     */
    public void updateLastSeen(String senderId) {
        if (senderId != null)
            mReference.child(AppConstants.USER_NODE).child(senderId).child(AppConstants.LAST_SEEN).setValue(ServerValue.TIMESTAMP);
    }

    /**
     * Method to set last message.
     */
    private void setLastMessage(String chatRoomId, MessageBean bean) {
        mReference.child(AppConstants.LAST_MESSAGE_NODE).child(chatRoomId).setValue(bean);
    }

    /**
     * Method to get last messages.
     */
    public void getLastMessages(final Activity activity, final String chatRoomId, final String name, final String profileUri, final String phone) {
        mLastMessageListener = mReference.child(AppConstants.LAST_MESSAGE_NODE)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getKey();
                        if (dataSnapshot.getValue() != null) {
                            if (dataSnapshot.getKey().equals(chatRoomId)) {
                                ChatContactBean bean = new ChatContactBean();
                                bean.setName(name);
                                if (profileUri != null)
                                    bean.setProfileUri(profileUri);
                                bean.setPhone(phone);
                                bean.setChatRoomId(chatRoomId);
                                bean.setMessageType(dataSnapshot.getValue(MessageBean.class).getMessageType());
                                bean.setLastMessage(dataSnapshot.getValue(MessageBean.class).getMessage());
                                ((MainActivity) activity).getInboxList(bean);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        ((MainActivity) activity).updateLastMessage(dataSnapshot.getKey(), dataSnapshot.getValue(MessageBean.class));
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
     * Method to remove listener from last message node.
     * */
    public void removeLastMessageNodeListener()
    {
        if (mLastMessageListener!=null)
            mReference.child(AppConstants.LAST_MESSAGE_NODE).removeEventListener(mLastMessageListener);
    }
}
