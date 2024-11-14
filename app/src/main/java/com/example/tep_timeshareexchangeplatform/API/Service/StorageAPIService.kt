package com.example.tep_timeshareexchangeplatform.API.Service

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StorageAPIService {
    @Multipart
    @POST("/s3/file/upload")
    suspend fun uploadFiles(
        @Header ("Authorization") token: String,
        @Part files: List<MultipartBody.Part>
    ): Response<List<String>>
}