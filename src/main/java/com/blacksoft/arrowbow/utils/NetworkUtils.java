package com.blacksoft.arrowbow.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to hold utils that are related to newtwork & connectivity
 */

public class NetworkUtils {

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private NetworkUtils() {
    }

    /**
     * checking if device is connected to a wifi or mobile data network
     *
     * @param context: a {@link NonNull} context object
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public final static boolean isConnectedToNetwork(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        if (manager != null)
            for (Network network : manager.getAllNetworks()) {

                networkInfo = manager.getNetworkInfo(network);
                int networkType = networkInfo.getType();

                if ((networkType == ConnectivityManager.TYPE_MOBILE
                        || networkType == ConnectivityManager.TYPE_WIFI)
                        && networkInfo.isConnected()) {
                    return true;
                }
            }

        return false;
    }
}