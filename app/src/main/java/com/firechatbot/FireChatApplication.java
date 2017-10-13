package com.firechatbot;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.firechatbot.activities.ChatActivity;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.database.FireDatabase;
import com.firechatbot.utils.AppSharedPreferences;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.lang.reflect.Field;


@ReportsCrashes(mailTo = "ankit.chauhan@appinventiv.com",
        customReportContent = {ReportField.LOGCAT}, mode = ReportingInteractionMode.SILENT)

public class FireChatApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static boolean activityInForeground;
    private Handler mHandler = new Handler();
    private Runnable mGuard;
    private AppSharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        mSharedPreferences = new AppSharedPreferences(getApplicationContext());
        mSharedPreferences.clearStoredInfo();
        setDefaultFont("SERIF", "SanFranciscoDisplay-Bold.otf");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    /**
     * Method to get replacing font.
     */
    private void setDefaultFont(String typefaceFieldName, String fontAssetName) {
        Typeface regularFont = Typeface.createFromAsset(getAssets(), fontAssetName);
        replaceFont(typefaceFieldName, regularFont);
    }

    /**
     * Method to replace default font.
     */
    private void replaceFont(String typefaceFieldName, Typeface newTypeface) {
        try {
            Field staticField = Typeface.class.getDeclaredField(typefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

        if (mGuard != null) {
            mHandler.removeCallbacks(mGuard);
            mGuard = null;
        }
        if (activity instanceof MainActivity || activity instanceof ChatActivity) {
            activityInForeground = true;
            FireDatabase.getInstance().updateOnlineStatus(mSharedPreferences.getUserInfo());
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        if (!activityInForeground)
            return;
                /*
                 * Use a 400ms guard to protect against jitter
                 * when switching between two activities
                 * in the same app
                 */
        mGuard = new Runnable() {
            @Override
            public void run() {
                if (activityInForeground) {
                    activityInForeground = false;
                    FireDatabase.getInstance().updateOfflineStatus(mSharedPreferences.getUserInfo());
                }
            }
        };
        mHandler.postDelayed(mGuard, 400);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof MainActivity)
            mSharedPreferences.clearStoredInfo();
    }
}
