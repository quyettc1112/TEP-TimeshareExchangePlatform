package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PostingsResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicPostingAPIService {

    // Get All Posting
    @GET("public/rental/postings")
    suspend fun getPostings(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("resortName") resortName: String,
    ): Response<PublicPostingResponse>


    // Get Posting Detail By ID
    @GET("public/rental/posting/{postingId}")
    suspend fun getPostingDetail(
        @Path("postingId") postingId: Int
    ): Response<PublicPostingDetailResponse>


}