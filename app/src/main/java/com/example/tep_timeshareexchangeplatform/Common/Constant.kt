package com.example.tep_timeshareexchangeplatform.Common

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.BlogModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.DestinationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.FAQModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.FacilitieModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.IntroSliderModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.MemberShipModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.MyOrderModel
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
        const val DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW = "selectedRoomTypePostingFlow"

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
                timeshareName = "Vịnh Hạ Long Resort",
                location = "Quảng Ninh, Việt Nam",
                date = "20/11/2024 - 25/11/2024",
                roomDetails = "Phòng Deluxe, 1 Giường, 3 Người",
                price = "2.500.000 VND",
                numberOfNights = " | 5 đêm"
            ),
            TimeshareModel(5,
                verifyText = true,
                favoriteIcon = R.drawable.baseline_favorite_border_24,
                imageTimeshare = R.drawable.im_matiral_timeshare,
                rating = 4.3f,
                ratingCount = "200 đánh giá",
                timeshareName = "Biển Vịnh Hạ Long Resort",
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
            LocationModel(1, "Thành Phố Đà Lạt", "Lâm Đồng, Việt Nam", R.drawable.im_material_da_lat, 1),
            LocationModel(2,"Thành Phố Hội An", "Quảng Nam, Việt Nam", R.drawable.im_material_vung_tau, 1),
            LocationModel(3,"Thành Phố Hạ Long", "Quảng Ninh, Việt Nam", R.drawable.im_material_vung_tau, 1),
            LocationModel(4,"Thành Phố Quy Nhơn", "Bình Định, Việt Nam", R.drawable.im_material_vung_tau,1 ),
            LocationModel(5,"Thành Phố Đồng Hới", "Quảng Bình, Việt Nam", R.drawable.im_material_vung_tau,1),
            LocationModel(6,"Khách sạn Gia Vinh", "Bà Rịa - Vũng Tàu, Việt Nam", R.drawable.im_material_nha_trang,2),
            LocationModel(7,"Khách sạn Cương Quyết", "Bà Rịa - Vũng Tàu, Việt Nam", R.drawable.im_material_quy_nhon,2),
            LocationModel(8,"Khách sạn Nguyễn Tấn", "Bà Rịa - Vũng Tàu, Việt Nam", R.drawable.im_material_phan_thiet,2),
            LocationModel(9,"Khách sạn Thành Đạt", "Bà Rịa - Vũng Tàu, Việt Nam", R.drawable.im_material_quy_nhon,2),
            LocationModel(10,"Khách sạn Thanh Long", "Bà Rịa - Vũng Tàu, Việt Nam", R.drawable.im_material_phu_yen,2),

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

        val listTimeshareImage = listOf(
            "https://i.pinimg.com/564x/0f/df/c5/0fdfc565bc883a817317592ec37eaffe.jpg",
            "https://i.pinimg.com/564x/3f/19/6f/3f196f360056fea74b88098a650131aa.jpg",
            "https://i.pinimg.com/564x/0b/48/2b/0b482bf30e232ff44d74d92e22bad3b8.jpg",
            "https://i.pinimg.com/736x/d9/5d/ca/d95dca792b2fdcb0c8c0021400c27cc7.jpg",
            "https://i.pinimg.com/736x/a3/df/db/a3dfdbb0283cac6b594c676d00696779.jpg"


        )


        val myOrderList = listOf(
            MyOrderModel(
                orderId = "12412441",
                status = "Đã xác nhận",
                timeshareName = "Flamingo Đại Lải | Studio King",
                checkInDate = "19-08-2024",
                checkInDay = "Thứ 2",
                checkOutDate = "23-08-2024",
                checkOutDay = "Thứ 6",
                timeshareType = "Phòng Studio King, 1 Giường, 4 Người",
                price = "1,000,000 VND",
                dateOfOrder = "20/09/2024",
                timeOfOrder = "19:10",
                paymentTypeIcon = R.drawable.ic_master_card,  // Replace with your actual drawable resource ID
                timeshareImage = R.drawable.im_matiral_timeshare // Replace with your actual drawable resource ID
            ),
            MyOrderModel(
                orderId = "98457392",
                status = "Đang chờ xử lý",
                timeshareName = "Vinpearl Nha Trang | Beachfront Villa",
                checkInDate = "10-10-2024",
                checkInDay = "Thứ 5",
                checkOutDate = "15-10-2024",
                checkOutDay = "Thứ 3",
                timeshareType = "Biệt thự 3 phòng ngủ, hướng biển",
                price = "8,500,000 VND",
                dateOfOrder = "05/10/2024",
                timeOfOrder = "15:30",
                paymentTypeIcon = R.drawable.ic_visa, // Replace with your actual drawable resource ID
                timeshareImage = R.drawable.im_matiral_timeshare // Replace with your actual drawable resource ID
            ),
            MyOrderModel(
                orderId = "58234987",
                status = "Đã hủy",
                timeshareName = "InterContinental Phú Quốc | Ocean View Suite",
                checkInDate = "01-11-2024",
                checkInDay = "Thứ 6",
                checkOutDate = "05-11-2024",
                checkOutDay = "Thứ 2",
                timeshareType = "Phòng Suite với ban công và hướng biển",
                price = "12,000,000 VND",
                dateOfOrder = "25/10/2024",
                timeOfOrder = "14:00",
                paymentTypeIcon = R.drawable.ic_paypal, // Replace with your actual drawable resource ID
                timeshareImage = R.drawable.im_matiral_timeshare // Replace with your actual drawable resource ID
            ),
            MyOrderModel(
                orderId = "76342890",
                status = "Đang chờ xác nhận",
                timeshareName = "Sun World Ba Na Hills | Mountain Retreat",
                checkInDate = "15-12-2024",
                checkInDay = "Chủ Nhật",
                checkOutDate = "20-12-2024",
                checkOutDay = "Thứ 6",
                timeshareType = "Nhà nghỉ trên đỉnh núi, 2 phòng ngủ",
                price = "7,200,000 VND",
                dateOfOrder = "10/12/2024",
                timeOfOrder = "10:20",
                paymentTypeIcon = R.drawable.ic_visa, // Replace with your actual drawable resource ID
                timeshareImage = R.drawable.im_matiral_timeshare // Replace with your actual drawable resource ID
            ),
            MyOrderModel(
                orderId = "45612378",
                status = "Đã thanh toán",
                timeshareName = "Sheraton Đà Nẵng | Presidential Suite",
                checkInDate = "20-12-2024",
                checkInDay = "Thứ 7",
                checkOutDate = "25-12-2024",
                checkOutDay = "Thứ 5",
                timeshareType = "Phòng Tổng thống, hướng biển với hồ bơi riêng",
                price = "25,000,000 VND",
                dateOfOrder = "18/12/2024",
                timeOfOrder = "09:00",
                paymentTypeIcon = R.drawable.ic_momo, // Replace with your actual drawable resource ID
                timeshareImage = R.drawable.im_matiral_timeshare // Replace with your actual drawable resource ID
            )
        )

        val listMemberShip = listOf(
            MemberShipModel(
                id = 1,
                name = "Gói Tháng",
                price = 99000,
                description = "Gói dành cho những ai muốn trải nghiệm dịch vụ của chúng tôi trong 1 tháng",
                duration = 1,
                type = "Gói Tháng",
                listBenefit = listOf(
                    "Đặt phòng nhanh chóng",
                    "Giảm giá 10% cho lần đặt phòng tiếp theo",
                    "Miễn phí hủy phòng",
                    "Hỗ trợ 24/7"
                )
            ),

            MemberShipModel(
                id = 2,
                name = "Gói Năm",
                price = 990000,
                description = "Gói dành cho những ai muốn trải nghiệm dịch vụ của chúng tôi trong 1 tháng",
                duration = 1,
                type = "Gói Năm",
                listBenefit = listOf(
                    "Đặt phòng nhanh chóng",
                    "Giảm giá 10% cho lần đặt phòng tiếp theo",
                    "Miễn phí hủy phòng",
                    "Hỗ trợ 24/7",
                    "Đặt phòng nhanh chóng",
                    "Giảm giá 10% cho lần đặt phòng tiếp theo",

                )
            ),

        )

        val listIntroSlider = listOf(
            IntroSliderModel(
                id = 1,
                title = "Trao Đổi Timeshare \n" +
                        " Đơn Giản, Tiện Lợi",
                description = "Linh hoạt trong việc trao đổi kỳ nghỉ và khám phá các khu nghỉ dưỡng mới.",
                image = R.drawable.ic_material_slider_1
            ),

            IntroSliderModel(
                id = 2,
                title = "Chuyển Đổi Timeshare\n" +
                        " Khám Phá Việt Nam",
                description = "Thay đổi địa điểm nghỉ dưỡng dễ dàng \n qua tính năng trao đổi thông minh.",
                image = R.drawable.ic_meterial_slider_2
            ),

            IntroSliderModel(
                id = 3,
                title = "Kỳ Nghỉ Đa Dạng\n" +
                        "Trải Nghiệm Toàn Quốc",
                description = "Trao đổi kỳ nghỉ dễ dàng với cộng đồng tận hưởng dịch vụ nghỉ dưỡng toàn cầu.",
                image = R.drawable.ic_meterial_slider_3
            ),

        )

        val listTimeshareCompany = listOf(
            "https://alma.vn/wp-content/uploads/2019/01/ALMA__logo.png",
            "https://th.bing.com/th/id/OIP.AWXcXU1vcOGQehDtJh8m5AHaEO?w=700&h=400&rs=1&pid=ImgDetMain",
            "https://storage.googleapis.com/youth-media/post-thumbnails/cPfglgEi3sEmtPwlq1EC1yn6VuxtHJ5NCG5JldFk.png",
            "https://th.bing.com/th/id/OIP.EAKmwEAsPqNb2dvIL6b63AAAAA?rs=1&pid=ImgDetMain",
            "https://everland.vn/upload/projects/original/crystal-holidays-heritage-ly-son-avartar-1666754442.png",
            "https://media.discordapp.net/attachments/1257221915135840267/1291317831580909568/ic_flc_holiday.png?ex=66ffa8f2&is=66fe5772&hm=68d8deee0a8fc5314bccedc3cc1b965a43f3f21fe7e90516f008983ecc336b7e&=&format=webp&quality=lossless&width=390&height=46"

        )

        val listFaq = listOf(
            FAQModel(
                id = 1,
                title = "Tôi có thể đặt phòng trực tuyến không?",
                desc = "Có, bạn có thể đặt phòng trực tuyến thông qua ứng dụng hoặc website của chúng tôi." ,
                isExpandable = false
            ),
            FAQModel(
                id = 2,
                title = "Tôi có thể hủy phòng không?",
                desc = "Có, bạn có thể hủy phòng thông qua ứng dụng hoặc website của chúng tôi." ,
                isExpandable = false
            ),
            FAQModel(
                id = 3,
                title = "Tôi có thể thay đổi ngày đặt phòng không?",
                desc = "Có, bạn có thể thay đổi ngày đặt phòng thông qua ứng dụng hoặc website của chúng tôi." ,
                isExpandable = false
            ),
            FAQModel(
                id = 4,
                title = "Tôi có thể đặt phòng cho người khác không?",
                desc = "Có, bạn có thể đặt phòng cho người khác thông qua ứng dụng hoặc website của chúng tôi." ,
                isExpandable = false
            ),
            FAQModel(
                id = 5,
                title = "Tôi có thể đặt phòng cho người khác không?",
                desc = "Có, bạn có thể đặt phòng cho người khác thông qua ứng dụng hoặc website của chúng tôi." ,
                isExpandable = false
            )
        )

    }
}