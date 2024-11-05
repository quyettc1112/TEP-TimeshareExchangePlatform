package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "description": "string",
  "nights": 0,
  "pricePerNights": 0,
  "isVerify": true,
  "isBookable": true,
  "timeshareId": 0,
  "roomInfoId": 0,
  "cancellationTypeId": 0,
  "cancellationTypeName": "string",
  "checkinDate": "2024-11-05",
  "checkoutDate": "2024-11-05",
  "expiredDate": "2024-11-05",
  "status": "string",
  "staffRefinementPrice": 0,
  "isActive": true,
  "rentalPackageId": 0,
  "rentalPackageRentalPackageName": "string",
  "ownerId": 0,
  "note": "string",
  "createdDate": "2024-11-05T16:12:48.106Z",
  "updatedDate": "2024-11-05T16:12:48.106Z",
  "priceValuation": 0
}
*/
data class PricingSupportResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("nights") val nights: Int,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("isVerify") val isVerify: Boolean,
    @SerializedName("isBookable") val isBookable: Boolean,
    @SerializedName("timeshareId") val timeshareId: Int,
    @SerializedName("roomInfoId") val roomInfoId: Int,
    @SerializedName("cancellationTypeId") val cancellationTypeId: Int,
    @SerializedName("cancellationTypeName") val cancellationTypeName: String,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("expiredDate") val expiredDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("staffRefinementPrice") val staffRefinementPrice: Int,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("rentalPackageRentalPackageName") val rentalPackageRentalPackageName: String,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("note") val note: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("priceValuation") val priceValuation: Int
)