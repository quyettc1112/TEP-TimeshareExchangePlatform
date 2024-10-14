package com.example.tep_timeshareexchangeplatform.Until.TokenManager

import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.apply() // Lưu các giá trị một cách không đồng bộ
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    fun clearTokens() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}