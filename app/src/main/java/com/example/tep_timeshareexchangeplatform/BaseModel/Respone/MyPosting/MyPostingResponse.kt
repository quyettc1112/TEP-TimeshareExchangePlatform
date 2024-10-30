package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting


import com.google.gson.annotations.SerializedName

class MyPostingResponse : ArrayList<MyPostingResponse.MyPostingResponseItem>(){
    data class MyPostingResponseItem(
        @SerializedName("rentalPostingId") val rentalPostingId: Int,
        @SerializedName("expiredDate") val expiredDate: String?,
        @SerializedName("ownerId") val ownerId: Int,
        @SerializedName("ownerName") val ownerName: String,
        @SerializedName("timeShareId") val timeShareId: Int,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomName") val roomName: String,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortName") val resortName: String?,
        @SerializedName("address") val address: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("nights") val nights: Int,
        @SerializedName("pricePerNights") val pricePerNights: Int,
        @SerializedName("totalPrice") val totalPrice: Int,
        @SerializedName("rentalPackageId") val rentalPackageId: Int,
        @SerializedName("rentalPackageName") val rentalPackageName: String,
        @SerializedName("checkinDate") val checkinDate: String,
        @SerializedName("checkoutDate") val checkoutDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("unitTypeDTO") val unitTypeDTO: UnitTypeDTO,
        @SerializedName("active") val active: Boolean
    ) {
        data class UnitTypeDTO(
            @SerializedName("id") val id: Int,
            @SerializedName("title") val title: String,
            @SerializedName("area") val area: String,
            @SerializedName("bathrooms") val bathrooms: Int,
            @SerializedName("bedrooms") val bedrooms: Int,
            @SerializedName("bedsFull") val bedsFull: Int,
            @SerializedName("bedsKing") val bedsKing: Int,
            @SerializedName("bedsSofa") val bedsSofa: Int,
            @SerializedName("bedsMurphy") val bedsMurphy: Int,
            @SerializedName("bedsQueen") val bedsQueen: Int,
            @SerializedName("bedsTwin") val bedsTwin: Int,
            @SerializedName("buildingsOption") val buildingsOption: String,
            @SerializedName("description") val description: String,
            @SerializedName("kitchen") val kitchen: String,
            @SerializedName("photos") val photos: String,
            @SerializedName("sleeps") val sleeps: Int,
            @SerializedName("view") val view: String
        )
    }
}