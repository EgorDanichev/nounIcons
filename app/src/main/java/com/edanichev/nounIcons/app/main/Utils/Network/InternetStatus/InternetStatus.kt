package com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import javax.inject.Inject

open class InternetStatus @Inject
constructor(private val context: Context) {

    open fun isNetworkConnected(): Boolean {
        var isConnected: Boolean
        try {
            val connectivity = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            connectivity?.let {
                val info = connectivity.allNetworkInfo
                if (info != null) {
                    for (anInfo in info) {
                        if (anInfo.state == NetworkInfo.State.CONNECTED) {
                            isConnected = true
                            return true
                        }
                    }
                }
            }
            return false

        } catch (e: Exception) {
            e.printStackTrace()
            isConnected = false
        }
        return isConnected
    }
}