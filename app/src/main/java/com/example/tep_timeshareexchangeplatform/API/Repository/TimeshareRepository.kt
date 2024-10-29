package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.TimeshareAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyPostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class TimeshareRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val timeshareAPIService: TimeshareAPIService by lazy {
        apiServiceFactory.createApiService(TimeshareAPIService::class.java, BaseAPI.BASE_API)
    }

    // Create Timeshare
    suspend fun postTimeshare(
        auth: String,
        timeshareDTO: TimeshareDTO
    ): Resource<MyPostingTimeshareResponse> {
        return try {
            val response = timeshareAPIService.postTimeshare("Bearer $auth", timeshareDTO)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // Get My Timeshare List
    suspend fun getMyTimeshareList(auth: String): Resource<List<MyTimeshareResponse>> {
        return try {
            val response = timeshareAPIService.getMyTimeshareList("Bearer $auth")
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }


    // Get My Timeshare Detail
    suspend fun getMyTimeshareDetail(
        auth: String,
        timeshareId: Int
    ): Resource<MyTimeshareDetailResponse> {
        return try {
            val response = timeshareAPIService.getMyTimeshareDetail("Bearer $auth", timeshareId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }


}