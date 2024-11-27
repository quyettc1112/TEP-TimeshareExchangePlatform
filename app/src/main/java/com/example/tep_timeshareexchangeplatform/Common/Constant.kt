package com.example.tep_timeshareexchangeplatform.Common

import android.content.Context
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.BlogModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.DestinationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.FAQModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.IntroSliderModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.NotificationModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ReviewModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeBase
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType
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

        const val DEFAULT_MY_POSTING_RESORT_NAME = "myPostingResortName"
        const val DEFAULT_MY_POSTING_ROOM_NAME = "myPostingRoomName"
        const val DEFAULT_MY_POSTING_CHECK_IN_DATE = "myPostingCheckInDate"
        const val DEFAULT_MY_POSTING_CHECK_OUT_DATE = "myPostingCheckOutDate"
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
                name = "Mạng Internet",
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

        val notificationList = listOf(
            NotificationModel(
                title = "Unwind Thông Báo",
                typeNotification = NotificationType.NOTIFICATION, // Loại thông báo
                description = "Chào mừng bạn đến với Unwind! Hãy khám phá và tận hưởng những trải nghiệm tuyệt vời cùng các dịch vụ đẳng cấp mà chúng tôi mang đến.",
                iconResId = R.raw.anim_notification, // Thay thế bằng icon thực tế của bạn
                timestamp = "5 phút trước",
                isRead = false
            ),
            NotificationModel(
                title = "Bạn đã nạp tiền vào ví Unwind",
                typeNotification = NotificationType.DEPOSIT, // Loại thông báo
                description = "Bạn đã nạp thành công 1.470.000 VNĐ vào ví Unwind. Nhấn để kiểm tra chi tiết giao dịch của bạn ngay bây giờ.",
                iconResId = R.raw.anim_deposit, // Thay thế bằng icon thực tế của bạn
                timestamp = "1 phút trước",
                isRead = false
            ),

            NotificationModel(
                title = "Bài Đăng Được Chấp Nhận",
                typeNotification = NotificationType.ACCEPT_POSTING, // Loại thông báo
                description = "Bài đăng của bạn đã được duyệt thành công bởi hệ thống. Hãy truy cập ngay để theo dõi lượng quan tâm và quản lý bài viết hiệu quả hơn.",
                iconResId = R.raw.anim_accept_posting, // Thay thế bằng icon thực tế của bạn
                timestamp = "1 giờ trước",
                isRead = false
            ),

            NotificationModel(
                title = "Bài Đăng Bị Từ Chối",
                typeNotification = NotificationType.REJECT_POSTING, // Loại thông báo
                description = "Rất tiếc, bài viết của bạn đã bị từ chối bởi hệ thống. Hãy kiểm tra kỹ nội dung bài viết, chỉnh sửa theo hướng dẫn và gửi lại để được xem xét.",
                iconResId = R.raw.anim_reject_posting, // Thay thế bằng icon thực tế của bạn
                timestamp = "20/08/2024",
                isRead = false
            ),

            NotificationModel(
                title = "Đặt Phòng Thành Công",
                typeNotification = NotificationType.DONE_BOOKING, // Loại thông báo
                description = "Chúc mừng bạn đã đặt phòng thành công tại Resort Vinpearl. Nhấn vào đây để xem thông tin đầy đủ.",
                iconResId = R.raw.anim_done_booking, // Thay thế bằng icon thực tế của bạn
                timestamp = "20/08/2024",
                isRead = false
            ),

            NotificationModel(
                title = "Gia Hạn Gói Thành Viên Thành Công",
                typeNotification = NotificationType.MEMBERSHIP, // Loại thông báo
                description = "Bạn đã gia hạn thành công gói Membership của mình. Nhấn để xem thông tin chi tiết về gói thành viên của bạn.",
                iconResId = R.raw.anim_membership, // Thay thế bằng icon thực tế của bạn
                timestamp = "1 ngày trước",
                isRead = false
            ),
            NotificationModel(
                title = "Unwind Thông Báo",
                typeNotification = NotificationType.NOTIFICATION, // Loại thông báo
                description = "Chào mừng bạn đến với Unwind!! Hãy khám phá và tận hưởng những trải nghiệm tuyệt vời cùng các dịch vụ đẳng cấp mà chúng tôi mang đến.",
                iconResId = R.raw.anim_notification, // Thay thế bằng icon thực tế của bạn
                timestamp = "5 phút trước",
                isRead = true
            ),
            NotificationModel(
                title = "Bạn đã nạp tiền vào ví Unwind",
                typeNotification = NotificationType.DEPOSIT, // Loại thông báo
                description = "Bạn đã nạp thành công 1.470.000 VNĐ vào ví Unwind. Nhấn để kiểm tra chi tiết giao dịch của bạn ngay bây giờ.",
                iconResId = R.raw.anim_deposit, // Thay thế bằng icon thực tế của bạn
                timestamp = "1 phút trước",
                isRead = true
            ),

            NotificationModel(
                title = "Bài Đăng Được Chấp Nhận",
                typeNotification = NotificationType.ACCEPT_POSTING, // Loại thông báo
                description = "Bài đăng của bạn đã được duyệt thành công bởi hệ thống. Hãy truy cập ngay để theo dõi lượng quan tâm và quản lý bài viết hiệu quả hơn.",
                iconResId = R.raw.anim_accept_posting, // Thay thế bằng icon thực tế của bạn
                timestamp = "1 giờ trước",
                isRead = true
            ),

            NotificationModel(
                title = "Bài Đăng Bị Từ Chối",
                typeNotification = NotificationType.REJECT_POSTING, // Loại thông báo
                description = "Rất tiếc, bài viết của bạn đã bị từ chối bởi hệ thống. Hãy kiểm tra kỹ nội dung bài viết, chỉnh sửa theo hướng dẫn và gửi lại để được xem xét.",
                iconResId = R.raw.anim_reject_posting, // Thay thế bằng icon thực tế của bạn
                timestamp = "20/08/2024",
                isRead = true
            ),

            NotificationModel(
                title = "Đặt Phòng Thành Công",
                typeNotification = NotificationType.DONE_BOOKING, // Loại thông báo
                description = "Chúc mừng bạn đã đặt phòng thành công tại Resort Vinpearl. Đừng quên kiểm tra các chi tiết đặt phòng để có trải nghiệm lưu trú tốt nhất. Nhấn vào đây để xem thông tin đầy đủ.",
                iconResId = R.raw.anim_done_booking, // Thay thế bằng icon thực tế của bạn
                timestamp = "20/08/2024",
                isRead = true
            ),

            NotificationModel(
                title = "Gia Hạn Gói Thành Viên Thành Công",
                typeNotification = NotificationType.MEMBERSHIP, // Loại thông báo
                description = "Bạn đã gia hạn thành công gói Membership của mình. Những quyền lợi ưu đãi sẽ được áp dụng ngay lập tức. Nhấn để xem thông tin chi tiết về gói thành viên của bạn.",
                iconResId = R.raw.anim_membership, // Thay thế bằng icon thực tế của bạn
                timestamp = "1 ngày trước",
                isRead = true
            )
        )

    }
}