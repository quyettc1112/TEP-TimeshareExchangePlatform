package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.PaymentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentAPIService {

    @GET("payment/url-payment")
    suspend fun getPaymentUrl(
        @Query("amount") amount : Int,
        @Query("orderTYpe") orderTYpe : String,
    ): Response<PaymentResponse>
}