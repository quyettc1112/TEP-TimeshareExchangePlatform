package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Exchange


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
  "createdDate": "2024-11-17T11:34:34.280Z",
  "updatedDate": "2024-11-17T11:34:34.280Z",
  "isActive": true
}
*/
data class ExchangeRequestResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("roomInfo") val roomInfo: RoomInfo,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("ownerFullName") val ownerFullName: String,
    @SerializedName("ownerAvatar") val ownerAvatar: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("exchangePosting") val exchangePosting: ExchangePosting,
    @SerializedName("note") val note: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("isActive") val isActive: Boolean
) {
    data class RoomInfo(
        @SerializedName("id") val id: Int,
        @SerializedName("roomInfoCode") val roomInfoCode: String,
        @SerializedName("roomInfoName") val roomInfoName: String,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("status") val status: String,
        @SerializedName("unitType") val unitType: UnitType
    ) {
        data class UnitType(
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
            @SerializedName("price") val price: Int,
            @SerializedName("description") val description: String,
            @SerializedName("kitchen") val kitchen: String,
            @SerializedName("photos") val photos: String,
            @SerializedName("resortId") val resortId: Int,
            @SerializedName("sleeps") val sleeps: Int,
            @SerializedName("view") val view: String,
            @SerializedName("isActive") val isActive: Boolean,
            @SerializedName("unitTypeAmenitiesList") val unitTypeAmenitiesList: List<UnitTypeAmenities>
        ) {
            data class UnitTypeAmenities(
                @SerializedName("name") val name: String,
                @SerializedName("type") val type: String,
                @SerializedName("isActive") val isActive: Boolean
            )
        }
    }

    data class ExchangePosting(
        @SerializedName("id") val id: Int,
        @SerializedName("description") val description: String,
        @SerializedName("nights") val nights: Int,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("isExchange") val isExchange: Boolean,
        @SerializedName("status") val status: String,
        @SerializedName("checkinDate") val checkinDate: String,
        @SerializedName("checkoutDate") val checkoutDate: String,
        @SerializedName("roomInfoId") val roomInfoId: Int,
        @SerializedName("roomInfoRoomInfoCode") val roomInfoRoomInfoCode: String,
        @SerializedName("roomInfoResortId") val roomInfoResortId: Int,
        @SerializedName("roomInfoResortResortName") val roomInfoResortResortName: String,
        @SerializedName("roomInfoResortLogo") val roomInfoResortLogo: String,
        @SerializedName("roomInfoUnitTypeId") val roomInfoUnitTypeId: Int,
        @SerializedName("roomInfoUnitTypeTitle") val roomInfoUnitTypeTitle: String,
        @SerializedName("roomInfoUnitTypePrice") val roomInfoUnitTypePrice: Int,
        @SerializedName("roomInfoUnitTypePhotos") val roomInfoUnitTypePhotos: String
    )
}