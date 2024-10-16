package com.example.tep_timeshareexchangeplatform.API.Service

import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RoomAPIService {

    @GET("customer/room/resort/{resortId}")
    suspend fun getRoomListByResortId(
        @Header ("Authorization") token: String,
        @Path("resortId") resortId: Int
    ): Response<List<RoomModel>>
}