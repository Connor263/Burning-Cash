package com.amanotes.beathop.di

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.amanotes.beathop.R
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppsFlyerLib.getInstance()
            .init(resources.getString(R.string.apps_dev_key), appsFlyerConversionListener, this)
        AppsFlyerLib.getInstance().start(this)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(resources.getString(R.string.one_signal_key))
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    }

    private val appsFlyerConversionListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
            data?.let {
                appsFlyerLiveData.postValue(it)
            }
        }

        override fun onConversionDataFail(error: String?) {}

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}

        override fun onAttributionFailure(error: String?) {}
    }

    companion object {
        val appsFlyerLiveData = MutableLiveData<MutableMap<String, Any>>()
    }
}