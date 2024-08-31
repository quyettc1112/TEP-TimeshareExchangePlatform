package com.example.tep_timeshareexchangeplatform.API.Factory

import retrofit2.Retrofit
import javax.inject.Inject

class ApiServiceFactory @Inject constructor(
    private val retrofitBuilder: Retrofit.Builder
) {
    fun <T> createApiService(apiClass: Class<T>, baseUrl: String): T {
        return retrofitBuilder
            .baseUrl(baseUrl)
            .build()
            .create(apiClass)
    }
}