package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.R

enum class AmenityDB(val model: AmenitiesModel, val imageResId: Int) {
    // KITCHEN
    COFFEE_MACHINE(AmenitiesModel("Máy pha cà phê", "Bếp", false), R.drawable.ic_air_conditioner),
    MICROWAVE(AmenitiesModel("Lò vi sóng", "Bếp", false), R.drawable.ic_air_conditioner),
    DISHWASHER(AmenitiesModel("Máy rửa chén", "Bếp", false), R.drawable.ic_air_conditioner),
    TOASTER(AmenitiesModel("Máy nướng bánh mì", "Bếp", false), R.drawable.ic_air_conditioner),
    LARGE_FRIDGE(AmenitiesModel("Tủ lạnh (lớn)", "Bếp", false), R.drawable.ic_air_conditioner),
    SMALL_FRIDGE(AmenitiesModel("Tủ lạnh (nhỏ)", "Bếp", false), R.drawable.ic_air_conditioner),
    STOVE(AmenitiesModel("Bếp lò", "Bếp", false), R.drawable.ic_air_conditioner),

    // ENTERTAINMENT
    DVD_PLAYER(AmenitiesModel("Máy phát DVD", "Giải Trí", false), R.drawable.ic_air_conditioner),
    BAR(AmenitiesModel("Quầy Bar", "Giải Trí", false), R.drawable.ic_air_conditioner),
    PROJECTOR(AmenitiesModel("Máy Chiếu Phim", "Giải Trí", false), R.drawable.ic_air_conditioner),
    INTERNET(AmenitiesModel("Mạng Internet", "Giải Trí", false), R.drawable.ic_internet),
    RADIO(AmenitiesModel("Radio", "Giải Trí", false), R.drawable.ic_air_conditioner),
    SMART_TV(AmenitiesModel("TV thông minh", "Giải Trí", false), R.drawable.ic_air_conditioner),
    TELEPHONE(AmenitiesModel("Điện thoại bàn", "Giải Trí", false), R.drawable.ic_air_conditioner),

    // FEATURES
    AIR_CONDITIONER(AmenitiesModel("Máy Điều Hòa", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    WIFI(AmenitiesModel("Wifi", "Tiện Nghi", false), R.drawable.ic_wifi),
    HOT_COLD_WATER(AmenitiesModel("Nước nóng/lạnh", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    FREE_WATER(AmenitiesModel("Nước uống miễn phí", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    BALCONY(AmenitiesModel("Sân hiên hoặc Ban Công", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    DINING_TABLE(AmenitiesModel("Bàn ăn", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    WORK_DESK(AmenitiesModel("Bàn làm việc", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    WASHER_DRYER(AmenitiesModel("Máy giặt và máy sấy (trong căn hộ)", "Tiện Nghi", false), R.drawable.ic_air_conditioner),

    // POLICY
    NO_SMOKING(AmenitiesModel("Không hút thuốc", "Chính Sách", false), R.drawable.ic_no_smooking),
    NO_PETS(AmenitiesModel("Không thú cưng", "Chính Sách", false), R.drawable.ic_no_pet),
    NO_PARTY(AmenitiesModel("Không tổ chức tiệc", "Chính Sách", false), R.drawable.ic_air_conditioner),
    MIN_AGE(AmenitiesModel("Độ tuổi tối thiểu để nhận phòng: 18", "Chính Sách", false), R.drawable.ic_air_conditioner);
}