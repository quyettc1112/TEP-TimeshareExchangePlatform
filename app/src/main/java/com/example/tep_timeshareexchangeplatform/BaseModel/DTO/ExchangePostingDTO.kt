package com.example.tep_timeshareexchangeplatform.BaseModel.DTO


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "description": "string",
  "nights": 0,
  "exchangePackageId": 0,
  "checkinDate": "2024-11-15",
  "checkoutDate": "2024-11-15",
  "timeshareId": 0,
  "imageUrls": [
    "string"
  ]
}
*/
data class ExchangePostingDTO(
    @SerializedName("description") val description: String,
    @SerializedName("nights") val nights: Int,
    @SerializedName("exchangePackageId") val exchangePackageId: Int,
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("timeshareId") val timeshareId: Int,
    @SerializedName("preferCheckinDate") val preferCheckinDate: String,
    @SerializedName("preferCheckoutDate") val preferCheckoutDate: String,
    @SerializedName("preferLocation") val preferLocation: String,
    @SerializedName("imageUrls") val imageUrls: List<String>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeInt(nights)
        parcel.writeInt(exchangePackageId)
        parcel.writeString(checkinDate)
        parcel.writeString(checkoutDate)
        parcel.writeInt(timeshareId)
        parcel.writeString(preferCheckinDate)
        parcel.writeString(preferCheckoutDate)
        parcel.writeString(preferLocation)
        parcel.writeStringList(imageUrls)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExchangePostingDTO> {
        override fun createFromParcel(parcel: Parcel): ExchangePostingDTO {
            return ExchangePostingDTO(parcel)
        }

        override fun newArray(size: Int): Array<ExchangePostingDTO?> {
            return arrayOfNulls(size)
        }
    }

}