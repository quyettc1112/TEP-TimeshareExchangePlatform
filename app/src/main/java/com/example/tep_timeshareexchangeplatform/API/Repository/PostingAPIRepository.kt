package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.PaymentAPIService
import com.example.tep_timeshareexchangeplatform.API.Service.PostingAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class PostingAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {

    // init API Service with ApiServiceFactory
    private val postingAPIService: PostingAPIService by lazy {
        apiServiceFactory.createApiService(PostingAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to get postings
    suspend fun getPostings(pageNo: Int, pageSize: Int, resortName: String) : Resource<PostingsResponse> {
        return try {
            val response = postingAPIService.getPostings(pageNo, pageSize, resortName)
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
    suspend fun getPostingDetail(postingId: Int) : Resource<PostingDetailResponse> {
        return try {
            val response = postingAPIService.getPostingDetail(postingId)
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