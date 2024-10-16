package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType


import com.google.gson.annotations.SerializedName

data class UnitTypeModel(
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
    @SerializedName("price") val price: Int,
    @SerializedName("description") val description: String,
    @SerializedName("kitchen") val kitchen: String,
    @SerializedName("photos") val photos: String,
    @SerializedName("resortId") val resortId: Any?,
    @SerializedName("sleeps") val sleeps: Int,
    @SerializedName("view") val view: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("unitTypeAmenitiesDTOS") val unitTypeAmenitiesDTOS: List<UnitTypeAmenitiesDTOS>
) {
    data class UnitTypeAmenitiesDTOS(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: Any?,
        @SerializedName("isActive") val isActive: Any?
    )
}