package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Sample.UserSampleModel
import retrofit2.Response
import retrofit2.http.GET

interface SampleAPIService {

    @GET("User")
    suspend fun getUserList(): Response<UserSampleModel>
}