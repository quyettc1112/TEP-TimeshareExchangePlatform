package com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareDetailActivity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.FacilitieAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTimeshareDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TimeshareDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityTimeshareDetailBinding
    private var imageAdapter = ImageAdapter(Constant.listTimeshareImage)
    private var facilityAdapter = FacilitieAdapter()
    private var reviewAdapter = ReviewAdapter()

    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)


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

        initAdapter()

        // Image
        setListImageTimeshare()
        // Facility
        setFacilitieListTimeshare()
        // Review
        setReviewTimeshare()

        // Set up the action for the button
        setToolBarAction()
        setRequestButtonAction()

    }

    private fun initAdapter() {
        facilityAdapter.submitList(Constant.listFacilite)
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
        binding.customToolbar.onStartIconClick =  {
            finish()
        }
    }
    private fun setFacilitieListTimeshare() {
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
            layoutManager = LinearLayoutManager(this@TimeshareDetailActivity)
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



}