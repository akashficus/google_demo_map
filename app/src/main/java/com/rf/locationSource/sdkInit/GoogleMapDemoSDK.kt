package com.rf.locationSource.sdkInit

import android.app.Application
import android.util.Log
import com.rf.locationSource.localDB.GoogleMapDemoDB
import com.rf.locationSource.utils.AppConfig
import com.rf.locationSource.utils.SharedPreference
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GoogleMapDemoSDK : Application() {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var appConfig: AppConfig

    override fun onCreate() {
        super.onCreate()
        Log.d(GoogleMapDemoDB::class.simpleName,"::"+appConfig.baseUrl)
        if (::appConfig.isInitialized) {
            Log.d(GoogleMapDemoDB::class.simpleName, "::" + appConfig.baseUrl)
        } else {
            Log.e("GoogleMapDemoDB", "Hilt injection failed!")
        }
    }
}