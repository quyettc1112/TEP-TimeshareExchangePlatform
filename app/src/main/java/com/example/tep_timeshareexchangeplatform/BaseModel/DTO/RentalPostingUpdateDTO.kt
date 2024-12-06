package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "description": "string",
  "pricePerNights": 0,
  "cancellationTypeId": 0,
  "imageUrls": [
    "string"
  ]
}
*/
data class RentalPostingUpdateDTO(
    @SerializedName("description") val description: String,
    @SerializedName("pricePerNights") val pricePerNights: Long,
    @SerializedName("cancellationTypeId") val cancellationTypeId: Int,
    @SerializedName("imageUrls") val imageUrls: List<String>
)