package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.DirectionResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.GeoJsonResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Path

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


    @GET("route/v1/driving/{start};{end}")
    suspend fun getRoute(
        @Path("start") start: String, // "lon1,lat1"
        @Path("end") end: String,     // "lon2,lat2"
        @Query("overview") overview: String = "full",
        @Query("geometries") geometries: String = "geojson"
    ): Response<DirectionResponse>


}