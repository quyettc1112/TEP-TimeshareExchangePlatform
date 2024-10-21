package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "fullName": "Trần Cương Quyết",
  "dob": "2024-01-11",
  "address": "string",
  "gender": "string",
  "phone": "012414412"
}
*/
data class CustomerDTO(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String
)