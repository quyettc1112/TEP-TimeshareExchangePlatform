package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

data class MyPostingModel(
    val id: Int,
    val name: String,
    val roomName: String,
    val location: String,
    val stayDates: String,
    val priceRange: String, // Số đêm (4 đêm)
    val packageName: String,        // Tên gói đăng bài (Gói Nâng Cao - 2)
    val packageDuration: String,    // Thời hạn gói (11/09/2024 - 11/03/2025)
    val isVerify: Boolean,
    val isPriceDemand: Boolean
)
