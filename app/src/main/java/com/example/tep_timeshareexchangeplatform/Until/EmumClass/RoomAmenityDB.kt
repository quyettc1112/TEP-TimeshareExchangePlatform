package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.R

enum class RoomAmenityDB(val model: AmenitiesModel, val imageResId: Int) {
    // KITCHEN
    COFFEE_MACHINE(AmenitiesModel("Máy pha cà phê", "Bếp", false), R.drawable.ic_coffe_machine),
    MICROWAVE(AmenitiesModel("Lò vi sóng", "Bếp", false), R.drawable.ic_microwave),
    DISHWASHER(AmenitiesModel("Máy rửa chén", "Bếp", false), R.drawable.ic_dish_washer),
    TOASTER(AmenitiesModel("Máy nướng bánh mì", "Bếp", false), R.drawable.ic_tosater),
    LARGE_FRIDGE(AmenitiesModel("Tủ lạnh (lớn)", "Bếp", false), R.drawable.ic_load_fride),
    SMALL_FRIDGE(AmenitiesModel("Tủ lạnh (nhỏ)", "Bếp", false), R.drawable.ic_load_fride),
    STOVE(AmenitiesModel("Bếp lò", "Bếp", false), R.drawable.ic_stove),

    // ENTERTAINMENT
    DVD_PLAYER(AmenitiesModel("Máy phát DVD", "Giải Trí", false), R.drawable.ic_dvd),
    BAR(AmenitiesModel("Quầy Bar", "Giải Trí", false), R.drawable.ic_bar),
    PROJECTOR(AmenitiesModel("Máy Chiếu Phim", "Giải Trí", false), R.drawable.ic_projecter),
    INTERNET(AmenitiesModel("Mạng LAN Internet", "Giải Trí", false), R.drawable.ic_internet),
    RADIO(AmenitiesModel("Radio", "Giải Trí", false), R.drawable.ic_radio),
    SMART_TV(AmenitiesModel("TV thông minh", "Giải Trí", false), R.drawable.ic_tv),
    TELEPHONE(AmenitiesModel("Điện thoại bàn", "Giải Trí", false), R.drawable.ic_tele_phone),

    // FEATURES
    AIR_CONDITIONER(AmenitiesModel("Máy Điều Hòa", "Tiện Nghi", false), R.drawable.ic_air_conditioner),
    WIFI(AmenitiesModel("Wifi", "Tiện Nghi", false), R.drawable.ic_wifi_d),
    HOT_COLD_WATER(AmenitiesModel("Nước nóng/lạnh", "Tiện Nghi", false), R.drawable.ic_hot_cold_water),
    FREE_WATER(AmenitiesModel("Nước uống miễn phí", "Tiện Nghi", false), R.drawable.ic_free_water),
    BALCONY(AmenitiesModel("Sân hiên hoặc Ban Công", "Tiện Nghi", false), R.drawable.ic_bacony),
    DINING_TABLE(AmenitiesModel("Bàn ăn", "Tiện Nghi", false), R.drawable.ic_table),
    WORK_DESK(AmenitiesModel("Bàn làm việc", "Tiện Nghi", false), R.drawable.ic_work_desk),
    WASHER_DRYER(AmenitiesModel("Máy giặt và máy sấy (trong căn hộ)", "Tiện Nghi", false), R.drawable.ic_dryer),

    // POLICY
    NO_SMOKING(AmenitiesModel("Không hút thuốc", "Chính Sách", false), R.drawable.ic_no_smooking),
    NO_PETS(AmenitiesModel("Không thú cưng", "Chính Sách", false), R.drawable.ic_no_pet),
    NO_PARTY(AmenitiesModel("Không tổ chức tiệc", "Chính Sách", false), R.drawable.ic_no_party),
    MIN_AGE(AmenitiesModel("Độ tuổi tối thiểu để nhận phòng: 18", "Chính Sách", false), R.drawable.ic_under_18);
}