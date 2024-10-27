package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen

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
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBillingBinding
import java.text.DecimalFormat

class PaymentResultActivity : BaseActivity() {
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
            Log.e("PaymentResultActivity", "memberShipResponse is null")
            return
        }
        binding.apply {
            tvPrice.text = "${formatPrice(memberShipResponse.walletTransactionDto.money)} VND"
            tvDate.text = memberShipResponse.walletTransactionDto.createdAt
            tvTransactionId.text = memberShipResponse.walletTransactionDto.id
            tvDescription.text = memberShipResponse.walletTransactionDto.description
            clickHandle(memberShipResponse.walletTransactionDto.id)

        }

    }

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }

    private fun clickHandle(transactionId: String) {
        binding.ctrRequestButton.setOnClickListener {
            // Done, Finish Payment Activity
            val intent = intent
            setResult(RESULT_OK, intent)

            // Go to MyTransactionDetailActivity
            val intentToTransactionDetail = Intent(this, MyTransactionDetailActivity::class.java)
            intentToTransactionDetail.putExtra(Constant.TRANSACTION_ID, transactionId)
            startActivity(intentToTransactionDetail)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}