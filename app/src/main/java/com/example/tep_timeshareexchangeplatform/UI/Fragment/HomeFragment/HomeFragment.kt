package com.example.tep_timeshareexchangeplatform.UI.Fragment.HomeFragment

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.BlogAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.DestianationAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ResortAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SuggestTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Until.SpannedGridLayoutManager.GridAdapter
import com.example.tep_timeshareexchangeplatform.Until.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val timeshareAdapter = SuggestTimeshareAdapter()
    private val resortAdapterMB = ResortAdapter()
    private val resortAdapterMT = ResortAdapter()
    private val resortAdapterMN = ResortAdapter()
    private val destianationAdapter = DestianationAdapter()
    private val blogAdapter = BlogAdapter()
    lateinit var gridAdapter : GridAdapter
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)


    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridAdapter = GridAdapter(Constant.destiantionList) { destinationModel ->
            // Xử lý sự kiện khi item được click
            Toast.makeText(requireContext(), "Clicked: ${destinationModel.destinationName}", Toast.LENGTH_SHORT).show()
        }
        timeshareAdapter.submitList(Constant.timeshareList)
        resortAdapterMB.submitList(Constant.resortListMB)
        resortAdapterMT.submitList(Constant.resortListMT)
        resortAdapterMN.submitList(Constant.resortListMN)
        destianationAdapter.submitList(Constant.destiantionList)
        blogAdapter.submitList(Constant.blogList)
        // Khởi tạo AutoScrollViewPagerHelper


        // Khởi tạo ActivityResultLauncher
        initActivityResultLauncher()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setAdapter()
        setItemResortClickListener()
        setSearchComponentClickEvent()



        return binding.root
    }

    fun setSearchComponentClickEvent() {
      binding.let {
          // Location Click Event
          it.llLocation.setOnClickListener {
              val intent = Intent(requireContext(), LocationActivity::class.java)
                launchForResult(intent) { selectedLocation ->
                    selectedLocation?.let { binding.tvLocation.text = selectedLocation }
                }
          }
      }
    }

    fun setAdapter() {
        // List Timesahre Recomend
        binding.rvSuggestTimeshare.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSuggestTimeshare.adapter = timeshareAdapter


        // List Blog
        binding.rcBlog.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcBlog.adapter = blogAdapter

        // List Resort Recomend MB
        binding.vpResortHotelMb.let {
            it.adapter = resortAdapterMB
            it.clipToPadding = true
            it.clipChildren = false
            it.offscreenPageLimit = 5
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS



        }
        // List Resort Recomend MT
        binding.vpResortHotelMt.let {
            it.adapter = resortAdapterMT
            it.clipToPadding = true
            it.clipChildren = false
            it.offscreenPageLimit = 5
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

        }
        // List Resort Recomend MN
        binding.vpResortHotelMn.let {
            it.adapter = resortAdapterMN
            it.clipToPadding = true
            it.clipChildren = false
            it.offscreenPageLimit = 5
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
        }

        // Auto Scroll
        autoScrollHelper.setupAutoScroll(binding.vpResortHotelMb)
        autoScrollHelper.setupAutoScroll(binding.vpResortHotelMt)
        autoScrollHelper.setupAutoScroll(binding.vpResortHotelMn)

        // List Destination
        val manager = SpannedGridLayoutManager(
            object : SpannedGridLayoutManager.GridSpanLookup {
                override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
                    // Conditions for 2x2 items
                    return when (position ) {
                        0 -> SpannedGridLayoutManager.SpanInfo(2, 1)
                        1 -> SpannedGridLayoutManager.SpanInfo(1, 2)
                        2 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        3 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        4 -> SpannedGridLayoutManager.SpanInfo(1, 2)
                        5 -> SpannedGridLayoutManager.SpanInfo(2, 1)
                        6 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        7 -> SpannedGridLayoutManager.SpanInfo(1, 1)
                        else -> {
                            SpannedGridLayoutManager.SpanInfo(1, 1)
                        }
                    }
                }
            },
            3,  // number of columns
            1f // how big is default item
        )
        binding.rvTouristDestination.let {
            it.adapter = gridAdapter
            it.layoutManager = manager
        }

    }

    fun setItemResortClickListener() {
        resortAdapterMB.let {
            it.onItemClick = {
                Toast.makeText(requireContext(), it.resortName.toString(), Toast.LENGTH_SHORT).show()
            }

            it.onFavoriteClick = {
                Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
            }
        }
        resortAdapterMT.let {
            it.onItemClick = {
                Toast.makeText(requireContext(), it.resortName.toString(), Toast.LENGTH_SHORT).show()
            }

            it.onFavoriteClick = {
                Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
            }
        }
        resortAdapterMN.let {
            it.onItemClick = {
                Toast.makeText(requireContext(), it.resortName.toString(), Toast.LENGTH_SHORT).show()
            }

            it.onFavoriteClick = {
                Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
            }
        }
        timeshareAdapter.let {
            it.onItemClick = {
                Toast.makeText(requireContext(), it.timeshareName.toString(), Toast.LENGTH_SHORT).show()
            }
            it.onFavoriteClick = {
                Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        autoScrollHelper.pauseAutoScroll()
    }

    override fun onResume() {
        super.onResume()
        autoScrollHelper.resumeAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHelper.clearAutoScroll(binding.vpResortHotelMb)  // Xóa thiết lập khi Fragment bị hủy
        autoScrollHelper.clearAutoScroll(binding.vpResortHotelMt)  // Xóa thiết lập khi Fragment bị hủy
        autoScrollHelper.clearAutoScroll(binding.vpResortHotelMn)  // Xóa thiết lập khi Fragment bị hủy
        //autoScrollHelper.clearAutoScroll(binding.anotherViewPager)
    }

    // Reusable launchForResult method to handle activity results
    private fun launchForResult(intent: Intent, resultHandler: (String?) -> Unit) {
        locationResultLauncher.launch(intent)
    }
    private fun initActivityResultLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation = data?.getStringExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY)
                    selectedLocation?.let {
                        binding.tvLocation.text = selectedLocation
                    }
                }
            }
    }

}