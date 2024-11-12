package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting


import com.google.gson.annotations.SerializedName

/**
{
  "totalPages": 0,
  "totalElements": 0,
  "size": 0,
  "content": [
    {
      "rentalPostingId": 0,
      "expiredDate": "2024-11-05",
      "ownerId": 0,
      "ownerName": "string",
      "timeShareId": 0,
      "roomInfoId": 0,
      "roomName": "string",
      "resortId": 0,
      "resortName": "string",
      "address": "string",
      "isVerify": true,
      "nights": 0,
      "pricePerNights": 0,
      "totalPrice": 0,
      "rentalPackageId": 0,
      "rentalPackageName": "string",
      "priceValuation": 0,
      "staffRefinementPrice": 0,
      "checkinDate": "2024-11-05",
      "checkoutDate": "2024-11-05",
      "status": "string",
      "unitTypeDTO": {
        "id": 0,
        "title": "string",
        "area": "string",
        "bathrooms": 0,
        "bedrooms": 0,
        "bedsFull": 0,
        "bedsKing": 0,
        "bedsSofa": 0,
        "bedsMurphy": 0,
        "bedsQueen": 0,
        "bedsTwin": 0,
        "buildingsOption": "string",
        "description": "string",
        "kitchen": "string",
        "photos": "string",
        "sleeps": 0,
        "view": "string"
      },
      "active": true
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
data class MyPostingResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("content") val content: List<Content>,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("empty") val empty: Boolean
) {
    data class Content(
        @SerializedName("rentalPostingId") val rentalPostingId: Int,
        @SerializedName("expiredDate") val expiredDate: String,
        @SerializedName("ownerId") val ownerId: Int,
        @SerializedName("ownerName") val ownerName: String,
        @SerializedName("timeShareId") val timeShareId: Int,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomName") val roomName: String,
        @SerializedName("resortId") val resortId: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("address") val address: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("nights") val nights: Int,
        @SerializedName("pricePerNights") val pricePerNights: Int,
        @SerializedName("totalPrice") val totalPrice: Int,
        @SerializedName("rentalPackageId") val rentalPackageId: Int,
        @SerializedName("rentalPackageName") val rentalPackageName: String,
        @SerializedName("priceValuation") val priceValuation: Long?,
        @SerializedName("staffRefinementPrice") val staffRefinementPrice: Long?,
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