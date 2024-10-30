package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "id": "8856edd6-66ec-49f0-b041-c5ec3b419ad3",
  "walletId": 7,
  "money": -199000,
  "transactionType": "RENTALPOSTING",
  "description": "Thanh toán đăng bài Gói nâng cao ",
  "paymentMethod": "WALLET",
  "createdAt": "29-10-2024 23:59:58",
  "fee": 0
}
*/
data class WalletPurchaseResponse(
    @SerializedName("id") val id: String,
    @SerializedName("walletId") val walletId: Int,
    @SerializedName("money") val money: Int,
    @SerializedName("transactionType") val transactionType: String,
    @SerializedName("description") val description: String,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("fee") val fee: Int
): Parcelable{
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

    companion object CREATOR : Parcelable.Creator<WalletPurchaseResponse> {
        override fun createFromParcel(parcel: Parcel): WalletPurchaseResponse {
            return WalletPurchaseResponse(parcel)
        }

        override fun newArray(size: Int): Array<WalletPurchaseResponse?> {
            return arrayOfNulls(size)
        }
    }

}