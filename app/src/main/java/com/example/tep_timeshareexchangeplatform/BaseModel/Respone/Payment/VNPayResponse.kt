package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment

data class VNPayResponse(
    val amount: Int,
    val responseCode: String,
    val transactionTime: String,
    val orderDetail: String
) {
}