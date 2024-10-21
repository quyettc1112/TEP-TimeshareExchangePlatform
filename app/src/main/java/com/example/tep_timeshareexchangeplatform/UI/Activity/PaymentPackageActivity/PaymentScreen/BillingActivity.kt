package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.PaymentScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionDetailActivity
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBillingBinding

class BillingActivity : BaseActivity() {
    private lateinit var binding: ActivityBillingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBillingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindData()
        clickHandle()
    }

    private fun bindData() {
        val intent = intent.getSerializableExtra(Constant.PAYMENT_SUCCESS)
        Log.d("CheckGetIntentDATa", "bindData: $intent")


    }

    private fun clickHandle() {
        binding.ctrRequestButton.setOnClickListener {
            startActivity(Intent(this, MyTransactionDetailActivity::class.java))
        }

        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}