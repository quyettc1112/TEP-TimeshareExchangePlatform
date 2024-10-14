package com.example.tep_timeshareexchangeplatform.Until

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody

object ErrorHandler {
    fun parseError(errorBody: ResponseBody?): String {
        return try {
            errorBody?.let {
                val errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                errorResponse.message
            } ?: "Unknown error occurred"
        } catch (e: Exception) {
            "Unknown error occurred"
        }
    }
}