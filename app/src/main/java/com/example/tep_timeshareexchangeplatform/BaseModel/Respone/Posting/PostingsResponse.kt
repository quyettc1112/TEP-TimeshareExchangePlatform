package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting


import com.google.gson.annotations.SerializedName

/**
{
  "content": [
    {
      "rentalPostingId": 3,
      "timeShareId": 4,
      "roomInfoId": 3,
      "roomName": null,
      "resortId": 1,
      "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang",
      "address": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
      "isVerify": false,
      "nights": 4,
      "pricePerNights": 500,
      "totalPrice": 2000,
      "checkinDate": "11-12-2024",
      "checkoutDate": "28-12-2024",
      "status": "Processing",
      "active": true
    },
    {
      "rentalPostingId": 4,
      "timeShareId": 1,
      "roomInfoId": 2,
      "roomName": "cc",
      "resortId": 1,
      "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang",
      "address": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
      "isVerify": true,
      "nights": 5,
      "pricePerNights": 500,
      "totalPrice": 2500,
      "checkinDate": "25-12-2024",
      "checkoutDate": "30-12-2024",
      "status": "Processing",
      "active": true
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
*/
data class PostingsResponse(
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
        @SerializedName("rentalPostingId") val rentalPostingId: Int,
        @SerializedName("timeShareId") val timeShareId: Int,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomName") val roomName: String?,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("address") val address: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("nights") val nights: Int,
        @SerializedName("pricePerNights") val pricePerNights: Int,
        @SerializedName("totalPrice") val totalPrice: Int,
        @SerializedName("checkinDate") val checkinDate: String,
        @SerializedName("checkoutDate") val checkoutDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("active") val active: Boolean
    )

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