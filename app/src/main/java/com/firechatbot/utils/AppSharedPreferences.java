package com.firechatbot.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    public AppSharedPreferences(Context context) {
        if (mSharedPreferences == null)
            mSharedPreferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME, AppConstants.PRIVATE_MODE);
    }

    /**
     * Method to create login session and store data.
     * */
    public void createLoginSession(String phone,String senderId)
    {
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(AppConstants.IS_LOGGED_IN,true);
        mEditor.putString(AppConstants.APP_USER_SENDER_ID,senderId);
        mEditor.putString(AppConstants.APP_USER_PHONE_NUMBER,phone);
        mEditor.commit();
    }

    /**
     * Method to retrieve stored information.
     * */
    public String getUserInfo()
    {
        return mSharedPreferences.getString(AppConstants.APP_USER_SENDER_ID,null);
    }

    /**
     * Method to clear shared preferences.
     * */
    public void clearStoredInfo()
    {
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(AppConstants.IS_LOGGED_IN,false);
        mEditor.putString(AppConstants.APP_USER_SENDER_ID,null);
        mEditor.commit();
    }
}
