package com.raian.cricketeoapp

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager

class MyApplication : Application() {

    companion object {
        var instance: MyApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}