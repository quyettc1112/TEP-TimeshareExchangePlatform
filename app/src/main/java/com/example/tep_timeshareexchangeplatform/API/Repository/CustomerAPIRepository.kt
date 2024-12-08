package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.CustomerAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RentalPostingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RentalPostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomAmenitiesDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.SentRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.UpdateExchangeBookingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.CancelBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DailySummaryDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DashboardDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ApproveExchangeResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangePriceValuationRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.RejectRequestRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback.FeedbackResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.PricingSupportResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.ExchangeRequestOnPostResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomDetailResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import java.util.Date
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
        postingDTO: RentalPostingDTO
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
    ): Resource<MyBookingRentalDetailResponse> {
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
    ): Resource<MyBookingRentalDetailResponse> {
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
                customerAPIService.postFeedbackBookingRental("Bearer $token", feedbackDTO)
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
        postingDTO: ExchangePostingDTO
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
    ): Resource<MyRentalPostingDetailResponse> {
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

    // Get Customer Exchange Request
    suspend fun getCustomerExchangeRequest(
        token: String,
        page: Int,
        size: Int
    ): Resource<MyExchangeRequestResponse> {
        return try {
            val response =
                customerAPIService.getCustomerExchangeRequest("Bearer $token", page, size)
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


    // Get My Exchange Request Detail
    suspend fun getCustomerExchangeRequestDetail(
        token: String,
        requestId: Int
    ): Resource<MyExchangeRequestDetailResponse> {
        return try {
            val response =
                customerAPIService.getCustomerExchangeRequestDetail("Bearer $token", requestId)
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

    suspend fun getCustomerExchangeRequestOnPost(
        token: String,
        postingId: Int,
        pageNo: Int,
        pageSize: Int,
    ): Resource<ExchangeRequestOnPostResponse> {
        return try {
            val response =
                customerAPIService.getCustomerExchangeRequestOnPost(
                    "Bearer $token",
                    postingId,
                    pageNo,
                    pageSize
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

    // Approve Exchange Request
    suspend fun approveExchangeRequest(
        token: String,
        requestId: Int
    ): Resource<ApproveExchangeResponse> {
        return try {
            val response =
                customerAPIService.approveExchangeRequest("Bearer $token", requestId)
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


    // Get My Booking Exchange
    suspend fun getMyBookingExchange(
        token: String,
        bookingId: Int
    ): Resource<MyBookingExchangeDetailResponse> {
        return try {
            val response = customerAPIService.getExchangeBookingDetail("Bearer $token", bookingId)
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

    // Post Feedback For Customer, Exchange
    suspend fun postFeedbackForCustomerExchange(
        token: String,
        feedbackDTO: FeedbackDTO
    ): Resource<FeedbackResponse> {
        return try {
            val response =
                customerAPIService.postFeedbackBookingExchange("Bearer $token", feedbackDTO)
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


    // Cancel Booking Request
    suspend fun cancelBookingRequest(
        token: String,
        bookingId: Int
    ): Resource<CancelBookingResponse> {
        return try {
            val response = customerAPIService.cancelRentalBooking("Bearer $token", bookingId)
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

    // Send Contact Request
    suspend fun sendContactRequest(
        token: String,
        postingId: Int,
        sentRequestDTO: SentRequestDTO
    ): Resource<Void> {
        return try {
            val response =
                customerAPIService.sendContactRequest("Bearer $token", postingId, sentRequestDTO)
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

    // Update Exchange Posting
    suspend fun updateExchangePosting(
        token: String,
        postingId: Int,
        exchangePostingUpdateDTO: ExchangePostingUpdateDTO
    ): Resource<Void> {
        return try {
            val response = customerAPIService.updateExchangePosting(
                "Bearer $token",
                postingId,
                exchangePostingUpdateDTO
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

    // Update Rental Posting
    suspend fun updateRentalPosting(
        token: String,
        postingId: Int,
        rentalPostingUpdateDTO: RentalPostingUpdateDTO
    ): Resource<Void> {
        return try {
            val response = customerAPIService.updateRentalPosting(
                "Bearer $token",
                postingId,
                rentalPostingUpdateDTO
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

    // Update Timeshare
    suspend fun updateTimeshare(
        token: String,
        timeShareId: Int,
        timeshareUpdateDTO: TimeshareUpdateDTO
    ): Resource<Void> {
        return try {
            val response = customerAPIService.updateTimeshare(
                "Bearer $token",
                timeShareId,
                timeshareUpdateDTO
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


    // Update Exchange Booking Customer Info
    suspend fun updateExchangeBookingCustomerInfo(
        token: String,
        bookingId: Int,
        updateExchangeBookingDTO: UpdateExchangeBookingDTO
    ): Resource<Void> {
        return try {
            val response = customerAPIService.updateExchangeBookingCustomerInfo(
                "Bearer $token",
                bookingId,
                updateExchangeBookingDTO
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

    // Get Dashboard Data
    suspend fun getDashboardData(
        token: String,
    ): Resource<DashboardDataResponse> {
        return try {
            val response = customerAPIService.getDashboardData("Bearer $token")
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


    // Reject Request Exchange
    suspend fun rejectExchangeRequest(
        token: String,
        requestId: Int
    ): Resource<RejectRequestRespone> {
        return try {
            val response = customerAPIService.rejectExchangeRequest("Bearer $token", requestId)
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

    // Exchange Price Valuation
    suspend fun exchangePriceValuation(
        token: String,
        requestId: Int,
        priceValuation: Long,
        note: String
    ): Resource<ExchangePriceValuationRespone> {
        return try {
            val response = customerAPIService.exchangePriceValuation(
                "Bearer $token",
                requestId,
                priceValuation,
                note
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

    //Daily Summary
    suspend fun getDailySummaryData(
        token: String,
        startDate: String,
        endDate: String
    ): Resource<DailySummaryDataResponse> {
        return try {
            val response =
                customerAPIService.getDailySummaryData("Bearer $token", startDate, endDate)
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