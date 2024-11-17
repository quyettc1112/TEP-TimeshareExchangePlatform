package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.StorageAPIService
import com.example.tep_timeshareexchangeplatform.Until.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

class StorageAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val storageAPIService: StorageAPIService by lazy {
        apiServiceFactory.createApiService(StorageAPIService::class.java, BaseAPI.BASE_API)
    }

    // Upload files
    suspend fun uploadFiles(
        token: String,
        files: List<MultipartBody.Part>
    ): Resource<List<String>> {
        return try {
            val response = storageAPIService.uploadFiles(token,files)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorHandler = response.errorBody()?.string()
                Resource.error("Error: ${response.code()}, Message: $errorHandler", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }

    }
}