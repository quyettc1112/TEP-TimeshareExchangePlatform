package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.TimeshareAPIService
import com.example.tep_timeshareexchangeplatform.API.Service.WalletAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class WalletAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val walletAPIService: WalletAPIService by lazy {
        apiServiceFactory.createApiService(WalletAPIService::class.java, BaseAPI.BASE_API)
    }

    // Get Wallet Transaction Detail By UUID
    suspend fun getWalletTransactionDetailByUUID(
        auth: String,
        uuid: String
    ): Resource<WalletDetailRespone> {
        return try {
            val response = walletAPIService.getWalletTransactionDetailByUUID("Bearer $auth", uuid)
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