package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.HomeFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.RoomSelectionDialog
import com.example.tep_timeshareexchangeplatform.Common.Adapter.BlogAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ResortAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.GridAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SpannedGridLayoutManager.SpannedGridLayoutManager
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.PostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.SearchPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail.ResortDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentHomeBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val publicsPostingAdapter = HomePostingAdapter()
    private val resortAdapterMB = ResortAdapter()
    private val blogAdapter = BlogAdapter()
    lateinit var gridAdapter: GridAdapter
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>
    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callAPIGetData()
        gridAdapter = GridAdapter(Constant.destiantionList) { destinationModel ->
            // Xử lý sự kiện khi item được click
            Toast.makeText(
                requireContext(),
                "Clicked: ${destinationModel.destinationName}",
                Toast.LENGTH_SHORT
            ).show()
        }

        publicsPostingAdapter.submitList(listOf())
        resortAdapterMB.submitList(listOf())
        blogAdapter.submitList(Constant.blogList)

        // Khởi tạo ActivityResultLauncher
        initActivityResultLauncher()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        showUIAdapter()
        onItemCLickHandler()
        onSearchComponentClickHandler()
        setAutoScroll()
        observerSearchComponent()
        observerViewModel()
        return binding.root
    }

    /**
     * Observer ExchangeOfResortViewModel
     *
     * Observer Search Component
     * Observer ExchangeOfResortViewModel
     */
    private fun observerSearchComponent() {
        // Quan sát các giá trị từ ExchangeOfResortViewModel
        mainViewModel.roomCount.observe(viewLifecycleOwner, Observer { count ->
            // Cập nhật giao diện với số phòng
            binding.tvTourist.text = mainViewModel.getRoomCount()
        })

        mainViewModel.adultCount.observe(viewLifecycleOwner, Observer { count ->
            // Cập nhật giao diện với số người lớn
            binding.tvTourist.text = mainViewModel.getRoomCount()
        })

        mainViewModel.childrenCount.observe(viewLifecycleOwner, Observer { count ->
            // Cập nhật giao diện với số trẻ em
            binding.tvTourist.text = mainViewModel.getRoomCount()
        })

        mainViewModel.location.observe(viewLifecycleOwner, Observer { location ->
            binding.tvLocation.text = location
        })


    }

    private fun observerViewModel() {
        homeViewModel.resortList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    (activity as MainActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    resource.data?.let { resortModel ->
                        resortAdapterMB.submitList(resortModel.content)
                    }

                }

                Status.ERROR -> {
                    MotionToast.Companion.createToast(
                        requireActivity(),
                        "Error",
                        "Error ${resource.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        })

        homeViewModel.home_PostingList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.lottiePostingLoading.visibility = View.GONE
                    publicsPostingAdapter.submitList(it.data?.content)
                }

                Status.ERROR -> {
                    MotionToast.Companion.createToast(
                        requireActivity(),
                        "Error",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    binding.lottiePostingLoading.visibility = View.VISIBLE
                }
            }
        }
    }


    /**
     * CALL API
     */
    private fun callAPIGetData() {
        homeViewModel.getResortList(0, 10, "")
        homeViewModel.getPublicPostingsHome(0, 10, "")
    }


    /**
     * BIND DATA
     *
     * Show UI Adapter
     * Bind Data of Adapter to UI
     */
    private fun showUIAdapter() {
        // List Timesahre Recomend
        binding.rvSuggestTimeshare.layoutManager = GridLayoutManager(
            requireContext(),
            1, // Số lượng hàng (span) là 1
            GridLayoutManager.HORIZONTAL, // Hiển thị theo chiều ngang
            false
        )
        binding.rvSuggestTimeshare.adapter = publicsPostingAdapter

        // List Blog
        binding.rcBlog.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcBlog.adapter = blogAdapter

        // List Resort Recomend MB
        binding.vpResortHotelMb.let {
            it.adapter = resortAdapterMB
            it.clipToPadding = true
            it.clipChildren = false
            it.offscreenPageLimit = 5
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

        }

        binding.vpResortHotelMn.let {
            it.adapter = resortAdapterMB
            it.clipToPadding = true
            it.clipChildren = false
            it.offscreenPageLimit = 5
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

        }

        binding.vpResortHotelMt.let {
            it.adapter = resortAdapterMB
            it.clipToPadding = true
            it.clipChildren = false
            it.offscreenPageLimit = 5
            it.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

        }

        // List Destination
        val manager = SpannedGridLayoutManager(
            object : SpannedGridLayoutManager.GridSpanLookup {
                override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
                    // Conditions for 2x2 items
                    return when (position) {
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


    /**
     * EVENT CLICK HANDLER
     *
     * Resort Item Click, Blog Item Click, Posting Item Click
     * Search Component Click
     */
    private fun onItemCLickHandler() {
        resortAdapterMB.let {
            it.onItemClick = {
                val intent = Intent(requireContext(), ResortDetailActivity::class.java)
                intent.putExtra(Constant.DEFAULT_RESORT_ID, it.id)
                startActivity(intent)
            }

            it.onFavoriteClick = {
                Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
            }
        }
        publicsPostingAdapter.let {
            it.onItemClick = {
                val intent = Intent(requireContext(), PostingDetailActivity::class.java)
                intent.putExtra(Constant.DEFAULT_POSTING_ID, it.rentalPostingId)
                startActivity(intent)
            }
            it.onFavoriteClick = {
                Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSearchComponentClickHandler() {
        binding.let {
            // Location Click Event
            it.llLocation.setOnClickListener {
                val intent = Intent(requireContext(), LocationActivity::class.java)
                locationResultLauncher.launch(intent)
            }
            it.llTourist.setOnClickListener {
                val roomSelectionDialog = RoomSelectionDialog.newInstance()
                roomSelectionDialog.show(parentFragmentManager, "RoomSelectionDialog")
            }
            it.llDate.setOnClickListener {
                val constraintsBuilder = CalendarConstraints.Builder()
                    .setValidator(object : CalendarConstraints.DateValidator {
                        override fun isValid(date: Long): Boolean {


                            return true
                        }

                        override fun describeContents(): Int = 0
                        override fun writeToParcel(dest: Parcel, flags: Int) {
                        }

                    })

                // Tạo DateRangePicker với CalendarConstraints
                val dateRangePicker =
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText(getString(R.string.date_range_picker))
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()

                // Hiển thị DateRangePicker khi nhấn nút
                dateRangePicker.show(requireActivity().supportFragmentManager, "DateRangePicker")


                // Lắng nghe sự kiện khi người dùng chọn ngày
                dateRangePicker.addOnPositiveButtonClickListener { selection ->
                    val startDate = selection?.first
                    val endDate = selection?.second

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val startDateString = startDate?.let { dateFormat.format(Date(it)) } ?: "N/A"
                    val endDateString = endDate?.let { dateFormat.format(Date(it)) } ?: "N/A"

                    binding.tvDate.text = "$startDateString - $endDateString"
                    mainViewModel.updateDateRange("$startDateString - $endDateString")

                }
            }

            it.btnSearch.setOnClickListener {
                val location = mainViewModel.getLocation()
                val dateRange = mainViewModel.getDateRange()
                val roomCount = mainViewModel.roomCount.value
                val intent = Intent(requireContext(), SearchPostingActivity::class.java)
                intent.putExtra(Constant.SEARCH_LOCATION, location)
                intent.putExtra(Constant.SEARCH_DATE, dateRange)
                intent.putExtra(Constant.SEARCH_ROOM, roomCount)
                startActivity(intent)


            }
        }
    }


    /**
     * AUTO SCROLL
     * Activity Result Launcher
     */
    private fun setAutoScroll() {
        // Auto Scroll
        autoScrollHelper.setupAutoScroll(binding.vpResortHotelMb)
        autoScrollHelper.setupAutoScroll(binding.vpResortHotelMt)
        autoScrollHelper.setupAutoScroll(binding.vpResortHotelMn)
    }

    private fun initActivityResultLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation =
                        data?.getStringExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY)
                    selectedLocation?.let {
                        mainViewModel.updateLocation(selectedLocation)
                    }
                }
            }
    }


    /**
     * LIFE CYCLE
     */
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

}