package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.ResortAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Feedback.FeedbacksResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortDetailModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class PublicResortAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val resortAPIService: ResortAPIService by lazy {
        apiServiceFactory.createApiService(ResortAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to get resort list
    suspend fun getResortList(
        pageNo: Int,
        pageSize: Int,
        resortName: String?
    ): Resource<ResortModelResponse> {
        return try {
            val response = resortAPIService.getResortList(pageNo, pageSize, resortName)
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

    // funtion to call API to get resort detail
    suspend fun getResortDetail(resortId: Int): Resource<ResortDetailModelResponse> {
        return try {
            val response = resortAPIService.getResortDetail(resortId)
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

    // function to call API to get unit type list by resort id
    suspend fun getUnitTypeListByResortId(
        token: String,
        resortId: Int
    ): Resource<List<UnitTypeModel>> {
        return try {
            val response = resortAPIService.getUnitTypeListByResortId("Bearer $token", resortId)
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


    // function to call API to get unit type list by resort id
    suspend fun getUnitTypeDetailById(token: String, unitTypeId: Int): Resource<UnitTypeModel> {
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

    // Get List Feedback Of Resort
    suspend fun getFeedbackListByResortId(
        resortId: Int,
        pageNo: Int,
        pageSize: Int
    ): Resource<FeedbacksResponse> {
        return try {
            val response = resortAPIService.getFeedbackListByResortId(resortId, pageNo, pageSize)
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