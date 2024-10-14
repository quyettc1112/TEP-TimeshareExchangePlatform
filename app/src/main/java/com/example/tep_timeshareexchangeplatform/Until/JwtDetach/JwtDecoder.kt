package com.example.tep_timeshareexchangeplatform.Until.JwtDetach

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.UserJWTPayloadModel
import android.util.Base64
import com.google.gson.Gson
class JwtDecoder() {

    fun parseJwtUsingGson(jwt: String): UserJWTPayloadModel? {
        val parts = jwt.split(".")
        if (parts.size != 3) return null

        // Decode phần payload từ base64-url thành chuỗi JSON
        val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE))

        // Sử dụng Gson để chuyển chuỗi JSON thành object
        return Gson().fromJson(payloadJson, UserJWTPayloadModel::class.java)
    }
}
