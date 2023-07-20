package com.raian.cricketeoapp.network

import android.content.Context
import android.net.ConnectivityManager
import com.raian.cricketeoapp.MyApplication

class NetworkUtil() {

    fun isConnected(): Boolean {
        val connectivityManager =
            MyApplication.instance?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}