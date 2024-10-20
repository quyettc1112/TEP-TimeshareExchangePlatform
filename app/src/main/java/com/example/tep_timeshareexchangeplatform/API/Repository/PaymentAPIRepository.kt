package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.PaymentAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class PaymentAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    // init API Service with ApiServiceFactory
    private val paymentAPIService: PaymentAPIService by lazy {
        apiServiceFactory.createApiService(PaymentAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to get payment url
    suspend fun getPaymentUrl(amount: Int, orderType: String): Resource<PaymentResponse> {
        return try {
            val response = paymentAPIService.getPaymentUrl(amount, orderType)
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