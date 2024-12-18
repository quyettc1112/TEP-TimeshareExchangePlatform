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
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PolicyActivity.Adapter.PolicyAdapter
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityPolicyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyActivity : BaseActivity() {
    private lateinit var binding: ActivityPolicyBinding
    private val policyAdapter = PolicyAdapter()
    private val viewModel: PolicyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.callGetAllPolicy()
        observeData()
        initAdapter()
        eventClickToolbar()
    }

    private fun initAdapter() {
        policyAdapter.submitList(listOf())
        binding.rvPolicy.apply {
            layoutManager = LinearLayoutManager(this@PolicyActivity)
            adapter = policyAdapter
        }
    }

    private fun observeData() {
        viewModel.policyResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    it.data?.let { policyAdapter.submitList(it) }
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