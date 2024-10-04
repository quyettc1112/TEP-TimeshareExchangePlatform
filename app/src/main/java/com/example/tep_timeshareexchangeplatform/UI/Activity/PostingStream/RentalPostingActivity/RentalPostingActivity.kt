package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingStream.RentalPostingActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
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

        CustomProgressBar(binding.progressBarLayout).updateProgress(3)



    }


}