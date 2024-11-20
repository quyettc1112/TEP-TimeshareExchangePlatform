package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.R

enum class ResortAmenityDB(val model: AmenitiesModel, val imageResId: Int) {
    OUTDOOR_POOL(AmenitiesModel("Hồ bơi ngoài trời", "Tiện Nghi Resort", false), R.drawable.ic_outdoor_pool),
    PLAYGROUND(AmenitiesModel("Khu vui chơi", "Tiện Nghi Resort", false), R.drawable.ic_play_ground),
    RESTAURANT(AmenitiesModel("Nhà hàng", "Tiện Nghi Resort", false), R.drawable.ic_restaurant),
    SAUNA(AmenitiesModel("Phòng xông hơi", "Tiện Nghi Resort", false), R.drawable.ic_sauna),
    SECURITY(AmenitiesModel("An ninh", "Tiện Nghi Resort", false), R.drawable.ic_security),
    SHARED_LAUNDRY(AmenitiesModel("Giặt là chung", "Tiện Nghi Resort", false), R.drawable.ic_shared_laundry),
    SHOPPING_AREA(AmenitiesModel("Khu mua sắm", "Tiện Nghi Resort", false), R.drawable.ic_shopping),
    SPA_SERVICES(AmenitiesModel("Dịch vụ Spa", "Tiện Nghi Resort", false), R.drawable.ic_spa),
    TENNIS(AmenitiesModel("Sân tennis", "Tiện Nghi Resort", false), R.drawable.ic_tennis),


    // POLICY
    NO_SMOKING(AmenitiesModel("Không hút thuốc", "Chính Sách Resort", false), R.drawable.ic_no_smooking),
    NO_PETS(AmenitiesModel("Không thú cưng", "Chính Sách Resort", false), R.drawable.ic_no_pet),
    MIN_AGE(AmenitiesModel("Độ tuổi tối thiểu: 18", "Chính Sách Resort", false), R.drawable.ic_under_18),

    // NEARBY ATTRACTIONS
    BEACH(AmenitiesModel("Bãi biển", "Điểm Tham Quan Resort", false), R.drawable.ic_beach),
    BOATING(AmenitiesModel("Đi thuyền", "Điểm Tham Quan", false), R.drawable.ic_boating),
    DRUG_STORE(AmenitiesModel("Nhà thuốc", "Điểm Tham Quan", false), R.drawable.ic_drug_strore),
    FISHING(AmenitiesModel("Câu cá", "Điểm Tham Quan", false), R.drawable.ic_fishing),
    GOLF(AmenitiesModel("Sân golf", "Điểm Tham Quan", false), R.drawable.ic_golf),
    MEDICAL_FACILITIES(AmenitiesModel("Cơ sở y tế", "Điểm Tham Quan", false), R.drawable.ic_medical),
    SCUBA_DIVING(AmenitiesModel("Lặn biển", "Điểm Tham Quan", false), R.drawable.ic_scuba),
    WATER_SKIING(AmenitiesModel("Trượt nước", "Điểm Tham Quan", false), R.drawable.ic_water_skiing),
    WIND_SURFING(AmenitiesModel("Lướt ván buồm", "Điểm Tham Quan", false), R.drawable.ic_wind_surfing);
}
