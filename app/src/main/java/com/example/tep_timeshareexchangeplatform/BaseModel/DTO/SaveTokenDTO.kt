package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "userId": 32,
  "fcmToken": "dDZmQFGrQuO1_DBQmh-w9C"
}
*/
data class SaveTokenDTO(
    @SerializedName("userId") val userId: Int,
    @SerializedName("fcmToken") val fcmToken: String
)