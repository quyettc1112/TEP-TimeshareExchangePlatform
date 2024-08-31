package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.User_ApiService
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.User
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.google.api.core.ApiService
import javax.inject.Inject

class User_Repository@Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    // Khởi tạo User_ApiService bằng ApiServiceFactory
    private val userApiService: User_ApiService by lazy {
        apiServiceFactory.createApiService(User_ApiService::class.java, BaseAPI.BASE_API)
    }

    // Hàm gọi API để lấy thông tin người dùng
    suspend fun getUser(userId: Int): Resource<User> {
        return try {
            val response = userApiService.getUser(userId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, \n Error Body: ${response.errorBody()} \n Error Message: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }
}