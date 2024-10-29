package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PostingTimeshare


import com.google.gson.annotations.SerializedName

/**
{
  "id": 23,
  "description": "string",
  "nights": 13,
  "pricePerNights": 0,
  "isVerify": false,
  "isBookable": false,
  "timeshareId": 1,
  "roomInfoId": 2,
  "cancellationTypeId": 1,
  "cancellationTypeName": "Flexible",
  "checkinDate": "29-10-2024",
  "checkoutDate": "29-10-2024",
  "expiredDate": "28-12-2024",
  "status": "PendingApproval",
  "staffRefinementPrice": null,
  "isActive": true,
  "rentalPackageId": 2,
  "rentalPackageRentalPackageName": "Gói nâng cao ",
  "ownerId": 31,
  "note": null,
  "createdDate": "29-10-2024 21::37:24",
  "updatedDate": "29-10-2024 21::37:24",
  "priceValuation": null
}
*/
data class PostingTimeshareResponse(
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
    @SerializedName("staffRefinementPrice") val staffRefinementPrice: Any?,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("rentalPackageRentalPackageName") val rentalPackageRentalPackageName: String,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("note") val note: Any?,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("updatedDate") val updatedDate: String,
    @SerializedName("priceValuation") val priceValuation: Any?
)