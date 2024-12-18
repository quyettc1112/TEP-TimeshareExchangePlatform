package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking


import com.google.gson.annotations.SerializedName


data class MyBookingRentalDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("rentalPosting") val rentalPosting: RentalPosting,
    @SerializedName("status") val status: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("primaryGuestName") val primaryGuestName: String,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isFeedback") val isFeedback: Boolean,
    @SerializedName("renterFullLegalName") val renterFullLegalName: String,
    @SerializedName("renterLegalPhone") val renterLegalPhone: String?,
    @SerializedName("renterLegalAvatar") val renterLegalAvatar: String?,
    @SerializedName("serviceFee") val serviceFee: Long,
    @SerializedName("totalPrice") val totalPrice: Long?,
    @SerializedName("totalNights") val totalNights: Long?,
    @SerializedName("pricePerNights") val pricePerNights: Long?,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("source") val source: String
) {
    data class RentalPosting(
        @SerializedName("id") val id: Int,
        @SerializedName("description") val description: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("isBookable") val isBookable: Boolean,
        @SerializedName("roomInfo") val roomInfo: RoomInfo,
        @SerializedName("cancellationType") val cancellationType: CancellationType,
        @SerializedName("rentalPackageId") val rentalPackageId: Int,
        @SerializedName("rentalPackageRentalPackageName") val rentalPackageRentalPackageName: String,
        @SerializedName("rentalPackageType") val rentalPackageType: String,
        @SerializedName("rentalPackagePrice") val rentalPackagePrice: Long?,
        @SerializedName("createdDate") val createdDate: Long,
        @SerializedName("updatedDate") val updatedDate: Long
    ) {
        data class RoomInfo(
            @SerializedName("id") val id: Int,
            @SerializedName("roomInfoCode") val roomInfoCode: String,
            @SerializedName("roomInfoName") val roomInfoName: String,
            @SerializedName("isActive") val isActive: Boolean,
            @SerializedName("status") val status: String,
            @SerializedName("unitType") val unitType: UnitType,
            @SerializedName("resortLogo") val resortLogo: String
        ) {
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
                @SerializedName("buildingsOption") val buildingsOption: Any?,
                @SerializedName("price") val price: Long?,
                @SerializedName("description") val description: String,
                @SerializedName("kitchen") val kitchen: String,
                @SerializedName("photos") val photos: String,
                @SerializedName("resortId") val resortId: Int,
                @SerializedName("resortResortName") val resortResortName: String,
                @SerializedName("location") val location: Location,
                @SerializedName("resortDescription") val resortDescription: String,
                @SerializedName("sleeps") val sleeps: Int,
                @SerializedName("view") val view: String,
                @SerializedName("isActive") val isActive: Boolean
            ) {
                data class Location(
                    @SerializedName("name") val name: String,
                    @SerializedName("displayName") val displayName: String,
                    @SerializedName("latitude") val latitude: String,
                    @SerializedName("longitude") val longitude: String,
                    @SerializedName("country") val country: String?,
                    @SerializedName("placeId") val placeId: String?
                )
            }
        }

        data class CancellationType(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String,
            @SerializedName("refundRate") val refundRate: Int,
            @SerializedName("durationBefore") val durationBefore: Int,
            @SerializedName("description") val description: String,
            @SerializedName("isActive") val isActive: Boolean
        )
    }
}