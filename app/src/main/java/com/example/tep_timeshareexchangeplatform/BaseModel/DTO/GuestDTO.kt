package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "primaryGuestName": "string",
  "primaryGuestPhone": "string",
  "primaryGuestEmail": "string"
}
*/
data class GuestDTO(
    @SerializedName("primaryGuestName") val primaryGuestName: String,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String
)