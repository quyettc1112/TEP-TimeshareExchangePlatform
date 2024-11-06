package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R

enum class MyBookingStatus(val apiStatus: String, val statusResId: Int) {
    BOOKED("Booked", R.string.booked),
    CHECK_IN("Checkin", R.string.checkin),
    CHECKOUT("Checkout", R.string.checkout),
    NO_SHOW("NoShow", R.string.no_show),
    CANCELED("Canceled", R.string.canceled),
    REFUND("Refund", R.string.refund),
    PAYMENT_COMPLETED("PaymentCompleted", R.string.payment_completed);

    companion object {
        fun fromApiStatus(apiStatus: String): MyBookingStatus? {
            return values().find { it.apiStatus.equals(apiStatus, ignoreCase = true) }
        }
    }

    // Get localized description from strings.xml
    fun getDescription(context: Context): String {
        return context.getString(statusResId)
    }
}