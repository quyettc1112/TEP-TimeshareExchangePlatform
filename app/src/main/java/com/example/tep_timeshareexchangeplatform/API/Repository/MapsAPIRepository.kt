package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.MapsAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.DirectionResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.GeoJsonResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Map.OverpassResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class MapsAPIRepository  @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    // init API Service with ApiServiceFactory
    private val mapsAPIService: MapsAPIService by lazy {
        apiServiceFactory.createApiService(MapsAPIService::class.java, BaseAPI.OPEN_STREET_MAP_API)
    }

    private val overpassAPIService: MapsAPIService by lazy {
        apiServiceFactory.createApiService(MapsAPIService::class.java, BaseAPI.OVERPASS_API)
    }

    private val routingAPIService: MapsAPIService by lazy {
        apiServiceFactory.createApiService(MapsAPIService::class.java, BaseAPI.ROUTING_API)
    }


    // function to call API to get reverse geocoding
    suspend fun getReverseGeocoding(latitude: Double, longitude: Double): Resource<GeoJsonResponse> {
        return try {
            val response = mapsAPIService.getReverseGeocoding(latitude, longitude)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: ${errorMessage}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }


    suspend fun getNodes(latitude: Double, longitude: Double): Resource<OverpassResponse> {
        val queryData = buildOverpassQuery(latitude, longitude)
        return try {
            val response = overpassAPIService.getNodes(queryData)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: ${errorMessage}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    fun buildOverpassQuery(latitude: Double, longitude: Double, radius: Int = 1000): String {
        return "[out:json];node(around:$radius,$latitude,$longitude)[\"name\"];out;"
    }


    suspend fun getRoute(start: String, end: String): Resource<DirectionResponse> {
        return try {
            val response = routingAPIService.getRoute(start, end)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: ${errorMessage}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

}