package com.example.tep_timeshareexchangeplatform.AppConfig.Application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class TEP_TimeshareExhangPlatform : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}