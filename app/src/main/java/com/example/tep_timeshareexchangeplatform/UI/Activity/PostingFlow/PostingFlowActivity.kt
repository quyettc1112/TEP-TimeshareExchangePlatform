package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment.Step_1_CheckTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment.Step_5_CreatePostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment.Step_2_CreateTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment.Step_6_PaymentPostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment.Step_4_SelectPackageFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment.Step_3_SelectTimeshareFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ProcessBar.ProcessBarManager
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostingFlowActivity : BaseActivity() {
    private lateinit var binding: ActivityRentalPostingBinding
    private lateinit var processBarManager: ProcessBarManager
    private lateinit var FragmentAdapter: FragmentAdapter
    private val postingFlowViewModel: PostingFlowViewModel by viewModels()
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
        getIntentValue()
    }

    private fun getIntentValue() {
        val intent = intent
        if (intent != null) {
            when (intent.getStringExtra(Constant.POSTING_TYPE_FLOW)) {
                Constant.RENTAL_POSTING_FLOW -> {
                    postingFlowViewModel.updateTypeOfPostingFlow(Constant.RENTAL_POSTING_FLOW)
                    binding.customToolbar3.setTitle("Đăng Bài Cho Thuê")
                }

                Constant.EXCHANGER_POSTING_FLOW -> {
                    postingFlowViewModel.updateTypeOfPostingFlow(Constant.EXCHANGER_POSTING_FLOW)
                    binding.customToolbar3.setTitle("Đăng Bài Trao Đổi")
                }
            }
        }


    }


    private fun initProcessBarWithViewModel() {
        // Init Process Bar
        processBarManager = ProcessBarManager(binding.progressBarLayout, postingFlowViewModel)

        // Observe Step with viewModel
        postingFlowViewModel.step.observe(this) {
            processBarManager.updateProgress(it)

            // Chuyển ViewPager sang trang tương ứng với step
            if (binding.viewPager.currentItem != postingFlowViewModel.step.value!! - 1) {
                binding.viewPager.setCurrentItem(postingFlowViewModel.step.value!! - 1, true)
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
                    postingFlowViewModel.updateStep(position + 1)
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}