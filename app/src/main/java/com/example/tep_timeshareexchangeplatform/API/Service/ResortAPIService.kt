package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.ResortModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ResortAPIService {

    @GET("public/resort")
    suspend fun getResortList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("resortName") resortName: String?
    ): Response<ResortModel>

}