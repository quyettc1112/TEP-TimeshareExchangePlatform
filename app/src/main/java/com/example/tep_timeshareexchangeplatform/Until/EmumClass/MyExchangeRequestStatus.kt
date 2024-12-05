package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R

enum class MyExchangeRequestStatus(val apiStatus: String, val statusResId: Int) {
    PENDING_OWNER("PendingOwner", R.string.pending_owner),
    PENDING_APPROVAL("PendingApproval", R.string.pending_approval),
    REJECT_APPROVAL("RejectApproval", R.string.reject_approval),
    COMPLETED("Complete", R.string.completed),
    PENDING_RENTER_PRICING("PendingRenterPricing", R.string.pending_renter_pricing),
    RENTER_REJECT("RenterReject", R.string.renter_reject),
    OWNER_REJECT("OwnerReject", R.string.owner_reject),
    PENDING_RENTER_PAYMENT("PendingRenterPayment", R.string.pending_renter_payment),
    PENDING_OWNER_PAYMENT("PendingOwnerPayment", R.string.pending_owner_payment);

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