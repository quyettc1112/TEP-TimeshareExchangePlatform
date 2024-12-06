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
data class RentalPostingDTO(
    @SerializedName("description") val description: String,
    @SerializedName("nights") val nights: Int,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("timeshareId") val timeshareId: Int,
    @SerializedName("cancellationTypeId") val cancellationTypeId: Int,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("rentalPackageId") val rentalPackageId: Int,
    @SerializedName("imageUrls") val imageUrls: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createStringArrayList() ?: emptyList()
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
        parcel.writeStringList(imageUrls)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RentalPostingDTO> {
        override fun createFromParcel(parcel: Parcel): RentalPostingDTO {
            return RentalPostingDTO(parcel)
        }

        override fun newArray(size: Int): Array<RentalPostingDTO?> {
            return arrayOfNulls(size)
        }
    }


}