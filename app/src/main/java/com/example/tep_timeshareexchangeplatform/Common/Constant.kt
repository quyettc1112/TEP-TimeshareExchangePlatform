package com.example.tep_timeshareexchangeplatform.Common

import android.content.Context
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.BlogModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.DestinationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.FAQModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.IntroSliderModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.NotificationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ReviewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DailySummaryDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeBase
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

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

        const val FCM_TOKEN_KEY = "fcmToken"

        const val RESORT_NAME = "resortName"
        const val RESORT_ID = "resortId"

        const val SEARCH_LOCATION = "searchLocation"
        const val SEARCH_DATE = "searchDate"
        const val SEARCH_ROOM = "searchRoom"

        const val FRAGMENT_HOME_CODE = "FH"
        const val ACTIVITY_RENTAL_POSTING_CODE = "ARP"

        const val IMAGE_POSITION = "imagePosition"
        const val IMAGE_LIST = "imageList"
        const val DEFAULT_RESORT_ID = "resortId"
        const val DEFAULT_RESORT_SEARCHED_SELECTION = "resortSearchedSelection"

        const val RESORT_LATITUDE = "resortLatitude"
        const val RESORT_LONGITUDE = "resortLongitude"

        const val DEFAULT_PACKAGE_SELECTION = "packageSelection"

        const val PAYMENT_URL = "paymentUrl"

        const val PAYMENT_SUCCESS = "paymentSuccess"

        const val AVG_RATING = "avgRating"
        const val TOTAL_RATING = "totalRating"

        const val PAYMENT_SUCCESS_PACKAGE = "paymentSuccessPackage"

        const val PAYMENT_SUCCESS_POSTING = "paymentSuccessPosting"

        const val PAYMENT_SUCCESS_VNPAY = "paymentSuccessVNPAY"

        const val TRANSACTION_ID = "transactionId"

        const val DEFAULT_POSTING_ID = "postingId"
        const val DEFAULT_MY_POSTING_ID = "myPostingId"
        const val DEFAULT_MY_EXCHANGE_REQUEST_ID = "id"
        const val DEFAULT_BLOG_ID= "postingId"
        const val DEFAULT_EXCHANGE_REQUEST_ON_POST= "postingId"
        const val PAYMENT_METHOD_TYPE = "paymentMethod"
        const val REQUEST_GET_MY_TIMESHARE = "requestGetMyTimeshare"
        const val DEFAULT_EXCHANGE_POSTING_ID = "exchangePostingId"

        const val DEFAULT_MY_POSTING_RESORT_NAME = "myPostingResortName"
        const val DEFAULT_MY_POSTING_ROOM_NAME = "myPostingRoomName"
        const val DEFAULT_MY_POSTING_CHECK_IN_DATE = "myPostingCheckInDate"
        const val DEFAULT_MY_POSTING_CHECK_OUT_DATE = "myPostingCheckOutDate"
        const val DEFAULT_MY_POSTING_IMAGE = "myPostingImage"
        const val staffRefinementPrice = "staffRefinementPrice"
        const val priceValuation = "priceValuation"
        const val DEFAULT_MY_POSTING_NIGHT = "myPostingNight"

        const val DEFAULT_BOOKING_ID = "bookingId"
        const val DEFAULT_BOOKING_STATUS = "bookingStatus"

        const val OWNER_POSTING_ID = "ownerPostingId"


        const val DEFAULT_MY_BOOKING_RENTAL = "myBookingRental"
        const val DEFAULT_MY_BOOKING_EXCHANGE = "myBookingExchange"
        const val GENERAL_ID_PAYMENT = "generalIdPayment"

        const val USER_LOGIN_STATE = "userLoginState"
        const val CUSTOMER_INFO = "customerInfo"
        const val PROFILE_INFO = "profileInfo"

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

        fun formatPriceLong(price: Long?): String {
            if (price == null) return "0"

            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

        fun formatPriceLongAbs(price: Long?): String {
            if (price == null) return "0"

            val formatter = DecimalFormat("#,###")
            return formatter.format(abs(price)) // Lấy giá trị tuyệt đối trước khi định dạng
        }

        fun formatDateByLocale(dateString: String, context: Context): String {
            // Định dạng của chuỗi ngày nhập vào (dd-MM-yyyy)
            if(dateString.isNullOrEmpty()) return ""

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
        fun formatDateByLocaleYMD(dateString: String, context: Context): String {
            // Định dạng của chuỗi ngày nhập vào (yyyy-MM-dd)
            if (dateString.isNullOrEmpty()) return ""

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

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

            if(dateString.isNullOrEmpty()) return ""
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


        fun getFormattedDateString(dateString: String, context: Context): String {
            return try {
                // Input format từ ExchangeOfResortViewModel (yyyy-MM-dd)
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val date: Date = inputDateFormat.parse(dateString) ?: return ""

                // Lấy ngôn ngữ người dùng từ PreferenceHelper
                val preferenceHelper = PreferenceHelper(context)
                val languageCode = preferenceHelper.getLanguage()

                // Output format dựa trên ngôn ngữ
                val outputDateFormat = if (languageCode == "vi") {
                    SimpleDateFormat("dd 'Tháng' M, yyyy", Locale.forLanguageTag("vi"))
                } else {
                    SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
                }

                outputDateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        fun getDayOfWeekString(dateString: String, context: Context): String {
            // Input date format
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
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

        fun mapExchangeToUnitTypeBase(
            exchangeDetail: MyExchangePostingDetailResponse
        ): UnitTypeBase {
            return UnitTypeBase(
                id = exchangeDetail.unitType.id,
                title = exchangeDetail.unitType.title,
                area = exchangeDetail.unitType.area,
                bathrooms = exchangeDetail.unitType.bathrooms,
                bedrooms = exchangeDetail.unitType.bedrooms,
                bedsFull = exchangeDetail.unitType.bedsFull,
                bedsKing = exchangeDetail.unitType.bedsKing,
                bedsSofa = exchangeDetail.unitType.bedsSofa,
                bedsMurphy = exchangeDetail.unitType.bedsMurphy,
                bedsQueen = exchangeDetail.unitType.bedsQueen,
                bedsTwin = exchangeDetail.unitType.bedsTwin,
                buildingsOption = exchangeDetail.unitType.buildingsOption,
                price = 0, // Giá chưa được định nghĩa trong API này, có thể điều chỉnh tùy logic
                description = exchangeDetail.unitType.description,
                kitchen = exchangeDetail.unitType.kitchen,
                photos = exchangeDetail.unitType.photos,
                resortId = exchangeDetail.resortId,
                sleeps = exchangeDetail.unitType.sleeps,
                view = exchangeDetail.unitType.view,
                isActive = exchangeDetail.active,
                unitTypeAmenitiesDTOS = exchangeDetail.unitTypeAmenities.map { amenity ->
                    UnitTypeBase.UnitTypeAmenitiesDTOS(
                        name = amenity.name,
                        type = amenity.type,
                        isActive = true // Giả sử tất cả tiện ích đều active
                    )
                }
            )
        }
        fun mapToUnitTypeBase(
            unitType: MyRentalPostingDetailResponse.UnitType,
            unitTypeAmenities: List<MyRentalPostingDetailResponse.UnitTypeAmenity>
        ): UnitTypeBase {
            return UnitTypeBase(
                id = unitType.id,
                title = unitType.title,
                area = unitType.area,
                bathrooms = unitType.bathrooms,
                bedrooms = unitType.bedrooms,
                bedsFull = unitType.bedsFull,
                bedsKing = unitType.bedsKing,
                bedsSofa = unitType.bedsSofa,
                bedsMurphy = unitType.bedsMurphy,
                bedsQueen = unitType.bedsQueen,
                bedsTwin = unitType.bedsTwin,
                buildingsOption = unitType.buildingsOption,
                price = 0, // Giá truyền vào
                description = unitType.description,
                kitchen = unitType.kitchen,
                photos = unitType.photos,
                resortId = null, // Nếu resortId không có trong dữ liệu UnitType
                sleeps = unitType.sleeps,
                view = unitType.view,
                isActive = false, // Trạng thái hoạt động
                unitTypeAmenitiesDTOS = unitTypeAmenities.map { amenity ->
                    UnitTypeBase.UnitTypeAmenitiesDTOS(
                        name = amenity.name,
                        type = amenity.type,
                        isActive = null // Đặt giá trị nếu cần xử lý theo logic cụ thể
                    )
                }
            )
        }
        fun mapUnitTypeModelToUnitTypeBase(unitTypeModel: UnitTypeModel): UnitTypeBase {
            return UnitTypeBase(
                id = unitTypeModel.id,
                title = unitTypeModel.title,
                area = unitTypeModel.area,
                bathrooms = unitTypeModel.bathrooms,
                bedrooms = unitTypeModel.bedrooms,
                bedsFull = unitTypeModel.bedsFull,
                bedsKing = unitTypeModel.bedsKing,
                bedsSofa = unitTypeModel.bedsSofa,
                bedsMurphy = unitTypeModel.bedsMurphy,
                bedsQueen = unitTypeModel.bedsQueen,
                bedsTwin = unitTypeModel.bedsTwin,
                buildingsOption = unitTypeModel.buildingsOption,
                price = unitTypeModel.price,
                description = unitTypeModel.description,
                kitchen = unitTypeModel.kitchen,
                photos = unitTypeModel.photos,
                resortId = unitTypeModel.resortId,
                sleeps = unitTypeModel.sleeps,
                view = unitTypeModel.view,
                isActive = unitTypeModel.isActive,
                unitTypeAmenitiesDTOS = unitTypeModel.unitTypeAmenitiesDTOS.map { amenity ->
                    UnitTypeBase.UnitTypeAmenitiesDTOS(
                        name = amenity.name,
                        type = amenity.type,
                        isActive = amenity.isActive
                    )
                }
            )
        }

        fun formatDateFromLong(dateInMillis: Long, format: String = "yyyy-MM-dd"): String {
            val date = Date(dateInMillis)
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            return formatter.format(date)
        }


        private fun unitTypeAmenities(unitType: MyExchangePostingDetailResponse.UnitType): List<UnitTypeModel.UnitTypeAmenitiesDTOS> {
            // Assuming you're mapping from unitType details for amenities; adjust based on source data
            return listOf(
                UnitTypeModel.UnitTypeAmenitiesDTOS(
                    name = "Sample Amenity", // Replace with actual logic if needed
                    type = null,
                    isActive = null
                )
            )
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
                type = "Bếp",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Lò vi sóng",
                type = "Bếp",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Máy rửa chén",
                type = "Bếp",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Máy nướng bánh mì",
                type = "Bếp",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Tủ lạnh (lớn)",
                type = "Bếp",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Tủ lạnh (nhỏ)",
                type = "Bếp",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Bếp lò",
                type = "Bếp",
                isChecked = false,
            ),
        )

        val listEntertament = listOf(
            AmenitiesModel(
                name = "Máy phát DVD",
                type = "Giải Trí",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Quầy Bar",
                type = "Giải Trí",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Máy Chiếu Phim",
                type = "Giải Trí",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Mạng Lan Internet",
                type = "Giải Trí",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Radio",
                type = "Giải Trí",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "TV thông minh",
                type = "Giải Trí",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Điện thoại bàn",
                type = "Giải Trí",
                isChecked = false,
            ),


            )

        val listFeatures = listOf(
            AmenitiesModel(
                name = "Máy Điều Hòa",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Wifi",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Nước nóng/lạnh",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Nước uống miễn phí",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Sân hiên hoặc Ban Công",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Bàn ăn",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Bàn làm việc",
                type = "Tiện Nghi",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Máy giặt và máy sấy (trong căn hộ)",
                type = "Tiện Nghi",
                isChecked = false,
            ),

        )

        val listPolicy = listOf(
            AmenitiesModel(
                name = "Không hút thuốc",
                type = "Chính Sách",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Không thú cưng",
                type = "Chính Sách",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Không tổ chức tiệc",
                type = "Chính Sách",
                isChecked = false,
            ),
            AmenitiesModel(
                name = "Độ tuổi tối thiểu để nhận phòng: 18",
                type = "Chính Sách",
                isChecked = false,
            )


        )

        fun mapRoomAmenitiesToAmenitiesModel(
            inputList: List<RoomDetailResponse.RoomAmenity>,
            amenityType: AmenityType
        ): List<AmenitiesModel> {
            return inputList
                .filter { it.type == amenityType.name } // Lọc theo AmenityType
                .map { amenity ->
                    AmenitiesModel(
                        name = amenity.name,
                        type = amenity.type,
                        isChecked = true
                    )
                }
        }

        val dummyResortList = listOf(
            ResortModelResponse.Content(
                id = 1,
                resortName = "Ana Mandara Villas Dalat Resort & Spa",
                resortLocationName = "Location 1",
                resortLocationDisplayName = "Đà Lạt, Lâm Đồng",
                resortLocationLatitude = "-49.055267",
                resortLocationLongitude = "-91.057432",
                logo = "https://i.pinimg.com/736x/b6/69/ec/b669eca9524f7bc97b19f50e2c9d0058.jpg",
                minPrice = 4016887,
                maxPrice = 7693369,
                status = "PENDING",
                address = "Address 1",
                timeshareCompanyId = 10,
                isActive = true,
                averageRating = 2.8f,
                totalRating = 432
            ),
            ResortModelResponse.Content(
                id = 2,
                resortName = "InterContinental Danang Sun Peninsula Resort",
                resortLocationName = "Location 2",
                resortLocationDisplayName = "Lăng Cô, Huế",
                resortLocationLatitude = "-59.472471",
                resortLocationLongitude = "31.435577",
                logo = "https://i.pinimg.com/736x/da/e1/4d/dae14db2be0400f2a8ed418061a3fc45.jpg",
                minPrice = 2870646,
                maxPrice = 7116206,
                status = "PENDING",
                address = "Address 2",
                timeshareCompanyId = 9,
                isActive = true,
                averageRating = 3.9f,
                totalRating = 247
            ),
            ResortModelResponse.Content(
                id = 3,
                resortName = "JW Marriott Phu Quoc Emerald Bay Resort & Spa ",
                resortLocationName = "Location 3",
                resortLocationDisplayName = "Đà Nẵng",
                resortLocationLatitude = "-5.757913",
                resortLocationLongitude = "-106.831450",
                logo = "https://i.pinimg.com/736x/90/7f/72/907f72716fa7bd54d7d243db2a039e56.jpg",
                minPrice = 2609779,
                maxPrice = 6669446,
                status = "PENDING",
                address = "Address 3",
                timeshareCompanyId = 5,
                isActive = false,
                averageRating = 2.6f,
                totalRating = 480
            ),
            ResortModelResponse.Content(
                id = 4,
                resortName = "Sofitel Legend Metropole Hanoi",
                resortLocationName = "Location 4",
                resortLocationDisplayName = "Hà Nội",
                resortLocationLatitude = "31.361967",
                resortLocationLongitude = "52.342541",
                logo = "https://i.pinimg.com/736x/76/3f/df/763fdf1b8a90b3929d9513bed5eeac44.jpg",
                minPrice = 2494283,
                maxPrice = 6535568,
                status = "PENDING",
                address = "Address 4",
                timeshareCompanyId = 5,
                isActive = false,
                averageRating = 2.5f,
                totalRating = 971
            ),
            ResortModelResponse.Content(
                id = 5,
                resortName = "Ana Mandara Villas Dalat Resort & Spa",
                resortLocationName = "Location 5",
                resortLocationDisplayName = "Cam Ranh, Khánh Hòa",
                resortLocationLatitude = "28.424863",
                resortLocationLongitude = "125.447153",
                logo = "https://i.pinimg.com/736x/74/72/f0/7472f0bcc25ef3db10c3fae271a6b977.jpg",
                minPrice = 4048727,
                maxPrice = 7084446,
                status = "ACTIVE",
                address = "Address 5",
                timeshareCompanyId = 7,
                isActive = false,
                averageRating = 4.4f,
                totalRating = 428
            )
        )

        val dummyPublicPostingList = listOf(
            PublicPostingResponse.Content(
                rentalPostingId = 1,
                expiredDate = "2025-10-10",
                ownerId = 151,
                ownerName = "Owner 1",
                timeShareId = 5,
                roomInfoId = 16,
                roomName = "Room 1",
                resortId = 19,
                resortName = "Vinpearl Resort Nha Trang",
                resortLocationName = "Location 1",
                resortLocationDisplayName = "Nha Trang, Khánh Hòa",
                isVerify = true,
                nights = 10,
                pricePerNights = 2543879,
                totalPrice = 47537698,
                rentalPackageId = 6,
                rentalPackageName = "Package 1",
                checkinDate = "1-05-2025",
                checkoutDate = "14-05-2025",
                status = "BOOKED",
                unitTypeDTO = PublicPostingResponse.Content.UnitTypeDTO(
                    id = 1,
                    title = "Unit Type 1",
                    area = "71 m²",
                    bathrooms = 2,
                    bedrooms = 2,
                    bedsFull = 2,
                    bedsKing = 2,
                    bedsSofa = 1,
                    bedsMurphy = 1,
                    bedsQueen = 0,
                    bedsTwin = 1,
                    buildingsOption = "Building B",
                    description = "Spacious and luxurious unit type 1 with modern amenities.",
                    kitchen = "Partial Kitchen",
                    photos = "https://i.pinimg.com/736x/3a/42/dc/3a42dcfa155606be36eae0a7eb5f4b5f.jpg",
                    sleeps = 3,
                    view = "Ocean View"
                ),
                active = true
            ),
            PublicPostingResponse.Content(
                rentalPostingId = 2,
                expiredDate = "2025-09-22",
                ownerId = 123,
                ownerName = "Owner 2",
                timeShareId = 24,
                roomInfoId = 40,
                roomName = "Room 2",
                resortId = 12,
                resortName = "The Reverie Saigon",
                resortLocationName = "TP. Hồ Chí Minh",
                resortLocationDisplayName = "Display Location 2",
                isVerify = true,
                nights = 14,
                pricePerNights = 3172336,
                totalPrice = 9746956,
                rentalPackageId = 6,
                rentalPackageName = "Package 2",
                checkinDate = "1-05-2025",
                checkoutDate = "14-05-2025",
                status = "AVAILABLE",
                unitTypeDTO = PublicPostingResponse.Content.UnitTypeDTO(
                    id = 2,
                    title = "Unit Type 2",
                    area = "94 m²",
                    bathrooms = 3,
                    bedrooms = 3,
                    bedsFull = 1,
                    bedsKing = 2,
                    bedsSofa = 1,
                    bedsMurphy = 1,
                    bedsQueen = 1,
                    bedsTwin = 1,
                    buildingsOption = "Building B",
                    description = "Spacious and luxurious unit type 2 with modern amenities.",
                    kitchen = "No Kitchen",
                    photos = "https://i.pinimg.com/736x/bd/3b/41/bd3b4180e2611be774e6755e274cd471.jpg",
                    sleeps = 2,
                    view = "Ocean View"
                ),
                active = false
            ),
            PublicPostingResponse.Content(
                rentalPostingId = 3,
                expiredDate = "2025-07-19",
                ownerId = 129,
                ownerName = "Owner 3",
                timeShareId = 47,
                roomInfoId = 43,
                roomName = "Room 3",
                resortId = 14,
                resortName = "Amanoi Resort",
                resortLocationName = "Vịnh Vĩnh Hy, Ninh Thuận",
                resortLocationDisplayName = "Display Location 3",
                isVerify = true,
                nights = 6,
                pricePerNights = 668500,
                totalPrice = 15104719,
                rentalPackageId = 7,
                rentalPackageName = "Package 3",
                checkinDate = "1-05-2025",
                checkoutDate = "14-05-2025",
                status = "BOOKED",
                unitTypeDTO = PublicPostingResponse.Content.UnitTypeDTO(
                    id = 3,
                    title = "Unit Type 3",
                    area = "97 m²",
                    bathrooms = 2,
                    bedrooms = 1,
                    bedsFull = 0,
                    bedsKing = 1,
                    bedsSofa = 1,
                    bedsMurphy = 0,
                    bedsQueen = 0,
                    bedsTwin = 0,
                    buildingsOption = "Building A",
                    description = "Spacious and luxurious unit type 3 with modern amenities.",
                    kitchen = "Partial Kitchen",
                    photos = "https://i.pinimg.com/736x/c0/5a/0d/c05a0dc992820cf7ea659f4fe3de5548.jpg",
                    sleeps = 8,
                    view = "City View"
                ),
                active = true
            )
        )


        val mockDailySummaryDataList = listOf(
            DailySummaryDataResponse(
                revenueCostByDateDtos = listOf(
                    DailySummaryDataResponse.RevenueCostByDateDto(
                        date = "2024-02-01",
                        revenueByCosts = 50000L,
                        revenueByDate = 150000L
                    ),
                    DailySummaryDataResponse.RevenueCostByDateDto(
                        date = "2024-02-02",
                        revenueByCosts = 60000L,
                        revenueByDate = 160000L
                    ),
                    DailySummaryDataResponse.RevenueCostByDateDto(
                        date = "2024-02-03",
                        revenueByCosts = 70000L,
                        revenueByDate = 170000L
                    )
                ),
                totalCosts = 180000L,
                totalRevenue = 480000L
            ),
            DailySummaryDataResponse(
                revenueCostByDateDtos = listOf(
                    DailySummaryDataResponse.RevenueCostByDateDto(
                        date = "2024-02-04",
                        revenueByCosts = 55000L,
                        revenueByDate = 155000L
                    ),
                    DailySummaryDataResponse.RevenueCostByDateDto(
                        date = "2024-02-05",
                        revenueByCosts = 65000L,
                        revenueByDate = 165000L
                    ),
                    DailySummaryDataResponse.RevenueCostByDateDto(
                        date = "2024-02-06",
                        revenueByCosts = 75000L,
                        revenueByDate = 175000L
                    )
                ),
                totalCosts = 195000L,
                totalRevenue = 495000L
            )
        )

        val dummyWalletList = listOf(
            WalletListResponse.Content(
                id = "4242",
                walletId = 6,
                money = 3404,
                transactionType = "DEPOSIT",
                description = "Giao dịch nạp tiền",
                paymentMethod = "PAYPAL",
                createdAt = "2025-01-22T11:16:52",
                fee = 6
            ),
            WalletListResponse.Content(
                id = "1951",
                walletId = 6,
                money = -4554,
                transactionType = "DEPOSIT",
                description = "Chuyển khoản",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-11-09T11:16:52",
                fee = 2
            ),
            WalletListResponse.Content(
                id = "7627",
                walletId = 5,
                money = 883,
                transactionType = "WITHDRAWAL",
                description = "Giao dịch nạp tiền",
                paymentMethod = "BANK_TRANSFER",
                createdAt = "2025-02-01T11:16:52",
                fee = 45
            ),
            WalletListResponse.Content(
                id = "1673",
                walletId = 4,
                money = -1370,
                transactionType = "TRANSFER",
                description = "Giao dịch rút tiền",
                paymentMethod = "BANK_TRANSFER",
                createdAt = "2025-01-23T11:16:52",
                fee = 5
            ),
            WalletListResponse.Content(
                id = "9479",
                walletId = 3,
                money = 3113,
                transactionType = "WITHDRAWAL",
                description = "Chuyển khoản",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-12-24T11:16:52",
                fee = 1
            ),
            WalletListResponse.Content(
                id = "1728",
                walletId = 9,
                money = -1491,
                transactionType = "TRANSFER",
                description = "Giao dịch nạp tiền",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-11-08T11:16:52",
                fee = 7
            ),

            )

        val dummyWalletReciceList = listOf(
            WalletListResponse.Content(
                id = "4242",
                walletId = 6,
                money = 3404,
                transactionType = "DEPOSIT",
                description = "Giao dịch nạp tiền",
                paymentMethod = "PAYPAL",
                createdAt = "2025-01-22T11:16:52",
                fee = 6
            ),
            WalletListResponse.Content(
                id = "1951",
                walletId = 6,
                money = 4554,
                transactionType = "DEPOSIT",
                description = "Chuyển khoản",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-11-09T11:16:52",
                fee = 2
            ),
            WalletListResponse.Content(
                id = "7627",
                walletId = 5,
                money = 883,
                transactionType = "WITHDRAWAL",
                description = "Giao dịch nạp tiền",
                paymentMethod = "BANK_TRANSFER",
                createdAt = "2025-02-01T11:16:52",
                fee = 45
            ),
            WalletListResponse.Content(
                id = "1673",
                walletId = 4,
                money = 1370,
                transactionType = "TRANSFER",
                description = "Giao dịch rút tiền",
                paymentMethod = "BANK_TRANSFER",
                createdAt = "2025-01-23T11:16:52",
                fee = 5
            ),
            WalletListResponse.Content(
                id = "9479",
                walletId = 3,
                money = 3113,
                transactionType = "WITHDRAWAL",
                description = "Chuyển khoản",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-12-24T11:16:52",
                fee = 1
            ),
            WalletListResponse.Content(
                id = "1728",
                walletId = 9,
                money = 1491,
                transactionType = "TRANSFER",
                description = "Giao dịch nạp tiền",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-11-08T11:16:52",
                fee = 7
            ),

            )

        val dummyWalletPaymentList = listOf(
            WalletListResponse.Content(
                id = "4242",
                walletId = 6,
                money = -3404,
                transactionType = "DEPOSIT",
                description = "Giao dịch nạp tiền",
                paymentMethod = "PAYPAL",
                createdAt = "2025-01-22T11:16:52",
                fee = 6
            ),
            WalletListResponse.Content(
                id = "1951",
                walletId = 6,
                money = -4554,
                transactionType = "DEPOSIT",
                description = "Chuyển khoản",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-11-09T11:16:52",
                fee = 2
            ),
            WalletListResponse.Content(
                id = "7627",
                walletId = 5,
                money = -883,
                transactionType = "WITHDRAWAL",
                description = "Giao dịch nạp tiền",
                paymentMethod = "BANK_TRANSFER",
                createdAt = "2025-02-01T11:16:52",
                fee = 45
            ),
            WalletListResponse.Content(
                id = "1673",
                walletId = 4,
                money = -1370,
                transactionType = "TRANSFER",
                description = "Giao dịch rút tiền",
                paymentMethod = "BANK_TRANSFER",
                createdAt = "2025-01-23T11:16:52",
                fee = 5
            ),
            WalletListResponse.Content(
                id = "9479",
                walletId = 3,
                money = -3113,
                transactionType = "WITHDRAWAL",
                description = "Chuyển khoản",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-12-24T11:16:52",
                fee = 1
            ),
            WalletListResponse.Content(
                id = "1728",
                walletId = 9,
                money = -1491,
                transactionType = "TRANSFER",
                description = "Giao dịch nạp tiền",
                paymentMethod = "CREDIT_CARD",
                createdAt = "2024-11-08T11:16:52",
                fee = 7
            )

        )




    }
}