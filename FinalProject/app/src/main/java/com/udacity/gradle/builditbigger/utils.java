package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class utils {

    private static final String LOG_TAG = "utils";

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            Log.e(LOG_TAG, "Error accessing ConnectivityManager");
            return false;
        }
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
