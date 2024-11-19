package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.PostingOfResortListActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.PostingOfResortListActivity.ChildFragment.Exchange.ExchangePostingResortFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.PostingOfResortListActivity.ChildFragment.Rental.RentalPostingResortFragment
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTimeshareListBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostingOfResortActivity : BaseActivity() {
    private lateinit var binding: ActivityTimeshareListBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private val postingOfResortViewModel: PostingOfResortViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimeshareListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent.getIntExtra(Constant.RESORT_ID, 0)
        if (intent == 0) finish()
        postingOfResortViewModel.setCurrentResortID(intent)



        getIntentData()
        setUpTabLayoutViewPager()

    }


    private fun enableEdgeToEdge() {
        window.setDecorFitsSystemWindows(false)
        window.navigationBarColor = getColor(R.color.white)
    }

    // Get Intent Data
    private fun getIntentData() {
        val resort_name = intent.getStringExtra(Constant.RESORT_NAME)
        binding.toolbar.apply {
            setTitle(resort_name)
        }
    }

    private fun setUpTabLayoutViewPager() {
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(RentalPostingResortFragment())
        listFragment.add(ExchangePostingResortFragment())

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


}