package com.firechatbot.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firechatbot.database.FireDatabase;

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
     */
    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Method to validate phone number.
     */
    public static boolean validateNumber(String number) {
        return (!TextUtils.isEmpty(number) && number.matches("[0-9]{10}") && (number.startsWith("9") || (number.startsWith("8")) || (number.startsWith("7"))));
    }

    /**
     * Method to check internet availability.
     */
    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * Method to hide keyboard.
     */
    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Method to make a url with lat and long.
     */
    public static String makeUrl(Double lat, Double lon) {
        return (("https://maps.googleapis.com/maps/api/staticmap?markers=") + lat + "," + lon + "&zoom=12&size=300x300&key=AIzaSyCHu2hTDThO5t0ePuO9y9Zi2pq_kZCQE2w");
    }

}
