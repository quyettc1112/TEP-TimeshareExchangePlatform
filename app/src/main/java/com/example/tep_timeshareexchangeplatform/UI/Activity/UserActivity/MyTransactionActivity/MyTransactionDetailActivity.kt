package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletDetailRespone
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ViewModel.MyTransactionViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyTransactionDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class MyTransactionDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyTransactionDetailBinding

    private val viewModel: MyTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeData()
        getDataIntent()

        binding.customToolbar.onStartIconClick =  {
            onBackPressed()
        }
    }

    private fun getDataIntent() {
        val intentValue = intent.getStringExtra(Constant.TRANSACTION_ID)
        if (intentValue != null) {
            viewModel.getWalletDetail(TokenManager(this).getAccessToken().toString(), intentValue)
            observeData()
        }
    }

    private fun observeData() {
        viewModel.walletDetailResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.createToast(
                        this,
                        "Error",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

    }

    private fun bindData(walletDetailResponse: WalletDetailRespone) {
        binding.apply {
            tvPriceTransaction.text = "${formatPrice(walletDetailResponse.money)} VND"
            tvTransactionId.text = walletDetailResponse.id
            tvTime.text = walletDetailResponse.createdAt
            tvPaymentMethod.text = walletDetailResponse.paymentMethod
            tvTotalFee.text = "${ formatPrice(walletDetailResponse.fee)} VND"
            tvDescription.text = walletDetailResponse.description.toString()
            tvServiceName.text = walletDetailResponse.transactionType.toString()
        }
        binding.customToolbar.setTitleDetail(walletDetailResponse.createdAt)
    }

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}