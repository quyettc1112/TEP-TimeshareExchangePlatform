package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerInfoResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingResponse
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
    ) : Response<MyPostingResponse>

    // Get My Posting Detail
    @GET("customer/rental/posting/{postingId}")
    suspend fun getMyPostingDetail(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int
    ) : Response<MyPostingDetailResponse>


    // Create Posting
    @POST("customer/rental/posting")
    suspend fun createPosting(
        @Header ("Authorization") token: String,
        @Body postingDTO: PostingTimeshareDTO
    ) : Response<PostingTimeshareResponse>
}