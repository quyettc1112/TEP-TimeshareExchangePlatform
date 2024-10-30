package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting


import com.google.gson.annotations.SerializedName

/**
{
  "content": [
    {
      "rentalPostingId": 3,
      "expiredDate": "22-11-2024",
      "ownerId": 6,
      "ownerName": "thanhlong",
      "timeShareId": 4,
      "roomInfoId": 3,
      "roomName": "test",
      "resortId": 1,
      "resortName": "Khách sạn Cương Quyết",
      "address": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
      "isVerify": false,
      "nights": 4,
      "pricePerNights": 500,
      "totalPrice": 2000,
      "rentalPackageId": 1,
      "rentalPackageName": "Gói Cơ Bản",
      "checkinDate": "11-12-2024",
      "checkoutDate": "28-12-2024",
      "status": "Processing",
      "unitTypeDTO": {
        "id": 1,
        "title": "Phòng Queen",
        "area": "string",
        "bathrooms": 1,
        "bedrooms": 2,
        "bedsFull": 0,
        "bedsKing": 0,
        "bedsSofa": 0,
        "bedsMurphy": 0,
        "bedsQueen": 0,
        "bedsTwin": 2,
        "buildingsOption": "string",
        "description": "string",
        "kitchen": "Bếp chung",
        "photos": "string",
        "sleeps": 4,
        "view": "string"
      },
      "active": true
    },
    {
      "rentalPostingId": 6,
      "expiredDate": "22-11-2024",
      "ownerId": 6,
      "ownerName": "thanhlong",
      "timeShareId": 19,
      "roomInfoId": 1,
      "roomName": "a",
      "resortId": 1,
      "resortName": "Khách sạn Cương Quyết",
      "address": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
      "isVerify": true,
      "nights": 3,
      "pricePerNights": 500,
      "totalPrice": 1500,
      "rentalPackageId": 2,
      "rentalPackageName": "Gói Nâng Cao",
      "checkinDate": "25-12-2024",
      "checkoutDate": "30-12-2024",
      "status": "Processing",
      "unitTypeDTO": {
        "id": 1,
        "title": "Phòng Queen",
        "area": "string",
        "bathrooms": 1,
        "bedrooms": 2,
        "bedsFull": 0,
        "bedsKing": 0,
        "bedsSofa": 0,
        "bedsMurphy": 0,
        "bedsQueen": 0,
        "bedsTwin": 2,
        "buildingsOption": "string",
        "description": "string",
        "kitchen": "Bếp chung",
        "photos": "string",
        "sleeps": 4,
        "view": "string"
      },
      "active": true
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
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
  "size": 2,
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
data class PublicPostingResponse(
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