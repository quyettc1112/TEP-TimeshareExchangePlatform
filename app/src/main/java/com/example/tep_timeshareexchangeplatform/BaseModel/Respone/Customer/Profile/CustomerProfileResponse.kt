package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "fullName": "string",
  "avatar": "string",
  "dob": "2024-11-18",
  "address": "string",
  "gender": "string",
  "phone": "string",
  "membershipId": 0,
  "membershipName": "string",
  "userId": 0,
  "userUserName": "string",
  "userEmail": "string",
  "isActive": true,
  "memberPurchaseDate": "2024-11-18",
  "memberExpiryDate": "2024-11-18",
  "isMember": true
}
*/
data class CustomerProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("membershipId") val membershipId: Int,
    @SerializedName("membershipName") val membershipName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userUserName") val userUserName: String,
    @SerializedName("userEmail") val userEmail: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("memberPurchaseDate") val memberPurchaseDate: String,
    @SerializedName("memberExpiryDate") val memberExpiryDate: String,
    @SerializedName("isMember") val isMember: Boolean,
    @SerializedName("walletId") val walletId: Int,
    @SerializedName("walletAvailableMoney") val walletAvailableMoney: Long,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(fullName)
        parcel.writeString(avatar)
        parcel.writeString(dob)
        parcel.writeString(address)
        parcel.writeString(gender)
        parcel.writeString(phone)
        parcel.writeInt(membershipId)
        parcel.writeString(membershipName)
        parcel.writeInt(userId)
        parcel.writeString(userUserName)
        parcel.writeString(userEmail)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(memberPurchaseDate)
        parcel.writeString(memberExpiryDate)
        parcel.writeByte(if (isMember) 1 else 0)
        parcel.writeInt(walletId)
        parcel.writeLong(walletAvailableMoney)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerProfileResponse> {
        override fun createFromParcel(parcel: Parcel): CustomerProfileResponse {
            return CustomerProfileResponse(parcel)
        }

        override fun newArray(size: Int): Array<CustomerProfileResponse?> {
            return arrayOfNulls(size)
        }
    }

}