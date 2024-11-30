package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.GeoJsonResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface MapsAPIService {

    @GET("reverse")
    suspend fun getReverseGeocoding(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "geojson"
    ): Response<GeoJsonResponse>


    @GET("interpreter")
    suspend fun getNodes(
        @Query("data") data: String
    ): Response<OverpassResponse>


}