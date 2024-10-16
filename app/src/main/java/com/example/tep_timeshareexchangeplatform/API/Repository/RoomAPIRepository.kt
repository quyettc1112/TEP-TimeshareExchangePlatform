package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.RoomAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class RoomAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val roomAPIService: RoomAPIService by lazy {
        apiServiceFactory.createApiService(RoomAPIService::class.java, BaseAPI.BASE_API)
    }

    suspend fun getRoomListByResortId(auth: String, resortId: Int) : Resource<List<RoomModel>> {
        return try {
            val response = roomAPIService.getRoomListByResortId("Bearer $auth", resortId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Error: ${response.code()}, Message: ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("Network Error: ${e.message}", null)
        }
    }
}