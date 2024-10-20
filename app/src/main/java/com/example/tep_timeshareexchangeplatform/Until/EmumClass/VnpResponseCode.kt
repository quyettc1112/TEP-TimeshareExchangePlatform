package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.R

enum class VnpResponseCode(val code: String, val description: String) {
    SUCCESS("00", R.string.vnp_success.toString()),
    SUSPICIOUS_TRANSACTION("07", R.string.vnp_suspicious_transaction.toString()),
    INTERNET_BANKING_NOT_REGISTERED("09", R.string.vnp_internet_banking_not_registered.toString()),
    AUTHENTICATION_FAILED_3_TIMES("10", R.string.vnp_authentication_failed_3_times.toString()),
    PAYMENT_TIMEOUT("11", R.string.vnp_payment_timeout.toString()),
    ACCOUNT_BLOCKED("12", R.string.vnp_account_blocked.toString()),
    OTP_INCORRECT("13", R.string.vnp_otp_incorrect.toString()),
    UNSUCCESSFUL_TRANSACTION("24", R.string.vnp_unsuccessful_transaction.toString()),
    INSUFFICIENT_FUNDS("51", R.string.vnp_insufficient_funds.toString()),
    DAILY_LIMIT_EXCEEDED("65", R.string.vnp_daily_limit_exceeded.toString()),
    BANK_MAINTENANCE("75", R.string.vnp_bank_maintenance.toString()),
    TRANSACTION_PASSWORD_EXCEEDED("79", R.string.vnp_transaction_password_exceeded.toString()),
    OTHER_ERRORS("99", R.string.vnp_other_errors.toString());

    companion object {
        fun fromCode(code: String): VnpResponseCode? {
            return values().find { it.code == code }
        }
    }
}
