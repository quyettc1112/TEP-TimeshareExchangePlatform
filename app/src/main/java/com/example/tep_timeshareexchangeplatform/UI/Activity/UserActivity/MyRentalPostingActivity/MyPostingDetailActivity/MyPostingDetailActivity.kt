package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPrice
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPriceLong
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.mapToUnitTypeBase
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail.ImageListActivity
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyPostingDetailBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostingDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyPostingDetailBinding
    private var imagePostingAdapter = ImagePostingAdapter()
    private var facilityAdapter = AmenitiesAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private val viewModel: MyPostingDetailViewModel by viewModels()

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPostingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentValue()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()


        binding.customToolbar.onStartIconClick = {
            finish()
        }
        binding.shimmerViewContainer.startShimmer()

    }

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())
        imagePostingAdapter.submitList(listOf())

        featuresAdapter.submitOriginalList(listOf())
        entertainmentAdapter.submitOriginalList(listOf())
        kitchenAdapter.submitOriginalList(listOf())
        policyAdapter.submitOriginalList(listOf())
    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_POSTING_ID, 0)
        val token = TokenManager(this)
        if (token.isLoggedIn() && token.getAccessToken() != null) {
            viewModel.getMyPostingDetail(token.getAccessToken().toString(), intent)
            observeMyPostingDetail()
        } else {
            MotionToast.Companion.createColorToast(
                this,
                "Bạn chưa đăng nhập",
                "Vui lòng đăng nhập để xem thông tin",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
        }
    }

    private fun observeMyPostingDetail() {
        viewModel.postingDetailResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                    binding.shimmerViewContainer.hideShimmer()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Lỗi",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }
    }

    private fun bindData(myRentalPostingDetailResponse: MyRentalPostingDetailResponse) {
        // Image List
        bindDataListImage(myRentalPostingDetailResponse.imageUrls)

        // Amenities
        bindDataAmenities(myRentalPostingDetailResponse)

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${myRentalPostingDetailResponse.unitType.title}")
            setTitleDetail("${myRentalPostingDetailResponse.checkinDate} - ${myRentalPostingDetailResponse.checkoutDate}")
        }

        // Resort Info
        binding.apply {
            tvResortName.text =
                myRentalPostingDetailResponse.resortName + " | " + myRentalPostingDetailResponse.unitType.title
            tvLocation.text = myRentalPostingDetailResponse.address

            if (myRentalPostingDetailResponse.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                myRentalPostingDetailResponse.checkinDate,
                this@MyPostingDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(
                    myRentalPostingDetailResponse.checkinDate,
                    this@MyPostingDetailActivity
                )


            tvCheckoutDate.text = Constant.getFormattedDate(
                myRentalPostingDetailResponse.checkoutDate,
                this@MyPostingDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(
                    myRentalPostingDetailResponse.checkoutDate,
                    this@MyPostingDetailActivity
                )
        }

        // BindDAta Package
        // Package Info
        binding.apply {
            if (myRentalPostingDetailResponse.rentalPackageName != null) {
                val rentalPackageEnum =
                    RentalPackageEnum.getPackageByName(myRentalPostingDetailResponse.rentalPackageName)
                if (rentalPackageEnum != null) {
                    tvPackageName.text = rentalPackageEnum?.name
                    if (myRentalPostingDetailResponse.expiredDate != null) {
                        tvExpiredDay.text = Constant.formatDateByLocale(
                            myRentalPostingDetailResponse.expiredDate ?: "2024-12-31",
                            binding.root.context
                        )
                    }
                }
            }
        }

        // Set Unit Type Of Posting
        bindDataUnitType(myRentalPostingDetailResponse)
        // Cancel Policy
        binding.apply {
            if (myRentalPostingDetailResponse.cancelType.toString() == "null") {
                tvCancelPolicy.text = "Không có"
                includeDetailBilling.tvCancellationPolicy.text = "Không có"
            } else {
                val refundPolicy = RefundPolicy.getShortDescriptionFromName(
                    this@MyPostingDetailActivity,
                    myRentalPostingDetailResponse.cancelType.toString()
                )
                tvCancelPolicy.text = refundPolicy
                includeDetailBilling.tvCancellationPolicy.text = refundPolicy
            }
        }

        // UI DTB
        binding.includeDetailBilling.apply {
            llPostingBy.visibility = View.GONE
            llRoomPricing.visibility = View.GONE
            tvResortNameDtb.text =
                myRentalPostingDetailResponse.resortName + " | " + myRentalPostingDetailResponse.unitType.title
            tvCheckInDate.text = Constant.formatDateByLocale(
                myRentalPostingDetailResponse.checkinDate,
                this@MyPostingDetailActivity
            )
            tvCheckOutDate.text = Constant.formatDateByLocale(
                myRentalPostingDetailResponse.checkoutDate,
                this@MyPostingDetailActivity
            )
            tvNumberNight.text = "${myRentalPostingDetailResponse.nights} đêm"

            if (myRentalPostingDetailResponse.pricePerNights == 0L) {
                tvRoomPricePerNight.text = "Đang Chờ Xác Nhận"
                tvEstimatedTotalPrice.text = "Đang Chờ Xác Nhận"
            } else {
                tvRoomPricePerNight.text =
                    "${myRentalPostingDetailResponse.pricePerNights?.let { formatPriceLong(it) }} đ"
                tvEstimatedTotalPrice.text =
                    "${myRentalPostingDetailResponse.totalPrice?.let { formatPriceLong(it) }} đ"
            }
            tvLocation.text = myRentalPostingDetailResponse.address

            Glide.with(binding.root.context)
                .load(myRentalPostingDetailResponse.unitType.photos)
                .placeholder(R.drawable.ripple_effect_white)
                .error(R.drawable.im_material_mn)
                .into(imImageTimeshare)

        }

        binding.includeDetailBilling.root.visibility = View.VISIBLE

        // Set Amenities
        facilityAdapter.submitList(listOf())



        when (MyPostingStatus.fromApiStatus(myRentalPostingDetailResponse.status)) {
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
                    R.color.green_verify
                )
            }

            MyPostingStatus.COMPLETED -> {
                applyStatusStyle(
                    this,
                    R.color.blue_header_section,
                    R.color.blue_full
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
            MyPostingStatus.fromApiStatus(myRentalPostingDetailResponse.status)
                ?.getDescription(this)

    }


    private fun bindDataUnitType(data: MyRentalPostingDetailResponse) {
        // Set Unit Type Of Posting
        binding.includeUnitType.apply {
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
                val unitTypeBase = mapToUnitTypeBase(data.unitType, data.unitTypeAmenities)
                val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
                unitTypeDataDialog.show(supportFragmentManager, "UnitTypeDataDialog")
            }
        }
    }

    private fun bindDataAmenities(data: MyRentalPostingDetailResponse) {
        featuresAdapter.submitOriginalList(mapToAmenitiesModel(data.roomAmenities))
        entertainmentAdapter.submitOriginalList(mapToAmenitiesModel(data.roomAmenities))
        kitchenAdapter.submitOriginalList(mapToAmenitiesModel(data.roomAmenities))
        policyAdapter.submitOriginalList(mapToAmenitiesModel(data.roomAmenities))


        val binding = binding.includeAmenities
        binding.rvFeatures.apply {
            featuresAdapter.filterByAmenityTypes(AmenityType.FEATURES)
            layoutManager = FlexboxLayoutManager(this@MyPostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            entertainmentAdapter.filterByAmenityTypes(AmenityType.ENTERTAINMENT)
            layoutManager = FlexboxLayoutManager(this@MyPostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = entertainmentAdapter
        }

        binding.rvKitchen.apply {
            kitchenAdapter.filterByAmenityTypes(AmenityType.KITCHEN)
            layoutManager = FlexboxLayoutManager(this@MyPostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = kitchenAdapter
        }

        binding.rvPolicy.apply {
            policyAdapter.filterByAmenityTypes(AmenityType.POLICY)
            layoutManager = FlexboxLayoutManager(this@MyPostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = policyAdapter
        }


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
            val layoutManagerCheck = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
            val intent = Intent(this@MyPostingDetailActivity, ImageListActivity::class.java)
            intent.putExtra(Constant.IMAGE_POSITION, position)
            intent.putStringArrayListExtra(
                Constant.IMAGE_LIST,
                ArrayList(imageList)
            )
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollHelper.pauseAutoScroll()
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
            llStatusContainer.setBackgroundColor(context.getColor(backgroundColorRes))
            tvStatus.setTextColor(context.getColor(textColorRes))
            cardStatus.setStrokeColor(context.getColor(textColorRes))
        }
    }

    fun mapToAmenitiesModel(amenities: List<MyRentalPostingDetailResponse.RoomAmenity>): List<AmenitiesModel> {
        return amenities.map { amenity ->
            AmenitiesModel(
                name = amenity.name,
                type = amenity.type,
                isChecked = false // Mặc định là chưa được chọn
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}