package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import com.google.gson.annotations.SerializedName

/**
{
  "id": 31,
  "fullName": "string",
  "memberExpiryDate": "22-10-2032",
  "membershipName": "membership_package_2",
  "userId": 42,
  "userUserName": null,
  "userRoleRoleName": "CUSTOMER",
  "isActive": true,
  "walletId": 7,
  "walletAvailableMoney": 30764000,
  "isMember": true
}
*/
data class CustomerInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("memberExpiryDate") val memberExpiryDate: String,
    @SerializedName("membershipName") val membershipName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userUserName") val userUserName: Any?,
    @SerializedName("userRoleRoleName") val userRoleRoleName: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("walletId") val walletId: Int,
    @SerializedName("walletAvailableMoney") val walletAvailableMoney: Int,
    @SerializedName("isMember") val isMember: Boolean
)