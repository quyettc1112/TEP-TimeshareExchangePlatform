package com.example.tep_timeshareexchangeplatform.Until.TokenManager

import android.content.Context
import android.content.SharedPreferences
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.google.gson.Gson

class TokenManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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

    // Lưu trạng thái đăng nhập
    fun saveUserLogState(state: UserLogState) {
        val editor = sharedPreferences.edit()
        editor.putString(Constant.USER_LOGIN_STATE, state.name) // Lưu tên của enum
        editor.apply()
    }

    // Lấy trạng thái đăng nhập
    fun getUserLogState(): UserLogState {
        val stateName = sharedPreferences.getString(Constant.USER_LOGIN_STATE, UserLogState.LOGGED_OUT.name)
        return UserLogState.valueOf(stateName!!) // Lấy lại giá trị enum từ tên đã lưu
    }

    // Xoá trạng thái đăng nhập
    fun clearUserLogState() {
        val editor = sharedPreferences.edit()
        editor.remove(Constant.USER_LOGIN_STATE) // Xoá key của trạng thái
        editor.apply()
    }


    // Lưu đối tượng CustomerInfoResponse vào SharedPreferences
    fun saveCustomerInfo(customerInfo: CustomerInfoResponse) {
        val editor = sharedPreferences.edit()
        val customerInfoJson = gson.toJson(customerInfo) // Chuyển đối tượng thành chuỗi JSON
        editor.putString(Constant.CUSTOMER_INFO, customerInfoJson)
        editor.apply()
    }

    // Lấy đối tượng CustomerInfoResponse từ SharedPreferences
    fun getCustomerInfo(): CustomerInfoResponse? {
        val customerInfoJson = sharedPreferences.getString(Constant.CUSTOMER_INFO, null)
        return if (customerInfoJson != null) {
            gson.fromJson(customerInfoJson, CustomerInfoResponse::class.java) // Chuyển chuỗi JSON thành đối tượng
        } else {
            null
        }
    }

    // Xoá thông tin khách hàng khỏi SharedPreferences
    fun clearCustomerInfo() {
        val editor = sharedPreferences.edit()
        editor.remove("customerInfo")
        editor.apply()
    }

    fun clearAllToken() {
        clearTokens()
        clearCustomerInfo()
        clearCustomerInfo()
    }






}