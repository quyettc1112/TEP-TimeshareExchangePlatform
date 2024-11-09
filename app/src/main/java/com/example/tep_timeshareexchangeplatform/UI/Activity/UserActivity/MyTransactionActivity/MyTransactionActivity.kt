package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment.TransactionAllFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionPaymentFragment.TransactionPaymentFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionReceiveFragment.TransactionReceiveFragment
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTranscationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTransactionActivity : BaseActivity() {

    private lateinit var binding: ActivityTranscationBinding
    private lateinit var FragmentAdapter: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTranscationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbar.onStartIconClick = {
            onBackPressed()
        }
        setUpTabLayoutViewPager()


    }

    private fun setUpTabLayoutViewPager() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(TransactionAllFragment())
        listFragment.add(TransactionPaymentFragment())
        listFragment.add(TransactionReceiveFragment())
        // Set up TabLayout
        binding.tblTopResort.let {
            // Add 2 tab
            it.addTab(it.newTab().setText("Tất Cả"))
            it.addTab(it.newTab().setText("Thanh Toán"))
            it.addTab(it.newTab().setText("Nhận Tiền"))

            // Set Text Color
            it.setTabTextColors(
                resources.getColor(R.color.black),
                resources.getColor(R.color.white)
            )

            // Set Tab Layout Onclick Event
            it.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.viewPager.currentItem = tab!!.position
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
        FragmentAdapter = FragmentAdapter(this, listFragment)
        binding.viewPager.let {
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



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}