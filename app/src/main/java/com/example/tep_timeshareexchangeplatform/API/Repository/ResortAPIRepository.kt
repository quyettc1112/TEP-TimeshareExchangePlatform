package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.ResortAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.ResortModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class ResortAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val resortAPIService: ResortAPIService by lazy {
        apiServiceFactory.createApiService(ResortAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to get resort list
    suspend fun getResortList(pageNo: Int, pageSize: Int, resortName: String?) : Resource<ResortModel> {
        try {
            val response = resortAPIService.getResortList(pageNo, pageSize, resortName)
            if (response.isSuccessful) {
                return Resource.success(response.body())
            } else {
                return Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            return Resource.error("Network Error: ${e.message}", null)
        }
    }


}