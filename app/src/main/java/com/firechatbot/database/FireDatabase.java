package com.firechatbot.database;


import android.app.Activity;
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
    public void writeNewUser(String firstName, String lastName, String phone) {
        UserDetailBean bean = new UserDetailBean();
        bean.setFirstName(firstName);
        bean.setLastName(lastName);
        bean.setPhone(phone);
        mReference.child(AppConstants.USER_NODE).push().setValue(bean);
    }

    /**
     * Method to check user account in database.
     */
    public void getUserProfile(final Activity activity, final String phone) {
        ((UserDetailActivity) activity).showViews();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.child(phone).exists())
                    {
                        ((UserDetailActivity)activity).startMainActivity();
                    }
                    Log.d("response",""+snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
