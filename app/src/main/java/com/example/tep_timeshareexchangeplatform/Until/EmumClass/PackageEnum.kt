package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel

enum class PackageEnum(val packageModel: PackageModel) {


    // Gói Membership
    MEMBERSHIP_MONTHLY(
        PackageModel(
            id = 1,
            name = "Gói 6 Tháng",
            price = 119000,
            description = "Gói trao đổi timeshare trong 1 tháng.",
            duration = 6,  // Thời gian sử dụng là 1 tháng
            type = "Membership",
            listBenefit = listOf(
                "Đặt phòng nhanh chóng",
                "Giảm giá 10% cho lần đặt phòng tiếp theo",
                "Miễn phí hủy phòng",
                "Hỗ trợ 24/7"
            )
        )
    ),

    MEMBERSHIP_YEARLY(
        PackageModel(
            id = 2,
            name = "Gói Năm",
            price = 239000,
            description = "Gói trao đổi timeshare trong 1 năm.",
            duration = 12,  // Thời gian sử dụng là 1 năm
            type = "Membership",
            listBenefit = listOf(
                "Đặt phòng nhanh chóng",
                "Giảm giá 10% cho lần đặt phòng tiếp theo",
                "Miễn phí hủy phòng",
                "Hỗ trợ 24/7",
                "Đặt phòng nhanh chóng thêm"
            )
        )
    ),

    // Gói Dịch Vụ
    BASIC_SERVICE(
        PackageModel(
            id = 3,
            name = "Gói Cơ Bản",
            price = 149000,
            description = "Unwind hỗ trợ quảng cáo và đưa người thuê đến với bạn.",
            duration = 1,  // Thời gian sử dụng là 1 tháng
            type = "Service",
            listBenefit = listOf(
                "Thông báo qua mail khi có người thuê",
                "Gắn thẻ 'Bài mới' trong 30 ngày"
            )
        )
    ),

    ADVANCED_SERVICE(
        PackageModel(
            id = 4,
            name = "Gói Nâng Cao",
            price = 179000,
            description = "Unwind cung cấp hệ thống đặt chỗ trực tuyến.",
            duration = 1,  // Thời gian sử dụng là 1 tháng
            type = "Service",
            listBenefit = listOf(
                "Thông báo qua mail khi có người thuê",
                "Gắn thẻ 'Bài mới' trong 30 ngày",
                "Gắn cờ 'Được xác minh' của Unwind",
                "Được xác minh bởi nhân viên của Resort",
                "Cho thuê trực tuyến"
            )
        )
    ),

    PREMIUM_SERVICE(
        PackageModel(
            id = 5,
            name = "Gói Premium",
            price = 199000,
            description = "Unwind hỗ trợ quảng cáo và quản lý toàn bộ quá trình cho thuê.",
            duration = 1,  // Thời gian sử dụng là 1 tháng
            type = "Service",
            listBenefit = listOf(
                "Thông báo qua mail khi có người thuê",
                "Gắn thẻ 'Bài mới' trong 30 ngày",
                "Gắn cờ 'Được xác minh' của Unwind",
                "Được xác minh bởi nhân viên của Resort",
                "Cho thuê trực tuyến",
                "Hỗ trợ định giá",
                "Hỗ trợ quản lý phòng và liên lạc"
            )
        )
    ),

    DELEGATED_SERVICE(
        PackageModel(
            id = 6,
            name = "Gói Ủy Quyền",
            price = 599000,
            description = "Unwind hỗ trợ toàn bộ quá trình cho thuê và thanh toán.",
            duration = 1,  // Thời gian sử dụng là 1 tháng
            type = "Service",
            listBenefit = listOf(
                "Thông báo qua mail khi có người thuê",
                "Gắn thẻ 'Bài mới' trong 30 ngày",
                "Gắn cờ 'Được xác minh' của Unwind"
            )
        )
    );

    companion object {
        fun getPackageById(id: Int): PackageModel? {
            return values().find { it.packageModel.id == id }?.packageModel
        }

        fun getPackageByName(name: String): PackageModel? {
            return values().find { it.packageModel.name == name }?.packageModel
        }
    }
}
