package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "fullName": "string",
  "avatar": "string",
  "dob": "2024-11-19",
  "address": "string",
  "gender": "string",
  "phone": "string"
}
*/
data class ProfileDTO(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("avatar") var avatar: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String
)