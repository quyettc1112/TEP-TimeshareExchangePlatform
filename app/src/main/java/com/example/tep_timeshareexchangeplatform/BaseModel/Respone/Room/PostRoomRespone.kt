package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room


import com.google.gson.annotations.SerializedName

/**
{
  "roomId": 14,
  "roomInfoCode": "user_create_5",
  "isActive": true,
  "resortId": 1,
  "roomName": "Phòng Vua Chúa",
  "status": "string",
  "unitType": {
    "id": 1,
    "title": "Phòng Queen",
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
    "sleeps": 0,
    "view": "string",
    "isActive": true
  },
  "createdAt": "16-10-2024 16:05:27",
  "roomAmenities": [
    {
      "name": "string",
      "type": "string",
      "isActive": true
    }
  ]
}
*/
data class PostRoomRespone(
    @SerializedName("roomId") val roomId: Int,
    @SerializedName("roomInfoCode") val roomInfoCode: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("status") val status: String,
    @SerializedName("unitType") val unitType: UnitType,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("roomAmenities") val roomAmenities: List<RoomAmenity>
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
        @SerializedName("sleeps") val sleeps: Int,
        @SerializedName("view") val view: String,
        @SerializedName("isActive") val isActive: Boolean
    )

    data class RoomAmenity(
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String,
        @SerializedName("isActive") val isActive: Boolean
    )
}