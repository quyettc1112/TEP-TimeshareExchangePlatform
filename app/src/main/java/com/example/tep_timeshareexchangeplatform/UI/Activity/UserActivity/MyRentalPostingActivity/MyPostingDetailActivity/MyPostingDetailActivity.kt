package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyRentalPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPrice
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
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
    private lateinit var imagePostingAdapter: ImagePostingAdapter
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
        setAmenitiesListTimeshare()

        binding.customToolbar.onStartIconClick = {
            finish()
        }
        binding.shimmerViewContainer.startShimmer()

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
        // Hide Unessary View

        binding.includePackagePosting.apply {
            tvPackageDescription.visibility = View.GONE
        }
        // Image List
        bindDataListImage(myRentalPostingDetailResponse.imageUrls)


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
            tvCheckInDate.text = Constant.formatDateByLocale(
                myRentalPostingDetailResponse.checkinDate,
                this@MyPostingDetailActivity
            )
            tvCheckOutDate.text = Constant.formatDateByLocale(
                myRentalPostingDetailResponse.checkoutDate,
                this@MyPostingDetailActivity
            )
            tvNights.text = "${myRentalPostingDetailResponse.nights} đêm"
        }

        // BindDAta Package
        bindPackageData(myRentalPostingDetailResponse.rentalPackageName)

        // Set Unit Type Of Posting
        binding.apply {
            tvRoomName.text =
                "Chi Tiết Phòng | ${myRentalPostingDetailResponse.unitType.title} #${myRentalPostingDetailResponse.roomName}"

            // Bath
            tvNumBath.text = myRentalPostingDetailResponse.unitType.bathrooms.toString()
            tvBed.text = myRentalPostingDetailResponse.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to myRentalPostingDetailResponse.unitType.bedsFull,
                "bedsKing" to myRentalPostingDetailResponse.unitType.bedsKing,
                "bedsSofa" to myRentalPostingDetailResponse.unitType.bedsSofa,
                "bedsMurphy" to myRentalPostingDetailResponse.unitType.bedsMurphy,
                "bedsQueen" to myRentalPostingDetailResponse.unitType.bedsQueen,
                "bedsTwin" to myRentalPostingDetailResponse.unitType.bedsTwin
            )
            tvNumBed.text = myRentalPostingDetailResponse.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = myRentalPostingDetailResponse.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = myRentalPostingDetailResponse.unitType.sleeps.toString()
            tvPerson.text =
                "${myRentalPostingDetailResponse.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later

        }

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

            if (myRentalPostingDetailResponse.pricePerNights == 0) {
                tvRoomPricePerNight.text = "Đang Chờ Xác Nhận"
                tvEstimatedTotalPrice.text = "Đang Chờ Xác Nhận"
            } else {
                tvRoomPricePerNight.text =
                    "${formatPrice(myRentalPostingDetailResponse.pricePerNights)} đ"
                tvEstimatedTotalPrice.text =
                    "${formatPrice(myRentalPostingDetailResponse.totalPrice)} đ"
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

    private fun bindPackageData(packageName: String) {
        val rentalPackageEnum = RentalPackageEnum.getPackageByName(packageName)


        when (rentalPackageEnum) {

            RentalPackageEnum.BASIC_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = rentalPackageEnum.name
                    tvPackagePrice.text = "${formatPrice(rentalPackageEnum.price)} VND"
                }
            }

            RentalPackageEnum.ADVANCED_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = rentalPackageEnum.name
                    tvPackagePrice.text = "${formatPrice(rentalPackageEnum.price)} VND"
                }
            }

            RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = rentalPackageEnum.name
                    tvPackagePrice.text = "${formatPrice(rentalPackageEnum.price)} VND"
                }
            }

            RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                binding.includePackagePosting.apply {
                    tvPackageName.text = rentalPackageEnum.name
                    tvPackagePrice.text = "${formatPrice(rentalPackageEnum.price)} VND"
                }
            }

        }
        binding.includePackagePosting.root.visibility = View.VISIBLE


    }

    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            llStatusContainer.visibility = View.VISIBLE
            llStatusContainer.setBackgroundColor(context.getColor(backgroundColorRes))
            tvStatus.setTextColor(context.getColor(textColorRes))
            cardStatus.setStrokeColor(context.getColor(textColorRes))
        }
    }

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())
        imagePostingAdapter = ImagePostingAdapter()
    }

    private fun bindDataListImage(imageList: List<String>) {
        imagePostingAdapter.submitList(imageList)
        binding.viewPager.apply {
            adapter = imagePostingAdapter
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