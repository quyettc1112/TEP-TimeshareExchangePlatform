package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingStream.RentalPostingActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingStream.RentalPostingActivity.Adapter.TimeshareCompanyAdapter
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPostingBinding

class RentalPostingActivity : BaseActivity() {
    private lateinit var binding: ActivityRentalPostingBinding
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
        initAdapter()
        initRecyclerView()
    }

    private fun initAdapter() {
        timeshareCompanyAdapter.submitList(Constant.listTimeshareCompany)
    }

    private fun initRecyclerView() {
        binding.rcTimeshareCompany.apply {
            adapter = timeshareCompanyAdapter
            layoutManager = GridLayoutManager(this@RentalPostingActivity, 2)
        }
    }
}