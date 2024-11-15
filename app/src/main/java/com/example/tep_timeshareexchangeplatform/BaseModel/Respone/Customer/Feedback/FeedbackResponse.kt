package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Feedback


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "ratingPoint": 0,
  "comment": "string",
  "resort": {
    "id": 0,
    "resortName": "string"
  },
  "customer": {
    "id": 0,
    "fullName": "string",
    "avatar": "string"
  },
  "createdDate": "2024-11-15T07:42:15.689Z",
  "isActive": true
}
*/
data class FeedbackResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("ratingPoint") val ratingPoint: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("resort") val resort: Resort,
    @SerializedName("customer") val customer: Customer,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("isActive") val isActive: Boolean
) {
    data class Resort(
        @SerializedName("id") val id: Int,
        @SerializedName("resortName") val resortName: String
    )

    data class Customer(
        @SerializedName("id") val id: Int,
        @SerializedName("fullName") val fullName: String,
        @SerializedName("avatar") val avatar: String
    )
}