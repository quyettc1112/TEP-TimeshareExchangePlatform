package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange


import com.google.gson.annotations.SerializedName

data class ExchangeRequestOnPostResponse(
    @SerializedName("content") val content: List<Content>,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("last") val last: Boolean,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("empty") val empty: Boolean
) {
    data class Content(
        @SerializedName("id") val id: Int,
        @SerializedName("roomInfo") val roomInfo: RoomInfo,
        @SerializedName("ownerId") val ownerId: Int,
        @SerializedName("ownerFullName") val ownerFullName: String,
        @SerializedName("ownerAvatar") val ownerAvatar: String,
        @SerializedName("startDate") val startDate: String,
        @SerializedName("endDate") val endDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("note") val note: Any?,
        @SerializedName("createdDate") val createdDate: String,
        @SerializedName("updatedDate") val updatedDate: String,
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
            @SerializedName("resortLocationName") val resortLocationName: String,
            @SerializedName("resortLocationDisplayName") val resortLocationDisplayName: String,
            @SerializedName("resortDescription") val resortDescription: String,
            @SerializedName("status") val status: String,
            @SerializedName("unitTypeId") val unitTypeId: Int,
            @SerializedName("unitTypeTitle") val unitTypeTitle: String,
            @SerializedName("unitTypePhotos") val unitTypePhotos: String
        )
    }

    data class Pageable(
        @SerializedName("pageNumber") val pageNumber: Int,
        @SerializedName("pageSize") val pageSize: Int,
        @SerializedName("sort") val sort: Sort,
        @SerializedName("offset") val offset: Int,
        @SerializedName("paged") val paged: Boolean,
        @SerializedName("unpaged") val unpaged: Boolean
    ) {
        data class Sort(
            @SerializedName("empty") val empty: Boolean,
            @SerializedName("sorted") val sorted: Boolean,
            @SerializedName("unsorted") val unsorted: Boolean
        )
    }

    data class Sort(
        @SerializedName("empty") val empty: Boolean,
        @SerializedName("sorted") val sorted: Boolean,
        @SerializedName("unsorted") val unsorted: Boolean
    )
}