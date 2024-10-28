package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.CustomerAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class CustomerAPIRepository@Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {

    private val customerAPIService: CustomerAPIService by lazy {
        apiServiceFactory.createApiService(CustomerAPIService::class.java, BaseAPI.BASE_API)
    }

    // Create customer
    suspend fun createCustomer(token: String, customerDTO: CustomerDTO): Resource<CustomerResponse> {
        return try {
            val response = customerAPIService.createCustomer("Bearer $token", customerDTO)
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

    // Check if customer exist
    suspend fun getIsCustomerExist(token: String): Resource<CustomerInfoResponse> {
        return try {
            val response = customerAPIService.getIsCustomerExist("Bearer $token")
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




    // Get my posting list
    suspend fun getMyPostingList(token: String): Resource<List<PostingsResponse.Content>> {
        return try {
            val response = customerAPIService.getMyPostingList("Bearer $token")
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


    // Get my posting detail
    suspend fun getMyPostingDetail(token: String, postingId: Int): Resource<PostingDetailResponse> {
        return try {
            val response = customerAPIService.getMyPostingDetail("Bearer $token", postingId)
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