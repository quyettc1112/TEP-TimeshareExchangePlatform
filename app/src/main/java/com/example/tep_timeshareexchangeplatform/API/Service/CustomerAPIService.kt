package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomAmenitiesDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.SentRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.CancelBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DashboardDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ApproveExchangeResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangeRequestResponse
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomerAPIService {

    @POST("customer")
    suspend fun createCustomer(
        @Header ("Authorization") token: String,
        @Body customerDTO: CustomerDTO
    ) : Response<CustomerResponse>


    // Get check Customer Exist
    @GET("customer/initialize")
    suspend fun getInitCustomer(
        @Header ("Authorization") token: String,
    ) : Response<CustomerInfoResponse>


    // Check Valid Year Timeshare of Customer
    @GET("customer/timeshare/valid-year/{timeshareId}")
    suspend fun getValidYearTimeshare(
        @Header ("Authorization") token: String,
        @Path ("timeshareId") timeshareId: Int
    ) : Response<ValidYearResponse>


    // Get My Posting List
    @GET("customer/rental/posting")
    suspend fun getMyPostingList(
        @Header ("Authorization") token: String,
        @Query ("page") page: Int,
        @Query ("size") size: Int
    ) : Response<MyRentalPostingsResponse>

    // Get My Posting Detail
    @GET("customer/rental/posting/{postingId}")
    suspend fun getMyPostingDetail(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int
    ) : Response<MyRentalPostingDetailResponse>


    // Create Posting
    @POST("customer/rental/posting")
    suspend fun createPosting(
        @Header ("Authorization") token: String,
        @Body postingDTO: PostingTimeshareDTO
    ) : Response<PostingTimeshareResponse>


    // Accept Price Support
    @POST("customer/rental/posting/confirmation/{postingId}")
    suspend fun acceptPriceSupport(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Query ("newPrice") newPrice: Float,
        @Query ("isAccepted") isAccepted: Boolean?
    ) : Response<PricingSupportResponse>

    // Get Customer  Booking
    @GET("customer/booking")
    suspend fun getCustomerBooking(
        @Header ("Authorization") token: String,
        @Query ("page") page: Int,
        @Query ("size") size: Int
    ) : Response<MyBookingResponse>


    // Get My Booking Detail Of Customer
    @GET("customer/rental/booking/{bookingId}")
    suspend fun getMyBookingDetail(
        @Header ("Authorization") token: String,
        @Path ("bookingId") bookingId: Int
    ) : Response<MyBookingRentalDetailResponse>


    // Create Booking Request
    @POST("customer/rental/booking/{postingId}")
    suspend fun createBookingRequest(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Body guestDTO: GuestDTO
    ) : Response<MyBookingRentalDetailResponse>


    // Get Customer Exchange Posting
    @GET("customer/exchange/posting")
    suspend fun getCustomerExchangePosting(
        @Header ("Authorization") token: String,
        @Query ("page") page: Int,
        @Query ("size") size: Int
    ) : Response<MyExchangePostingsResponse>


    // Get Customer Exchange Posting Detail
    @GET("customer/exchange/posting/{postingId}")
    suspend fun getCustomerExchangePostingDetail(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int
    ) : Response<MyExchangePostingDetailResponse>

    // Posting Exchange Posting
    @POST("/api/customer/exchange/posting")
    suspend fun createExchangePosting(
        @Header ("Authorization") token: String,
        @Body postingDTO: ExchangeTimeshareDTO
    ) : Response<PostingTimeshareResponse>



    // Post Feedback For Customer, Rental
    @POST("customer/feedback/rental")
    suspend fun postFeedbackBookingRental(
        @Header ("Authorization") token: String,
        @Body feedbackDTO: FeedbackDTO
    ) : Response<FeedbackResponse>

    @POST("customer/feedback/exchange")
    suspend fun postFeedbackBookingExchange(
        @Header ("Authorization") token: String,
        @Body feedbackDTO: FeedbackDTO
    ) : Response<FeedbackResponse>


    // Get My Timeshare Detail
    @GET("customer/timeshare/{timeShareID}")
    suspend fun getMyTimeshareDetail(
        @Header ("Authorization") token: String,
        @Path("timeShareID") timeshareId: Int
    ): Response<MyTimeshareDetailResponse>
    //

    // Send Exchange Request
    @POST("customer/exchange/request/{postingId}")
    suspend fun sendExchangeRequest(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Body exchangeRequestDTO: ExchangeRequestDTO
    ) : Response<ExchangeRequestResponse>

    // Call Get User Profile
    @GET("customer/profile")
    suspend fun getCustomerProfile(
        @Header ("Authorization") token: String
    ) : Response<CustomerProfileResponse>

    @PUT("customer/profile")
    suspend fun updateCustomerProfile(
        @Header ("Authorization") token: String,
        @Body profileDTO: ProfileDTO
    ) : Response<CustomerProfileResponse>


    // Get Room Detail Info
    @GET("customer/room/{roomId}")
    suspend fun getRoomDetailById(
        @Header ("Authorization") token: String,
        @Path ("roomId") roomId: Int
    ): Response<RoomDetailResponse>


    // Update Room Amenities By TimeShareId
    @PUT("customer/room/room-amenity/{roomId}")
    suspend fun updateRoomAmenitiesByRoomId(
        @Header ("Authorization") token: String,
        @Path ("roomId") roomId: Int,
        @Body roomInfoAmenities: RoomAmenitiesDTO
    ): Response<RoomDetailResponse>

    // Deactivate Rental Posting
    @PUT("customer/deactivate/{postingId}")
    suspend fun deactivateRentalPosting(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int
    ): Response<MyRentalPostingDetailResponse>

    // Deactivate Exchange Posting
    @PUT("customer/deactivate/exchange/{postingId}")
    suspend fun deactivateExchangePosting(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int
    ): Response<MyExchangePostingsResponse>


    // Get Customer Exchange Request
    @GET("customer/exchange/request")
    suspend fun getCustomerExchangeRequest(
        @Header ("Authorization") token: String,
        @Query ("page") page: Int,
        @Query ("size") size: Int
    ) : Response<MyExchangeRequestResponse>


    // Get Customer Exchange Request Detail
    @GET("customer/exchange/request/{requestId}")
    suspend fun getCustomerExchangeRequestDetail(
        @Header ("Authorization") token: String,
        @Path ("requestId") requestId: Int
    ) : Response<MyExchangeRequestDetailResponse>

    // Get Customer Exchange Request On Post
    @GET("customer/exchange/request/posting/{postingId}")
    suspend fun getCustomerExchangeRequestOnPost(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Query ("pageNo") pageNo: Int,
        @Query ("pageSize") pageSize: Int,
    ) : Response<ExchangeRequestOnPostResponse>

    @POST("customer/exchange/request/approval/{requestId}")
    suspend fun approveExchangeRequest(
        @Header ("Authorization") token: String,
        @Path ("requestId") requestId: Int
    ) : Response<ApproveExchangeResponse>

    @GET("customer/exchange/booking/{bookingId}")
    suspend fun getExchangeBookingDetail(
        @Header ("Authorization") token: String,
        @Path ("bookingId") bookingId: Int
    ) : Response<MyBookingExchangeDetailResponse>

    // Call cancel Booking
    @POST("customer/rental/booking/cancel/{bookingId}")
    suspend fun cancelRentalBooking(
        @Header ("Authorization") token: String,
        @Path ("bookingId") bookingId: Int
    ) : Response<CancelBookingResponse>

    // Send Contact Request to Owner
    @POST("customer/rental/booking/form/{postingId}")
    suspend fun sendContactRequest(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Body sentRequestDTO: SentRequestDTO
    ) : Response<Void>

    @GET("customer/dashboard")
    suspend fun getDashboardData(
        @Header ("Authorization") token: String,
    ) : Response<DashboardDataResponse>






}