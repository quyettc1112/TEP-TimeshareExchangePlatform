package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort


import com.google.gson.annotations.SerializedName
data class ResortDetailModelResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("resortName") val resortName: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("minPrice") val minPrice: Int,
    @SerializedName("maxPrice") val maxPrice: Int,
    @SerializedName("status") val status: String,
    @SerializedName("address") val address: String,
    @SerializedName("timeshareCompanyId") val timeshareCompanyId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("resortAmenityList") val resortAmenityList: List<ResortAmenity>,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("unitTypeDtoList") val unitTypeDtoList: List<UnitTypeDto>,
    @SerializedName("feedbackList") val feedbackList: List<Feedback>,
    @SerializedName("imageUrls") val imageUrls: List<String>
) {
    data class ResortAmenity(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )

    data class UnitTypeDto(
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
        @SerializedName("price") val price: Int,
        @SerializedName("description") val description: String,
        @SerializedName("kitchen") val kitchen: String,
        @SerializedName("photos") val photos: String,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("sleeps") val sleeps: Int,
        @SerializedName("view") val view: String,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("unitTypeAmenitiesList") val unitTypeAmenitiesList: List<UnitTypeAmenities>
    ) {
        data class UnitTypeAmenities(
            @SerializedName("name") val name: String,
            @SerializedName("type") val type: String?,
            @SerializedName("isActive") val isActive: Boolean
        )
    }

    data class Feedback(
        @SerializedName("ratingPoint") val ratingPoint: Int,
        @SerializedName("comment") val comment: String,
        @SerializedName("user") val user: User,
        @SerializedName("createdDate") val createdDate: String,
        @SerializedName("isActive") val isActive: Boolean
    ) {
        data class User(
            @SerializedName("fullName") val fullName: String,
            @SerializedName("avatar") val avatar: String?
        )
    }
}