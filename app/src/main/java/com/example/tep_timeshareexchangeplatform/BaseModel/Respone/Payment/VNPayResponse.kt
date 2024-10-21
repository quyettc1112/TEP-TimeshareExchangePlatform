package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment

import com.google.gson.annotations.SerializedName

data class VNPayResponse(
    @SerializedName("amount") val amount: Int,
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("transactionTime") val transactionTime: String,
    @SerializedName("orderDetail") val orderDetail: String,
    @SerializedName("walletTransactionId") val walletTransactionId: String
) {
}