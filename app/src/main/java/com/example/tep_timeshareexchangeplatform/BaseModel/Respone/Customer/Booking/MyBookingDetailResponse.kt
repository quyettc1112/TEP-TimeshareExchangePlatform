package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "rentalPosting": {
    "id": 0,
    "description": "string",
    "isVerify": true,
    "isBookable": true,
    "roomInfo": {
      "id": 0,
      "roomInfoCode": "string",
      "roomInfoName": "string",
      "createdAt": "2024-11-06T09:01:33.839Z",
      "updatedAt": "2024-11-06T09:01:33.839Z",
      "isActive": true,
      "resortId": 0,
      "status": "string",
      "unitTypeId": 0
    },
    "cancellationType": {
      "id": 0,
      "name": "string",
      "refundRate": 0,
      "durationBefore": 0,
      "description": "string",
      "isActive": true
    },
    "rentalPackageId": 0,
    "rentalPackageRentalPackageName": "string",
    "rentalPackageType": "string",
    "rentalPackagePrice": 0,
    "createdDate": "2024-11-06T09:01:33.839Z",
    "updatedDate": "2024-11-06T09:01:33.839Z"
  },
  "status": "string",
  "checkinDate": "2024-11-06",
  "checkoutDate": "2024-11-06",
  "primaryGuestName": "string",
  "primaryGuestPhone": "string",
  "primaryGuestEmail": "string",
  "isActive": true,
  "isFeedback": true,
  "renterFullLegalName": "string",
  "renterLegalPhone": "string",
  "renterLegalAvatar": "string",
  "serviceFee": 0,
  "totalPrice": 0,
  "totalNights": 0,
  "pricePerNights": 0,
  "createdDate": "2024-11-06T09:01:33.839Z",
  "updatedDate": "2024-11-06T09:01:33.839Z"
}
*/
data class MyBookingDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("rentalPosting") val rentalPosting: RentalPosting,
    @SerializedName("status") val status: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("primaryGuestName") val primaryGuestName: String,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isFeedback") val isFeedback: Boolean,
    @SerializedName("renterFullLegalName") val renterFullLegalName: String,
    @SerializedName("renterLegalPhone") val renterLegalPhone: String,
    @SerializedName("renterLegalAvatar") val renterLegalAvatar: String,
    @SerializedName("serviceFee") val serviceFee: Int,
    @SerializedName("totalPrice") val totalPrice: Int,
    @SerializedName("totalNights") val totalNights: Int,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String
) {
    data class RentalPosting(
        @SerializedName("id") val id: Int,
        @SerializedName("description") val description: String,
        @SerializedName("isVerify") val isVerify: Boolean,
        @SerializedName("isBookable") val isBookable: Boolean,
        @SerializedName("roomInfo") val roomInfo: RoomInfo,
        @SerializedName("cancellationType") val cancellationType: CancellationType,
        @SerializedName("rentalPackageId") val rentalPackageId: Int,
        @SerializedName("rentalPackageRentalPackageName") val rentalPackageRentalPackageName: String,
        @SerializedName("rentalPackageType") val rentalPackageType: String,
        @SerializedName("rentalPackagePrice") val rentalPackagePrice: Int,
        @SerializedName("createdDate") val createdDate: String,
        @SerializedName("updatedDate") val updatedDate: String
    ) {
        data class RoomInfo(
            @SerializedName("id") val id: Int,
            @SerializedName("roomInfoCode") val roomInfoCode: String,
            @SerializedName("roomInfoName") val roomInfoName: String,
            @SerializedName("createdAt") val createdAt: String,
            @SerializedName("updatedAt") val updatedAt: String,
            @SerializedName("isActive") val isActive: Boolean,
            @SerializedName("resortId") val resortId: Int,
            @SerializedName("status") val status: String,
            @SerializedName("unitTypeId") val unitTypeId: Int
        )

        data class CancellationType(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String,
            @SerializedName("refundRate") val refundRate: Int,
            @SerializedName("durationBefore") val durationBefore: Int,
            @SerializedName("description") val description: String,
            @SerializedName("isActive") val isActive: Boolean
        )
    }
}