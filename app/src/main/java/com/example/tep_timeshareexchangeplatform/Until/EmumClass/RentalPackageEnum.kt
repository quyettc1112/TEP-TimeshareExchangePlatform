package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel

enum class RentalPackageEnum(val packageModel: PackageModel) {


    // Gói Membership
    MEMBERSHIP_MONTHLY(
        PackageModel(
            id = 1,
            name = "Gói Thành Viên 6 Tháng",
            price = 119000,
            description = "",
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
            name = "Gói Thành Viên 1 Năm",
            price = 239000,
            description = "",
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
            id = 1,
            name = "Gói Cơ Bản",
            price = 149000,
            description = "Unwind hỗ trợ quảng cáo và đưa người thuê đến với bạn.",
            duration = 2,  // Thời gian sử dụng là 1 tháng
            type = "Basic",
            listBenefit = listOf(
                "Thông báo qua mail khi có người thuê",
                "Gắn thẻ 'Bài mới' trong 30 ngày"
            )
        )
    ),

    ADVANCED_SERVICE(
        PackageModel(
            id = 2,
            name = "Gói Nâng Cao",
            price = 199000,
            description = "Unwind cung cấp hệ thống đặt chỗ trực tuyến.",
            duration = 2,  // Thời gian sử dụng là 1 tháng
            type = "Standard",
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
            id = 3,
            name = "Gói Premium",
            price = 239000,
            description = "Unwind hỗ trợ quảng cáo và quản lý toàn bộ quá trình cho thuê.",
            duration = 12,  // Thời gian sử dụng là 1 tháng
            type = "Premium",
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
            id = 4,
            name = "Gói Ủy Quyền",
            price = 599000,
            description = "Unwind hỗ trợ toàn bộ quá trình cho thuê và thanh toán.",
            duration = 12,  // Thời gian sử dụng là 1 tháng
            type = "VIP",
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

        fun getPackageEnumById(id: Int): RentalPackageEnum? {
            return values().find { it.packageModel.id == id }
        }
    }
}
