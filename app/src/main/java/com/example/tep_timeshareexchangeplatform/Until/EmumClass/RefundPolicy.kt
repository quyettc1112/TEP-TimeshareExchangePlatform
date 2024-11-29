package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R
import kotlin.time.Duration

enum class RefundPolicy(val id: Int, val duration: Int) {
    FULL_REFUND(1, 60),
    PARTIAL_REFUND(2, 60),
    NO_REFUND(3, 0);

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

    companion object {
        // Map JSON names to short description from strings.xml
        fun getShortDescriptionFromName(context: Context, name: String): String {
            return when (name) {
                "Flexible" -> context.getString(R.string.full_refund)
                "Moderate" -> context.getString(R.string.partial_refund)
                "Strict" -> context.getString(R.string.no_refund)
                else -> throw IllegalArgumentException("Unknown refund policy name: $name")
            }
        }

        fun getRefundPolicyById(id: Int): RefundPolicy? {
            return values().find { it.id == id }
        }
    }
}