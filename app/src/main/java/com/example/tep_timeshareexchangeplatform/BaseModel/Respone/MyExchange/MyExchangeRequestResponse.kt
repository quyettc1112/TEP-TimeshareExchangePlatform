package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange


import com.google.gson.annotations.SerializedName

/**
{
  "totalPages": 0,
  "totalElements": 0,
  "size": 0,
  "content": [
    {
      "id": 0,
      "startDate": "2024-11-17",
      "endDate": "2024-11-17",
      "status": "string",
      "exchangePosting": {
        "id": 0,
        "description": "string",
        "nights": 0,
        "isVerify": true,
        "isExchange": true,
        "status": "string",
        "checkinDate": "2024-11-17",
        "checkoutDate": "2024-11-17",
        "roomInfoId": 0,
        "roomInfoRoomInfoCode": "string",
        "roomInfoResortId": 0,
        "roomInfoResortResortName": "string",
        "roomInfoResortLogo": "string",
        "roomInfoUnitTypeId": 0,
        "roomInfoUnitTypeTitle": "string",
        "roomInfoUnitTypePrice": 0,
        "roomInfoUnitTypePhotos": "string"
      },
      "note": "string",
      "createdDate": "2024-11-17T12:40:25.690Z",
      "updatedDate": "2024-11-17T12:40:25.690Z",
      "isActive": true
    }
  ],
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": true,
    "unsorted": true
  },
  "pageable": {
    "offset": 0,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0,
    "unpaged": true
  },
  "numberOfElements": 0,
  "first": true,
  "last": true,
  "empty": true
}
*/
data class MyExchangeRequestResponse(
    @SerializedName("content") val content: List<Content>,
    @SerializedName("empty") val empty: Boolean,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("number") val number: Int,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("size") val size: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("totalPages") val totalPages: Int
) {
    data class Content(
        @SerializedName("createdDate") val createdDate: String,
        @SerializedName("endDate") val endDate: String,
        @SerializedName("exchangePosting") val exchangePosting: ExchangePosting,
        @SerializedName("id") val id: Int,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("note") val note: String,
        @SerializedName("startDate") val startDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("updatedDate") val updatedDate: String
    ) {
        data class ExchangePosting(
            @SerializedName("checkinDate") val checkinDate: String,
            @SerializedName("checkoutDate") val checkoutDate: String,
            @SerializedName("description") val description: String,
            @SerializedName("id") val id: Int,
            @SerializedName("isExchange") val isExchange: Boolean,
            @SerializedName("isVerify") val isVerify: Boolean,
            @SerializedName("nights") val nights: Int,
            @SerializedName("roomInfoId") val roomInfoId: Int,
            @SerializedName("roomInfoResortId") val roomInfoResortId: Int,
            @SerializedName("roomInfoResortLogo") val roomInfoResortLogo: String,
            @SerializedName("roomInfoResortResortName") val roomInfoResortResortName: String,
            @SerializedName("roomInfoRoomInfoCode") val roomInfoRoomInfoCode: String,
            @SerializedName("roomInfoUnitTypeId") val roomInfoUnitTypeId: Int,
            @SerializedName("roomInfoUnitTypePhotos") val roomInfoUnitTypePhotos: String,
            @SerializedName("roomInfoUnitTypePrice") val roomInfoUnitTypePrice: Int,
            @SerializedName("roomInfoUnitTypeTitle") val roomInfoUnitTypeTitle: String,
            @SerializedName("status") val status: String
        )
    }

    data class Pageable(
        @SerializedName("offset") val offset: Int,
        @SerializedName("pageNumber") val pageNumber: Int,
        @SerializedName("pageSize") val pageSize: Int,
        @SerializedName("paged") val paged: Boolean,
        @SerializedName("sort") val sort: Sort,
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