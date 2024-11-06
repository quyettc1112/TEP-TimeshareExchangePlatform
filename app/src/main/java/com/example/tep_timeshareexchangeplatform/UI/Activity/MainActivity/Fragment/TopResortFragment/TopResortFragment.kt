package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.TopDialog.TopDialogFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ExchangePostingFragment.ExchangePostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment.ResortAdapterRV
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.OnBottomNavVisibilityListener
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment.ResortFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.PublicPostingFragment.PublicPostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopResortBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopResortFragment : BaseFragment(R.layout.fragment_top_resort) {

    private lateinit var binding: FragmentTopResortBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private var bottomNavVisibilityListener: OnBottomNavVisibilityListener? = null
    private lateinit var resortAdapter: ResortAdapterRV
    private val mainViewModel: MainViewModel by activityViewModels()
    private val dialog = TopDialogFragment()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBottomNavVisibilityListener) {
            bottomNavVisibilityListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnBottomNavVisibilityListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopResortBinding.inflate(inflater, container, false)
        setUpTabLayoutViewPager()
        setEventSearchComponent()
        observeData()
        return binding.root
    }

    private fun observeData() {
        // Observe data from ViewModel Location
        mainViewModel.location.observe(viewLifecycleOwner) {
            binding.tvSearchLocation.text = it
        }

        // Observe data from ViewModel DateRange
        mainViewModel.dateRange.observe(viewLifecycleOwner) {
            binding.tvDate.text = it
        }

        // Observe data from ViewModel Resort
        mainViewModel.roomCount.observe(viewLifecycleOwner) {
            binding.tvRoom.text = mainViewModel.getRoomCount()
        }


    }


    private fun setUpTabLayoutViewPager() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(ResortFragment())
        listFragment.add(PublicPostingFragment())
        listFragment.add(ExchangePostingFragment())
        // Set up TabLayout
        binding.tblTopResort.let {
            // Add 2 tab
            it.addTab(it.newTab().setText("Top Resort"))
            it.addTab(it.newTab().setText("Bài Đăng"))
            it.addTab(it.newTab().setText("Trao Đổi"))

            // Set Text Color
            it.setTabTextColors(
                resources.getColor(R.color.black),
                resources.getColor(R.color.white)
            )

            // Set Tab Layout Onclick Event
            it.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.vpResortTimeshare.currentItem = tab!!.position
                        bottomNavVisibilityListener!!.showBottomNav()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Not thing to do
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Not thing to do
                }
            })

        }

        // Set up ViewPager
        FragmentAdapter = FragmentAdapter(requireActivity(), listFragment)
        binding.vpResortTimeshare.let {
            it.adapter = FragmentAdapter
            it.isUserInputEnabled = false
            it.offscreenPageLimit = 2
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.tblTopResort.selectTab(binding.tblTopResort.getTabAt(position))
                }
            })
        }

    }

    private fun setEventSearchComponent() {
        binding.crSearchComponent.setOnClickListener {
            val roomSelectionDialog = TopDialogFragment.newInstance()
            roomSelectionDialog.show(parentFragmentManager, "RoomSelectionDialog")

            roomSelectionDialog.setOnSearchClickListener {
                mainViewModel.apply {
                    resetCurrentResortPage()
                    resetCurrentPostingPage()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

    }

}