package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.SampleAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Sample.UserSampleModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class SampleAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val sampleAPIService: SampleAPIService by lazy {
        apiServiceFactory.createApiService(SampleAPIService::class.java, BaseAPI.MOCK_API)
    }

    // Get user list
    suspend fun getUserList() : Resource<UserSampleModel> {
        return try {
            val response = sampleAPIService.getUserList()
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorHandler = response.errorBody()?.string()
                Resource.error("Error: ${response.code()}, Message: $errorHandler", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }
}