package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.TimeshareAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import retrofit2.Response
import javax.inject.Inject

class TimeshareRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val timeshareAPIService: TimeshareAPIService by lazy {
        apiServiceFactory.createApiService(TimeshareAPIService::class.java, BaseAPI.BASE_API)
    }

    suspend fun postTimeshare(auth: String, timeshareDTO: TimeshareDTO) : Resource<PostingTimeshareResponse> {
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


}