package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange


import com.google.gson.annotations.SerializedName
data class MyExchangeRequestDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("roomInfo") val roomInfo: RoomInfo,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("ownerFullName") val ownerFullName: String,
    @SerializedName("ownerAvatar") val ownerAvatar: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("exchangePosting") val exchangePosting: ExchangePosting,
    @SerializedName("note") val note: Any?,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("priceValuation") val priceValuation: Long?,
    @SerializedName("isActive") val isActive: Boolean
) {
    data class RoomInfo(
        @SerializedName("id") val id: Int,
        @SerializedName("roomInfoCode") val roomInfoCode: String,
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("updatedAt") val updatedAt: String,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortResortName") val resortResortName: String,
        @SerializedName("resortLogo") val resortLogo: String,
        @SerializedName("location") val location: Location,
        @SerializedName("resortDescription") val resortDescription: String,
        @SerializedName("status") val status: String,
        @SerializedName("unitType") val unitType: UnitType
    ) {
        data class Location(
            @SerializedName("name") val name: String,
            @SerializedName("displayName") val displayName: String,
            @SerializedName("latitude") val latitude: String,
            @SerializedName("longitude") val longitude: String,
            @SerializedName("country") val country: String,
            @SerializedName("placeId") val placeId: Any?
        )

        data class UnitType(
            @SerializedName("id") val id: Int,
            @SerializedName("title") val title: String,
            @SerializedName("price") val price: Int,
            @SerializedName("description") val description: String,
            @SerializedName("photos") val photos: String
        )
    }

    data class ExchangePosting(
        @SerializedName("id") val id: Int,
        @SerializedName("description") val description: String,
        @SerializedName("nights") val nights: Int,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("isExchange") val isExchange: Boolean,
        @SerializedName("status") val status: String,
        @SerializedName("checkinDate") val checkinDate: String,
        @SerializedName("checkoutDate") val checkoutDate: String,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomInfoRoomInfoCode") val roomInfoRoomInfoCode: String,
        @SerializedName("roomInfoResortId") val roomInfoResortId: Int,
        @SerializedName("roomInfoResortResortName") val roomInfoResortResortName: String,
        @SerializedName("roomInfoResortLogo") val roomInfoResortLogo: String,
        @SerializedName("roomInfoUnitTypeId") val roomInfoUnitTypeId: Int,
        @SerializedName("roomInfoUnitTypeTitle") val roomInfoUnitTypeTitle: String,
        @SerializedName("roomInfoUnitTypePrice") val roomInfoUnitTypePrice: Int,
        @SerializedName("roomInfoUnitTypePhotos") val roomInfoUnitTypePhotos: String
    )
}