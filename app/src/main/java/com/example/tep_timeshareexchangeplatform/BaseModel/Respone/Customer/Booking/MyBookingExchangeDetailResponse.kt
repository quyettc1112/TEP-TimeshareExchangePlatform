package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking


import com.google.gson.annotations.SerializedName


data class MyBookingExchangeDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("roomInfo") val roomInfo: RoomInfo,
    @SerializedName("status") val status: String,
    @SerializedName("checkinDate") val checkinDate: String?,
    @SerializedName("checkoutDate") val checkoutDate: String?,
    @SerializedName("type") val type: Any?,
    @SerializedName("primaryGuestName") val primaryGuestName: String?,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String?,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String?,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isFeedback") val isFeedback: Boolean?,
    @SerializedName("renterFullLegalName") val renterFullLegalName: String?,
    @SerializedName("renterLegalPhone") val renterLegalPhone: String?,
    @SerializedName("renterLegalAvatar") val renterLegalAvatar: String?,
    @SerializedName("serviceFee") val serviceFee: Long?,
    @SerializedName("nights") val nights: Int?,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("source") val source: String,
    @SerializedName("isPrimaryGuest") val isPrimaryGuest: Boolean,
) {
    data class RoomInfo(
        @SerializedName("roomInfoCode") val roomInfoCode: String,
        @SerializedName("roomInfoName") val roomInfoName: String,
        @SerializedName("unitType") val unitType: UnitType
    ) {
        data class UnitType(
            @SerializedName("id") val id: Int,
            @SerializedName("title") val title: String,
            @SerializedName("price") val price: Int,
            @SerializedName("description") val description: String,
            @SerializedName("photos") val photos: String,
            @SerializedName("resortId") val resortId: Int,
            @SerializedName("resortName") val resortName: String,
            @SerializedName("resortLogo") val resortLogo: String,
            @SerializedName("resortDescription") val resortDescription: String
        )
    }
}