package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.AmenitiesBottomSheetFragment.AmenitiesBottomSheetFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.AmenitiesModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeBase
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImageAmenitiesAdapter.RoomAmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.CustomeDialog.UpdateRentalBottomDialog
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyTimeshareDetailBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTimeshareDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyTimeshareDetailBinding
    private var facilityAdapter = AmenitiesAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private val myTimeshareDetailViewModel: MyTimeshareDetailViewModel by viewModels()

    private var featuresAdapter = RoomAmenitiesAdapter()
    private var entertainmentAdapter = RoomAmenitiesAdapter()
    private var kitchenAdapter = RoomAmenitiesAdapter()
    private var policyAdapter = RoomAmenitiesAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTimeshareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }

        initAdapter()
        observeViewModel()
        setEventButtonRequestClick()
        eventClickShowUpdateBottomSheet()
    }

    private fun observeViewModel() {
        myTimeshareDetailViewModel.myTimeshareDetail.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    it.data?.let { response ->
                        bindDataTimeshareDetail(response)
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Lỗi Khi Tải Dữ Liệu", "Không thể tải dữ liệu")
                    Log.d("ErrorMyTimeshare", it.message.toString())
                }

                null -> {}
            }
        }
    }

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())

        val myTimeshareId = intent.getIntExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, 0)
        val token = TokenManager(this).getAccessToken()
        if (myTimeshareId == 0 || token == null) {
            showErrorToast("Lỗi", "Không có Id Timeshare")
            return
        } else {
            Log.d("MyTimeshareIdasasd", myTimeshareId.toString())
            myTimeshareDetailViewModel.getMyTimeshareDetail(token, myTimeshareId)
        }
    }

    private fun eventClickShowUpdateBottomSheet() {
        myTimeshareDetailViewModel.resetAllValue()
        binding.customToolbar.onEndIconClick = {
            val updateTimeshareBottomDialog = UpdateTimeshareBottomDialog(
                myTimeshareDetailViewModel = myTimeshareDetailViewModel
            )
            updateTimeshareBottomDialog.show(supportFragmentManager, "UpdateTimeshareBottomDialog")
        }
    }


    // Bind Data
    private fun bindDataTimeshareDetail(myTimeshareDetailResponse: MyTimeshareDetailResponse) {
        // Check in Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                myTimeshareDetailResponse.startDate,
                this@MyTimeshareDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(
                    myTimeshareDetailResponse.startDate,
                    this@MyTimeshareDetailActivity
                )


            tvCheckoutDate.text = Constant.getFormattedDate(
                myTimeshareDetailResponse.endDate,
                this@MyTimeshareDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(
                    myTimeshareDetailResponse.endDate,
                    this@MyTimeshareDetailActivity
                )
        }

        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("Chi Tiết Timeshare")
            setTitleDetail("${myTimeshareDetailResponse.resortName}")
        }

        // Image
        Glide.with(this)
            .load(myTimeshareDetailResponse.resortImage)
            .error(R.drawable.im_material_mn)
            .placeholder(R.drawable.ripple_effect_white)
            .into(binding.ivTimeshareDetail)

        binding.apply {
            // Resort Name, Location
            tvResortName.text = myTimeshareDetailResponse.resortName.toString()
            tvLocation.text = myTimeshareDetailResponse.resortAddress.toString()
        }
        bindDataUnitType(myTimeshareDetailResponse)
        bindDataAmenities(myTimeshareDetailResponse)

        binding.cvRequestContaner.visibility = View.GONE


    }

    private fun bindDataUnitType(data: MyTimeshareDetailResponse) {
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
                val unitTypeBase = mapToUnitTypeBase(data.unitType, data.resortId, isActive = true)
                val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
                unitTypeDataDialog.show(supportFragmentManager, "UnitTypeDataDialog")
            }
        }
    }

    private fun bindDataAmenities(data: MyTimeshareDetailResponse) {
        featuresAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        entertainmentAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        kitchenAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))
        policyAdapter.submitOriginalList(mapRoomAmenitiesToAmenitiesModel(data.roomAmenities))


        val binding = binding.includeAmenities
        binding.rvFeatures.apply {
            featuresAdapter.filterByAmenityTypes(AmenityType.FEATURES)
            layoutManager = FlexboxLayoutManager(this@MyTimeshareDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = featuresAdapter
        }

        binding.rvAmenitiesEntertainment.apply {
            entertainmentAdapter.filterByAmenityTypes(AmenityType.ENTERTAINMENT)
            layoutManager = FlexboxLayoutManager(this@MyTimeshareDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP            // Cho phép các mục xuống dòng nếu không đủ chỗ
            }
            adapter = entertainmentAdapter
        }

        binding.rvKitchen.apply {
            kitchenAdapter.filterByAmenityTypes(AmenityType.KITCHEN)
            layoutManager = FlexboxLayoutManager(this@MyTimeshareDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = kitchenAdapter
        }

        binding.rvPolicy.apply {
            policyAdapter.filterByAmenityTypes(AmenityType.POLICY)
            layoutManager = FlexboxLayoutManager(this@MyTimeshareDetailActivity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START  // Đảm bảo các mục căn đều theo chiều dọc
                flexWrap = FlexWrap.WRAP
            }
            adapter = policyAdapter
        }


    }

    private fun setEventButtonRequestClick() {
        binding.ctrRequestButton.setOnClickListener {
            showConfirmDialog(
                title = "Confirm",
                message = "Are you sure you want to select this Timeshare?",
                positiveButtonTitle = "Yes",
                negativeButtonTitle = "No",
                textButton = null,
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {

                    }

                    override fun positiveAction() {
                        val timeshareDetail =
                            myTimeshareDetailViewModel.myTimeshareDetail.value?.data
                        val myTimeshareResponse = timeshareDetail?.let { it1 ->
                            MyTimeshareResponse.Content(
                                timeShareId = it1.timeShareId,
                                resortName = it1.resortName,
                                resortImage = it1.resortImage,
                                roomCode = it1.roomCode,
                                bathRoom = it1.unitType.bathrooms,
                                bedRooms = it1.unitType.bedrooms,
                                startDate = it1.startDate,
                                endDate = it1.endDate
                            )
                        }
                        intentValueToPostingFlow(myTimeshareResponse!!)
                    }
                }
            )
        }

    }


    private fun intentValueToPostingFlow(myTimeshareResponse: MyTimeshareResponse.Content) {
        val intent = Intent()
        intent.putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, myTimeshareResponse)
        setResult(RESULT_OK, intent)
        finish()
    }

    fun mapToUnitTypeBase(
        unitType: MyTimeshareDetailResponse.UnitType,
        resortId: Int,
        isActive: Boolean
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
            price = 0, // Nếu cần lấy giá, thay thế giá trị phù hợp
            description = unitType.description,
            kitchen = unitType.kitchen,
            photos = unitType.photos,
            resortId = resortId,
            sleeps = unitType.sleeps,
            view = unitType.view,
            isActive = isActive,
            unitTypeAmenitiesDTOS = emptyList() // Nếu có danh sách tiện ích, hãy thay thế
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollHelper.pauseAutoScroll()
    }


    private fun displayBedsInfo(unitTypeMap: Map<String, Any>): String {
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

    private fun mapRoomAmenitiesToAmenitiesModel(roomAmenities: List<MyTimeshareDetailResponse.RoomAmenity>): List<AmenitiesModel> {
        return roomAmenities.map { roomAmenity ->
            AmenitiesModel(
                name = roomAmenity.name,
                type = roomAmenity.type,
                isChecked = false // Mặc định là chưa được chọn
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}