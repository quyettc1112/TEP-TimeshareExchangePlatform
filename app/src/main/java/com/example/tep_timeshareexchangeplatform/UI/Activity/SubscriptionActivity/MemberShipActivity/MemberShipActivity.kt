package com.example.tep_timeshareexchangeplatform.UI.Activity.SubscriptionActivity.MemberShipActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.SubscriptionActivity.MemberShipActivity.Adapter.MemberShipAdapter
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMemberShipBinding

class MemberShipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemberShipBinding
    private var memberShipAdapter = MemberShipAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMemberShipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        initViewPagerView()

        binding.customToolbar4.onStartIconClick = {
            onBackPressed()
            finish()
        }

        binding.imgNext.setOnClickListener {
           startActivity(Intent(this, RentalPostingActivity::class.java))
        }

    }

    private fun initAdapter() {
        memberShipAdapter.submitList(Constant.listMemberShip)
    }

    private fun initViewPagerView() {
        binding.vpMemberShip.adapter = memberShipAdapter
        // Set Indicator
        binding.indicator.apply {
            setViewPager(binding.vpMemberShip)
            tintIndicator(ContextCompat.getColor(context, R.color.blue_full))
        }
    }
}