package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface User_ApiService {
    @GET("User/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<User>
}