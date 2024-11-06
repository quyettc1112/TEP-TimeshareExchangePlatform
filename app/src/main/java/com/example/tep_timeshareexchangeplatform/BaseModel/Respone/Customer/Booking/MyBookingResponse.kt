package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking


import com.google.gson.annotations.SerializedName

/**
{
  "totalPages": 0,
  "totalElements": 0,
  "size": 0,
  "content": [
    {
      "bookingId": 0,
      "source": "string",
      "status": "string",
      "renterId": 0,
      "checkinDate": "2024-11-06",
      "checkoutDate": "2024-11-06",
      "primaryGuestName": "string",
      "primaryGuestPhone": "string",
      "primaryGuestEmail": "string",
      "isActive": true,
      "renterFullLegalName": "string",
      "renterLegalAvatar": "string",
      "renterLegalPhone": "string",
      "unitTypeTitle": "string",
      "resortId": 0,
      "resortName": "string",
      "logo": "string"
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
  "first": true,
  "last": true,
  "numberOfElements": 0,
  "empty": true
}
*/
data class MyBookingResponse(
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
        @SerializedName("bookingId") val bookingId: Int,
        @SerializedName("source") val source: String,
        @SerializedName("status") val status: String,
        @SerializedName("renterId") val renterId: Int,
        @SerializedName("checkinDate") val checkinDate: String,
        @SerializedName("checkoutDate") val checkoutDate: String,
        @SerializedName("primaryGuestName") val primaryGuestName: String,
        @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
        @SerializedName("primaryGuestEmail") val primaryGuestEmail: String,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("renterFullLegalName") val renterFullLegalName: String,
        @SerializedName("renterLegalAvatar") val renterLegalAvatar: String,
        @SerializedName("renterLegalPhone") val renterLegalPhone: String,
        @SerializedName("unitTypeTitle") val unitTypeTitle: String,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("logo") val logo: String
    )

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