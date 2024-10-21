package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import com.google.gson.annotations.SerializedName

/**
{
  "id": 10,
  "fullName": "Trần Cương Quyết",
  "dob": "11-01-2024",
  "address": "string",
  "gender": "string",
  "phone": "012414412",
  "memberPurchaseDate": null,
  "memberExpiryDate": null,
  "membershipId": null,
  "country": null,
  "street": null,
  "city": null,
  "state": null,
  "postalCode": null,
  "note": null,
  "user": {
    "id": 33,
    "userName": null,
    "email": "quyet@gmail.com",
    "isActive": true
  },
  "isActive": true,
  "isMember": false
}
*/
data class CustomerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("memberPurchaseDate") val memberPurchaseDate: Any?,
    @SerializedName("memberExpiryDate") val memberExpiryDate: Any?,
    @SerializedName("membershipId") val membershipId: Any?,
    @SerializedName("country") val country: Any?,
    @SerializedName("street") val street: Any?,
    @SerializedName("city") val city: Any?,
    @SerializedName("state") val state: Any?,
    @SerializedName("postalCode") val postalCode: Any?,
    @SerializedName("note") val note: Any?,
    @SerializedName("user") val user: User,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isMember") val isMember: Boolean
) {
    data class User(
        @SerializedName("id") val id: Int,
        @SerializedName("userName") val userName: Any?,
        @SerializedName("email") val email: String,
        @SerializedName("isActive") val isActive: Boolean
    )
}