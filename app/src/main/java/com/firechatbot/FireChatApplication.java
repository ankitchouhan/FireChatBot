package com.firechatbot;


import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.lang.reflect.Field;


@ReportsCrashes(mailTo = "ankit.chauhan@appinventiv.com",
        customReportContent = {ReportField.LOGCAT}, mode = ReportingInteractionMode.SILENT)

public class FireChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultFont("SERIF","SanFranciscoDisplay-Bold.otf");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    /**
     * Method to get replacing font.
     * */
    private void setDefaultFont(String typefaceFieldName, String fontAssetName) {
        Typeface regularFont = Typeface.createFromAsset(getAssets(), fontAssetName);
        replaceFont(typefaceFieldName, regularFont);
    }

    /**
     * Method to replace default font.
     * */
    private void replaceFont(String typefaceFieldName, Typeface newTypeface) {
        try {
            Field staticField = Typeface.class.getDeclaredField(typefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
