package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel

enum class ExchangePackageEnum (val packageModel: PackageModel) {
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
    );

    companion object {
        fun getPackageById(id: Int): PackageModel? {
            return RentalPackageEnum.values().find { it.packageModel.id == id }?.packageModel
        }

        fun getPackageByName(name: String): PackageModel? {
            return RentalPackageEnum.values().find { it.packageModel.name == name }?.packageModel
        }
    }
}