package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail

import android.content.Context
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
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.MyExchangePostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.mapExchangeToUnitTypeBase
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail.ImageListActivity
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
    private var imagePostingAdapter = ImagePostingAdapter()
    private var facilityAdapter = AmenitiesAdapter()
    private val viewModel: MyExchangeDetailViewModel by viewModels()
private var postingId: Int =0

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()

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

        binding.customToolbar.onStartIconClick = {
            finish()
        }
        binding.shimmerViewContainer.startShimmer()
    }
    private fun initAdapter() {
        facilityAdapter.submitList(listOf())
        imagePostingAdapter.apply {
            submitList(listOf())
        }

        featuresAdapter.submitOriginalList(listOf())
        entertainmentAdapter.submitOriginalList(listOf())
        kitchenAdapter.submitOriginalList(listOf())
        policyAdapter.submitOriginalList(listOf())

    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_POSTING_ID, 0)
         postingId = intent;
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
        // BindDAta Package
       /* bindPackageData(myExchangePostingDetail.exchangePackageName)
*/
        // List Image
        bindDataListImage(myExchangePostingDetail.imageUrls)

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
            setTitle("${myExchangePostingDetail.resortName}")
            setTitleDetail("Phòng ${myExchangePostingDetail.roomName}")
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
            btnListRequest.setOnClickListener {
                val intent = Intent(this@MyExchangeDetailActivity, ExchangeRequestOnPostActivity::class.java)
                intent.putExtra(Constant.DEFAULT_EXCHANGE_REQUEST_ON_POST,postingId)
                startActivity(
                    intent
                )
            }
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

            llRoomPricing.visibility = View.GONE
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

    private fun bindDataUnitType(data: MyExchangePostingDetailResponse) {
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
            val intent = Intent(this@MyExchangeDetailActivity, ImageListActivity::class.java)
            intent.putExtra(Constant.IMAGE_POSITION, position)
            intent.putStringArrayListExtra(
                Constant.IMAGE_LIST,
                ArrayList(imageList)
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

    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            llStatusContainer.visibility = View.VISIBLE
            llStatusContainer.setBackgroundColor(context.getColor(backgroundColorRes))
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
}