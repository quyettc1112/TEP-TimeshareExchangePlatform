package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Room


import com.google.gson.annotations.SerializedName

/**
{
    "id": 4,
    "roomInfoCode": "123",
    "roomInfoName": "string",
    "createdAt": null,
    "updatedAt": null,
    "isActive": true,
    "resortId": 1,
    "status": "string",
    "unitTypeId": 1
  }
*/
data class RoomModel(
    @SerializedName("id") val id: Int,
    @SerializedName("roomInfoCode") val roomInfoCode: String,
    @SerializedName("roomInfoName") val roomInfoName: String,
    @SerializedName("createdAt") val createdAt: Any?,
    @SerializedName("updatedAt") val updatedAt: Any?,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("unitTypeId") val unitTypeId: Int
)