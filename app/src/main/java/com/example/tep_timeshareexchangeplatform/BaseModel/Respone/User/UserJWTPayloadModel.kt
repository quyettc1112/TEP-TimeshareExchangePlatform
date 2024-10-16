package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User

import com.google.gson.annotations.SerializedName

data class UserJWTPayloadModel (
    @SerializedName("sub") val sub: String,
    @SerializedName("email") val email: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("RoleName") val roleName: String,
    @SerializedName("iat") val issuedAt: Long,
    @SerializedName("exp") val expiration: Long
)