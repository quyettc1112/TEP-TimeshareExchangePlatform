package com.example.tep_timeshareexchangeplatform.Until.EmumClass

enum class NotificationType (val notificationType: String) {
    NOTIFICATION("Thông báo chung ·"),
    DEPOSIT("Nạp tiền vào Unwind Wallet ·"),
    REJECT_POSTING("Bài Đăng Bị Từ Chối ·"),
    ACCEPT_POSTING("Bài Đăng Được Chấp Nhận ·"),
    DONE_BOOKING("Đặt Phòng Thành Công ·"),
    MEMBERSHIP("Gia Hạn Gói Thành Viên Thành Công ·")

}