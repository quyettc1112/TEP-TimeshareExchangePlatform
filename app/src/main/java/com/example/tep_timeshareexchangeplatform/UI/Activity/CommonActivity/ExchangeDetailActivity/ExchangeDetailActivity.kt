package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ExchangeDetailActivity

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
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.OwnerInfoActivity.OwnerInfoActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberInfoDialog
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalActivity
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityExchangeDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityExchangeDetailBinding
    private var imageAdapter = ImageAdapter(Constant.listTimeshareImage)
    private var facilityAdapter = AmenitiesAdapter()
    private var reviewAdapter = ReviewAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private lateinit var tokenManager: TokenManager
    private val exchangeDetailViewModel: ExchangeDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExchangeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)
        initAdapter()
        getIntentValue()
        setListImageTimeshare()
        setToolBarAction()
        amenitiesListTimeshare()
        setReviewTimeshare()
    }


    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_POSTING_ID, 0)
        if (intent == 0) {
            finish()
        }
        exchangeDetailViewModel.getExchangeDetail(intent)
        observePostingDetail()

    }

    private fun observePostingDetail() {
        exchangeDetailViewModel.exchangeDetail.observe(this) {
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
        exchangeDetailViewModel.isCustomerExist.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    if (it.data!!.isMember) {
                        tokenManager.saveCustomerInfo(it.data)
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                        val intent =
                            Intent(this@ExchangeDetailActivity, OwnerInfoActivity::class.java)
                        startActivity(intent)
                    } else {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                        //intentToMemberShipActivity()
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message!!.contains("404")) {
                        //intentToMemberShipActivity()
                    }
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Call Create Customer
        exchangeDetailViewModel.createCustomerResponse.observe(this) {
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
                    exchangeDetailViewModel.callIsCustomerExist(tokenManager.getAccessToken()!!)
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

    private fun bindDataPostingDetail(postingDetail: ExchangeDetailResponse) {
        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetail.unitType.title}")
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

        // Checkin Date, Check out Date
        binding.apply {
            tvCheckinDate.text = Constant.getFormattedDate(
                postingDetail.checkinDate,
                this@ExchangeDetailActivity
            )
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkinDate, this@ExchangeDetailActivity)


            tvCheckoutDate.text = Constant.getFormattedDate(
                postingDetail.checkoutDate,
                this@ExchangeDetailActivity
            )
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(postingDetail.checkoutDate, this@ExchangeDetailActivity)
        }

        // Set Unit Type Of Posting
        binding.apply {
            tvRoomName.text =
                "Chi Tiết Phòng | ${postingDetail.unitType.title} #${postingDetail.roomName}"

            // Bath
            tvNumBath.text = postingDetail.unitType.bathrooms.toString()
            tvBed.text = postingDetail.unitType.bedrooms.toString()

            // Beds
            val unitTypeMap = mapOf(
                "bedsFull" to postingDetail.unitType.bedsFull,
                "bedsKing" to postingDetail.unitType.bedsKing,
                "bedsSofa" to postingDetail.unitType.bedsSofa,
                "bedsMurphy" to postingDetail.unitType.bedsMurphy,
                "bedsQueen" to postingDetail.unitType.bedsQueen,
                "bedsTwin" to postingDetail.unitType.bedsTwin
            )
            tvNumBed.text = postingDetail.unitType.bedrooms.toString()
            tvBed.text = displayBedsInfo(unitTypeMap)

            // Kitchen
            tvKitchen.text = postingDetail.unitType.kitchen
            tvNumKitchen.text = 1.toString()

            // Max Guest
            tvNumPerson.text = postingDetail.unitType.sleeps.toString()
            tvPerson.text = "${postingDetail.unitType.sleeps.toString()} người lớn tối đa"

            // Room Policy
            // Do IT Later

        }


        // UI DTB
        binding.apply {
            llPricing.visibility = View.GONE
            Glide.with(this@ExchangeDetailActivity)
                .load(postingDetail.imageUrls)
                .error(R.drawable.im_matiral_timeshare)
                .into(imImageTimeshare)
            tvResortNameDtb.text = postingDetail.resortName
            tvLocationDtb.text = postingDetail.address
            tvCheckInDateDtb.text = Constant.formatDateByLocale(
                postingDetail.checkinDate,
                this@ExchangeDetailActivity
            )
            tvCheckOutDateDtb.text = Constant.formatDateByLocale(
                postingDetail.checkoutDate,
                this@ExchangeDetailActivity
            )
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

    private fun initAdapter() {
        facilityAdapter.submitList(listOf())
        reviewAdapter.submitList(Constant.listReview)
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

    private fun setToolBarAction() {
        binding.customToolbar.onStartIconClick = {
            finish()
        }
    }

    private fun amenitiesListTimeshare() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvResortFacilities.let {
            it.layoutManager = flexboxLayoutManager
            it.adapter = facilityAdapter
        }
    }

    private fun setReviewTimeshare() {
        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@ExchangeDetailActivity)
        }
    }

    /*private fun setRequestButtonAction() {
        binding.ctrRequestButton.setOnClickListener {
            val postingDetail = exchangeDetailViewModel.exchangeDetail.value!!.data
            if (postingDetail!!.exchangePackageName != null) {
                val exchangePackge =
                    ExchangePackageEnum.getPackageByName(postingDetail.exchangePackageName)
                when (exchangePackge) {
                    RentalPackageEnum.BASIC_SERVICE.packageModel -> {
                        exchangeDetailViewModel.callIsCustomerExist(tokenManager.getAccessToken()!!)
                    }

                    else -> {
                        val userLogState = tokenManager.getUserLogState()
                        when (userLogState) {
                            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                                val intent = Intent(this, PaymentRentalActivity::class.java)
                                intent.putExtra(
                                    Constant.DEFAULT_POSTING_ID,
                                    postingDetail.exchangePostingId
                                )
                                startActivity(intent)
                            }

                            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                                val intent = Intent(this, PaymentRentalActivity::class.java)
                                intent.putExtra(
                                    Constant.DEFAULT_POSTING_ID,
                                    postingDetail.exchangePostingId
                                )
                                startActivity(intent)
                            }

                            UserLogState.LOGGED_IN_AS_USER -> {
                                val dialogFragment = MemberInfoDialog.newInstance()
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
                                })
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
    }*/

}