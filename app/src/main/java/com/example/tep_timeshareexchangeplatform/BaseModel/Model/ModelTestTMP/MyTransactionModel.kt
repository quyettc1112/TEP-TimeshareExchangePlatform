package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

data class MyTransactionModel(
    val transactionID: String,   // 123456789
    val recipientName: String,   // LE THI UYEN
    val paymentMethod: String,        // Techcombank
    val transactionType: String, // Chuyển tiền/Thanh toán
    val transactionTime: String, // 08:53 - 13/10/2024
    val walletBalance: String,   // ****** (Số dư ví: ******)
    val transactionAmount: String, // -93.000
    val type: Int
)
