package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.AuthAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RegisterDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.LoginResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.RegisterResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class AuthAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    // init API Service with ApiServiceFactory
    private val authAPIService: AuthAPIService by lazy {
        apiServiceFactory.createApiService(AuthAPIService::class.java, BaseAPI.BASE_API)
    }

    // function to call API to login
    suspend fun login(loginDTO: LoginDTO): Resource<LoginResponse> {
        return try {
            val response = authAPIService.login(loginDTO)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                // Use ErrorHandler to parse the error message
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // function to call API to register
    suspend fun register(registerDTO: RegisterDTO): Resource<RegisterResponse> {
        return try {
            val response = authAPIService.register(registerDTO)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                // Use ErrorHandler to parse the error message
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // function to call API to send email forgot password
    suspend fun forgotPassword(email: String): Resource<Void> {
        return try {
            val response = authAPIService.forgotPassword(email)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                // Use ErrorHandler to parse the error message
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

    // function to call API to reset password
    suspend fun resetPassword(email: String, token: String, newPassword: String): Resource<Void> {
        return try {
            val response = authAPIService.resetPassword(email, token, newPassword)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                // Use ErrorHandler to parse the error message
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: $errorMessage", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }

}