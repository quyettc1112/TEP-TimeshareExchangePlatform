package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType

data class NotificationModel(
    val title: String,               // Tiêu đề chính
    val typeNotification: NotificationType,            // Phụ đề (thời gian)
    val description: String,         // Nội dung mô tả
    val iconResId: Int,              // ID của icon
    val timestamp: String,
    val isRead: Boolean
)
