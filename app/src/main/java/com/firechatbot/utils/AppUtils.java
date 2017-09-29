package com.firechatbot.utils;


import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

public class AppUtils {

    /**
     * Method to display snackBar.
     */
    public static void snackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }


    /**
     * Method to validate phone number.
     */
    public static boolean validateNumber(String number) {
        return (!TextUtils.isEmpty(number) && number.matches("[0-9]{10}"));
    }


}
