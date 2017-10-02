package com.firechatbot.database;


import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.firechatbot.activities.UserDetailActivity;
import com.firechatbot.pojo.UserDetailBean;
import com.firechatbot.utils.AppConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireDatabase {


    private static FireDatabase mInstance;
    private DatabaseReference mReference;

    private FireDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mReference = FirebaseDatabase.getInstance().getReference();
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
        if (imageUri!=null)
            bean.setProfileUri(imageUri.toString());
        mReference.child(AppConstants.USER_NODE).child(userId).setValue(bean);
    }

    /**
     * Method to check user account in database.
     */
    public void getUserProfile(final Activity activity, final String phone) {
       // ((UserDetailActivity) activity).showViews();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                   // Log.d("response",""+snapshot.child(AppConstants.USER_PHONE).getValue());
                    if (snapshot.child(phone).exists())
                    {
                       // ((UserDetailActivity)activity).startMainActivity();
                    }
                    //UserDetailBean bean = snapshot.getValue(UserDetailBean.class);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
