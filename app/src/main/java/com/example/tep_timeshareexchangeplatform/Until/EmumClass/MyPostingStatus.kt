package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R

enum class MyPostingStatus(val apiStatus: String, val statusResId: Int) {
    PENDING_APPROVAL("PendingApproval", R.string.pending_approval),
    AWAITING_CONFIRMATION("AwaitingConfirmation", R.string.waiting_confirmation),
    PROCESSING("Processing", R.string.processing),
    COMPLETED("Completed", R.string.completed),
    REJECTED("Reject", R.string.rejected),
    REJECT_PRICE("RejectPrice", R.string.reject_price),
    PENDING_PRICING("PendingPricing", R.string.pending_pricing),
    CLOSED("Closed", R.string.closed);

    companion object {
        fun fromApiStatus(apiStatus: String): MyPostingStatus? {
            return values().find { it.apiStatus.equals(apiStatus, ignoreCase = true) }
        }
    }

    // Get localized description from strings.xml
    fun getDescription(context: Context): String {
        return context.getString(statusResId)
    }
}