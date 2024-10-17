package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.PostingTimeshareResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TimeshareAPIService {

    @POST("customer/timeshare")
    suspend fun postTimeshare(
        @Header ("Authorization") token: String,
        @Body timeshareDTO: TimeshareDTO
    ): Response<PostingTimeshareResponse>


    @GET("customer/timeshares")
    suspend fun getMyTimeshareList(
        @Header ("Authorization") token: String
    ): Response<List<MyTimeshareResponse>>
}