package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment.Step_1_CheckTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment.Step_5_CreatePostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment.Step_2_CreateTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment.Step_6_PaymentPostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment.Step_4_SelectPackageFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment.Step_3_SelectTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ProcessBar.ProcessBarManager
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RentalPostingActivity : BaseActivity() {
    private lateinit var binding: ActivityRentalPostingBinding
    private lateinit var processBarManager: ProcessBarManager
    private lateinit var FragmentAdapter: FragmentAdapter
    private val rentalPostingViewModel: RentalPostingViewModel by viewModels()
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
        initProcessBarWithViewModel()
        setPostingFlowScreen()
        binding.customToolbar3.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun initProcessBarWithViewModel() {
        // Init Process Bar
        processBarManager = ProcessBarManager(binding.progressBarLayout, rentalPostingViewModel)

        // Observe Step with viewModel
        rentalPostingViewModel.step.observe(this) {
            processBarManager.updateProgress(it)

            // Chuyển ViewPager sang trang tương ứng với step
            if (binding.viewPager.currentItem != rentalPostingViewModel.step.value!! - 1) {
                binding.viewPager.setCurrentItem(rentalPostingViewModel.step.value!! - 1, true)
            }
        }


    }
    private fun setPostingFlowScreen() {
        val listFragment: ArrayList<Fragment> = ArrayList()

        // Step 1 Check Availability Timeshare
        listFragment.add(Step_1_CheckTimeshareFragment())

        // Step 2 Create Timeshare
        listFragment.add(Step_2_CreateTimeshareFragment())

        // Step 3 Select Timeshare
        listFragment.add(Step_3_SelectTimeshareFragment())

        // Step 4 Select Package Posting
        listFragment.add(Step_4_SelectPackageFragment())

        // Step 5 Create Timeshare Posting
        listFragment.add(Step_5_CreatePostingFragment())

        // Step 6 Payment
        listFragment.add(Step_6_PaymentPostingFragment())

        FragmentAdapter = FragmentAdapter(this, listFragment)

        binding.viewPager.apply {
            adapter = FragmentAdapter
            isUserInputEnabled = false
            offscreenPageLimit = 6
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    rentalPostingViewModel.updateStep(position + 1)
                }
            })
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}