package com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetStatus {

    public static final String NETWORK_CHANGE_MESSAGE = "XXX";
    public static boolean isNetworkConnected(Context context) {
        boolean inConnected = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                inConnected = false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            inConnected = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            inConnected = false;
        }
        return inConnected;
    }

}