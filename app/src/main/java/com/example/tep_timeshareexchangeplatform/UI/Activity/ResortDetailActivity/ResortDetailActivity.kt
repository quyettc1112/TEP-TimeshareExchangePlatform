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
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.RoomTypeAdapter
import com.example.tep_timeshareexchangeplatform.databinding.ActivityResortDetailBinding

class ResortDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityResortDetailBinding
    private lateinit var resortImageListAdapter: ResortImageListAdapter
    private var roomTypeAdapter = RoomTypeAdapter()

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
        initAdapter()

        // Set Resort Detail Info
        setListImageResort()
        setRoomTypeListResort()
    }

    private fun initAdapter() {
        roomTypeAdapter.submitList(Constant.listRoomType)
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

}