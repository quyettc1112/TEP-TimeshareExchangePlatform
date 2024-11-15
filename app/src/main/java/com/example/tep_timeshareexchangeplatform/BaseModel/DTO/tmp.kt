package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "description": "string",
  "nights": 0,
  "pricePerNights": 0,
  "timeshareId": 0,
  "cancellationTypeId": 0,
  "checkinDate": "2024-11-15",
  "checkoutDate": "2024-11-15",
  "rentalPackageId": 0,
  "imageUrls": [
    "string"
  ]
}
*/
data class tmp(
    @SerializedName("description") val description: String,
    @SerializedName("nights") val nights: Int,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("timeshareId") val timeshareId: Int,
    @SerializedName("cancellationTypeId") val cancellationTypeId: Int,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("imageUrls") val imageUrls: List<String>
)