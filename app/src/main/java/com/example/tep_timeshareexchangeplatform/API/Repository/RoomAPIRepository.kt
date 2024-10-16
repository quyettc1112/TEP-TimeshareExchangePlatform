package com.example.tep_timeshareexchangeplatform.API.Repository

import com.example.tep_timeshareexchangeplatform.API.BaseAPI.BaseAPI
import com.example.tep_timeshareexchangeplatform.API.Factory.ApiServiceFactory
import com.example.tep_timeshareexchangeplatform.API.Service.RoomAPIService
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.PostRoomRespone
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import javax.inject.Inject

class RoomAPIRepository @Inject constructor(
    private val apiServiceFactory: ApiServiceFactory
) {
    private val roomAPIService: RoomAPIService by lazy {
        apiServiceFactory.createApiService(RoomAPIService::class.java, BaseAPI.BASE_API)
    }

    // Get room list by resort id
    suspend fun getRoomListByResortId(auth: String, resortId: Int) : Resource<List<RoomModel>> {
        return try {
            val response = roomAPIService.getRoomListByResortId("Bearer $auth", resortId)
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

    // Post room
    suspend fun postRoom(auth: String, roomDTO: RoomDTO) : Resource<PostRoomRespone> {
        return try {
            val response = roomAPIService.postRoom("Bearer $auth", roomDTO)
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