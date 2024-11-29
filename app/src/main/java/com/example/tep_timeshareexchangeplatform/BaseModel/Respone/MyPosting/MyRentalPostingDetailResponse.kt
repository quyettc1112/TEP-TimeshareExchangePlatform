package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting


import com.google.gson.annotations.SerializedName

/**
{
  "rentalPostingId": 23,
  "description": "string",
  "expiredDate": "28-12-2024",
  "ownerId": 31,
  "ownerName": "string",
  "timeShareId": 1,
  "roomInfoId": 2,
  "roomName": "cc",
  "resortId": 1,
  "resortName": "Khách sạn Cương Quyết",
  "address": "Premier Pearl Hotel Vung Tau toa lac tai khu vuc / thanh pho Phuong 2. /n",
  "isVerify": false,
  "nights": 13,
  "pricePerNights": 0,
  "totalPrice": 0,
  "cancelType": null,
  "rentalPackageId": 2,
  "rentalPackageName": "Gói Nâng Cao",
  "rentalPackageDuration": "60",
  "rentalPackageDescription": "Sử dụng hệ thống đặt chỗ trực tiếp từ Unwind ",
  "checkinDate": "29-10-2024",
  "checkoutDate": "29-10-2024",
  "status": "Reject",
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
      "name": "Quầy bar",
      "type": "Các tính năng và tiện nghi tại chỗ"
    },
    {
      "id": 4078,
      "name": "Sòng bạc",
      "type": "Các tính năng và tiện nghi tại chỗ"
    },
    {
      "id": 4079,
      "name": "Wifi",
      "type": "Các tính năng và tiện nghi tại chỗ"
    },
    {
      "id": 4080,
      "name": "Hồ bơi ngoài trời",
      "type": "Các tính năng và tiện nghi tại chỗ"
    },
    {
      "id": 4081,
      "name": "Golf",
      "type": "Các điểm tham quan gần đó"
    },
    {
      "id": 4082,
      "name": "Lướt ván buồm",
      "type": "Các điểm tham quan gần đó"
    },
    {
      "id": 4083,
      "name": "Lướt ván buồm",
      "type": "Các điểm tham quan gần đó"
    },
    {
      "id": 4084,
      "name": "Cưỡi ngựa",
      "type": "Các điểm tham quan gần đó"
    }
  ],
  "roomAmenities": [
    {
      "id": 1,
      "name": "string",
      "type": "string"
    }
  ],
  "unitTypeAmenities": [
    {
      "id": 189,
      "name": "WiFi",
      "type": null
    },
    {
      "id": 190,
      "name": "test",
      "type": "test1"
    }
  ],
  "active": true
}
*/
data class MyRentalPostingDetailResponse(
    @SerializedName("rentalPostingId") val rentalPostingId: Int,
    @SerializedName("description") val description: String,
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
    @SerializedName("nights") val nights: Long?,
    @SerializedName("pricePerNights") val pricePerNights: Long?,
    @SerializedName("totalPrice") val totalPrice: Long?,
    @SerializedName("cancelType") val cancelType: Any?,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("rentalPackageName") val rentalPackageName: String,
    @SerializedName("rentalPackageDuration") val rentalPackageDuration: String,
    @SerializedName("rentalPackageDescription") val rentalPackageDescription: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("unitType") val unitType: UnitType,
    @SerializedName("resortAmenities") val resortAmenities: List<ResortAmenity>,
    @SerializedName("roomAmenities") val roomAmenities: List<RoomAmenity>,
    @SerializedName("unitTypeAmenities") val unitTypeAmenities: List<UnitTypeAmenity>,
    @SerializedName("imageUrls") val imageUrls: List<String>,
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

    data class RoomAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )

    data class UnitTypeAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String?
    )
}