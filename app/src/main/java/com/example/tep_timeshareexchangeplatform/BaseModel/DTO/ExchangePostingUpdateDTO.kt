package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "description": "string",
  "imageUrls": [
    "string"
  ]
}
*/
data class ExchangePostingUpdateDTO(
    @SerializedName("description") val description: String,
    @SerializedName("imageUrls") val imageUrls: List<String>
)