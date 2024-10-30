package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingDetailActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.VNPAYPurchaseResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletPurchaseResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPrice
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PostStatus
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

    private fun bindData(myPostingDetailResponse: MyPostingDetailResponse) {
        // Hide Unessary View


        binding.includePackagePosting.apply {
            tvPackageDescription.visibility = View.GONE
        }

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${myPostingDetailResponse.unitType.title}")
            setTitleDetail("${myPostingDetailResponse.checkinDate} - ${myPostingDetailResponse.checkoutDate}")
        }

        // Resort Info
        binding.apply {
            tvResortName.text =
                myPostingDetailResponse.resortName + " | " + myPostingDetailResponse.unitType.title
            tvLocation.text = myPostingDetailResponse.address

            if (myPostingDetailResponse.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckInDate.text =  Constant.formatDateByLocale(myPostingDetailResponse.checkinDate, this@MyPostingDetailActivity)
            tvCheckOutDate.text =  Constant.formatDateByLocale(myPostingDetailResponse.checkoutDate, this@MyPostingDetailActivity)
            tvNights.text = "${myPostingDetailResponse.nights} đêm"
        }

        // BindDAta Package
        bindPackageData(myPostingDetailResponse.rentalPackageName)

        // Set Unit Type Of Posting
        binding.apply {
            tvRoomName.text =
                "Chi Tiết Phòng | ${myPostingDetailResponse.unitType.title} #${myPostingDetailResponse.roomName}"

            // Bath
            tvNumBath.text = myPostingDetailResponse.unitType.bathrooms.toString()
            tvBed.text = myPostingDetailResponse.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to myPostingDetailResponse.unitType.bedsFull,
                "bedsKing" to myPostingDetailResponse.unitType.bedsKing,
                "bedsSofa" to myPostingDetailResponse.unitType.bedsSofa,
                "bedsMurphy" to myPostingDetailResponse.unitType.bedsMurphy,
                "bedsQueen" to myPostingDetailResponse.unitType.bedsQueen,
                "bedsTwin" to myPostingDetailResponse.unitType.bedsTwin
            )
            tvNumBed.text = myPostingDetailResponse.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = myPostingDetailResponse.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = myPostingDetailResponse.unitType.sleeps.toString()
            tvPerson.text = "${myPostingDetailResponse.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later

        }

        // Cancel Policy
        binding.apply {
            if (myPostingDetailResponse.cancelType.toString() == "null") {
                tvCancelPolicy.text = "Không có"
                includeDetailBilling.tvCancellationPolicy.text = "Không có"
            } else {
                tvCancelPolicy.text = myPostingDetailResponse.cancelType.toString()
                includeDetailBilling.tvCancellationPolicy.text =
                    myPostingDetailResponse.cancelType.toString()
            }
        }

        // UI DTB
        binding.includeDetailBilling.apply {
            tvResortNameDtb.text =
                myPostingDetailResponse.resortName + " | " + myPostingDetailResponse.unitType.title
            tvCheckInDate.text = myPostingDetailResponse.checkinDate
            tvCheckOutDate.text = myPostingDetailResponse.checkoutDate
            tvNumberNight.text = "${myPostingDetailResponse.nights} đêm"
            tvRoomPricePerNight.text = "${myPostingDetailResponse.pricePerNights} đ"
            tvEstimatedTotalPrice.text = "${myPostingDetailResponse.totalPrice} đ"
            tvLocation.text = myPostingDetailResponse.address
        }


        // Set Amenities
        facilityAdapter.submitList(listOf())

        binding.apply {
            // Thiết lập văn bản trạng thái
            tvStatus.text = when (myPostingDetailResponse.status) {
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
            val (backgroundColor, textColor) = when (myPostingDetailResponse.status) {
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

        when (PostStatus.fromApiStatus(myPostingDetailResponse.status)) {
            PostStatus.PENDING_APPROVAL -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            PostStatus.AWAITING_CONFIRMATION -> {
                applyStatusStyle(
                    this,
                    R.color.status_awaiting_confirmation_bg,
                    R.color.status_awaiting_confirmation_text
                )
            }

            PostStatus.PROCESSING -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.green_verify
                )
            }

            PostStatus.COMPLETED -> {
                applyStatusStyle(
                    this,
                    R.color.blue_header_section,
                    R.color.blue_full
                )
            }

            PostStatus.REJECTED -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            PostStatus.PENDING_PRICING -> {
                applyStatusStyle(
                    this,
                    R.color.status_awaiting_confirmation_bg,
                    R.color.status_awaiting_confirmation_text
                )
            }

            PostStatus.CLOSED -> {
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
        binding.tvStatus.text = PostStatus.fromApiStatus(myPostingDetailResponse.status)?.getDescription(this)


    }

    private fun bindPackageData(packageName : String) {
        val packageEnum = PackageEnum.getPackageByName(packageName)

        when (packageEnum) {

            PackageEnum.BASIC_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = packageEnum.name
                    tvPackagePrice.text = "${formatPrice(packageEnum.price)} VND"
                }
            }

            PackageEnum.ADVANCED_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = packageEnum.name
                    tvPackagePrice.text = "${formatPrice(packageEnum.price)} VND"
                }
            }
            PackageEnum.PREMIUM_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = packageEnum.name
                    tvPackagePrice.text = "${formatPrice(packageEnum.price)} VND"
                }
            }
            PackageEnum.DELEGATED_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = packageEnum.name
                    tvPackagePrice.text = "${formatPrice(packageEnum.price)} VND"
                }
            }

        }





    }

    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            ctrRequestButton.backgroundTintList = context.getColorStateList(backgroundColorRes)
            tvStatus.setTextColor(context.getColor(textColorRes))
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