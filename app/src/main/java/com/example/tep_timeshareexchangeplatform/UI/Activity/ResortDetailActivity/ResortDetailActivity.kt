package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Resort.ResortDetailModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ResortImageListAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.UnitTypeAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Custom.CustomDialog
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareListActivity.TimeshareListActivity
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityResortDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResortDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityResortDetailBinding
    private lateinit var resortImageListAdapter: ResortImageListAdapter
    private var unitTypeAdapter = UnitTypeAdapter(true)
    private var amenitiesAdapter = AmenitiesAdapter()
    private var reviewAdapter = ReviewAdapter()
    private val resortDetailViewModel: ResortDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResortDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val resortId = intent.getIntExtra(Constant.DEFAULT_RESORT_ID, 0)
        if (resortId != 0) {
            resortDetailViewModel.getResortDetail(resortId)
        } else {
            finish()
        }

        // Init Adapter
        initAdapter()

        // Observe Data
        observeData()



        // Not yet Implemented
        resortImageListAdapter = ResortImageListAdapter(Constant.listImage) {
            val intent = Intent(this, ImageListActivity::class.java)
            intent.putExtras(Bundle().apply {
                putInt("imagePosition", it)
            })
            startActivity(intent)
        }

    }

    private fun observeData() {
        resortDetailViewModel.resortDetail.observe(this) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
                Status.SUCCESS -> {
                    resources.data?.let { resortDetail ->
                        // Set Resort Detail Info
                        bindDataResortInfo(resortDetailViewModel.resortDetail.value?.data!!)

                        setListImageResort()
                        bindDataUnitType(resortDetail.unitTypeDtoList)
                        bindDataAmenities()
                        setReviewResort()

                        // Action Event
                        setTypeRoomClickAction()
                        setButtonSelectRoomClick()
                        actionCustomToolbar()
                    }
                    hideLoadingWaiting()
                }
                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorDialog(resources.message.toString(), "")

                }
            }
        }
    }

    private fun actionCustomToolbar() {
        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun initAdapter() {
        unitTypeAdapter.submitList(listOf())
        amenitiesAdapter.submitList(Constant.listFacilite)
        reviewAdapter.submitList(Constant.listReview)
    }

    private fun setTypeRoomClickAction() {
        unitTypeAdapter.apply {
            onItemClick = {
                bindDataUnitTypeDetailDialog(resortDetailViewModel.resortDetail.value?.data!!)
            }

            onButtonBookClick = {
                bindDataUnitTypeDetailDialog(resortDetailViewModel.resortDetail.value?.data!!)
            }
        }


    }

    // Binding Data Group Function
    private fun bindDataResortInfo(resortDetailModel: ResortDetailModel) {
        binding.apply {
            tvResortName.text = resortDetailViewModel.resortDetail.value?.data?.resortName
            tvLocation.text = resortDetailViewModel.resortDetail.value?.data?.address
            tvMinPrice.text = "${resortDetailViewModel.resortDetail.value?.data?.minPrice.toString()} VND / 1 đêm"
            tvDescription.text = resortDetailViewModel.resortDetail.value?.data?.description.toString()
            if (resortDetailModel.isActive) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
                tvFindMore.visibility = View.GONE
            }

        }
    }
    private fun bindDataUnitType(resorts: List<ResortDetailModel.UnitTypeDto>) {
        unitTypeAdapter.submitList(resorts)
        binding.rvResortRoomType.apply {
            adapter = unitTypeAdapter
            layoutManager = LinearLayoutManager(this@ResortDetailActivity, LinearLayoutManager.VERTICAL, false)
        }
    }
    private fun bindDataAmenities() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvResortFacilities.let {
            it.layoutManager = flexboxLayoutManager
            it.adapter = amenitiesAdapter
        }
    }

    private fun bindDataUnitTypeDetailDialog(resortDetailModel: ResortDetailModel) {
        val resortDetailDialog = CustomDialog(this)
        resortDetailDialog.show()


    }

    private fun setListImageResort() {
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
        binding.recyclerViewResortImage.apply {
            adapter = resortImageListAdapter
            layoutManager = manager

        }

    }
    private fun setButtonSelectRoomClick() {
        binding.btnSelectRoom.setOnClickListener {
            val intent = Intent(this, TimeshareListActivity::class.java)
            startActivity(intent)
        }

    }
    private fun setReviewResort() {
        binding.rvReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@ResortDetailActivity)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }




}