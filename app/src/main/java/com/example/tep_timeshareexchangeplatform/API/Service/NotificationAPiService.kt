package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NotificationAPiService {
    @GET("notification/customer")
    suspend fun getCustomerNotification(
        @Header ("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ) : Response<NotificationResponse>
}