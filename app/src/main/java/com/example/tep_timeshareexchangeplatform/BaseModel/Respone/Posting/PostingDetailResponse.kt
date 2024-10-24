package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting


import com.google.gson.annotations.SerializedName

/**
{
  "rentalPostingId": 1,
  "timeShareId": 2,
  "roomInfoId": 1,
  "roomName": "a",
  "resortId": 1,
  "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang",
  "address": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
  "isVerify": false,
  "nights": 4,
  "pricePerNights": 500,
  "totalPrice": 2000,
  "cancelType": null,
  "rentalPackageId": 2,
  "rentalPackageName": "Gói cao cấp",
  "rentalPackageDescription": "Sử dụng hệ thống đặt chỗ trực tiếp từ Unwind ",
  "checkinDate": "18-12-2024",
  "checkoutDate": "22-12-2024",
  "status": "PendingApproval",
  "unitType": {
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
  "resortAmenities": [
    {
      "id": 4077,
      "name": "string",
      "type": "string"
    },
    {
      "id": 4078,
      "name": "string",
      "type": "string"
    },
    {
      "id": 4079,
      "name": "string",
      "type": "string"
    },
    {
      "id": 4080,
      "name": "string",
      "type": "string"
    },
    {
      "id": 4081,
      "name": "string",
      "type": "string"
    },
    {
      "id": 4082,
      "name": "q",
      "type": "1"
    },
    {
      "id": 4083,
      "name": "q",
      "type": "1"
    },
    {
      "id": 4084,
      "name": "1",
      "type": "1"
    }
  ],
  "active": true
}
*/
data class PostingDetailResponse(
    @SerializedName("rentalPostingId") val rentalPostingId: Int,
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
    @SerializedName("cancelType") val cancelType: Any?,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("rentalPackageName") val rentalPackageName: String,
    @SerializedName("rentalPackageDescription") val rentalPackageDescription: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("unitType") val unitType: UnitType,
    @SerializedName("resortAmenities") val resortAmenities: List<ResortAmenity>,
    @SerializedName("active") val active: Boolean
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

    data class ResortAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )
}