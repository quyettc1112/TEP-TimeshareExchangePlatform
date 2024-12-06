package com.example.tep_timeshareexchangeplatform.Until.EmumClass

enum class ExchangeOption(val description: String, val id: Int, val actionDescription: String) {
    PAY_DIFFERENCE_TO_OWNER(
        "Bù tiền cho chủ sở hữu",
        1,
        "Bạn sẽ thanh toán số tiền chênh lệch giữa hai tài sản để hoàn tất giao dịch trao đổi. Số tiền sẽ được chuyển trực tiếp cho chủ sở hữu tài sản."
    ),
    OWNER_PAYS_DIFFERENCE(
        "Chủ sở hữu bù tiền",
        2,
        "Chủ sở hữu sẽ thanh toán số tiền chênh lệch giữa hai tài sản để hoàn tất giao dịch trao đổi. Số tiền này sẽ được chuyển đến tài khoản của bạn."
    ),
    NO_PAYMENT_NEEDED(
        "Không bù tiền",
        3,
        "Cả hai bên đều không cần thanh toán thêm bất kỳ khoản tiền nào. Giao dịch trao đổi sẽ được thực hiện trực tiếp mà không có chênh lệch tài chính."
    );

    // Lấy mô tả dựa trên ID
    companion object {
        fun fromId(id: Int): ExchangeOption? {
            return values().find { it.id == id }
        }
    }
}