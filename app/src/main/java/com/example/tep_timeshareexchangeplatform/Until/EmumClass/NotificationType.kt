package com.example.tep_timeshareexchangeplatform.Until.EmumClass

enum class NotificationType(val notificationType: String) {
    RENTAL_POSTING("RentalPosting"),
    EXCHANGE_POSTING("ExchangePosting"),
    RENTAL_BOOKING("RentalBooking"),
    EXCHANGE_BOOKING("ExchangeBooking"),
    EXCHANGE_REQUEST("ExchangeRequest");

    companion object {
        // Hàm để tìm enum từ key
        fun fromKey(key: String): NotificationType? {
            return values().find { it.notificationType == key }
        }
    }

}