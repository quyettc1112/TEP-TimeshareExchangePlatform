package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
  "customerId": 31,
  "walletTransactionDto": {
    "id": "202459bc-aec6-4b45-be06-3a8179ac1563",
    "walletId": 7,
    "money": -119000,
    "transactionType": "MEMBERSHIP",
    "description": "Thanh toán membership 6 tháng",
    "paymentMethod": "VNPAY",
    "createdAt": "21-10-2024 18:43:51",
    "fee": 0
  }
}
*/

data class MemberShipResponse(
    @SerializedName("customerId") val customerId: Int,
    @SerializedName("walletTransactionDto") val walletTransactionDto: WalletTransactionDto
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(WalletTransactionDto::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(customerId)
        parcel.writeParcelable(walletTransactionDto, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemberShipResponse> {
        override fun createFromParcel(parcel: Parcel): MemberShipResponse {
            return MemberShipResponse(parcel)
        }

        override fun newArray(size: Int): Array<MemberShipResponse?> {
            return arrayOfNulls(size)
        }
    }

    data class WalletTransactionDto(
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
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt()
        )

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

        companion object CREATOR : Parcelable.Creator<WalletTransactionDto> {
            override fun createFromParcel(parcel: Parcel): WalletTransactionDto {
                return WalletTransactionDto(parcel)
            }

            override fun newArray(size: Int): Array<WalletTransactionDto?> {
                return arrayOfNulls(size)
            }
        }
    }
}