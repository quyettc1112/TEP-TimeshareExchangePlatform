package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R

enum class MyExchangeRequestStatus(val apiStatus: String, val statusResId: Int) {
    PENDING_APPROVAL("PendingApproval", R.string.pending_approval),
    PENDING_CUSTOMER("PendingCustomer", R.string.pending_customer),
    COMPLETED("Complete", R.string.completed),
    REJECTED("Reject", R.string.rejected);

    companion object {
        fun fromApiStatus(apiStatus: String): MyExchangeRequestStatus? {
            return values().find { it.apiStatus.equals(apiStatus, ignoreCase = true) }
        }
    }

    // Get localized description from strings.xml
    fun getDescription(context: Context): String {
        return context.getString(statusResId)
    }
}