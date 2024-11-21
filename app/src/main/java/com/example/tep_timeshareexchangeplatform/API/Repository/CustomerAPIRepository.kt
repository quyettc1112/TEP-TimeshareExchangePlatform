package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.CustomerAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomAmenitiesDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback.FeedbackResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.PricingSupportResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class CustomerAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {

    private val customerAPIService: CustomerAPIService by lazy {
        apiServiceFactory.createApiService(CustomerAPIService::class.java, BaseAPI.BASE_API)
    }

    // Create customer
    suspend fun createCustomer(
        token: String,
        customerDTO: CustomerDTO
    ): Resource<CustomerResponse> {
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
            val response = customerAPIService.getInitCustomer("Bearer $token")
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
    suspend fun getValidYearTimeshare(
        token: String,
        timeshareId: Int
    ): Resource<ValidYearResponse> {
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
    suspend fun getCustomerPostingList(
        token: String,
        page: Int,
        size: Int
    ): Resource<MyRentalPostingsResponse> {
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
    suspend fun getMyPostingDetail(
        token: String,
        postingId: Int
    ): Resource<MyRentalPostingDetailResponse> {
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
    suspend fun createPosting(
        token: String,
        postingDTO: PostingTimeshareDTO
    ): Resource<PostingTimeshareResponse> {
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
    suspend fun acceptPriceSupport(
        token: String,
        postingId: Int,
        newPrice: Float,
        isAccepted: Boolean?
    ): Resource<PricingSupportResponse> {
        return try {
            val response = customerAPIService.acceptPriceSupport(
                "Bearer $token",
                postingId,
                newPrice,
                isAccepted
            )
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
    suspend fun getCustomerBooking(
        token: String,
        page: Int,
        size: Int
    ): Resource<MyBookingResponse> {
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


    // Get my booking detail
    suspend fun getCustomerBookingDetail(
        token: String,
        bookingId: Int
    ): Resource<MyBookingDetailResponse> {
        return try {
            val response = customerAPIService.getMyBookingDetail("Bearer $token", bookingId)
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

    // Create Booking Request
    suspend fun createBookingRequest(
        token: String,
        postingId: Int,
        guestDTO: GuestDTO
    ): Resource<MyBookingDetailResponse> {
        return try {
            val response =
                customerAPIService.createBookingRequest("Bearer $token", postingId, guestDTO)
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

    // Get Customer Exchange Posting
    suspend fun getCustomerExchangePosting(
        token: String,
        page: Int,
        size: Int
    ): Resource<MyExchangePostingsResponse> {
        return try {
            val response =
                customerAPIService.getCustomerExchangePosting("Bearer $token", page, size)
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


    // Get My Exchange Posting Detail
    suspend fun getCustomerExchangePostingDetail(
        token: String,
        postingId: Int
    ): Resource<MyExchangePostingDetailResponse> {
        return try {
            val response =
                customerAPIService.getCustomerExchangePostingDetail("Bearer $token", postingId)
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

    // Post Feedback For Customer, Rental
    suspend fun postFeedbackForCustomerRental(
        token: String,
        feedbackDTO: FeedbackDTO
    ): Resource<FeedbackResponse> {
        return try {
            val response =
                customerAPIService.postFeedbackForCustomerRental("Bearer $token", feedbackDTO)
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

    // Create Exchange Posting
    suspend fun createExchangePosting(
        token: String,
        postingDTO: ExchangeTimeshareDTO
    ): Resource<PostingTimeshareResponse> {
        return try {
            val response =
                customerAPIService.createExchangePosting("Bearer $token", postingDTO)
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

    // Get Timeshare Detail of Customer
    suspend fun getTimeShareDetail(
        token: String,
        timeShareId: Int
    ): Resource<MyTimeshareDetailResponse> {
        return try {
            val response = customerAPIService.getMyTimeshareDetail("Bearer $token", timeShareId)
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

    // Send Exchange Request
    suspend fun sendExchangeRequest(
        token: String,
        postingId: Int,
        exchangeRequestDTO: ExchangeRequestDTO
    ): Resource<ExchangeRequestResponse> {
        return try {
            val response = customerAPIService.sendExchangeRequest(
                "Bearer $token",
                postingId,
                exchangeRequestDTO
            )
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

    // Get Customer Profile
    suspend fun getCustomerProfile(token: String): Resource<CustomerProfileResponse> {
        return try {
            val response = customerAPIService.getCustomerProfile("Bearer $token")
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

    // Update Customer Profile
    suspend fun updateCustomerProfile(
        token: String,
        profileDTO: ProfileDTO
    ): Resource<CustomerProfileResponse> {
        return try {
            val response = customerAPIService.updateCustomerProfile("Bearer $token", profileDTO)
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

    // Get Room Detail By ID
    suspend fun getRoomDetailById(token: String, roomId: Int): Resource<RoomDetailResponse> {
        return try {
            val response = customerAPIService.getRoomDetailById("Bearer $token", roomId)
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


    // Update Room Amenities By Room ID
    suspend fun updateRoomAmenitiesByRoomId(
        token: String,
        roomId: Int,
        amenities: RoomAmenitiesDTO
    ): Resource<RoomDetailResponse> {
        return try {
            val response =
                customerAPIService.updateRoomAmenitiesByRoomId("Bearer $token", roomId, amenities)
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

    // Deactivate Rental Posting
    suspend fun deactivateRentalPosting(
        token: String,
        postingId: Int
    ): Resource<MyRentalPostingsResponse> {
        return try {
            val response = customerAPIService.deactivateRentalPosting("Bearer $token", postingId)
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

    // Deactivate Exchange Posting
    suspend fun deactivateExchangePosting(
        token: String,
        postingId: Int
    ): Resource<MyExchangePostingsResponse> {
        return try {
            val response = customerAPIService.deactivateExchangePosting("Bearer $token", postingId)
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