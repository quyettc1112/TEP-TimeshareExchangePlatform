package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.MemberShipResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDepositResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.ViewModel.PaymentResultViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBillingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentResultActivity : BaseActivity() {
    private lateinit var binding: ActivityBillingBinding
    private val paymentResultViewModel: PaymentResultViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

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
        tokenManager = TokenManager(this)

        // Call get new available balance
        paymentResultViewModel.getCustomerInfo(tokenManager.getAccessToken().toString())
        observeData()

    }

    private fun observeData() {
        paymentResultViewModel.customerInfoResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    if (it.data!!.isMember) {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                        tokenManager.saveCustomerInfo(it.data)
                    }
                    else {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                        tokenManager.saveCustomerInfo(it.data)
                    }
                    bindData()
                }
                Status.ERROR -> {
                    hideLoadingWaiting()
                }
            }
        }
    }



    private fun bindData() {
        val intent = intent ?: run {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show()
            return
        }
        when {
            intent.hasExtra(Constant.PAYMENT_SUCCESS_PACKAGE) -> {
                val memberShipResponse = intent.getParcelableExtra<MemberShipResponse>(Constant.PAYMENT_SUCCESS_PACKAGE)
                if (memberShipResponse == null) {
                    return
                }

                // Áp dụng dữ liệu vào UI
                binding.apply {
                    tvPrice.text = "${formatPrice(memberShipResponse.walletTransactionDto.money)} VND"
                    tvDate.text = memberShipResponse.walletTransactionDto.createdAt
                    tvTransactionId.text = memberShipResponse.walletTransactionDto.id
                    tvDescription.text = memberShipResponse.walletTransactionDto.description
                    clickHandle(memberShipResponse.walletTransactionDto.id)
                }
            }

            intent.hasExtra(Constant.PAYMENT_SUCCESS_DEPOSIT) -> {
                val walletDepositResponse = intent.getParcelableExtra<WalletDepositResponse>(Constant.PAYMENT_SUCCESS_DEPOSIT)
                if (walletDepositResponse == null) {
                    return
                }

                // Áp dụng dữ liệu vào UI
                binding.apply {
                    tvPrice.text = "${formatPrice(walletDepositResponse.money)} VND"
                    tvDate.text = walletDepositResponse.createdAt
                    tvTransactionId.text = walletDepositResponse.id
                    tvDescription.text = walletDepositResponse.description
                    clickHandle(walletDepositResponse.id)
                }
            }

            else -> {
                Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
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