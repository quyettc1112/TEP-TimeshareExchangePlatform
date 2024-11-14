package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPrice
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailViewModel
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyExchangeDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangDetailBinding
    private var imageAdapter = ImageAdapter(Constant.listTimeshareImage)
    private var facilityAdapter = AmenitiesAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private val viewModel: MyExchangeDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyExchangDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getIntentValue()

        initAdapter()
        setListImageTimeshare()
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
            viewModel.getCustomerExchangeDetail(token.getAccessToken().toString(), intent)
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
        viewModel.myExchangeDetail.observe(this) {
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
    private fun bindData(myExchangePostingDetail: MyExchangePostingDetailResponse) {
        // Hide Unessary View


        binding.includePackagePosting.apply {
            tvPackageDescription.visibility = View.GONE
        }

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${myExchangePostingDetail.unitType.title}")
            setTitleDetail("${myExchangePostingDetail.checkinDate} - ${myExchangePostingDetail.checkoutDate}")
        }

        // Resort Info
        binding.apply {
            tvResortName.text =
                myExchangePostingDetail.resortName + " | " + myExchangePostingDetail.unitType.title
            tvLocation.text = myExchangePostingDetail.address

            if (myExchangePostingDetail.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckInDate.text = Constant.formatDateByLocale(
                myExchangePostingDetail.checkinDate,
                this@MyExchangeDetailActivity
            )
            tvCheckOutDate.text = Constant.formatDateByLocale(
                myExchangePostingDetail.checkoutDate,
                this@MyExchangeDetailActivity
            )
            tvNights.text = "${myExchangePostingDetail.nights} đêm"
        }

        // BindDAta Package
        bindPackageData(myExchangePostingDetail.exchangePackageName)

        // Set Unit Type Of Posting
        binding.apply {
            tvRoomName.text =
                "Chi Tiết Phòng | ${myExchangePostingDetail.unitType.title} #${myExchangePostingDetail.roomName}"

            // Bath
            tvNumBath.text = myExchangePostingDetail.unitType.bathrooms.toString()
            tvBed.text = myExchangePostingDetail.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to myExchangePostingDetail.unitType.bedsFull,
                "bedsKing" to myExchangePostingDetail.unitType.bedsKing,
                "bedsSofa" to myExchangePostingDetail.unitType.bedsSofa,
                "bedsMurphy" to myExchangePostingDetail.unitType.bedsMurphy,
                "bedsQueen" to myExchangePostingDetail.unitType.bedsQueen,
                "bedsTwin" to myExchangePostingDetail.unitType.bedsTwin
            )
            tvNumBed.text = myExchangePostingDetail.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = myExchangePostingDetail.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = myExchangePostingDetail.unitType.sleeps.toString()
            tvPerson.text =
                "${myExchangePostingDetail.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later

        }


        // UI DTB
        binding.includeDetailBilling.apply {

            llPostingBy.visibility = View.GONE
            tvResortNameDtb.text =
                myExchangePostingDetail.resortName + " | " + myExchangePostingDetail.unitType.title
            tvCheckInDate.text = Constant.formatDateByLocale(
                myExchangePostingDetail.checkinDate,
                this@MyExchangeDetailActivity
            )
            tvCheckOutDate.text = Constant.formatDateByLocale(
                myExchangePostingDetail.checkoutDate,
                this@MyExchangeDetailActivity
            )
            tvNumberNight.text = "${myExchangePostingDetail.nights} đêm"

            tvLocation.text = myExchangePostingDetail.address

            Glide.with(binding.root.context)
                .load(myExchangePostingDetail.unitType.photos)
                .placeholder(R.drawable.ripple_effect_white)
                .error(R.drawable.im_material_mn)
                .into(imImageTimeshare)

        }

        binding.includeDetailBilling.root.visibility = View.VISIBLE

        // Set Amenities
        facilityAdapter.submitList(listOf())



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
            MyPostingStatus.fromApiStatus(myExchangePostingDetail.status)
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
}