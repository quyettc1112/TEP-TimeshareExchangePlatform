package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User


import com.google.gson.annotations.SerializedName

/**
{
  "email": "stringasdasda",
  "username": "stringasd as"
}
*/
data class RegisterResponse(
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String
)