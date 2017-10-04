package com.firechatbot.utils;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.firechatbot.R;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.activities.SignUpActivity;
import com.firechatbot.activities.UserDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationUtils {

    private FirebaseAuth mAuth;
    private static AuthenticationUtils mInstance;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private AuthenticationUtils() {
        mAuth = FirebaseAuth.getInstance();
    }


    /**
     * Method to return instance of this class.
     */
    public static AuthenticationUtils getInstance() {
        if (mInstance == null)
            mInstance = new AuthenticationUtils();
        return mInstance;
    }

    /**
     * Method to signIn Anonymously.
     */
    public void signInAnonymously(final Activity activity) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (activity instanceof UserDetailActivity)
                                ((UserDetailActivity) activity).uploadUserProfile(getUser().getUid());
                            else
                                ((SignUpActivity) activity).startMainActivity();
                        } else if (activity instanceof UserDetailActivity) {
                            ((UserDetailActivity) activity).hideViews();
                            AppUtils.displayToast(activity, activity.getString(R.string.fail_authenticate));
                        }else
                        {
                            ((SignUpActivity)activity).hideViews();
                            AppUtils.displayToast(activity, activity.getString(R.string.fail_authenticate));
                        }
                    }
                });
    }

    /**
     * Method to assign authStateListener.
     */
    public void authStateListener(final Activity activity) {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (getUser() == null)
                    ((MainActivity) activity).startSignUpActivity();
            }
        };
    }


    /**
     * Method to add authStateListener.
     */
    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * Method to remove authStateListener.
     */
    public void removeAuthStateListener() {
        if (mAuth != null)
            mAuth.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * Method to signOut user.
     */
    public void signOut() {
        if (mAuth.getCurrentUser() != null)
            mAuth.signOut();
    }

    /**
     * Method to return current user.
     */
    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }


}
