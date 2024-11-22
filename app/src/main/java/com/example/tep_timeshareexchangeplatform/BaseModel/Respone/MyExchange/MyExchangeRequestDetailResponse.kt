package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange


import com.google.gson.annotations.SerializedName

/**
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
  "createdDate": "2024-11-17T12:44:48.261Z",
  "updatedDate": "2024-11-17T12:44:48.261Z",
  "isActive": true
}
*/
data class MyExchangeRequestDetailResponse(
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("exchangePosting") val exchangePosting: ExchangePosting,
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