package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentRentalActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPaymentBinding

class PaymentRentalActivity : BaseActivity() {
    private lateinit var binding: ActivityRentalPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRentalPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ctrRequestButton.setOnClickListener {
            intentToRentalPaymentConfirmActivity()
        }

        binding.customToolbar.setOnClickListener {
            finish()
        }
    }

    private fun intentToRentalPaymentConfirmActivity() {
        val intent = Intent(this, PaymentRentalConfirmActivity::class.java)
        startActivity(intent)
    }
}