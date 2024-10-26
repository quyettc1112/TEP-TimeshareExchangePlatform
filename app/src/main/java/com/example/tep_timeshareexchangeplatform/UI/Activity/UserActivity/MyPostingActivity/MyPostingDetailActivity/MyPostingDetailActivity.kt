package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingDetailActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyPostingDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostingDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyPostingDetailBinding
    private var imageAdapter = ImageAdapter(Constant.listTimeshareImage)
    private var facilityAdapter = AmenitiesAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private val viewModel: MyPostingDetailViewModel by viewModels()


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
        setListImageTimeshare()
        setAmenitiesListTimeshare()

        binding.customToolbar.onStartIconClick = {
            finish()
        }

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

    private fun bindData(postingDetailResponse: PostingDetailResponse) {
        // Hide Unessary View
        binding.includePackagePosting.apply {
            tvPackageDescription.visibility = View.GONE
        }

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetailResponse.unitType.title}")
            setTitleDetail("${postingDetailResponse.checkinDate} - ${postingDetailResponse.checkoutDate}")
        }

        // Resort Info
        binding.apply {
            tvResortName.text =
                postingDetailResponse.resortName + " | " + postingDetailResponse.unitType.title
            tvLocation.text = postingDetailResponse.address

            if (postingDetailResponse.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckInDate.text = postingDetailResponse.checkinDate
            tvCheckOutDate.text = postingDetailResponse.checkoutDate
            tvNights.text = "${postingDetailResponse.nights} đêm"
        }

        // Set Unit Type Of Posting
        binding.apply {
            tvRoomName.text =
                "Chi Tiết Phòng | ${postingDetailResponse.unitType.title} #${postingDetailResponse.roomName}"

            // Bath
            tvNumBath.text = postingDetailResponse.unitType.bathrooms.toString()
            tvBed.text = postingDetailResponse.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to postingDetailResponse.unitType.bedsFull,
                "bedsKing" to postingDetailResponse.unitType.bedsKing,
                "bedsSofa" to postingDetailResponse.unitType.bedsSofa,
                "bedsMurphy" to postingDetailResponse.unitType.bedsMurphy,
                "bedsQueen" to postingDetailResponse.unitType.bedsQueen,
                "bedsTwin" to postingDetailResponse.unitType.bedsTwin
            )
            tvNumBed.text = postingDetailResponse.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = postingDetailResponse.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = postingDetailResponse.unitType.sleeps.toString()
            tvPerson.text = "${postingDetailResponse.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later

        }

        // Cancel Policy
        binding.apply {
            if (postingDetailResponse.cancelType.toString() == "null") {
                tvCancelPolicy.text = "Không có"
                includeDetailBilling.tvCancellationPolicy.text = "Không có"
            } else {
                tvCancelPolicy.text = postingDetailResponse.cancelType.toString()
                includeDetailBilling.tvCancellationPolicy.text =
                    postingDetailResponse.cancelType.toString()
            }

        }

        // UI DTB
        binding.includeDetailBilling.apply {
            tvResortNameDtb.text =
                postingDetailResponse.resortName + " | " + postingDetailResponse.unitType.title
            tvCheckInDate.text = postingDetailResponse.checkinDate
            tvCheckOutDate.text = postingDetailResponse.checkoutDate
            tvNumberNight.text = "${postingDetailResponse.nights} đêm"
            tvRoomPricePerNight.text = "${postingDetailResponse.pricePerNights} đ"
            tvEstimatedTotalPrice.text = "${postingDetailResponse.totalPrice} đ"
            tvLocation.text = postingDetailResponse.address
        }

        // Data for Request
        binding.apply {
            tvPrice.text = "${postingDetailResponse.totalPrice} đ"
            tvDate.text =
                "${postingDetailResponse.checkinDate} - ${postingDetailResponse.checkoutDate}"

        }

        // Set Amenities
        facilityAdapter.submitList(postingDetailResponse.resortAmenities)

        binding.apply {
            // Thiết lập văn bản trạng thái
            tvStatus.text = when (postingDetailResponse.status) {
                "PendingApproval" -> "Đang chờ duyệt"
                "Processing" -> "Đang xử lý"
                "PendingPricing" -> "Đã từ chối"
                "AwaitingConfirmation" -> "Hết hạn"
                "Closed" -> "Đã hủy"
                "Completed" -> "Hoàn thành"
                "Expired" -> "Hoàn thành"
                else -> "Không xác định"
            }

            // Thiết lập màu nền và màu chữ dựa trên trạng thái
            val (backgroundColor, textColor) = when (postingDetailResponse.status) {
                "PendingApproval" -> Pair(R.color.pendingApprovalBackground, R.color.pendingApprovalText)
                "Processing" -> Pair(R.color.processingBackground, R.color.processingText)
                "PendingPricing" -> Pair(R.color.pendingPricingBackground, R.color.pendingPricingText)
                "AwaitingConfirmation" -> Pair(R.color.awaitingConfirmationBackground, R.color.awaitingConfirmationText)
                "Closed" -> Pair(R.color.closedBackground, R.color.closedText)
                "Completed" -> Pair(R.color.completedBackground, R.color.completedText)
                "Expired" -> Pair(R.color.expiredBackground, R.color.expiredText)
                else -> Pair(R.color.unknownBackground, R.color.unknownText)
            }

            // Áp dụng màu nền cho `ctr_request_button`
            ctrRequestButton.backgroundTintList = ContextCompat.getColorStateList(this@MyPostingDetailActivity, backgroundColor)

            // Áp dụng màu chữ cho `tvStatus`
            tvStatus.setTextColor(ContextCompat.getColor(this@MyPostingDetailActivity, textColor))
        }



    }

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())
    }

    private fun setListImageTimeshare() {
        // Set List Image Timeshare
        binding.viewPager.apply {
            adapter = imageAdapter
        }
        binding.indicator.setViewPager(binding.viewPager)

        // Set Image Auto Scroll, Auto Scroll Time = 3s
        autoScrollHelper.setupAutoScroll(binding.viewPager)

        // Set Action for Button Next Page and Back To
        binding.ivNextPage.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
        }
        binding.icBackTo.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        }
    }

    private fun setAmenitiesListTimeshare() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvResortFacilities.let {
            it.layoutManager = flexboxLayoutManager
            it.adapter = facilityAdapter
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}