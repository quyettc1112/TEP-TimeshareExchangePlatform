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
        auth: String,
        page: Int,
        size: Int
    ): Resource<WalletListResponse> {
        return try {
            val response = walletAPIService.getWalletTransaction("Bearer $auth", page, size)
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
                walletAPIService.createRentalPostingTransactionByVNPAY("Bearer $token", uuid, rentalPackageId)
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
                walletAPIService.createRentalPostingTransactionByWallet("Bearer $token", rentalPackageId)
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

    // Booking Rental By VN Pay
    suspend fun bookingRentalVNPAY(
        token: String,
        uuid: String,
        postingId: Int
    ): Resource<VNPAYPurchaseResponse> {
        return try {
            val response = walletAPIService.bookingRentalVNPAY("Bearer $token", uuid, postingId)
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

    // Booking Rental By Wallet
    suspend fun bookingRentalWallet(
        token: String,
        postingId: Int
    ): Resource<WalletPurchaseResponse> {
        return try {
            val response = walletAPIService.bookingRentalWallet("Bearer $token", postingId)
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

    // Create Exchange Posting Transcation
    suspend fun createExchangePostingTransaction(
        token: String,
        uuid: String,
        exchangePackageId: Int
    ): Resource<VNPAYPurchaseResponse> {
        return try {
            val response = walletAPIService.createExchangePostingTransactionByVNPAY("Bearer $token", uuid, exchangePackageId)
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
    // Create Exchange Posting Transaction By Wallet
    suspend fun createExchangePostingTransactionByWallet(
        token: String,
        postingId: Int
    ): Resource<WalletPurchaseResponse> {
        return try {
            val response = walletAPIService.createExchangePostingTransactionByWallet("Bearer $token", postingId)
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


    // Get Spent Transaction
    suspend fun getSpentTransaction(
        token: String,
        page: Int,
        size: Int
    ): Resource<WalletListResponse> {
        return try {
            val response = walletAPIService.getSpentTransaction("Bearer $token", page, size)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // Get Received Transaction
    suspend fun getReceivedTransaction(
        token: String,
        page: Int,
        size: Int
    ): Resource<WalletListResponse> {
        return try {
            val response = walletAPIService.getReceivedTransaction("Bearer $token", page, size)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // Payment Exchange Request Wallet
    suspend fun paymentExchangeRequestWallet(
        token: String,
        requestId: Int
    ): Resource<WalletPurchaseResponse> {
        return try {
            val response = walletAPIService.paymentExchangeRequestWallet("Bearer $token", requestId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // Create Exchange Posting Transaction By VNPay
    suspend fun createExchangeRequestTransactionByVNPAY(
        token: String,
        uuid: String,
        requestId: Int
    ): Resource<VNPAYPurchaseResponse> {
        return try {
            val response = walletAPIService.createExchangeRequestTransactionByVNPAY("Bearer $token", uuid, requestId)
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