package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

import com.google.common.base.Verify

data class MyPosting(
    val name: String,               // Tên resort (Flamingo Đại Lải)
    val location: String,           // Địa điểm (Đại Lải, Phúc Yên, Việt Nam)
    val stayDates: String,          // Ngày ở (26/08/2024 - 30/08/2024)
    val priceRange: String,         // Giá phòng (500.000 - 1,300,000 VND)
    val numberOfNights: String,     // Số đêm (4 đêm)
    val rating: Float,              // Đánh giá (4.5)
    val numberOfReviews: Int,       // Số lượng đánh giá (213 đánh giá)
    val packageName: String,        // Tên gói đăng bài (Gói Nâng Cao - 2)
    val packageDuration: String,    // Thời hạn gói (11/09/2024 - 11/03/2025)
    val durationMonths: String,     // Số tháng của gói (6 Tháng)
    val isVerify: Boolean
)
