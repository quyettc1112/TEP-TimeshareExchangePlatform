package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WalletAPIService {

    @GET("wallet/wallet-transaction/{uuid}")
    suspend fun getWalletTransactionDetailByUUID(
        @Header("Authorization") token: String,
        @Path("uuid") uuid: String
    ): Response<WalletDetailRespone>

    // Get List Transaction
    @GET("wallet/customer")
    suspend fun getWalletTransaction(
        @Header("Authorization") token: String
    ): Response<WalletListResponse>

}