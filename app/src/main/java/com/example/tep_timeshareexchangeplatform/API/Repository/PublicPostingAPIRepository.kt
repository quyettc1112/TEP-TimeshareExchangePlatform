package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.PublicPostingAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangesResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class PublicPostingAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {

    // init API Service with ApiServiceFactory
    private val publicPostingAPIService: PublicPostingAPIService by lazy {
        apiServiceFactory.createApiService(PublicPostingAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to get postings
    suspend fun getPublicPostings(
        pageNo: Int,
        pageSize: Int,
        resortName: String
    ): Resource<PublicPostingResponse> {
        return try {
            val response = publicPostingAPIService.getRentalPostings(pageNo, pageSize, resortName)
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

    // function to call API to get posting detail
    suspend fun getPublicPostingDetail(postingId: Int): Resource<PublicPostingDetailResponse> {
        return try {
            val response = publicPostingAPIService.getPostingDetail(postingId)
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

    // Get all exchange postings
    suspend fun getExchangePostings(
        pageNo: Int,
        pageSize: Int,
        resortName: String
    ): Resource<ExchangesResponse> {
        return try {
            val response = publicPostingAPIService.getExchangePostings(pageNo, pageSize, resortName)
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


    // Get exchange posting detail by ID
    suspend fun getExchangePostingDetail(postingId: Int): Resource<ExchangeDetailResponse> {
        return try {
            val response = publicPostingAPIService.getExchangePostingDetail(postingId)
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

    // call get rental posting of resort by ID
    suspend fun getRentalPostingOfResortByID(
        resortId: Int,
        pageNo: Int,
        pageSize: Int
    ): Resource<PublicPostingResponse> {
        return try {
            val response = publicPostingAPIService.getRentalPostingOfResortByID(resortId, pageNo, pageSize)
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