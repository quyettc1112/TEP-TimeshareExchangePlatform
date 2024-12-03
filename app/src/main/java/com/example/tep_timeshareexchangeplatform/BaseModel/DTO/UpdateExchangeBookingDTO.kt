package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "primaryGuestName": "string",
  "primaryGuestPhone": "string",
  "primaryGuestEmail": "string",
  "primaryGuestCountry": "string",
  "primaryGuestStreet": "string",
  "primaryGuestCity": "string",
  "primaryGuestState": "string",
  "primaryGuestPostalCode": "string"
}
*/
data class UpdateExchangeBookingDTO(
    @SerializedName("primaryGuestName") val primaryGuestName: String,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String,
    @SerializedName("primaryGuestCountry") val primaryGuestCountry: String,
    @SerializedName("primaryGuestStreet") val primaryGuestStreet: String,
    @SerializedName("primaryGuestCity") val primaryGuestCity: String,
    @SerializedName("primaryGuestState") val primaryGuestState: String,
    @SerializedName("primaryGuestPostalCode") val primaryGuestPostalCode: String
)