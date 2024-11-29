package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

data class SentRequestDTO(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("note") val note: String,
    @SerializedName("phone") val phone: String
)