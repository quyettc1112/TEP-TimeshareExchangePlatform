package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "ratingPoint": 0,
  "comment": "string",
  "bookingId": 0
}
*/
data class FeedbackDTO(
    @SerializedName("ratingPoint") val ratingPoint: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("bookingId") val bookingId: Int
)