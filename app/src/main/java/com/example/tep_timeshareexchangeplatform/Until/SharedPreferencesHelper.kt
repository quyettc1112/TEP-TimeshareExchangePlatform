package com.example.tep_timeshareexchangeplatform.Until

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_settings"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        // Thêm các key khác nếu cần
    }

    // Lưu User ID
    fun setUserId(userId: Int) {
        preferences.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int {
        return preferences.getInt(KEY_USER_ID, -1) // Trả về -1 nếu không tìm thấy
    }

    // Lưu chế độ tối
    fun setDarkModeEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun isDarkModeEnabled(): Boolean {
        return preferences.getBoolean(KEY_DARK_MODE, false) // Trả về false nếu không tìm thấy
    }

    // Lưu trạng thái thông báo
    fun setNotificationsEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return preferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true) // Trả về true nếu không tìm thấy
    }

    // Xóa tất cả các cài đặt
    fun clear() {
        preferences.edit().clear().apply()
    }
}