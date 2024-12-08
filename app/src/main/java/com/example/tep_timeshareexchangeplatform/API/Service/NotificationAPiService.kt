package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotiUpdateRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationAPiService {
    @GET("notification/customer")
    suspend fun getCustomerNotification(
        @Header ("Authorization") token: String,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ) : Response<NotificationResponse>

    // Mark Read
    @POST("notification/mark-read/{notiId}")
    suspend fun markReadNotification(
        @Header ("Authorization") token: String,
        @Path("notiId") notiId: Int
    ) : Response<NotiUpdateRespone>

    // Mark All Read
    @POST("notification/mark-read/all/user/{userId}")
    suspend fun markAllReadNotification(
        @Header ("Authorization") token: String,
        @Path("userId") userId: Int
    ) : Response<Boolean>
}