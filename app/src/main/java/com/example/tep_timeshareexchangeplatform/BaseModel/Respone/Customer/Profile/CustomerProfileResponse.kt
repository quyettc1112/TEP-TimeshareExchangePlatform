package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "fullName": "string",
  "avatar": "string",
  "dob": "2024-11-18",
  "address": "string",
  "gender": "string",
  "phone": "string",
  "membershipId": 0,
  "membershipName": "string",
  "userId": 0,
  "userUserName": "string",
  "userEmail": "string",
  "isActive": true,
  "memberPurchaseDate": "2024-11-18",
  "memberExpiryDate": "2024-11-18",
  "isMember": true
}
*/
data class CustomerProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("membershipId") val membershipId: Int,
    @SerializedName("membershipName") val membershipName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userUserName") val userUserName: String,
    @SerializedName("userEmail") val userEmail: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("memberPurchaseDate") val memberPurchaseDate: String,
    @SerializedName("memberExpiryDate") val memberExpiryDate: String,
    @SerializedName("isMember") val isMember: Boolean
)