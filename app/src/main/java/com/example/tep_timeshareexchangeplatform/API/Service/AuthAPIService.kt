package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ChangePasswordDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RegisterDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.LoginResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPIService {

    @POST("auth/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body registerDTO: RegisterDTO): Response<RegisterResponse>

    // Send Email Forgot Password
    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Query("email") email: String
    ): Response<Void>

    // Call API Reset Password
    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Query("email") email: String,
        @Query("token") token: String,
        @Query("newPassword") newPassword: String
    ): Response<Void>

    // Call Change Password API
    @POST("customer/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordDTO: ChangePasswordDTO
    ): Response<Void>

}