package com.example.tep_timeshareexchangeplatform.Common

import android.content.Context
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.BlogModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.DestinationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.FAQModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.IntroSliderModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ReviewModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        const val DEFAULT_SELECTION_MY_TIMESHARE = "selectedMyTimeharePostingFlow"

        const val SEARCH_LOCATION = "searchLocation"
        const val SEARCH_DATE = "searchDate"
        const val SEARCH_ROOM = "searchRoom"

        const val FRAGMENT_HOME_CODE = "FH"
        const val ACTIVITY_RENTAL_POSTING_CODE = "ARP"

        const val DEFAULT_RESORT_ID = "resortId"
        const val DEFAULT_RESORT_SEARCHED_SELECTION = "resortSearchedSelection"

        const val DEFAULT_PACKAGE_SELECTION = "packageSelection"

        const val PAYMENT_URL = "paymentUrl"

        const val PAYMENT_SUCCESS = "paymentSuccess"

        const val PAYMENT_SUCCESS_PACKAGE = "paymentSuccessPackage"

        const val PAYMENT_SUCCESS_POSTING = "paymentSuccessPosting"

        const val PAYMENT_SUCCESS_VNPAY = "paymentSuccessVNPAY"

        const val TRANSACTION_ID = "transactionId"

        const val DEFAULT_POSTING_ID = "postingId"
        const val DEFAULT_MY_POSTING_ID = "myPostingId"
        const val DEFAULT_MY_EXCHANGE_REQUEST_ID = "id"
        const val DEFAULT_MY_EXCHANGE_REQUEST_ID_1 = "requestId"
        const val DEFAULT_BLOG_ID= "postingId"
        const val PAYMENT_METHOD_TYPE = "paymentMethod"
        const val REQUEST_GET_MY_TIMESHARE = "requestGetMyTimeshare"

        const val DEFAULT_MY_POSTING_RESORT_NAME = "myPostingResortName"
        const val DEFAULT_MY_POSTING_ROOM_NAME = "myPostingRoomName"
        const val DEFAULT_MY_POSTING_CHECK_IN_DATE = "myPostingCheckInDate"
        const val DEFAULT_MY_POSTING_CHECK_OUT_DATE = "myPostingCheckOutDate"
        const val staffRefinementPrice = "staffRefinementPrice"
        const val priceValuation = "priceValuation"
        const val DEFAULT_MY_POSTING_NIGHT = "myPostingNight"

        const val DEFAULT_MY_BOOKING_SELECTED_ID = "myBookingSelectedId"
        const val GENERAL_ID_PAYMENT = "generalIdPayment"

        const val USER_LOGIN_STATE = "userLoginState"
        const val CUSTOMER_INFO = "customerInfo"

        const val POSTING_TYPE_FLOW = "postingTypeFlow"
        const val RENTAL_POSTING_FLOW = "rentalPostingFlow"
        const val EXCHANGER_POSTING_FLOW = "exchangerPostingFlow"
        const val POSTING_TIMESHARE_DTO = "postingTimeshareDTO"

        const val LOGGED_OUT = "loggedOut"
        const val AVAILABLE_MONEY = "availableMoney"


        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

        fun formatPriceLong(price: Long): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

        fun formatDateByLocale(dateString: String, context: Context): String {
            // Định dạng của chuỗi ngày nhập vào (dd-MM-yyyy)
            val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

            // Chuyển chuỗi ngày thành đối tượng Date
            val date: Date = inputDateFormat.parse(dateString) ?: return ""

            // Sử dụng PreferenceHelper để lấy ngôn ngữ đã lưu
            val preferenceHelper = PreferenceHelper(context)
            val languageCode = preferenceHelper.getLanguage()

            // Định dạng ngày tháng dựa trên ngôn ngữ đã lưu
            val dateFormat = if (languageCode == "vi") {
                // Định dạng cho Tiếng Việt (thêm thứ vào)
                SimpleDateFormat("EEEE, dd 'Tháng' M, yyyy", Locale.forLanguageTag("vi"))
            } else {
                // Định dạng cho Tiếng Anh hoặc ngôn ngữ khác (thêm thứ vào)
                SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.ENGLISH)
            }

            return dateFormat.format(date)
        }

        fun getDayOfWeek(dateString: String, context: Context): String {
            // Input date format
            val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date: Date = inputDateFormat.parse(dateString) ?: return ""

            // Get saved language preference
            val preferenceHelper = PreferenceHelper(context)
            val languageCode = preferenceHelper.getLanguage()

            // Format for day of the week
            val dayFormat = if (languageCode == "vi") {
                SimpleDateFormat("EEEE", Locale.forLanguageTag("vi"))
            } else {
                SimpleDateFormat("EEEE", Locale.ENGLISH)
            }

            return dayFormat.format(date)
        }

        fun getFormattedDate(dateString: String, context: Context): String {
            // Input date format
            val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date: Date = inputDateFormat.parse(dateString) ?: return ""

            // Get saved language preference
            val preferenceHelper = PreferenceHelper(context)
            val languageCode = preferenceHelper.getLanguage()

            // Format date based on saved language
            val dateFormat = if (languageCode == "vi") {
                SimpleDateFormat("dd 'Tháng' M, yyyy", Locale.forLanguageTag("vi"))
            } else {
                SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
            }

            return dateFormat.format(date)
        }




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
        fun displayBedsInfo(unitTypeMap: Map<String, Any>): String {
            val bedTypes = listOf(
                "bedsFull" to "Full",
                "bedsKing" to "King",
                "bedsSofa" to "Sofa",
                "bedsMurphy" to "Murphy",
                "bedsQueen" to "Queen",
                "bedsTwin" to "Twin"
            )

            val bedsList = bedTypes.mapNotNull { (key, label) ->
                val count = unitTypeMap[key] as? Int ?: 0 // Ép kiểu thành Int
                if (count > 0) "$count giường $label" else null
            }.joinToString(", ")

            return if (bedsList.isNotEmpty()) bedsList else "Không có giường"
        }




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


        val listMemberShip = listOf(
            RentalPackageEnum.MEMBERSHIP_MONTHLY.packageModel,
            RentalPackageEnum.MEMBERSHIP_YEARLY.packageModel,

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
                desc = "Có, bạn có thể đặt phòng trực tuyến thông qua ứng dụng hoặc website của chúng tôi.",
                isExpandable = false
            ),
            FAQModel(
                id = 2,
                title = "Tôi có thể hủy phòng không?",
                desc = "Có, bạn có thể hủy phòng thông qua ứng dụng hoặc website của chúng tôi.",
                isExpandable = false
            ),
            FAQModel(
                id = 3,
                title = "Tôi có thể thay đổi ngày đặt phòng không?",
                desc = "Có, bạn có thể thay đổi ngày đặt phòng thông qua ứng dụng hoặc website của chúng tôi.",
                isExpandable = false
            ),
            FAQModel(
                id = 4,
                title = "Tôi có thể đặt phòng cho người khác không?",
                desc = "Có, bạn có thể đặt phòng cho người khác thông qua ứng dụng hoặc website của chúng tôi.",
                isExpandable = false
            ),
            FAQModel(
                id = 5,
                title = "Tôi có thể đặt phòng cho người khác không?",
                desc = "Có, bạn có thể đặt phòng cho người khác thông qua ứng dụng hoặc website của chúng tôi.",
                isExpandable = false
            )
        )

        val listAmenities = listOf(
            AmenitiesModel(
                name = "Máy pha cà phê",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Lò vi sóng",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Máy rửa chén",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Máy nướng bánh mì",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Tủ lạnh (lớn)",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Tủ lạnh (nhỏ)",
                isChecked = false,
            ),
        )

        val listEntertament = listOf(
            AmenitiesModel(
                name = "Máy phát DVD",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Mạng Internet",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Radio",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "TV thông minh",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Điện thoại bàn",
                isChecked = false,
            ),


            )

        val listPolicy = listOf(
            AmenitiesModel(
                name = "Không hút thuốc",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Không thú cưng",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Không tổ chức tiệc",
                isChecked = false,
            ),


            )

        val rentalPackageList = listOf(
            PackageModel(
                id = 1,
                name = "Gói Cơ Bản",
                price = 149000,
                description = "(DIY) Unwind sẽ hỗ trợ quảng cáo và đưa người thuê đến với bạn. Cá nhân bạn sẽ hoàn thiện các hợp đồng và chi tiết.",
                duration = 1,
                type = "Gói Tháng",
                listBenefit = listOf(
                    "Thông báo qua mail khi có người thuê",
                    "Gắn thẻ “Bài mới” trong 30 ngày",
                )
            ),
            PackageModel(
                id = 2,
                name = "Gói Nâng Cao",
                price = 179000,
                description = "(DIY) Sử dụng hệ thống đặt chỗ trực tuyến của Unwind để tăng khả năng tiếp cận người thuê.",
                duration = 1,
                type = "Gói Tháng",
                listBenefit = listOf(
                    "Thông báo qua mail khi có người thuê",
                    "Gắn thẻ “Bài mới” trong 30 ngày",
                    "Gán cờ “Được xác minh” của Unwind",
                    "Được xác minh bới nhân viên của Resort, Khách sạn",
                    "Cho thuê trực tuyến"
                )
            ),
            PackageModel(
                id = 3,
                name = "Gói Premium",
                price = 199000,
                description = "Unwind sẽ hỗ trợ từng bước - từ đăng bài, quảng cáo đến thỏa thuận cho thuê và thanh toán.",
                duration = 1,
                type = "Gói Tháng",
                listBenefit = listOf(
                    "Thông báo qua mail khi có người thuê",
                    "Gắn thẻ “Bài mới” trong 30 ngày",
                    "Gán cờ “Được xác minh” của Unwind",
                    "Được xác minh bới nhân viên của Resort, Khách sạn",
                    "Cho thuê trực tuyến",
                    "Hỗ trợ định giá",
                    "Hỗ trợ quản lý phòng và liên lạc"
                )
            ),
            PackageModel(
                id = 4,
                name = "Gói Ủy Quyền",
                price = 599000,
                description = "Unwind sẽ hỗ trợ từng bước - từ đăng bài, quảng cáo đến thỏa thuận cho thuê và thanh toán.",
                duration = 1,
                type = "Gói Tháng",
                listBenefit = listOf(
                    "Thông báo qua mail khi có người thuê",
                    "Gắn thẻ “Bài mới” trong 30 ngày",
                    "Gán cờ “Được xác minh” của Unwind",
                )
            ),
        )
        val exchangePackageList = listOf(
            PackageModel(
                id = 1,
                name = "Gói Cơ Bản",
                price = 149000,
                description = "(DIY) Unwind sẽ hỗ trợ quảng cáo và đưa người thuê đến với bạn. Cá nhân bạn sẽ hoàn thiện các hợp đồng và chi tiết.",
                duration = 1,
                type = "Basic",
                listBenefit = listOf(
                    "Thông báo qua mail khi có người thuê",
                    "Gắn thẻ “Bài mới” trong 30 ngày",
                )
            ),
            PackageModel(
                id = 2,
                name = "Gói Nâng Cao",
                price = 199000,
                description = "(DIY) Sử dụng hệ thống đặt chỗ trực tuyến của Unwind để tăng khả năng tiếp cận người thuê.",
                duration = 1,
                type = "Standard",
                listBenefit = listOf(
                    "Thông báo qua mail khi có người thuê",
                    "Gắn thẻ “Bài mới” trong 30 ngày",
                    "Gán cờ “Được xác minh” của Unwind",
                    "Được xác minh bới nhân viên của Resort, Khách sạn",
                    "Cho thuê trực tuyến"
                )
            )
        )


    }
}