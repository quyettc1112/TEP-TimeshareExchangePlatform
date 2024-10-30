package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.WalletAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.VNPAYPurchaseResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletPurchaseResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
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

    // Get Wallet Transaction
    suspend fun getWalletTransaction(
        auth: String
    ): Resource<WalletListResponse> {
        return try {
            val response = walletAPIService.getWalletTransaction("Bearer $auth")
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // Extend Membership By VN Pay
    suspend fun extendMembershipVNPAY(
        token: String,
        uuid: String,
        membershipId: Int
    ): Resource<MemberShipResponse> {
        return try {
            val response =
                walletAPIService.extendMembershipVNPAY("Bearer $token", uuid, membershipId)
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

    // Extend Membership By Wallet
    suspend fun extendMembershipWallet(
        token: String,
        membershipId: Int
    ): Resource<MemberShipResponse> {
        return try {
            val response = walletAPIService.extendMembershipWallet("Bearer $token", membershipId)
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


    // Deposit Money By VN Pay
    suspend fun depositMoneyVNPAY(token: String, uuid: String): Resource<VNPAYPurchaseResponse> {
        return try {
            val response = walletAPIService.depositMoneyVNPAY("Bearer $token", uuid)
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

    // Purchase Package Posting By VN Pay
    suspend fun purchasePackagePostingVNPAY(
        token: String,
        uuid: String,
        rentalPackageId: Int
    ): Resource<VNPAYPurchaseResponse> {
        return try {
            val response =
                walletAPIService.purchasePackagePostingVNPAY("Bearer $token", uuid, rentalPackageId)
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

    // Purchase Package Posting By Wallet
    suspend fun purchasePackagePostingWallet(
        token: String,
        rentalPackageId: Int
    ): Resource<WalletPurchaseResponse> {
        return try {
            val response =
                walletAPIService.purchasePackagePostingWallet("Bearer $token", rentalPackageId)
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