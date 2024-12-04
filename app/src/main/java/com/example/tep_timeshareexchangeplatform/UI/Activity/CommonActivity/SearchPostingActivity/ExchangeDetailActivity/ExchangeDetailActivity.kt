package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ExchangeDetailActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeBase
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.LoginActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity.MapViewActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.OwnerInfoActivity.OwnerInfoActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity.RequestExchangeActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.SearchPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberInfoDialog
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail.ImageListActivity
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityExchangeDetailBinding
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTimeshareDetailBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ExchangeDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityTimeshareDetailBinding
    private var facilityAdapter = AmenitiesAdapter()
    private var reviewAdapter = ReviewAdapter()
    private var imagePostingAdapter = ImagePostingAdapter()
    private lateinit var tokenManager: TokenManager
    private val exchangeDetailViewModel: ExchangeDetailViewModel by viewModels()

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()

    private var exchangeId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimeshareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)
        initAdapter()
        getIntentValue()
        setToolBarAction()
        eventClickShowMaps()
    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_POSTING_ID, 0)
        if (intent == 0) {
            finish()
        }
        exchangeId = intent
        exchangeDetailViewModel.getExchangeDetail(intent)
        observePostingDetail()

    }

    private fun observePostingDetail() {
        exchangeDetailViewModel.exchangeDetail.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                    binding.shimmerViewContainer.startShimmer()
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.hideShimmer()
                    Log.d("CheckPostingDetailData", "observePostingDetail: ${it.data}")
                    bindDataPostingDetail(it.data!!)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Lỗi", "Không thể tải dữ liệu")
                }
            }
        }

        exchangeDetailViewModel.isCustomerExist.observe(this) {
            when (it.status) {
                // Case User have Profile Info
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    saveUserLogState(it)
                    // Case User is Member
                    if (it.data!!.isMember) {
                        val intent = Intent(this, RequestExchangeActivity::class.java)
                        intent.putExtra(Constant.DEFAULT_POSTING_ID, exchangeId)
                        startActivity(intent)
                    } else {
                        intentToMemberShipActivity()
                    }
                }
                // Case User have not Profile Info
                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message!!.contains("404")) {
                        intentToMemberShipActivity()
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_USER)
                    }
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }


    }

    private fun bindDataPostingDetail(postingDetail: ExchangeDetailResponse) {
        binding.cvRequestContanerExchange.visibility = View.VISIBLE
        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetail.resortName}")
            setTitleDetail("${postingDetail.checkinDate} đến ${postingDetail.checkoutDate}")

        }

        // Bind Image Timeshare
        bindDataListImage(postingDetail.imageUrls)

        // Resort Info
        binding.apply {
            tvResortName.text = postingDetail.resortName + " | " + postingDetail.roomCode
            tvLocation.text = postingDetail.location.displayName

            if (postingDetail.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                postingDetail.checkinDate,
                this@ExchangeDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkinDate, this@ExchangeDetailActivity)


            tvCheckoutDate.text = Constant.getFormattedDate(
                postingDetail.checkoutDate,
                this@ExchangeDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkoutDate, this@ExchangeDetailActivity)
        }

        // Set Unit Type Of Posting
        bindDataUnitType(postingDetail)

        // Amenities
        bindDataAmenities(postingDetail)

        // UI DTB
        binding.apply {
            tvLocationDtb.text = postingDetail.location.displayName
            tvResortNameDtb.text = postingDetail.resortName + " | " + postingDetail.roomCode
            tvCheckInDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkinDate, this@ExchangeDetailActivity)
            tvCheckOutDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkoutDate, this@ExchangeDetailActivity)
            tvNightDtb.text = "${postingDetail.nights} đêm"
            tvRoomPricePerNight.visibility = View.GONE
            tvEstimatedTotalPrice.visibility = View.GONE
            tvPostedBy.text = "Đăng bởi ${postingDetail.ownerName}"

            Glide.with(this@ExchangeDetailActivity)
                .load(postingDetail.unitType.photos)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.imImageTimeshare)

        }

        eventClickRequestButton(postingDetail)


    }

    private fun bindDataAmenities(data: ExchangeDetailResponse) {
        featuresAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        entertainmentAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        kitchenAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        policyAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))


        val binding = binding.includeAmenities
        binding.rvFeatures.apply {
            featuresAdapter.filterByAmenityTypes(AmenityType.FEATURES)
            layoutManager = FlexboxLayoutManager(this@ExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            entertainmentAdapter.filterByAmenityTypes(AmenityType.ENTERTAINMENT)
            layoutManager = FlexboxLayoutManager(this@ExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = entertainmentAdapter
        }

        binding.rvKitchen.apply {
            kitchenAdapter.filterByAmenityTypes(AmenityType.KITCHEN)
            layoutManager = FlexboxLayoutManager(this@ExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = kitchenAdapter
        }

        binding.rvPolicy.apply {
            policyAdapter.filterByAmenityTypes(AmenityType.POLICY)
            layoutManager = FlexboxLayoutManager(this@ExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = policyAdapter
        }


    }

    private fun bindDataUnitType(data: ExchangeDetailResponse) {
        // Set Unit Type Of Posting
        binding.includeUnitType.apply {
            tvRoomCode.text = data.roomCode
            tvRoomName.text = data.roomName
            tvRoomType.text = data.unitType.title

            // Bath
            tvNumBath.text = data.unitType.bathrooms.toString()
            tvBed.text = data.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to data.unitType.bedsFull,
                "bedsKing" to data.unitType.bedsKing,
                "bedsSofa" to data.unitType.bedsSofa,
                "bedsMurphy" to data.unitType.bedsMurphy,
                "bedsQueen" to data.unitType.bedsQueen,
                "bedsTwin" to data.unitType.bedsTwin
            )
            tvNumBed.text = data.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = data.unitType.kitchen
            tvNumKitchen.text = 1.toString()


            // Max Guest
            tvNumPerson.text = data.unitType.sleeps.toString()
            tvPerson.text =
                "${data.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later
            // Unit Type Detail
            btnViewDetail.setOnClickListener {
                val unitTypeBase = mapToUnitTypeBase(
                    data.unitType,
                    data.resortId,
                    data.active,
                    data.unitTypeAmenities
                )
                val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
                unitTypeDataDialog.show(supportFragmentManager, "UnitTypeDataDialog")
            }
        }
    }

    private fun callCheckProfileCustomer() {
        if (!tokenManager.isLoggedIn()) {
            showWarningToast(
                "Bạn chưa đăng nhập!",
                "Vui lòng đăng nhập để thực hiện chức năng này"
            )
            startActivity(Intent(this, LoginActivity::class.java))
        }

        if (tokenManager.getAccessToken() != null) {
            exchangeDetailViewModel.callIsCustomerExist(tokenManager.getAccessToken().toString())
        }
    }

    private fun eventClickRequestButton(postingDetail: ExchangeDetailResponse) {
        // Button
        val customerInfo = tokenManager.getProfileInfo()
        if (postingDetail.ownerId == customerInfo?.id) {
            binding.cvRequestContanerExchange.backgroundTintList =
                resources.getColorStateList(R.color.green_verify)
            binding.tvRequestExchange.text = "Bài Đăng Của Bạn"
        } else {
            binding.tvRequestExchange.text = "Gửi Yêu Cầu Trao Đổi"
            binding.cvRequestContanerExchange.setOnClickListener {
                callCheckProfileCustomer()
            }
        }
    }

    private fun eventClickShowMaps() {
        binding.llSeeMap.setOnClickListener {
            val intent = Intent(this, MapViewActivity::class.java)
            intent.putExtra(
                Constant.RESORT_LATITUDE,
                exchangeDetailViewModel.exchangeDetail.value?.data?.location?.latitude
            )
            intent.putExtra(
                Constant.RESORT_LONGITUDE,
                exchangeDetailViewModel.exchangeDetail.value?.data?.location?.longitude
            )
            startActivity(intent)
        }
    }

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

    private fun initAdapter() {
        imagePostingAdapter.submitList(listOf())
        facilityAdapter.submitList(listOf())
        reviewAdapter.submitList(listOf())
        imagePostingAdapter.submitList(listOf())
    }

    private fun bindDataListImage(imageList: List<String>) {
        // List Destination

        val manager = SpannedGridLayoutManager(
            object : SpannedGridLayoutManager.GridSpanLookup {
                override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
                    // Conditions for 2x2 items
                    return when (position) {
                        0 -> SpannedGridLayoutManager.SpanInfo(2, 2)
                        1 -> SpannedGridLayoutManager.SpanInfo(2, 2)
                        2 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        3 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        4 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        5 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        else -> {
                            SpannedGridLayoutManager.SpanInfo(1, 1)
                        }
                    }
                }
            },
            4,  // number of columns
            1f // how big is default item
        )

        imagePostingAdapter.submitList(imageList)
        if (imageList.size == 1) {
            val layoutManagerCheck =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewResortImage.apply {
                adapter = imagePostingAdapter
                layoutManager = layoutManagerCheck
            }
        } else {
            binding.recyclerViewResortImage.apply {
                adapter = imagePostingAdapter
                layoutManager = manager
            }
        }

        imagePostingAdapter.onItemClickListener = { position ->
            val intent = Intent(this@ExchangeDetailActivity, ImageListActivity::class.java)
            intent.putExtra(Constant.IMAGE_POSITION, position)
            intent.putStringArrayListExtra(
                Constant.IMAGE_LIST,
                ArrayList(imageList)
            )
            startActivity(intent)
        }

    }

    private fun setToolBarAction() {
        binding.customToolbar.onStartIconClick = {
            finish()
        }
    }

    fun mapToUnitTypeBase(
        unitType: ExchangeDetailResponse.UnitType,
        resortId: Int,
        isActive: Boolean,
        unitTypeAmenities: List<ExchangeDetailResponse.UnitTypeAmenity>
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
            price = 0, // Giá chưa xác định, cập nhật theo logic
            description = unitType.description,
            kitchen = unitType.kitchen,
            photos = unitType.photos,
            resortId = resortId,
            sleeps = unitType.sleeps,
            view = unitType.view,
            isActive = isActive,
            unitTypeAmenitiesDTOS = unitTypeAmenities.map { amenity ->
                UnitTypeBase.UnitTypeAmenitiesDTOS(
                    name = amenity.name,
                    type = amenity.type,
                    isActive = true // Có thể điều chỉnh theo dữ liệu thực tế
                )
            }
        )
    }

    fun mapRoomAmenitiesToAmenitiesModel(roomAmenities: List<ExchangeDetailResponse.RoomAmenity>): List<AmenitiesModel> {
        return roomAmenities.map { roomAmenity ->
            AmenitiesModel(
                name = roomAmenity.name,
                type = roomAmenity.type,
                isChecked = false // Mặc định là chưa được chọn
            )
        }
    }

    private fun showCreateProfileDialog() {
        val dialogUpdateCustomer =
            MemberInfoDialog(
                this,
                object : MemberInfoDialog.ConfirmCallback {
                    override fun positiveAction(customerDTO: CustomerDTO) {
                        callCreateCustomer(customerDTO)
                    }
                })
        dialogUpdateCustomer.show()
    }

    private fun callCreateCustomer(customerDTO: CustomerDTO) {
        exchangeDetailViewModel.callCreateCustomer(
            tokenManager.getAccessToken().toString(),
            customerDTO
        )
    }

    private fun saveUserLogState(it: Resource<CustomerProfileResponse>) {
        if (it.data!!.isMember) {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
        }
        // User Is Not Member
        else {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
        }
        tokenManager.saveProfileInfo(it.data)
    }

    private fun intentToMemberShipActivity() {
        startActivity(Intent(this, MemberShipActivity::class.java))
    }

}