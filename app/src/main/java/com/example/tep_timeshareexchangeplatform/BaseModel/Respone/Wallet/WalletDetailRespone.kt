package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet


import com.google.gson.annotations.SerializedName

/**
{
  "id": "6905f134-92e3-4940-9be7-16b9d4696ce0",
  "walletId": null,
  "money": -124121,
  "transactionType": null,
  "description": null,
  "paymentMethod": "VNPAY",
  "createdAt": "22-10-2024 04:59:25",
  "fee": 0
}
*/
data class WalletDetailRespone(
    @SerializedName("id") val id: String,
    @SerializedName("walletId") val walletId: Any?,
    @SerializedName("money") val money: Int,
    @SerializedName("transactionType") val transactionType: Any?,
    @SerializedName("description") val description: Any?,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("fee") val fee: Int
)