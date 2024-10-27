package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WalletAPIService {

    // Get Wallet Transaction Detail By UUID
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

    // Extend Membership By VN Pay
    @POST("wallet/VNPAY/membership")
    suspend fun extendMembershipVNPAY(
        @Header ("Authorization") token: String,
        @Query("uuid") uuid: String,
        @Query("membership_id") membershipId: Int
    ) : Response<MemberShipResponse>

}