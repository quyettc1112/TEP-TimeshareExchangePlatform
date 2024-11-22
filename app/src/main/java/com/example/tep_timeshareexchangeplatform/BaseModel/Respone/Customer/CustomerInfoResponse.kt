package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "id": 31,
  "fullName": "string",
  "memberExpiryDate": "22-10-2032",
  "membershipName": "membership_package_2",
  "userId": 42,
  "userUserName": null,
  "userRoleRoleName": "CUSTOMER",
  "isActive": true,
  "walletId": 7,
  "walletAvailableMoney": 30764000,
  "isMember": true
}
*/
data class CustomerInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("memberExpiryDate") val memberExpiryDate: String,
    @SerializedName("membershipName") val membershipName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userUserName") val userUserName: String?,
    @SerializedName("userRoleRoleName") val userRoleRoleName: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("walletId") val walletId: Int,
    @SerializedName("walletAvailableMoney") val walletAvailableMoney: Int,
    @SerializedName("isMember") val isMember: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(fullName)
        parcel.writeString(memberExpiryDate)
        parcel.writeString(membershipName)
        parcel.writeInt(userId)
        parcel.writeString(userRoleRoleName)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeInt(walletId)
        parcel.writeInt(walletAvailableMoney)
        parcel.writeByte(if (isMember) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerInfoResponse> {
        override fun createFromParcel(parcel: Parcel): CustomerInfoResponse {
            return CustomerInfoResponse(parcel)
        }

        override fun newArray(size: Int): Array<CustomerInfoResponse?> {
            return arrayOfNulls(size)
        }
    }

}