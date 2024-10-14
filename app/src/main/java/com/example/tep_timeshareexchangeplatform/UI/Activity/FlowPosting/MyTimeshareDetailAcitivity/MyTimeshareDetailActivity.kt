package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.MyTimeshareDetailAcitivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareDetailActivity.Adapter.ImageAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyTimeshareDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MyTimeshareDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyTimeshareDetailBinding
    private var imageAdapter = ImageAdapter(Constant.listTimeshareImage)
    private var facilityAdapter = AmenitiesAdapter()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)

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
        initAdapter()
        setListImageTimeshare()
        setFacilitieListTimeshare()
        setEventButtonRequestClick()
    }

    private fun initAdapter() {
        facilityAdapter.submitList(Constant.listFacilite)
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
    private fun setFacilitieListTimeshare() {
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
                        // Fake Data
                        val myTimeshareModel =  MyTimeshareModel(
                            id = 1,
                            name = "Flamingo Đại Lải",
                            roomName = "Phòng Studio King, 1 Giường, 4 Người",
                            checkInDate = "18/08/2024",
                            checkOutDate = "23/08/2024",
                            numberOfNight = 6,
                            price = "1,000,000 VND",
                            image = "https://i.pinimg.com/564x/5e/f1/72/5ef1725d7e391e26605f07f74eec6d6b.jpg"
                        )
                        intentValueToPostingFlow(myTimeshareModel)
                    }
                }
            )
        }

    }


    private fun intentValueToPostingFlow(myTimeshareModel : MyTimeshareModel) {
        val intent = Intent()
        intent.putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, myTimeshareModel)
        setResult(RESULT_OK, intent)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        autoScrollHelper.pauseAutoScroll()
    }
}