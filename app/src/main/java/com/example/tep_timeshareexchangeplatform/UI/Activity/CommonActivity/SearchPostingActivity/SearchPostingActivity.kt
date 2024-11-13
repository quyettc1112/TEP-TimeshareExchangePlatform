package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.ExchangePostingFragment.ExchangePostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment.TransactionAllFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionPaymentFragment.TransactionPaymentFragment
import com.example.tep_timeshareexchangeplatform.databinding.ActivitySearchPostingBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPostingActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchPostingBinding
    private lateinit var FragmentAdapter: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpTabLayoutViewPager()
        binding.toolbar.onStartIconClick = {
            onBackPressed()
        }


    }

    private fun setUpTabLayoutViewPager() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(PublicPostingFragment())
        listFragment.add(ExchangePostingFragment())

        // Set up TabLayout
        binding.tblTopResort.let {
            // Add 2 tab
            it.addTab(it.newTab().setText("Cho Thuê"))
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