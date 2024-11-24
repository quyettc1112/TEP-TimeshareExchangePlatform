package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.PostingDetailActivity

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
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeBase
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.OwnerInfoActivity.OwnerInfoActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberInfoDialog
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail.ImageListActivity
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTimeshareDetailBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostingDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityTimeshareDetailBinding
    private var imagePostingAdapter = ImagePostingAdapter()
    private val postingDetailViewModel: PostingDetailViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()


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
        getIntentValue()

        initAdapter()


        // Set up the action for the button
        setToolBarAction()
        setRequestButtonAction()

    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_POSTING_ID, 0)
        if (intent == 0) {
            finish()
        }
        postingDetailViewModel.getPostingDetail(intent)
        observePostingDetail()

    }

    private fun observePostingDetail() {
        postingDetailViewModel.postingDetail.observe(this) {
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
                    MotionToast.Companion.createColorToast(
                        this,
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        // Call check User Is Member
        postingDetailViewModel.isCustomerExist.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    if (it.data!!.isMember) {
                        tokenManager.saveProfileInfo(it.data)
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                        val intent =
                            Intent(this@PostingDetailActivity, OwnerInfoActivity::class.java)
                        startActivity(intent)
                    } else {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                        intentToMemberShipActivity()
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message!!.contains("404")) {
                        intentToMemberShipActivity()
                    }
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Call Create Customer
        postingDetailViewModel.createCustomerResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    MotionToast.createColorToast(
                        this,
                        "Success",
                        "Create Customer Success",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    postingDetailViewModel.callIsCustomerExist(tokenManager.getAccessToken()!!)
                }

                Status.ERROR -> {
                    Log.d("CheckErrorCreate", it.message.toString() + " " + it.message.toString())
                    MotionToast.createColorToast(
                        this,
                        "Error",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    hideLoadingWaiting()
                }
            }
        }


    }

    private fun bindDataPostingDetail(postingDetail: PublicPostingDetailResponse) {
        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetail.resortName}")
            setTitleDetail("${postingDetail.checkinDate} đến ${postingDetail.checkoutDate}")

        }

        // Resort Info
        binding.apply {
            tvResortName.text = postingDetail.resortName + " | " + postingDetail.unitType.title
            tvLocation.text = postingDetail.address

            if (postingDetail.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Bind Image
        bindDataListImage(postingDetail.imageUrls)


        // Checkin Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                postingDetail.checkinDate,
                this@PostingDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkinDate, this@PostingDetailActivity)


            tvCheckoutDate.text = Constant.getFormattedDate(
                postingDetail.checkoutDate,
                this@PostingDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkoutDate, this@PostingDetailActivity)
        }

        // Set Unit Type Of Posting
        bindDataUnitType(postingDetail)

        // Cancel Policy
        binding.apply {
            if (postingDetail.cancelType.toString() == "null") {
                tvCancelPolicy.text = "Không có"
                tvCancelPolicyDtb.text = "Không có"
            } else {
                val refundPolicy = RefundPolicy.getShortDescriptionFromName(
                    this@PostingDetailActivity,
                    postingDetail.cancelType.toString()
                )
                tvCancelPolicy.text = refundPolicy
                tvCancelPolicyDtb.text = refundPolicy
            }
        }

        // UI DTB
        binding.apply {
            tvResortNameDtb.text = postingDetail.resortName + " | " + postingDetail.unitType.title
            tvCheckInDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkinDate, this@PostingDetailActivity)
            tvCheckOutDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkoutDate, this@PostingDetailActivity)
            tvNightDtb.text = "${postingDetail.nights} đêm"
            tvRoomPricePerNight.text =
                "${Constant.formatPriceLong(postingDetail.pricePerNights)} VNĐ / 1 đêm"
            tvEstimatedTotalPrice.text =
                "${Constant.formatPriceLong(postingDetail.totalPrice)} VNĐ / ${postingDetail.nights} đêm"
            tvPostedBy.text = "Đăng bởi ${postingDetail.ownerName}"

        }

        // Data for Request
        binding.apply {
            tvPrice.text =
                "${Constant.formatPriceLong(postingDetail.totalPrice)} đ / ${postingDetail.nights} đêm"
            tvDate.text = Constant.getFormattedDate(
                postingDetail.checkinDate,
                this@PostingDetailActivity
            ) + " đến " + Constant.getFormattedDate(
                postingDetail.checkoutDate,
                this@PostingDetailActivity
            )

        }


        // Package Info
        binding.apply {
            if (postingDetail.rentalPackageName != null) {
                val rentalPackageEnum =
                    RentalPackageEnum.getPackageByName(postingDetail.rentalPackageName)
                when (rentalPackageEnum) {
                    RentalPackageEnum.BASIC_SERVICE.packageModel -> {
                        tvNotion.visibility = View.VISIBLE
                        tvMemberRequest.visibility = View.VISIBLE
                        ctrRequestButton.backgroundTintList =
                            resources.getColorStateList(R.color.redPrimary)
                    }

                    RentalPackageEnum.ADVANCED_SERVICE.packageModel -> {
                        tvNotion.visibility = View.GONE
                        tvMemberRequest.visibility = View.GONE
                        ctrRequestButton.backgroundTintList =
                            resources.getColorStateList(R.color.blue_full)

                    }

                    RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                        tvNotion.visibility = View.GONE
                        tvMemberRequest.visibility = View.GONE
                        ctrRequestButton.backgroundTintList =
                            resources.getColorStateList(R.color.blue_full)
                    }

                    RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                        tvNotion.visibility = View.GONE
                        tvMemberRequest.visibility = View.GONE
                        ctrRequestButton.backgroundTintList =
                            resources.getColorStateList(R.color.blue_full)
                    }

                }
            }
        }

        val customerInfo = tokenManager.getProfileInfo()
        if (postingDetail.ownerId == customerInfo?.id) {
            binding.apply {
                tvPrice.visibility = View.GONE
                tvDate.visibility = View.GONE
                tvNotion.visibility = View.GONE
                tvMemberRequest.visibility = View.GONE
                tvRequest.text = "Bài Đăng Của Bạn - Không Thể Đặt Phòng"
                ctrRequestButton.isEnabled = false
            }
        }

        bindDataAmenities(postingDetail)

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
        imagePostingAdapter = ImagePostingAdapter()
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
            val intent = Intent(this@PostingDetailActivity, ImageListActivity::class.java)
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

    private fun bindDataUnitType(data: PublicPostingDetailResponse) {
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

    private fun bindDataAmenities(data: PublicPostingDetailResponse) {
        featuresAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        entertainmentAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        kitchenAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        policyAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))


        val binding = binding.includeAmenities
        binding.rvFeatures.apply {
            featuresAdapter.filterByAmenityTypes(AmenityType.FEATURES)
            layoutManager = FlexboxLayoutManager(this@PostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            entertainmentAdapter.filterByAmenityTypes(AmenityType.ENTERTAINMENT)
            layoutManager = FlexboxLayoutManager(this@PostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = entertainmentAdapter
        }

        binding.rvKitchen.apply {
            kitchenAdapter.filterByAmenityTypes(AmenityType.KITCHEN)
            layoutManager = FlexboxLayoutManager(this@PostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = kitchenAdapter
        }

        binding.rvPolicy.apply {
            policyAdapter.filterByAmenityTypes(AmenityType.POLICY)
            layoutManager = FlexboxLayoutManager(this@PostingDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = policyAdapter
        }


    }

    private fun setRequestButtonAction() {
        binding.ctrRequestButton.setOnClickListener {
            val postingDetail = postingDetailViewModel.postingDetail.value!!.data
            if (postingDetail!!.rentalPackageName != null) {
                val rentalPackageEnum =
                    RentalPackageEnum.getPackageByName(postingDetail.rentalPackageName)
                when (rentalPackageEnum) {
                    RentalPackageEnum.BASIC_SERVICE.packageModel -> {
                        postingDetailViewModel.callIsCustomerExist(tokenManager.getAccessToken()!!)
                    }

                    else -> {
                        val userLogState = tokenManager.getUserLogState()
                        when (userLogState) {
                            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                                val intent = Intent(this, PaymentRentalActivity::class.java)
                                intent.putExtra(
                                    Constant.DEFAULT_POSTING_ID,
                                    postingDetail.rentalPostingId
                                )
                                startActivity(intent)
                            }

                            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                                val intent = Intent(this, PaymentRentalActivity::class.java)
                                intent.putExtra(
                                    Constant.DEFAULT_POSTING_ID,
                                    postingDetail.rentalPostingId
                                )
                                startActivity(intent)
                            }

                            UserLogState.LOGGED_IN_AS_USER -> {
                                /*val dialogFragment = MemberInfoDialog.newInstance()
                                dialogFragment.show(supportFragmentManager, dialogFragment.tag)
                                dialogFragment.setOnClickRequestButton(object :
                                    MemberInfoDialog.OnClickRequestButton {
                                    override fun onClickRequestButton(customerDTO: CustomerDTO) {
                                        // Call API Create Customer
                                        postingDetailViewModel.callCreateCustomer(
                                            tokenManager.getAccessToken()
                                                .toString(), customerDTO
                                        )
                                    }
                                })*/
                                // startActivity(Intent(this, MainActivity::class.java))
                            }

                            UserLogState.LOGGED_OUT -> {
                                finish()
                                MotionToast.Companion.createColorToast(
                                    this,
                                    "Thất Bại",
                                    "Vui lòng đăng nhập để thực hiện chức năng này",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    null
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    private fun intentToMemberShipActivity() {
        startActivity(Intent(this, MemberShipActivity::class.java))
    }

    fun mapToUnitTypeBase(
        unitType: PublicPostingDetailResponse.UnitType,
        resortId: Int,
        isActive: Boolean,
        unitTypeAmenities: List<PublicPostingDetailResponse.UnitTypeAmenity>
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

    fun mapRoomAmenitiesToAmenitiesModel(roomAmenities: List<PublicPostingDetailResponse.RoomAmenity>): List<AmenitiesModel> {
        return roomAmenities.map { roomAmenity ->
            AmenitiesModel(
                name = roomAmenity.name,
                type = roomAmenity.type,
                isChecked = false // Mặc định là chưa được chọn
            )
        }
    }


}