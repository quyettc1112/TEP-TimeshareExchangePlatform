package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import com.google.gson.annotations.SerializedName

class MyPostingResponse : ArrayList<MyPostingResponse.MyPostingResponseItem>(){
    data class MyPostingResponseItem(
        @SerializedName("rentalPostingId") val rentalPostingId: Int,
        @SerializedName("timeShareId") val timeShareId: Int,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomName") val roomName: String,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("address") val address: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("nights") val nights: Int,
        @SerializedName("pricePerNights") val pricePerNights: Int,
        @SerializedName("totalPrice") val totalPrice: Int,
        @SerializedName("checkinDate") val checkinDate: String,
        @SerializedName("checkoutDate") val checkoutDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("active") val active: Boolean
    )
}