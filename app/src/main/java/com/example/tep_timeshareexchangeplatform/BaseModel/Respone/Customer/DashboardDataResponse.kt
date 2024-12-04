package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import com.google.gson.annotations.SerializedName

/**
{
  "totalPosting": 0,
  "totalRentalRenter": 0,
  "totalExchangerRenter": 0,
  "totalRequest": 0,
  "totalBooking": 0
}
*/
data class DashboardDataResponse(
    @SerializedName("totalBooking") val totalBooking: Int,
    @SerializedName("totalExchangerRenter") val totalExchangerRenter: Int,
    @SerializedName("totalPosting") val totalPosting: Int,
    @SerializedName("totalRentalRenter") val totalRentalRenter: Int,
    @SerializedName("totalRequest") val totalRequest: Int
)