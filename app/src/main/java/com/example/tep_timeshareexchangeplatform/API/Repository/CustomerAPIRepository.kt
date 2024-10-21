package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.CustomerAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
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

}