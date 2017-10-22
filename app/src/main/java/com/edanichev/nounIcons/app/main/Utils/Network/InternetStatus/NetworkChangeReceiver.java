package com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public NetworkChangeReceiver() {}

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Intent networkIntent = new Intent(InternetStatus.NETWORK_CHANGE_MESSAGE);
        if(!InternetStatus.isNetworkConnected(context)) {
            networkIntent.putExtra("connected",false);
            context.sendBroadcast(networkIntent);
        } else {
            networkIntent.putExtra("connected",true);
            context.sendBroadcast(networkIntent);
        }
    }
}