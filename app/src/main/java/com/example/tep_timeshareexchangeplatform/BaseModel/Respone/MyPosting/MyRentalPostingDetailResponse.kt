package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting


import com.google.gson.annotations.SerializedName

data class MyRentalPostingDetailResponse(
    @SerializedName("rentalPostingId") val rentalPostingId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("expiredDate") val expiredDate: String,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("timeShareId") val timeShareId: Int,
    @SerializedName("roomInfoId") val roomInfoId: Int,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("resortName") val resortName: String,
    @SerializedName("roomCode") val roomCode: String,
    @SerializedName("location") val location: Location?,
    @SerializedName("isVerify") val isVerify: Boolean,
    @SerializedName("nights") val nights: Long?,
    @SerializedName("pricePerNights") val pricePerNights: Long?,
    @SerializedName("totalPrice") val totalPrice: Long?,
    @SerializedName("cancelTypeId") val cancelTypeId: Int?,
    @SerializedName("cancelType") val cancelType: String,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("rentalPackageName") val rentalPackageName: String,
    @SerializedName("rentalPackageDuration") val rentalPackageDuration: String,
    @SerializedName("rentalPackageDescription") val rentalPackageDescription: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("unitType") val unitType: UnitType,
    @SerializedName("resortAmenities") val resortAmenities: List<ResortAmenity>,
    @SerializedName("roomAmenities") val roomAmenities: List<RoomAmenity>,
    @SerializedName("unitTypeAmenities") val unitTypeAmenities: List<UnitTypeAmenity>,
    @SerializedName("imageUrls") val imageUrls: List<String>,
    @SerializedName("active") val active: Boolean
) {
    data class Location(
        @SerializedName("name") val name: String,
        @SerializedName("displayName") val displayName: String?,
        @SerializedName("latitude") val latitude: String?,
        @SerializedName("longitude") val longitude: String?,
        @SerializedName("country") val country: String,
        @SerializedName("placeId") val placeId: Int?
    )

    data class UnitType(
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

    data class ResortAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )

    data class RoomAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )

    data class UnitTypeAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String?
    )
}