package com.lord_ukaka.worldnews

import android.app.Application
import android.content.Context
import com.airbnb.lottie.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NewsApplication: Application() {

    //Load Timber on app creation

    companion object {
        private var instance: NewsApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}