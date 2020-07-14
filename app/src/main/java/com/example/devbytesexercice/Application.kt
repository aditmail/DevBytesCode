package com.example.devbytesexercice

import android.app.Application
import timber.log.Timber

class Applications : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}