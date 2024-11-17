package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.VNPAYPurchaseResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletPurchaseResponse
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
    @GET("wallet/customer/wallet-transaction")
    suspend fun getWalletTransaction(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<WalletListResponse>

    /**
     * Extend Membership By VN Pay
     *
     */
    @POST("wallet/vnpay/membership")
    suspend fun extendMembershipVNPAY(
        @Header ("Authorization") token: String,
        @Query("uuid") uuid: String,
        @Query("membership_id") membershipId: Int
    ) : Response<MemberShipResponse>

    // Extend Membership By Wallet
    @POST("wallet/wallet/membership")
    suspend fun extendMembershipWallet(
        @Header ("Authorization") token: String,
        @Query("membership_id") membershipId: Int
    ) : Response<MemberShipResponse>


    /**
     * Deposit Money By VN Pay
     */
    // Deposit Money By VN Pay
    @POST("wallet/vnpay/deposit-wallet")
    suspend fun depositMoneyVNPAY(
        @Header ("Authorization") token: String,
        @Query("uuid") uuid: String,
    ) : Response<VNPAYPurchaseResponse>


    /**
     * Purchase Package Posting
     */
    // Purchase Package Posting By VN Pay
    @POST("wallet/vnpay/rental/posting")
    suspend fun createRentalPostingTransactionByVNPAY(
        @Header ("Authorization") token: String,
        @Query("uuid") uuid: String,
        @Query("rentalPackageId") rentalPackageId : Int
    ) : Response<VNPAYPurchaseResponse>

    // Create Exchange Posting Transcation
    @POST("wallet/vnpay/exchange/posting")
    suspend fun createExchangePostingTransactionByVNPAY(
        @Header("Authorization") token: String,
        @Query("uuid") uuid: String,
        @Query("postingId") postingId: Int
    ): Response<VNPAYPurchaseResponse>

    // Purchase Package Posting By Wallet
    @POST("wallet/wallet/rental/posting")
    suspend fun createRentalPostingTransactionByWallet(
        @Header ("Authorization") token: String,
        @Query("rentalPackageId") rentalPackageId : Int
    ) : Response<WalletPurchaseResponse>

    // Create Exchange Posting Transaction
    // Purchase Package Posting By Wallet
    @POST("wallet/wallet/exchange/posting")
    suspend fun createExchangePostingTransactionByWallet(
        @Header ("Authorization") token: String,
        @Query("postingId") postingId : Int
    ) : Response<WalletPurchaseResponse>




    /**
     *  Rental Boking
     */
    // Booking Rental By VN Pay
    @POST("wallet/vnpay/rental/booking")
    suspend fun bookingRentalVNPAY(
        @Header ("Authorization") token: String,
        @Query("uuid") uuid: String,
        @Query("postingId") postingId : Int
    ) : Response<VNPAYPurchaseResponse>


    // Booking Rental By Wallet
    @POST("wallet/wallet/rental/booking")
    suspend fun bookingRentalWallet(
        @Header ("Authorization") token: String,
        @Query("postingId") postingId : Int
    ) : Response<WalletPurchaseResponse>






}