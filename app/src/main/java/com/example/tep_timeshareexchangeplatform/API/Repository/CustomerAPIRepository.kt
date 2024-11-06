package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.CustomerAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.PricingSupportResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
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
            val response = customerAPIService.getIsCustomerInit("Bearer $token")
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

    // Get Valid Year Timeshare
    suspend fun getValidYearTimeshare(token: String, timeshareId: Int): Resource<ValidYearResponse> {
        return try {
            val response = customerAPIService.getValidYearTimeshare("Bearer $token", timeshareId)
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
    suspend fun getMyPostingList(token: String, page: Int, size: Int): Resource<MyPostingResponse> {
        return try {
            val response = customerAPIService.getMyPostingList("Bearer $token", page, size)
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
    suspend fun getMyPostingDetail(token: String, postingId: Int): Resource<MyPostingDetailResponse> {
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

    // Create posting
    suspend fun createPosting(token: String, postingDTO: PostingTimeshareDTO): Resource<PostingTimeshareResponse> {
        return try {
            val response = customerAPIService.createPosting("Bearer $token", postingDTO)
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

    // Price support Response
    suspend fun acceptPriceSupport(token: String, postingId: Int, newPrice: Float, isAccepted: Boolean?): Resource<PricingSupportResponse> {
        return try {
            val response = customerAPIService.acceptPriceSupport("Bearer $token", postingId, newPrice, isAccepted)
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


    // Get customer booking
    suspend fun getCustomerBooking(token: String, page: Int, size: Int): Resource<MyBookingResponse> {
        return try {
            val response = customerAPIService.getCustomerBooking("Bearer $token", page, size)
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