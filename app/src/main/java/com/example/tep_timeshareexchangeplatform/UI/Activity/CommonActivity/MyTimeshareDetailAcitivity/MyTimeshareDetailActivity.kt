package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyTimeshareDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@AndroidEntryPoint
class MyTimeshareDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyTimeshareDetailBinding
    private var imageAdapter = ImageAdapter(listOf())
    private var facilityAdapter = AmenitiesAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private val myTimeshareDetailViewModel: MyTimeshareDetailViewModel by viewModels()

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
        setListImageTimeshare()
        setAmenitiesListTimeshare()
        setEventButtonRequestClick()
    }

    private fun observeViewModel() {
        myTimeshareDetailViewModel.myTimeshareDetail.observe(this) {
            when (it.status) {
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
                    MotionToast.Companion.createColorToast(
                        this,
                        "Error",
                        "Error ${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_thin)
                    )
                }
            }
        }
    }

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())

        val myTimeshareId = intent.getIntExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, 0)
        val token = TokenManager(this).getAccessToken()
        if (myTimeshareId == 0 || token == null) {
            MotionToast.Companion.createColorToast(
                this,
                "Error",
                "Error when get data",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.inter_thin)
            )
            return
        } else {
            myTimeshareDetailViewModel.getMyTimeshareDetail(token, myTimeshareId)
        }
    }

    // Bind Data
    private fun bindDataTimeshareDetail(myTimeshareDetailResponse: MyTimeshareDetailResponse) {
        binding.apply {

            binding.customToolbar.apply {
                setTitle("${myTimeshareDetailResponse.unitType.title}")
                setTitleDetail("${myTimeshareDetailResponse.startDate} đến ${myTimeshareDetailResponse.endDate}")

            }
            // Resort Name, Location
            tvResortName.text = myTimeshareDetailResponse.resortName.toString()
            tvLocation.text = myTimeshareDetailResponse.resortAddress.toString()

            // Check In Date, Check Out Date
            tvCheckIn.text = Constant.formatDateByLocale(
                myTimeshareDetailResponse.startDate,
                binding.root.context
            )
            tvCheckOut.text =
                Constant.formatDateByLocale(myTimeshareDetailResponse.endDate, binding.root.context)

            val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

            val startDate = LocalDate.parse(myTimeshareDetailResponse.startDate, dateFormat)
            val endDate = LocalDate.parse(myTimeshareDetailResponse.endDate, dateFormat)

// Tính số đêm
            val nights = ChronoUnit.DAYS.between(startDate, endDate).toInt()

            tvNights.text = "$nights đêm"

            // Unit Type Detail
            tvRoomName.text = "Chi tiết phòng | ${myTimeshareDetailResponse.roomName.toString()}"
            tvNumBathroom.text = myTimeshareDetailResponse.unitType.bathrooms.toString()
            tvNumBed.text = myTimeshareDetailResponse.unitType.bedrooms.toString()
            tvNumPerson.text = myTimeshareDetailResponse.unitType.sleeps.toString()


        }


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


    override fun onDestroy() {
        super.onDestroy()
        autoScrollHelper.pauseAutoScroll()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}