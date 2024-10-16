package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ResortAPIService {

    // Get List
    @GET("public/resort")
    suspend fun getResortList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("resortName") resortName: String?
    ): Response<ResortModelResponse>

    // Get Resort Detail
    @GET("public/resort/{resortId}")
    suspend fun getResortDetail(
        @Path("resortId") resortId: Int
    ): Response<ResortDetailModelResponse>


    @GET("public/unit-type/{unitTypeId}")
    suspend fun getUnitTypeDetailById(
        @Header("Authorization") token: String,
        @Path("unitTypeId") unitTypeId: Int
    ): Response<UnitTypeModel>


}