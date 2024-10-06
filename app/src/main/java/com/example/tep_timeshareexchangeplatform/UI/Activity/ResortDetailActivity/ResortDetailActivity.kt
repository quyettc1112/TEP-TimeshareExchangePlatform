package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ResortImageListAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.FacilitieAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ReviewAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.RoomTypeAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareListActivity.TimeshareListActivity
import com.example.tep_timeshareexchangeplatform.databinding.ActivityResortDetailBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ResortDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityResortDetailBinding
    private lateinit var resortImageListAdapter: ResortImageListAdapter
    private var roomTypeAdapter = RoomTypeAdapter(true)
    private var facilitieAdapter = FacilitieAdapter()
    private var reviewAdapter = ReviewAdapter()


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
        resortImageListAdapter = ResortImageListAdapter(Constant.listImage) {
            val intent = Intent(this, ImageListActivity::class.java)
            intent.putExtras(Bundle().apply {
                putInt("imagePosition", it)
            })
            startActivity(intent)
        }

        // Init Adapter
        initAdapter()

        // Set Resort Detail Info
        setListImageResort()
        setRoomTypeListResort()
        setFacilitieListResort()
        setReviewResort()

        // Action Event
        setTypeRoomClickAction()
        setButtonSelectRoomClick()
        actionCustomToolbar()

    }

    private fun actionCustomToolbar() {
        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun initAdapter() {
        roomTypeAdapter.submitList(Constant.listRoomType)
        facilitieAdapter.submitList(Constant.listFacilite)
        reviewAdapter.submitList(Constant.listReview)
    }

    private fun setTypeRoomClickAction() {
        roomTypeAdapter.apply {
            onItemClick = {
                val intent = Intent(this@ResortDetailActivity, TimeshareListActivity::class.java)
                startActivity(intent)
            }

            onButonBookClick = {
                val intent = Intent(this@ResortDetailActivity, TimeshareListActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun setListImageResort() {
        // List Destination
        val manager = SpannedGridLayoutManager(
            object : SpannedGridLayoutManager.GridSpanLookup {
                override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
                    // Conditions for 2x2 items
                    return when (position ) {
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

    private fun setRoomTypeListResort() {
        binding.rvResortRoomType.apply {
            adapter = roomTypeAdapter
            layoutManager = LinearLayoutManager(this@ResortDetailActivity)
        }


    }

    private fun setFacilitieListResort() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvResortFacilities.let {
            it.layoutManager = flexboxLayoutManager
            it.adapter = facilitieAdapter
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