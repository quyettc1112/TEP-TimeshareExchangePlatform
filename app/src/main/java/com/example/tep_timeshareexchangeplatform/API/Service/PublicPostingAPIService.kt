package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangesResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicPostingAPIService {

    // Get All Posting
    @GET("public/rental/postings")
    suspend fun getRentalPostings(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("resortName") resortName: String,
    ): Response<PublicPostingResponse>


    // Get Posting Detail By ID
    @GET("public/rental/posting/{postingId}")
    suspend fun getPostingDetail(
        @Path("postingId") postingId: Int
    ): Response<PublicPostingDetailResponse>

    // Get ALL Exchange Posting
    @GET("public/exchange/postings")
    suspend fun getExchangePostings(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("resortName") resortName: String,
    ): Response<ExchangesResponse>

    // Get Exchange Posting Detail By ID
    @GET("public/exchange/posting/{postingId}")
    suspend fun getExchangePostingDetail(
        @Path("postingId") postingId: Int
    ): Response<ExchangeDetailResponse>

    // Get List Posting of Resort By Id ()
    @GET("public/posting/{resortId}")
    suspend fun getRentalPostingOfResortByID(
        @Path("resortId") resortId: Int,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
    ) : Response<PublicPostingResponse>


}