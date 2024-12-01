package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting


import com.google.gson.annotations.SerializedName

data class MyExchangePostingsResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("content") val content: List<Content>,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("empty") val empty: Boolean
) {
    data class Content(
        @SerializedName("exchangePostingId") val exchangePostingId: Int,
        @SerializedName("expired") val expired: String,
        @SerializedName("ownerId") val ownerId: Int,
        @SerializedName("ownerName") val ownerName: String,
        @SerializedName("timeShareId") val timeShareId: Int,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomName") val roomName: String,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("resortLocationName") val resortLocationName: String,
        @SerializedName("resortLocationDisplayName") val resortLocationDisplayName: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("nights") val nights: Int,
        @SerializedName("exchangePackageId") val exchangePackageId: Int,
        @SerializedName("exchangePackageName") val exchangePackageName: String,
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

    data class Sort(
        @SerializedName("empty") val empty: Boolean,
        @SerializedName("sorted") val sorted: Boolean,
        @SerializedName("unsorted") val unsorted: Boolean
    )

    data class Pageable(
        @SerializedName("offset") val offset: Int,
        @SerializedName("sort") val sort: Sort,
        @SerializedName("paged") val paged: Boolean,
        @SerializedName("pageNumber") val pageNumber: Int,
        @SerializedName("pageSize") val pageSize: Int,
        @SerializedName("unpaged") val unpaged: Boolean
    ) {
        data class Sort(
            @SerializedName("empty") val empty: Boolean,
            @SerializedName("sorted") val sorted: Boolean,
            @SerializedName("unsorted") val unsorted: Boolean
        )
    }
}