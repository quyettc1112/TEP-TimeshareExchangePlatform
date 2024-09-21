package com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.RoomSelectionDialog
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.TopDialog.TopDialogFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.ChildFragment.ResortFragment.ResortFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.ChildFragment.TimeshareFragment.TimeshareFragment
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopResortBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TopResortFragment : BaseFragment(R.layout.fragment_top_resort)  {

    private lateinit var binding: FragmentTopResortBinding
    private lateinit var FragmentAdapter: FragmentAdapter


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
        return binding.root
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
                    if (tab != null) { binding.vpResortTimeshare.currentItem = tab!!.position }
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