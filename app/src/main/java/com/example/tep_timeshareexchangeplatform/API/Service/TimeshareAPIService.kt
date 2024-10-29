package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyPostingTimeshareResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TimeshareAPIService {

    // Create Timeshare
    @POST("customer/timeshare")
    suspend fun postTimeshare(
        @Header ("Authorization") token: String,
        @Body timeshareDTO: TimeshareDTO
    ): Response<MyPostingTimeshareResponse>


    // Get My Timeshare List
    @GET("customer/timeshares")
    suspend fun getMyTimeshareList(
        @Header ("Authorization") token: String
    ): Response<List<MyTimeshareResponse>>

    // Get My Timeshare Detail
    @GET("customer/timeshare/{timeShareID}")
    suspend fun getMyTimeshareDetail(
        @Header ("Authorization") token: String,
        @Path("timeShareID") timeshareId: Int
    ): Response<MyTimeshareDetailResponse>
}