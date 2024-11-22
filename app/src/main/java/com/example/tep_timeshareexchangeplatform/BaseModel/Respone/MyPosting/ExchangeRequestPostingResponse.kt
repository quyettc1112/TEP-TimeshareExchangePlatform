package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting


import com.google.gson.annotations.SerializedName

/**
{
  "totalPages": 0,
  "totalElements": 0,
  "size": 0,
  "content": [
    {
      "id": 0,
      "roomInfo": {
        "id": 0,
        "roomInfoCode": "string",
        "roomInfoName": "string",
        "isActive": true,
        "status": "string",
        "unitType": {
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
          "price": 0,
          "description": "string",
          "kitchen": "string",
          "photos": "string",
          "resortId": 0,
          "sleeps": 0,
          "view": "string",
          "isActive": true,
          "unitTypeAmenitiesList": [
            {
              "name": "string",
              "type": "string",
              "isActive": true
            }
          ]
        }
      },
      "ownerId": 0,
      "ownerFullName": "string",
      "ownerAvatar": "string",
      "startDate": "2024-11-17",
      "endDate": "2024-11-17",
      "status": "string",
      "note": "string",
      "createdDate": "2024-11-17T15:08:29.276Z",
      "updatedDate": "2024-11-17T15:08:29.276Z",
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
data class ExchangeRequestPostingResponse(
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
        @SerializedName("id") val id: Int,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("note") val note: String,
        @SerializedName("ownerAvatar") val ownerAvatar: String,
        @SerializedName("ownerFullName") val ownerFullName: String,
        @SerializedName("ownerId") val ownerId: Int,
        @SerializedName("roomInfo") val roomInfo: RoomInfo,
        @SerializedName("startDate") val startDate: String,
        @SerializedName("status") val status: String,
        @SerializedName("updatedDate") val updatedDate: String
    ) {
        data class RoomInfo(
            @SerializedName("id") val id: Int,
            @SerializedName("isActive") val isActive: Boolean,
            @SerializedName("roomInfoCode") val roomInfoCode: String,
            @SerializedName("roomInfoName") val roomInfoName: String,
            @SerializedName("status") val status: String,
            @SerializedName("unitType") val unitType: UnitType
        ) {
            data class UnitType(
                @SerializedName("area") val area: String,
                @SerializedName("bathrooms") val bathrooms: Int,
                @SerializedName("bedrooms") val bedrooms: Int,
                @SerializedName("bedsFull") val bedsFull: Int,
                @SerializedName("bedsKing") val bedsKing: Int,
                @SerializedName("bedsMurphy") val bedsMurphy: Int,
                @SerializedName("bedsQueen") val bedsQueen: Int,
                @SerializedName("bedsSofa") val bedsSofa: Int,
                @SerializedName("bedsTwin") val bedsTwin: Int,
                @SerializedName("buildingsOption") val buildingsOption: String,
                @SerializedName("description") val description: String,
                @SerializedName("id") val id: Int,
                @SerializedName("isActive") val isActive: Boolean,
                @SerializedName("kitchen") val kitchen: String,
                @SerializedName("photos") val photos: String,
                @SerializedName("price") val price: Int,
                @SerializedName("resortId") val resortId: Int,
                @SerializedName("sleeps") val sleeps: Int,
                @SerializedName("title") val title: String,
                @SerializedName("unitTypeAmenitiesList") val unitTypeAmenitiesList: List<UnitTypeAmenities>,
                @SerializedName("view") val view: String
            ) {
                data class UnitTypeAmenities(
                    @SerializedName("isActive") val isActive: Boolean,
                    @SerializedName("name") val name: String,
                    @SerializedName("type") val type: String
                )
            }
        }
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