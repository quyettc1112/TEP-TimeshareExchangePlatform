package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "description": "string",
  "nights": 0,
  "pricePerNights": 0,
  "timeshareId": 0,
  "cancellationTypeId": 0,
  "checkinDate": "2024-10-29",
  "checkoutDate": "2024-10-29",
  "rentalPackageId": 0
}
*/
data class PostingTimeshareDTO(
    @SerializedName("description") val description: String,
    @SerializedName("nights") val nights: Int,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("timeshareId") val timeshareId: Int,
    @SerializedName("cancellationTypeId") val cancellationTypeId: Int,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("rentalPackageId") val rentalPackageId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeInt(nights)
        parcel.writeInt(pricePerNights)
        parcel.writeInt(timeshareId)
        parcel.writeInt(cancellationTypeId)
        parcel.writeString(checkinDate)
        parcel.writeString(checkoutDate)
        parcel.writeInt(rentalPackageId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostingTimeshareDTO> {
        override fun createFromParcel(parcel: Parcel): PostingTimeshareDTO {
            return PostingTimeshareDTO(parcel)
        }

        override fun newArray(size: Int): Array<PostingTimeshareDTO?> {
            return arrayOfNulls(size)
        }
    }


}