package com.example.tep_timeshareexchangeplatform.Common

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.BlogModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.DestinationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.FacilitieModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ResortModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ReviewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.RoomTypeModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.TimeshareModel
import com.example.tep_timeshareexchangeplatform.R

class Constant {

    companion object {
        // Nice Bottom Nav Bar
        const val ITEM_TAG = "item"
        const val ICON_ATTRIBUTE = "icon"
        const val TITLE_ATTRIBUTE = "title"
        const val WHITE_COLOR_HEX = "#FFFFFF"

        const val DEFAULT_INDICATOR_COLOR = "#5F8BFF"
        const val DEFAULT_TEXT_COLOR = "#444444"

        const val DEFAULT_PRIMARY_COLOR = "#5F8BFF"
        const val DEFAULT_PRIMARY_COLOR_INACTIVE = "#CFCFCF"
        const val DEFAULT_TEXT_COLOR_ACTIVE = "#6A82F8"
        const val DEFAULT_TEXT_COLOR_ACTIVE_TOP_RERSORT = "#FF7F0D"
        const val DEFAULT_TEXT_COLOR_BADGE = "#FF0E0E"

        const val DEFAULT_SELECTION_LOCATION_KEY = "selectedLocation"
        const val DEFAULT_SELECTION_DATE_KEY = "selectedDate"

        val timeshareList = listOf(
            TimeshareModel(
                1,
                verifyText = true,
                favoriteIcon = R.drawable.baseline_favorite_border_24,
                imageTimeshare = R.drawable.im_matiral_timeshare,
                rating = 4.5f,
                ratingCount = "254 đánh giá",
                timeshareName = "Nha Trang Resort - Cương Quyết",
                location = "Khánh Hòa, Việt Nam",
                date = "26/08/2024 - 30/08/2024",
                roomDetails = "Phòng Studio, 1 Giường, 4 Người, Emegency, Fukami",
                price = "1.300.000 VND",
                numberOfNights = " | 4 đêm"
            ),
            TimeshareModel(
                2,
                verifyText = true,
                favoriteIcon = R.drawable.baseline_favorite_border_24,
                imageTimeshare = R.drawable.im_matiral_timeshare,
                rating = 4.0f,
                ratingCount = "150 đánh giá",
                timeshareName = "Phú Quốc Resort",
                location = "Rạch Giá, Kiên Giang, Việt Nam",
                date = "01/09/2024 - 05/09/2024",
                roomDetails = "Phòng Deluxe, 2 Giường, 6 Người",
                price = "2.000.000 VND",
                numberOfNights = " | 5 đêm"
            ),
            TimeshareModel(
                3,
                verifyText = true,
                favoriteIcon = R.drawable.baseline_favorite_border_24,
                imageTimeshare = R.drawable.im_matiral_timeshare,
                rating = 4.7f,
                ratingCount = "300 đánh giá",
                timeshareName = "Đà Nẵng Resort",
                location = "Đà Nẵng, Việt Nam",
                date = "10/10/2024 - 15/10/2024",
                roomDetails = "Phòng Suite, 1 Giường, 2 Người",
                price = "3.000.000 VND",
                numberOfNights = " | 5 đêm"
            ),
            TimeshareModel(4,
                verifyText = true,
                favoriteIcon = R.drawable.baseline_favorite_border_24,
                imageTimeshare = R.drawable.im_matiral_timeshare,
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                timeshareName = "Hạ Long Resort",
                location = "Quảng Ninh, Việt Nam",
                date = "20/11/2024 - 25/11/2024",
                roomDetails = "Phòng Deluxe, 1 Giường, 3 Người",
                price = "2.500.000 VND",
                numberOfNights = " | 5 đêm"
            )
        )

        val resortListMB = listOf(
            ResortModel(
                id = 1,
                resortImage = R.drawable.im_material_resort,
                resortName = "Nha Trang Resort",
                rating = 4.5f,
                ratingCount = "254 đánh giá",
                location = "Khánh Hòa, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "550.000 - 1.300.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 2,
                resortImage = R.drawable.im_material_resort,
                resortName = "Phú Quốc Resort",
                rating = 4.0f,
                ratingCount = "150 đánh giá",
                location = "Kiên Giang, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "750.000 - 2.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 3,
                resortImage = R.drawable.im_material_resort,
                resortName = "Đà Nẵng Resort",
                rating = 4.7f,
                ratingCount = "300 đánh giá",
                location = "Đà Nẵng, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "1.000.000 - 3.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 4,
                resortImage = R.drawable.im_material_resort,
                resortName = "Hạ Long Resort",
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                location = "Quảng Ninh, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "650.000 - 2.500.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 5,
                resortImage = R.drawable.im_material_resort,
                resortName = "Đại Lải Resort",
                rating = 4.2f,
                ratingCount = "180 đánh giá",
                location = "Vĩnh Phúc, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = " 700.000 - 1.800.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 6,
                resortImage = R.drawable.im_material_mt,
                resortName = "Nha Trang Resort",
                rating = 4.5f,
                ratingCount = "254 đánh giá",
                location = "Khánh Hòa, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "550.000 - 1.300.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id =7,
                resortImage = R.drawable.im_material_mt,
                resortName = "Phú Quốc Resort",
                rating = 4.0f,
                ratingCount = "150 đánh giá",
                location = "Kiên Giang, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "750.000 - 2.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 8,
                resortImage = R.drawable.im_material_mt,
                resortName = "Đà Nẵng Resort",
                rating = 4.7f,
                ratingCount = "300 đánh giá",
                location = "Đà Nẵng, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "1.000.000 - 3.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 9,
                resortImage = R.drawable.im_material_mt,
                resortName = "Hạ Long Resort",
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                location = "Quảng Ninh, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "650.000 - 2.500.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 10,
                resortImage = R.drawable.im_material_mt,
                resortName = "Đại Lải Resort",
                rating = 4.2f,
                ratingCount = "180 đánh giá",
                location = "Vĩnh Phúc, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = " 700.000 - 1.800.000 VND",
                numberOfNights = "| 1 đêm"
            )
        )
        val resortListMT = listOf(
            ResortModel(
                id = 6,
                resortImage = R.drawable.im_material_mt,
                resortName = "Nha Trang Resort",
                rating = 4.5f,
                ratingCount = "254 đánh giá",
                location = "Khánh Hòa, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "550.000 - 1.300.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id =7,
                resortImage = R.drawable.im_material_mt,
                resortName = "Phú Quốc Resort",
                rating = 4.0f,
                ratingCount = "150 đánh giá",
                location = "Kiên Giang, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "750.000 - 2.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 8,
                resortImage = R.drawable.im_material_mt,
                resortName = "Đà Nẵng Resort",
                rating = 4.7f,
                ratingCount = "300 đánh giá",
                location = "Đà Nẵng, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "1.000.000 - 3.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 9,
                resortImage = R.drawable.im_material_mt,
                resortName = "Hạ Long Resort",
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                location = "Quảng Ninh, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "650.000 - 2.500.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 10,
                resortImage = R.drawable.im_material_mt,
                resortName = "Đại Lải Resort",
                rating = 4.2f,
                ratingCount = "180 đánh giá",
                location = "Vĩnh Phúc, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = " 700.000 - 1.800.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 11,
                resortImage = R.drawable.im_material_mn,
                resortName = "Nha Trang Resort",
                rating = 4.5f,
                ratingCount = "254 đánh giá",
                location = "Khánh Hòa, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "550.000 - 1.300.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 12,
                resortImage = R.drawable.im_material_mn,
                resortName = "Phú Quốc Resort",
                rating = 4.0f,
                ratingCount = "150 đánh giá",
                location = "Kiên Giang, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "750.000 - 2.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 13,
                resortImage = R.drawable.im_material_mn,
                resortName = "Đà Nẵng Resort",
                rating = 4.7f,
                ratingCount = "300 đánh giá",
                location = "Đà Nẵng, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "1.000.000 - 3.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 14,
                resortImage = R.drawable.im_material_mn,
                resortName = "Hạ Long Resort",
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                location = "Quảng Ninh, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "650.000 - 2.500.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 15,
                resortImage = R.drawable.im_material_mn,
                resortName = "Đại Lải Resort",
                rating = 4.2f,
                ratingCount = "180 đánh giá",
                location = "Vĩnh Phúc, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = " 700.000 - 1.800.000 VND",
                numberOfNights = "| 1 đêm"
            )
        )
        val resortListMN = listOf(
            ResortModel(
                id = 11,
                resortImage = R.drawable.im_material_mn,
                resortName = "Nha Trang Resort",
                rating = 4.5f,
                ratingCount = "254 đánh giá",
                location = "Khánh Hòa, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "550.000 - 1.300.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 12,
                resortImage = R.drawable.im_material_mn,
                resortName = "Phú Quốc Resort",
                rating = 4.0f,
                ratingCount = "150 đánh giá",
                location = "Kiên Giang, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "750.000 - 2.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 13,
                resortImage = R.drawable.im_material_mn,
                resortName = "Đà Nẵng Resort",
                rating = 4.7f,
                ratingCount = "300 đánh giá",
                location = "Đà Nẵng, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "1.000.000 - 3.000.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 14,
                resortImage = R.drawable.im_material_mn,
                resortName = "Hạ Long Resort",
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                location = "Quảng Ninh, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = "650.000 - 2.500.000 VND",
                numberOfNights = "| 1 đêm"
            ),
            ResortModel(
                id = 15,
                resortImage = R.drawable.im_material_mn,
                resortName = "Đại Lải Resort",
                rating = 4.2f,
                ratingCount = "180 đánh giá",
                location = "Vĩnh Phúc, Việt Nam",
                roomDetails = "123 Timeshare cho thuê có sẵn",
                price = " 700.000 - 1.800.000 VND",
                numberOfNights = "| 1 đêm"
            )
        )


        val destiantionList = listOf(
            DestinationModel(
                id = 1,
                destinationImage = R.drawable.im_material_phu_quoc,
                destinationName = "Phú Quốc",
            ),
            DestinationModel(
                id = 2,
                destinationImage = R.drawable.im_meterial_da_nang,
                destinationName = "Đà Nẵng",
            ),
            DestinationModel(
                id = 3,
                destinationImage = R.drawable.im_material_da_lat,
                destinationName = "Đà Lạt",
            ),
            DestinationModel(
                id = 4,
                destinationImage = R.drawable.im_material_quy_nhon,
                destinationName = "Quy NHơn",
            ),
            DestinationModel(
                id = 5,
                destinationImage = R.drawable.im_material_nha_trang,
                destinationName = "Nha Trang",
            ),
            DestinationModel(
                id = 6,
                destinationImage = R.drawable.im_material_phan_thiet,
                destinationName = "Phan Thiết",
            ),
            DestinationModel(
                id = 7,
                destinationImage = R.drawable.im_material_phu_yen,
                destinationName = "Phú Yên",
            ),
            DestinationModel(
                id = 8,
                destinationImage = R.drawable.im_material_vung_tau,
                destinationName = "Vũng Tàu",
            ),

        )

        val blogList = listOf(
            BlogModel(1, R.drawable.im_material_mn, "Flamingo Đại Lải  co rat nhieu gai xinh"),
            BlogModel(2, R.drawable.im_material_mn, "Flamingo Đại Lải  co rat nhieu gai xinh"),
            BlogModel(3, R.drawable.im_material_mn, "Flamingo Đại Lải  co rat nhieu gai xinh"),
            BlogModel(4, R.drawable.im_material_mn, "Flamingo Đại Lải  co rat nhieu gai xinh"),
            BlogModel(5, R.drawable.im_material_mn, "Flamingo Đại Lải  co rat nhieu gai xinh")
        )
        val cityList = listOf(
            LocationModel(1, "Thành Phố Đà Lạt", "Lâm Đồng, Việt Nam"),
            LocationModel(2,"Thành Phố Hội An", "Quảng Nam, Việt Nam"),
            LocationModel(3,"Thành Phố Hạ Long", "Quảng Ninh, Việt Nam"),
            LocationModel(4,"Thành Phố Quy Nhơn", "Bình Định, Việt Nam"),
            LocationModel(5,"Thành Phố Đồng Hới", "Quảng Bình, Việt Nam"),
            // Add more cities here...
        )


        val listImage = listOf(
            "https://i.pinimg.com/564x/05/fb/0e/05fb0e639fc5f3373433f4a8594bea54.jpg",
            "https://i.pinimg.com/564x/02/18/15/021815591bfa2023e4ef3f9cc1d15590.jpg",
            "https://i.pinimg.com/564x/02/18/15/021815591bfa2023e4ef3f9cc1d15590.jpg",
            "https://i.pinimg.com/736x/4d/4c/a7/4d4ca70285d1bc68fee56ed86770b47c.jpg",
            "https://i.pinimg.com/736x/39/b0/d2/39b0d2f927ae284e9b593ad1239768de.jpg",
            "https://i.pinimg.com/736x/39/b0/d2/39b0d2f927ae284e9b593ad1239768de.jpg",
        )

        // Get List Room Type
        val listRoomType = listOf(
            RoomTypeModel(1, "Phòng Studio", "https://i.pinimg.com/564x/05/fb/0e/05fb0e639fc5f3373433f4a8594bea54.jpg"),
            RoomTypeModel(2, "Phòng Queen", "https://i.pinimg.com/736x/09/b6/5f/09b65f9ba22e9a314d059b814e7d62a3.jpg"),
        )

        // Get list Facilitie
        val listFacilite = listOf(
            FacilitieModel(1, "Điều hoà nhiệt độ", R.drawable.ic_air_conditioner),
            FacilitieModel(2, "Lễ tân 24h", R.drawable.ic_receptionist),
            FacilitieModel(3, "Chỗ đỗ xe", R.drawable.ic_car_aprking),
            FacilitieModel(4, "Sân thượng", R.drawable.ic_roff_top),
            FacilitieModel(5, "Phòng gia đình", R.drawable.ic_family_room),
            FacilitieModel(6, "Chỗ đỗ xe", R.drawable.ic_car_aprking),
            FacilitieModel(7, "Sân thượng", R.drawable.ic_roff_top),
            FacilitieModel(8, "Phòng gia đình", R.drawable.ic_family_room),
            FacilitieModel(9, "Phòng gia đình", R.drawable.ic_family_room),
            FacilitieModel(10, "Phòng gia đình", R.drawable.ic_family_room),
            FacilitieModel(11, "Phòng gia đình", R.drawable.ic_family_room),
        )

        // Get list Review
        val listReview = listOf(
            ReviewModel(1, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
            ReviewModel(2, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
            ReviewModel(3, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
            ReviewModel(4, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
            ReviewModel(5, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
            ReviewModel(6, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
            ReviewModel(7, "Phòng đẹp, sạch sẽ, nhân viên thân thiện", 5, "26/08/2024", 1, 1),
        )

    }
}