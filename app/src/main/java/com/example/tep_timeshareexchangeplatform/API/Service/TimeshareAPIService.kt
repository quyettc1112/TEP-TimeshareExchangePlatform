package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyPostingTimeshareResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Header ("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<MyTimeshareResponse>

    // Get My Timeshare Detail
    @GET("customer/timeshare/{timeShareID}")
    suspend fun getMyTimeshareDetail(
        @Header ("Authorization") token: String,
        @Path("timeShareID") timeshareId: Int
    ): Response<MyTimeshareDetailResponse>
}