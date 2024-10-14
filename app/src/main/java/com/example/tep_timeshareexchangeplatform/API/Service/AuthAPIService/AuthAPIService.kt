package com.example.tep_timeshareexchangeplatform.API.Service.AuthAPIService

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPIService {

    @POST("auth/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<LoginResponse>

}