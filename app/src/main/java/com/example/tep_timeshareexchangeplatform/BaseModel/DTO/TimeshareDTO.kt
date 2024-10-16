package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "status": "string",
  "startYear": 2024,
  "endYear": 2025,
  "startDate": "2024-05-10",
  "endDate": "2024-05-16",
  "roomInfoId": 1
}
*/
data class TimeshareDTO(
    @SerializedName("status") val status: String,
    @SerializedName("startYear") val startYear: Int,
    @SerializedName("endYear") val endYear: Int,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("roomInfoId") val roomInfoId: Int
)