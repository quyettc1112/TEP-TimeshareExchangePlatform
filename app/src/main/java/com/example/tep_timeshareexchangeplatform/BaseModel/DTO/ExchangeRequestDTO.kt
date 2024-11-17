package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "timeshareId": 0,
  "startDate": "2024-11-17",
  "endDate": "2024-11-17",
  "exchangePostingId": 0
}
*/
data class ExchangeRequestDTO(
    @SerializedName("timeshareId") val timeshareId: Int,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("exchangePostingId") val exchangePostingId: Int
)