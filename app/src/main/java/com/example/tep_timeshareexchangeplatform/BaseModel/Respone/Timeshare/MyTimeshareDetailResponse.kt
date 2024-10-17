package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare


import com.google.gson.annotations.SerializedName

/**
{
  "timeShareId": 1,
  "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang",
  "roomCode": "123",
  "roomName": "cc",
  "roomId": 2,
  "resortAddress": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
  "resortId": 1,
  "startDate": "24-08-2019",
  "endDate": "24-08-2019",
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
    "description": "string",
    "kitchen": "string",
    "photos": "string",
    "sleeps": 0,
    "view": "string"
  }
}
*/
data class MyTimeshareDetailResponse(
    @SerializedName("timeShareId") val timeShareId: Int,
    @SerializedName("resortName") val resortName: String,
    @SerializedName("roomCode") val roomCode: String,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("roomId") val roomId: Int,
    @SerializedName("resortAddress") val resortAddress: String,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
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
        @SerializedName("description") val description: String,
        @SerializedName("kitchen") val kitchen: String,
        @SerializedName("photos") val photos: String,
        @SerializedName("sleeps") val sleeps: Int,
        @SerializedName("view") val view: String
    )
}