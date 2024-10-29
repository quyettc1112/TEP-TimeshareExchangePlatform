package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R

enum class RefundPolicy(val id: Int) {
    FULL_REFUND(1),
    PARTIAL_REFUND(2),
    NO_REFUND(3);

    // Lấy mô tả ngắn gọn từ tài nguyên ngôn ngữ (strings.xml)
    fun getShortDescription(context: Context): String {
        return when (this) {
            FULL_REFUND -> context.getString(R.string.full_refund)
            PARTIAL_REFUND -> context.getString(R.string.partial_refund)
            NO_REFUND -> context.getString(R.string.no_refund)
        }
    }

    // Lấy mô tả chi tiết từ tài nguyên ngôn ngữ (strings.xml)
    fun getLongDescription(context: Context): String {
        return when (this) {
            FULL_REFUND -> context.getString(R.string.full_refund_long)
            PARTIAL_REFUND -> context.getString(R.string.partial_refund_long)
            NO_REFUND -> context.getString(R.string.no_refund_long)
        }
    }
}