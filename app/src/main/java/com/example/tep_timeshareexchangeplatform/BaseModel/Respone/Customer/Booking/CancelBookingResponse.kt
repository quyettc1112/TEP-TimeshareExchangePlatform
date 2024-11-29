package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CancelBookingResponse(
    @SerializedName("checkinDate") val checkinDate: String,
    @SerializedName("checkoutDate") val checkoutDate: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("id") val id: Int,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isFeedback") val isFeedback: Boolean,
    @SerializedName("pricePerNights") val pricePerNights: Int,
    @SerializedName("primaryGuestEmail") val primaryGuestEmail: String,
    @SerializedName("primaryGuestName") val primaryGuestName: String,
    @SerializedName("primaryGuestPhone") val primaryGuestPhone: String,
    @SerializedName("renterFullLegalName") val renterFullLegalName: String,
    @SerializedName("renterLegalAvatar") val renterLegalAvatar: String,
    @SerializedName("renterLegalPhone") val renterLegalPhone: String,
    @SerializedName("serviceFee") val serviceFee: Int,
    @SerializedName("source") val source: String,
    @SerializedName("status") val status: String,
    @SerializedName("totalNights") val totalNights: Int,
    @SerializedName("totalPrice") val totalPrice: Int,
    @SerializedName("updatedDate") val updatedDate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checkinDate)
        parcel.writeString(checkoutDate)
        parcel.writeString(createdDate)
        parcel.writeInt(id)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isFeedback) 1 else 0)
        parcel.writeInt(pricePerNights)
        parcel.writeString(primaryGuestEmail)
        parcel.writeString(primaryGuestName)
        parcel.writeString(primaryGuestPhone)
        parcel.writeString(renterFullLegalName)
        parcel.writeString(renterLegalAvatar)
        parcel.writeString(renterLegalPhone)
        parcel.writeInt(serviceFee)
        parcel.writeString(source)
        parcel.writeString(status)
        parcel.writeInt(totalNights)
        parcel.writeInt(totalPrice)
        parcel.writeString(updatedDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CancelBookingResponse> {
        override fun createFromParcel(parcel: Parcel): CancelBookingResponse {
            return CancelBookingResponse(parcel)
        }

        override fun newArray(size: Int): Array<CancelBookingResponse?> {
            return arrayOfNulls(size)
        }
    }

}