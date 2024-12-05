package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "startDate": "2024-12-05",
  "endDate": "2024-12-05",
  "status": "string",
  "exchangePosting": {
    "id": 0,
    "description": "string",
    "nights": 0,
    "roomInfoResortId": 0,
    "roomInfoResortResortName": "string",
    "roomInfoResortLogo": "string",
    "roomInfoUnitTypeId": 0,
    "roomInfoUnitTypeTitle": "string",
    "roomInfoUnitTypePhotos": "string"
  },
  "note": "string",
  "createdDate": "2024-12-05T08:21:41.234Z",
  "updatedDate": "2024-12-05T08:21:41.234Z",
  "priceValuation": 0,
  "isActive": true
}
*/
data class RejectRequestRespone(
    @SerializedName("id") val id: Int,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("exchangePosting") val exchangePosting: ExchangePosting,
    @SerializedName("note") val note: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("priceValuation") val priceValuation: Int,
    @SerializedName("isActive") val isActive: Boolean
) {
    data class ExchangePosting(
        @SerializedName("id") val id: Int,
        @SerializedName("description") val description: String,
        @SerializedName("nights") val nights: Int,
        @SerializedName("roomInfoResortId") val roomInfoResortId: Int,
        @SerializedName("roomInfoResortResortName") val roomInfoResortResortName: String,
        @SerializedName("roomInfoResortLogo") val roomInfoResortLogo: String,
        @SerializedName("roomInfoUnitTypeId") val roomInfoUnitTypeId: Int,
        @SerializedName("roomInfoUnitTypeTitle") val roomInfoUnitTypeTitle: String,
        @SerializedName("roomInfoUnitTypePhotos") val roomInfoUnitTypePhotos: String
    )
}