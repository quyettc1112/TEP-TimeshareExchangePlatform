package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.ResortAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class ResortAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val resortAPIService: ResortAPIService by lazy {
        apiServiceFactory.createApiService(ResortAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to get resort list
    suspend fun getResortList(pageNo: Int, pageSize: Int, resortName: String?) : Resource<ResortModelResponse> {
        return try {
            val response = resortAPIService.getResortList(pageNo, pageSize, resortName)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // funtion to call API to get resort detail
    suspend fun getResortDetail(resortId: Int) : Resource<ResortDetailModelResponse> {
        return try {
            val response = resortAPIService.getResortDetail(resortId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    suspend fun getUnitTypeDetailById(token: String, unitTypeId: Int) : Resource<UnitTypeModel> {
        return try {
            val response = resortAPIService.getUnitTypeDetailById("Bearer $token", unitTypeId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }


}