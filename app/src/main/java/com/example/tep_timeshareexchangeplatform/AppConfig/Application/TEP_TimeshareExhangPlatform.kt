package com.example.tep_timeshareexchangeplatform.AppConfig.Application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TEP_TimeshareExhangPlatform : Application() {
    companion object {
        private var instance: TEP_TimeshareExhangPlatform? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        instance = this
    }


}