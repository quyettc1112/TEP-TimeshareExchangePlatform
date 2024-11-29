package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogResponse
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
        @Query("resortId") resortId: Int? = null,
    ): Response<ExchangesResponse>

    // Get Exchange Posting Detail By ID
    @GET("public/exchange/posting/{postingId}")
    suspend fun getExchangePostingDetail(
        @Path("postingId") postingId: Int
    ): Response<ExchangeDetailResponse>

    @GET("public/posting/{resortId}")
    suspend fun getRentalPostingOfResortByID(
        @Path("resortId") resortId: Int,
        @Query("page") pageNo: Int,
        @Query("size") pageSize: Int,
    ) : Response<PublicPostingResponse>

    @GET("public/blog/postings")
    suspend fun getBlog(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("title") title: String,
    ): Response<BlogResponse>

    @GET("public/blog/{postingId}")
    suspend fun getBlogDetail(
        @Path("postingId") postingId: Int
    ): Response<BlogDetailResponse>


    @GET("public/exchange/postings")
    suspend fun getExchangePostingsByResortId(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("resortName") resortName: String,
    ): Response<ExchangesResponse>


}