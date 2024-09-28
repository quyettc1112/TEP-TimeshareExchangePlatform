package com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareDetailActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.FacilitieAdapter
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

    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)

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
        setListImageTimeshare()
        setAutoScroll()
        setFacilitieListTimeshare()


        // Set up the action for the button
        setButtonAction()
        setChangeImageListAction()
    }

    private fun initAdapter() {
        facilityAdapter.submitList(Constant.listFacilite)
    }
    private fun setListImageTimeshare() {
        binding.viewPager.apply {
            adapter = imageAdapter
        }
        binding.indicator.setViewPager(binding.viewPager)
    }
    private fun setAutoScroll() {
        autoScrollHelper.setupAutoScroll(binding.viewPager)
    }
    private fun setChangeImageListAction() {
        binding.ivNextPage.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
        }

        binding.icBackTo.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        }

    }
    private fun setButtonAction() {
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
}