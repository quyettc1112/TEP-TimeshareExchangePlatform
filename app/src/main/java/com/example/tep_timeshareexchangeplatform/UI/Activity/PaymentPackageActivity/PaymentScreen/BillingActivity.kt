package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.PaymentScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBillingBinding
import java.text.DecimalFormat

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

    }

    private fun bindData() {
        val memberShipResponse = intent.getParcelableExtra<MemberShipResponse>(Constant.PAYMENT_SUCCESS)
        if (memberShipResponse == null) {
            Log.e("BillingActivity", "memberShipResponse is null")
            return
        }
        binding.apply {
            tvMoneyTrancsaction.text = "${formatPrice(memberShipResponse.walletTransactionDto.money)} VND"
            paymentDate.text = memberShipResponse.walletTransactionDto.createdAt
            transactionId.text = memberShipResponse.walletTransactionDto.id

            clickHandle(memberShipResponse.walletTransactionDto.id)

        }

    }

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }

    private fun clickHandle(transactionId: String) {
        binding.ctrRequestButton.setOnClickListener {
            val intent = Intent(this, MyTransactionDetailActivity::class.java)
            intent.putExtra(Constant.TRANSACTION_ID, transactionId)
            startActivity(intent)
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