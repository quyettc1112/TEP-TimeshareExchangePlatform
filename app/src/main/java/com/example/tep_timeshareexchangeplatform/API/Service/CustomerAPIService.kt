package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange.ExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback.FeedbackResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.PricingSupportResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.ExchangeRequestOnPostResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.ExchangeRequestPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare.PostingTimeshareResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
    suspend fun getIsCustomerInit(
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
    ) : Response<MyBookingDetailResponse>


    // Create Booking Request
    @POST("customer/rental/booking/{postingId}")
    suspend fun createBookingRequest(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Body guestDTO: GuestDTO
    ) : Response<MyBookingDetailResponse>


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
    suspend fun postFeedbackForCustomerRental(
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
    @POST("customer/exchange/booking/{postingId}")
    suspend fun sendExchangeRequest(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Body exchangeRequestDTO: ExchangeRequestDTO
    ) : Response<ExchangeRequestResponse>

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

    @GET("customer/exchange/request/posting/{postingId}")
    suspend fun getCustomerExchangeRequestOnPost(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int,
        @Query ("pageNo") pageNo: Int,
        @Query ("pageSize") pageSize: Int,
    ) : Response<ExchangeRequestOnPostResponse>

}