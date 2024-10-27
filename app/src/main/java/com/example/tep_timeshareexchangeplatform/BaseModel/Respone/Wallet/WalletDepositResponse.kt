package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "id": "396d31c8-c007-4f7b-9e80-df4b975be403",
  "walletId": 7,
  "money": 1000000,
  "transactionType": "DEPOSITMONEY",
  "description": "Nạp tiền vào ví",
  "paymentMethod": "VNPAY",
  "createdAt": "27-10-2024 09:47:54",
  "fee": 0
}
*/
data class WalletDepositResponse(
    @SerializedName("id") val id: String,
    @SerializedName("walletId") val walletId: Int,
    @SerializedName("money") val money: Int,
    @SerializedName("transactionType") val transactionType: String,
    @SerializedName("description") val description: String,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("fee") val fee: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(walletId)
        parcel.writeInt(money)
        parcel.writeString(transactionType)
        parcel.writeString(description)
        parcel.writeString(paymentMethod)
        parcel.writeString(createdAt)
        parcel.writeInt(fee)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WalletDepositResponse> {
        override fun createFromParcel(parcel: Parcel): WalletDepositResponse {
            return WalletDepositResponse(parcel)
        }

        override fun newArray(size: Int): Array<WalletDepositResponse?> {
            return arrayOfNulls(size)
        }
    }

}