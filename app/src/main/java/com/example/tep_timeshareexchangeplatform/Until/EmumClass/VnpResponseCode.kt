package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R

enum class VnpResponseCode(val code: String, val descriptionResId: Int) {
    SUCCESS("00", R.string.vnp_success),
    SUSPICIOUS_TRANSACTION("07", R.string.vnp_suspicious_transaction),
    INTERNET_BANKING_NOT_REGISTERED("09", R.string.vnp_internet_banking_not_registered),
    AUTHENTICATION_FAILED_3_TIMES("10", R.string.vnp_authentication_failed_3_times),
    PAYMENT_TIMEOUT("11", R.string.vnp_payment_timeout),
    ACCOUNT_BLOCKED("12", R.string.vnp_account_blocked),
    OTP_INCORRECT("13", R.string.vnp_otp_incorrect),
    UNSUCCESSFUL_TRANSACTION("24", R.string.vnp_unsuccessful_transaction),
    INSUFFICIENT_FUNDS("51", R.string.vnp_insufficient_funds),
    DAILY_LIMIT_EXCEEDED("65", R.string.vnp_daily_limit_exceeded),
    BANK_MAINTENANCE("75", R.string.vnp_bank_maintenance),
    TRANSACTION_PASSWORD_EXCEEDED("79", R.string.vnp_transaction_password_exceeded),
    OTHER_ERRORS("99", R.string.vnp_other_errors);

    // Hàm lấy chuỗi từ descriptionResId bằng cách truyền vào Context
    fun getString(context: Context): String {
        return context.getString(descriptionResId)
    }

    companion object {
        fun fromCode(code: String): VnpResponseCode? {
            return values().find { it.code == code }
        }
    }
}