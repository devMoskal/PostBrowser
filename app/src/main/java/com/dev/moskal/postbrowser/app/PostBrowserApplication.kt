package com.dev.moskal.postbrowser.app

import android.app.Application
import com.dev.moskal.postbrowser.BuildConfig
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class PostBrowserApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initLogs()
        AndroidThreeTen.init(this)
    }

    private fun initLogs() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}