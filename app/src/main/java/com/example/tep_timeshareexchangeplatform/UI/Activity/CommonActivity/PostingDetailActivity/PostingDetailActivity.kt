package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Posting.PostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTimeshareDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostingDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityTimeshareDetailBinding
    private var imageAdapter = ImageAdapter(Constant.listTimeshareImage)
    private var facilityAdapter = AmenitiesAdapter()
    private var reviewAdapter = ReviewAdapter()

    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)

    private val postingDetailViewModel: PostingDetailViewModel by viewModels()

    private var isExpanded = true
    private var expandedHeight = 140.dp // Initial height in dp
    private var collapsedHeight = 100.dp // Collapsed height in dp

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

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

        getIntentValue()


        initAdapter()

        // Image
        setListImageTimeshare()
        // Facility
        amenitiesListTimeshare()
        // Review
        setReviewTimeshare()

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
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
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

    }

    private fun bindDataPostingDetail(postingDetail: PostingDetailResponse) {
        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetail.unitType.title}")
            setTitleDetail("${postingDetail.checkinDate} - ${postingDetail.checkoutDate}")
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
            tvCheckInDate.text = postingDetail.checkinDate
            tvCheckOutDate.text = postingDetail.checkoutDate
            tvNight.text = "${postingDetail.nights} đêm"
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

        // Cancel Policy
        binding.apply {
            if (postingDetail.cancelType.toString() == "null") {
                tvCancelPolicy.text = "Không có"
                tvCancelPolicyDtb.text = "Không có"
            } else {
                tvCancelPolicy.text = postingDetail.cancelType.toString()
                tvCancelPolicyDtb.text = postingDetail.cancelType.toString()
            }

        }

        // UI DTB
        binding.apply {
            tvResortNameDtb.text = postingDetail.resortName + " | " + postingDetail.unitType.title
            tvCheckInDateDtb.text = postingDetail.checkinDate
            tvCheckOutDateDtb.text = postingDetail.checkoutDate
            tvNightDtb.text = "${postingDetail.nights} đêm"
            tvRoomPricePerNight.text = "${postingDetail.pricePerNights} đ"
            tvEstimatedTotalPrice.text = "${postingDetail.totalPrice} đ"
        }

        // Data for Request
        binding.apply {
            tvPrice.text = "${postingDetail.totalPrice} đ"
            tvDate.text = "${postingDetail.checkinDate} - ${postingDetail.checkoutDate}"

        }

        // Set Amenities
        facilityAdapter.submitList(postingDetail.resortAmenities)





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
            layoutManager = LinearLayoutManager(this@PostingDetailActivity)
        }
    }

    private fun setRequestButtonAction() {
        binding.llSeeAll.setOnClickListener {
            if (isExpanded) {
                collapseCardView(
                    binding.cvRequestContaner,
                    binding.tvPrice,
                    binding.tvDate,
                    binding.tvNotion
                )
                binding.apply {
                    tvSeeAll.text = "Mở rộng"
                    imExpanded.setImageResource(R.drawable.ic_expend)
                }
            } else {
                expandCardView(
                    binding.cvRequestContaner,
                    binding.tvPrice,
                    binding.tvDate,
                    binding.tvNotion
                )
                binding.apply {
                    tvSeeAll.text = "Thu nhỏ"
                    imExpanded.setImageResource(R.drawable.ic_expend_open)
                }
            }
            isExpanded = !isExpanded
        }

        binding.ctrRequestButton.setOnClickListener {
            startActivity(Intent(this, PaymentRentalActivity::class.java))
        }
    }

    private fun collapseCardView(cardView: CardView, vararg viewsToHide: View) {
        val animator = ValueAnimator.ofInt(expandedHeight, collapsedHeight)
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            val layoutParams = cardView.layoutParams
            layoutParams.height = value
            cardView.layoutParams = layoutParams
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewsToHide.forEach { it.visibility = View.GONE }
            }
        })

        animator.duration = 300
        animator.start()
    }

    private fun expandCardView(cardView: CardView, vararg viewsToShow: View) {
        val animator = ValueAnimator.ofInt(collapsedHeight, expandedHeight)
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            val layoutParams = cardView.layoutParams
            layoutParams.height = value
            cardView.layoutParams = layoutParams
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                viewsToShow.forEach { it.visibility = View.VISIBLE }
            }
        })

        animator.duration = 300
        animator.start()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}