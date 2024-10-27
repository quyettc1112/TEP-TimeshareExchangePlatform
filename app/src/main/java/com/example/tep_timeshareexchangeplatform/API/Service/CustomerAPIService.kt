package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingsResponse
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


    // Get Is Customer Exist
    @GET("customer/user/{userId}")
    suspend fun getIsCustomerExist(
        @Header ("Authorization") token: String,
        @Path ("userId") userId: Int
    ) : Response<CustomerResponse>




    // Get My Posting List
    @GET("customer/rental/posting")
    suspend fun getMyPostingList(
        @Header ("Authorization") token: String
    ) : Response<List<PostingsResponse.Content>>

    // Get My Posting Detail
    @GET("customer/rental/posting/{postingId}")
    suspend fun getMyPostingDetail(
        @Header ("Authorization") token: String,
        @Path ("postingId") postingId: Int
    ) : Response<PostingDetailResponse>
}