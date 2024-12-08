package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import com.google.gson.annotations.SerializedName

/**
{
  "totalRevenue": 0,
  "totalCosts": 0,
  "revenueCostByDateDtos": [
    {
      "date": "2024-12-07T14:19:40.424Z",
      "revenueByDate": 0,
      "revenueByCosts": 0
    }
  ]
}
*/
data class DailySummaryDataResponse(
    @SerializedName("revenueCostByDateDtos") val revenueCostByDateDtos: List<RevenueCostByDateDto>,
    @SerializedName("totalCosts") val totalCosts: Long,
    @SerializedName("totalRevenue") val totalRevenue: Long
) {
    data class RevenueCostByDateDto(
        @SerializedName("date") val date: String,
        @SerializedName("revenueByCosts") val revenueByCosts: Long,
        @SerializedName("revenueByDate") val revenueByDate: Long
    )
}