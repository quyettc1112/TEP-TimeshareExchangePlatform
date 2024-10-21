package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
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

    // Extend Membership of Customer
    @POST("customer/membership")
    suspend fun extendMembership(
        @Header ("Authorization") token: String,
        @Query ("uuid") uuid: String,
        @Query ("membership_id") membershipId: Int
    ) : Response<MemberShipResponse>
}