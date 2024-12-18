package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PolicyActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.FaqAdapter
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityFaqactivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FAQActivity : BaseActivity() {
    private lateinit var binding: ActivityFaqactivityBinding
    private val viewModel: PolicyViewModel by viewModels()
    private var faqAdapter = FaqAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFaqactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        viewModel.callGetAllFAQ()
        observeData()
        eventClickToolbar()
    }

    private fun initAdapter() {
        faqAdapter.submitList(listOf())
        binding.rvPolicy.apply {
            layoutManager = LinearLayoutManager(this@FAQActivity)
            adapter = faqAdapter
        }
    }

    private fun observeData() {
        viewModel.faqResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    it.data?.let { faqAdapter.submitList(it) }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                }
            }
        })
    }

    private fun eventClickToolbar() {
        binding.customToolbar.onStartIconClick = {
            finish()
        }
    }
}