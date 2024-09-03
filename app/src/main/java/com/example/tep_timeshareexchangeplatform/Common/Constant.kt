package com.example.tep_timeshareexchangeplatform.Common

import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ResortModel
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
            )
        )
        val resortListMT = listOf(
            ResortModel(
                id = 1,
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
                id = 2,
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
                id = 3,
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
                id = 4,
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
                id = 5,
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
        val resortListMN = listOf(
            ResortModel(
                id = 1,
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
                id = 2,
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
                id = 3,
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
                id = 4,
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
                id = 5,
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
    }
}