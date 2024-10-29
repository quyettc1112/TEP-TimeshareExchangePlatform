package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare


import com.google.gson.annotations.SerializedName

/**
{
  "timeShareId": 20,
  "status": "string",
  "startYear": 2024,
  "endYear": 2025,
  "startDate": "10-05-2024",
  "endDate": "16-05-2024",
  "owner": "quy",
  "createdAt": "16-10-2024 14:50:25",
  "isActive": true,
  "roomInfo": {
    "id": 2,
    "roomInfoCode": "123",
    "roomInfoName": "cc",
    "createdAt": "14-10-2024 19:53:57",
    "updatedAt": "15-10-2024 09:56:41",
    "isActive": true,
    "resortId": 1,
    "status": "booking",
    "unitTypeId": 1
  }
}
*/
data class MyPostingTimeshareResponse(
    @SerializedName("timeShareId") val timeShareId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("startYear") val startYear: Int,
    @SerializedName("endYear") val endYear: Int,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("roomInfo") val roomInfo: RoomInfo
) {
    data class RoomInfo(
        @SerializedName("id") val id: Int,
        @SerializedName("roomInfoCode") val roomInfoCode: String,
        @SerializedName("roomInfoName") val roomInfoName: String,
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("updatedAt") val updatedAt: String,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("status") val status: String,
        @SerializedName("unitTypeId") val unitTypeId: Int
    )
}