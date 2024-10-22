package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import com.google.gson.annotations.SerializedName

/**
{
  "email": "string",
  "username": "string",
  "password": "string",
  "roleId": 0
}
*/
data class RegisterDTO(
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("roleId") val roleId: Int
)