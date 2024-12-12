package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.mapExchangeToUnitTypeBase
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MapViewActivity.MapViewActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail.ImageListActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.CustomDialog.UpdateExchangeBottomDialog
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity.ExchangeRequestOnPostActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.MyOrderActivity
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangDetailBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyExchangeDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangDetailBinding
    private lateinit var imagePostingAdapter : ImagePostingAdapter
    private var facilityAdapter = AmenitiesAdapter()
    private lateinit var tokenManager: TokenManager
    private val viewModel: MyExchangeDetailViewModel by viewModels()
    private var postingId: Int = 0

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyExchangDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getIntentValue()
        initAdapter()

        binding.customToolbar.onStartIconClick = {
            finish()
        }
        binding.shimmerViewContainer.startShimmer()


        eventClickShowMaps()
    }

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())

        featuresAdapter.submitOriginalList(listOf())
        entertainmentAdapter.submitOriginalList(listOf())
        kitchenAdapter.submitOriginalList(listOf())
        policyAdapter.submitOriginalList(listOf())

    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_POSTING_ID, 0)
        postingId = intent;
        if (tokenManager.isLoggedIn() && tokenManager.getAccessToken() != null) {
            viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
            observeMyPostingDetail()
        } else {
            showWarningToast("Bạn chưa đăng nhập", "Vui lòng đăng nhập để xem thông tin")
        }
    }

    private fun observeMyPostingDetail() {
        viewModel.myExchangeDetail.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                    binding.shimmerViewContainer.hideShimmer()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Lỗi Tải Dữ Liệu", "Không thể lấy thông tin bài đăng")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }
    }

    private fun eventClickShowMaps() {
        binding.llSeeMap.setOnClickListener {
            val intent = Intent(this, MapViewActivity::class.java)
            intent.putExtra(Constant.RESORT_LATITUDE, viewModel.myExchangeDetail.value?.data?.location?.latitude)
            intent.putExtra(Constant.RESORT_LONGITUDE, viewModel.myExchangeDetail.value?.data?.location?.longitude)
            startActivity(intent)
        }
    }


    private fun bindData(myExchangePostingDetail: MyExchangePostingDetailResponse) {
        // List Image
        bindDataListImage()

        // Unit Type
        bindDataUnitType(myExchangePostingDetail)

        // Amenities
        bindDataAmenities(myExchangePostingDetail)

        // Package Info
        binding.apply {
            if (myExchangePostingDetail.exchangePackageId != null) {
                val exchangePackageEnum =
                    ExchangePackageEnum.getPackageById(myExchangePostingDetail.exchangePackageId)
                if (exchangePackageEnum != null) {
                    tvPackageName.text = exchangePackageEnum?.name
                    if (myExchangePostingDetail.expiredDate != null) {
                        tvExpiredDay.text = Constant.formatDateByLocale(
                            myExchangePostingDetail.expiredDate ?: "2024-12-31",
                            binding.root.context
                        )
                    }
                }
            }

        }

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("Chi Tiết Bài Đăng Trao Đổi")
            setTitleDetail("${myExchangePostingDetail.resortName}")
        }

        // Resort Info
        binding.apply {
            tvResortName.text =
                myExchangePostingDetail.resortName + " | " + myExchangePostingDetail.roomCode
            tvLocation.text = myExchangePostingDetail.location?.displayName ?: "Không có thông tin"

            if (myExchangePostingDetail.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Check in Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                myExchangePostingDetail.checkinDate,
                this@MyExchangeDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(
                    myExchangePostingDetail.checkinDate,
                    this@MyExchangeDetailActivity
                )


            tvCheckoutDate.text = Constant.getFormattedDate(
                myExchangePostingDetail.checkoutDate,
                this@MyExchangeDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(
                    myExchangePostingDetail.checkoutDate,
                    this@MyExchangeDetailActivity
                )
        }

        //Request List
        binding.apply {
            cvRequestContaner.setOnClickListener {
                val intent =
                    Intent(this@MyExchangeDetailActivity, ExchangeRequestOnPostActivity::class.java)
                intent.putExtra(Constant.DEFAULT_EXCHANGE_REQUEST_ON_POST, postingId)
                startActivity(
                    intent
                )
            }
        }

        // UI DTB
        binding.includeDetailBilling.apply {
            llPostingBy.visibility = View.GONE
            tvResortNameDtb.text =
                myExchangePostingDetail.resortName + " | " + myExchangePostingDetail.roomCode
            tvCheckInDate.text = Constant.formatDateByLocale(
                myExchangePostingDetail.checkinDate,
                this@MyExchangeDetailActivity
            )
            tvCheckOutDate.text = Constant.formatDateByLocale(
                myExchangePostingDetail.checkoutDate,
                this@MyExchangeDetailActivity
            )
            tvNumberNight.text = "${myExchangePostingDetail.nights} đêm"
            tvLocation.text = myExchangePostingDetail.location?.displayName ?: "Không có thông tin"

            llRoomPricing.visibility = View.GONE
            Glide.with(binding.root.context)
                .load(myExchangePostingDetail.unitType.photos)
                .placeholder(R.drawable.ripple_effect_white)
                .error(R.drawable.im_material_mn)
                .into(imImageTimeshare)

        }
        binding.includeDetailBilling.llCancellationPolicy.visibility = View.GONE
        binding.includeDetailBilling.root.visibility = View.VISIBLE

        // Set Amenities
        facilityAdapter.submitList(listOf())

        // Show Status
        when (MyPostingStatus.fromApiStatus(myExchangePostingDetail.status)) {
            MyPostingStatus.PENDING_APPROVAL -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyPostingStatus.AWAITING_CONFIRMATION -> {
                applyStatusStyle(
                    this,
                    R.color.status_awaiting_confirmation_bg,
                    R.color.status_awaiting_confirmation_text
                )
            }

            MyPostingStatus.PROCESSING -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.success_bg_color
                )
            }

            MyPostingStatus.COMPLETED -> {
                applyStatusStyle(
                    this,
                    R.color.blue_header_section,
                    R.color.blue_full
                )
            }

            MyPostingStatus.ACCEPTED -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.green_verify
                )
            }

            MyPostingStatus.REJECTED -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            MyPostingStatus.PENDING_PRICING -> {
                applyStatusStyle(
                    this,
                    R.color.status_awaiting_confirmation_bg,
                    R.color.status_awaiting_confirmation_text
                )
            }

            MyPostingStatus.CLOSED -> {
                applyStatusStyle(
                    this,
                    R.color.status_closed_bg,
                    R.color.status_closed_text
                )
            }

            MyPostingStatus.REJECT_PRICE -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            else -> {
                // Default or unknown status case
                applyStatusStyle(
                    this,
                    R.color.status_unknown_bg,
                    R.color.status_unknown_text
                )
            }
        }
        binding.tvStatus.text =
            MyPostingStatus.fromApiStatus(myExchangePostingDetail.status)
                ?.getDescription(this)

        // Show Update Status
        if (MyPostingStatus.fromApiStatus(myExchangePostingDetail.status) == MyPostingStatus.PROCESSING) {
            binding.apply {
                customToolbar.isShowEndIcon(true)
                customToolbar.onEndIconClick = {
                    showUpdateExchangeDialog()
                }
            }
        } else {
            binding.apply {
                customToolbar.isShowEndIcon(false)
            }
        }

        // Description
        binding.etNote.setText(myExchangePostingDetail.description)


    }

    private fun bindDataUnitType(data: MyExchangePostingDetailResponse) {
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
                val unitTypeBase = mapExchangeToUnitTypeBase(data)
                val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
                unitTypeDataDialog.show(supportFragmentManager, "UnitTypeDataDialog")
            }
        }
    }

    private fun bindDataAmenities(data: MyExchangePostingDetailResponse) {
        featuresAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        entertainmentAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        kitchenAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        policyAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))


        val binding = binding.includeAmenities
        binding.rvFeatures.apply {
            featuresAdapter.filterByAmenityTypes(AmenityType.FEATURES)
            layoutManager = FlexboxLayoutManager(this@MyExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            entertainmentAdapter.filterByAmenityTypes(AmenityType.ENTERTAINMENT)
            layoutManager = FlexboxLayoutManager(this@MyExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = entertainmentAdapter
        }

        binding.rvKitchen.apply {
            kitchenAdapter.filterByAmenityTypes(AmenityType.KITCHEN)
            layoutManager = FlexboxLayoutManager(this@MyExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = kitchenAdapter
        }

        binding.rvPolicy.apply {
            policyAdapter.filterByAmenityTypes(AmenityType.POLICY)
            layoutManager = FlexboxLayoutManager(this@MyExchangeDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = policyAdapter
        }


    }

    private fun bindDataListImage() {
        // List Destination
        imagePostingAdapter = ImagePostingAdapter()

        imagePostingAdapter.submitList(viewModel.getImageList())

        val layoutManagerCheck = if (viewModel.getImageList().size == 1) {
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        } else {
            SpannedGridLayoutManager(
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
        }
        binding.recyclerViewResortImage.apply {
            adapter = imagePostingAdapter
            layoutManager = layoutManagerCheck
        }
        imagePostingAdapter.onItemClickListener = { position ->
            val intent = Intent(this@MyExchangeDetailActivity, ImageListActivity::class.java)
            intent.putExtra(Constant.IMAGE_POSITION, position)
            intent.putStringArrayListExtra(
                Constant.IMAGE_LIST,
                ArrayList(viewModel.getImageList())
            )
            startActivity(intent)
        }
        imagePostingAdapter.submitList(viewModel.getImageList())

    }

    private fun showUpdateExchangeDialog() {
        viewModel.resetUpdateExchangeResponse()
        val updateExchangeBottomDialog = UpdateExchangeBottomDialog(
            description = binding.etNote.text.toString(),
            myExchangeDetailViewModel = viewModel
        )
        updateExchangeBottomDialog.show(supportFragmentManager, "UpdateExchangeBottomDialog")
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

    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            llStatusContainer.visibility = View.VISIBLE
            llStatusContainer.backgroundTintList = context.getColorStateList(backgroundColorRes)
            tvStatus.setTextColor(context.getColor(textColorRes))
            cardStatus.setStrokeColor(context.getColor(textColorRes))
        }
    }

    fun mapRoomAmenitiesToAmenitiesModel(roomAmenities: List<MyExchangePostingDetailResponse.RoomAmenity>): List<AmenitiesModel> {
        return roomAmenities.map { roomAmenity ->
            AmenitiesModel(
                name = roomAmenity.name,
                type = roomAmenity.type,
                isChecked = false // Mặc định là chưa được chọn
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_POSTING_ID, 0)
        viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
    }
}