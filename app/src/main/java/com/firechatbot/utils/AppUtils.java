package com.firechatbot.utils;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import static android.R.id.message;

public class AppUtils {

    /**
     * Method to display snackBar.
     */
    public static void snackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Method to display toast.
     * */
    public static void displayToast(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }


    /**
     * Method to validate phone number.
     */
    public static boolean validateNumber(String number) {
        return (!TextUtils.isEmpty(number) && number.matches("[0-9]{10}") && (number.startsWith("9")||(number.startsWith("8"))||(number.startsWith("7"))));
    }


}
