package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.TimeshareCompanyAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment.CheckTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment.CreatePostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment.CreateTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment.PaymentPostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment.SelectPackageFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment.SelectTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ProcessBar.ProcessBarManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPostingBinding

class RentalPostingActivity : BaseActivity() {
    private lateinit var binding: ActivityRentalPostingBinding
    private lateinit var processBarManager: ProcessBarManager
    private lateinit var FragmentAdapter: FragmentAdapter
    var step: Int = 1
    private var timeshareCompanyAdapter = TimeshareCompanyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRentalPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        processBarManager = ProcessBarManager(binding.progressBarLayout)
        processBarManager.updateProgress(step)

        setPostingFlowScreen()

    }

    private fun setPostingFlowScreen() {
        val listFragment: ArrayList<Fragment> = ArrayList()

        // Step 1 Check Availability Timeshare
        listFragment.add(CheckTimeshareFragment())

        // Step 2 Create Timeshare
        listFragment.add(CreateTimeshareFragment())

        // Step 3 Select Timeshare
        listFragment.add(SelectTimeshareFragment())

        // Step 4 Select Package Posting
        listFragment.add(SelectPackageFragment())

        // Step 5 Create Timeshare Posting
        listFragment.add(CreatePostingFragment())

        // Step 6 Payment
        listFragment.add(PaymentPostingFragment())

        FragmentAdapter = FragmentAdapter(this, listFragment)

        binding.viewPager.apply {
            adapter = FragmentAdapter
            isUserInputEnabled = true
            offscreenPageLimit = 6
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    step = position + 1
                    processBarManager.updateProgress(step)
                }
            })
        }

    }




}