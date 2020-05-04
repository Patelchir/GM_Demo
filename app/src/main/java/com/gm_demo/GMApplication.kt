package com.gm_demo

import android.app.Application
import com.gm_demo.network.ApiClient

class GMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.initRetrofit()
    }
}