package com.example.tep_timeshareexchangeplatform.Until

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    companion object {
        const val LANGUAGE_KEY = "language_key"
    }

    fun saveLanguage(languageCode: String) {
        prefs.edit().putString(LANGUAGE_KEY, languageCode).apply()
    }

    fun getLanguage(): String? {
        return prefs.getString(LANGUAGE_KEY, "vi") // Default language is Vietnamese
    }
}