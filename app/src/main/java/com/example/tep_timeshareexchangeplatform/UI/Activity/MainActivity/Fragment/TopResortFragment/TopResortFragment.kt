package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.TopDialog.TopDialogFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.OnBottomNavVisibilityListener
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment.ResortFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment.TimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopResortBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopResortFragment : BaseFragment(R.layout.fragment_top_resort)  {

    private lateinit var binding: FragmentTopResortBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private var bottomNavVisibilityListener: OnBottomNavVisibilityListener? = null
    private val topResortViewModel: TopResortViewModel by viewModels()


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
        observeViewModel()
        binding = FragmentTopResortBinding.inflate(inflater, container, false)
        setUpTabLayoutViewPager()
        setEventSearchComponent()
        return binding.root
    }

    private fun observeViewModel(){
        topResortViewModel.postingsResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as MainActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {

                }

                Status.ERROR -> {
                    // Show error
                }
            }
        }
    }

    private fun setUpTabLayoutViewPager(){
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(ResortFragment())
        listFragment.add(TimeshareFragment())

        // Set up TabLayout
        binding.tblTopResort.let {
            // Add 2 tab
            it.addTab(it.newTab().setText("Top Resort"))
            it.addTab(it.newTab().setText("Timeshare"))

            // Set Text Color
            it.setTabTextColors(
                resources.getColor(R.color.black),
                resources.getColor(R.color.white)
            )

            // Set Tab Layout Onclick Event
            it.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
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
            it.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    binding.tblTopResort.selectTab(binding.tblTopResort.getTabAt(position))
                }
            })
        }

    }
    private fun setEventSearchComponent(){
        binding.crSearchComponent.setOnClickListener {
            val dialog = TopDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, "TopDialogFragment")
        }



    }

}