package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.NotificationAPiService
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotificationResponse
import com.example.tep_timeshareexchangeplatform.Until.ErrorHandler
import com.example.tep_timeshareexchangeplatform.Until.Resource
import retrofit2.Response
import javax.inject.Inject

class NotificationAPIRepository  @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
){
    private val notificationAPIService: NotificationAPiService by lazy {
        apiServiceFactory.createApiService(NotificationAPiService::class.java, BaseAPI.BASE_API)
    }

    suspend fun getCustomerNotification(token: String, pageNo: Int, pageSize: Int): Resource<NotificationResponse> {
        return try {
            val response = notificationAPIService.getCustomerNotification("Bearer $token", pageNo, pageSize)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errorMessage = ErrorHandler.parseError(response.errorBody())
                Resource.error("Error: ${response.code()}, Message: ${errorMessage}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }
}