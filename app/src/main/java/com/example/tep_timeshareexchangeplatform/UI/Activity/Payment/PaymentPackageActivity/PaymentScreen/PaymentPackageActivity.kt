package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.ViewModel.PaymentPackageViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityPaymentPackageBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentPackageActivity : BaseActivity() {
    private lateinit var binding: ActivityPaymentPackageBinding
    private val paymentPackageViewModel: PaymentPackageViewModel by viewModels()
    private lateinit var paymentResultLauncher: ActivityResultLauncher<Intent>

    private var packageId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setCustomToolBar()
        getIntentData()
        onRequestButtonClicked()

        // When Payment Sucess, finish this activity
        initActivityResultLauncher()
    }

    private fun observeData() {
        paymentPackageViewModel.responseUrl.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    intentToVNPAYActivity(it.data?.url.toString())
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }
    }

    private fun setCustomToolBar() {
        binding.customToolbar6.onStartIconClick = {
            finish()
        }
    }

    private fun getIntentData() {
        packageId = intent.getIntExtra(Constant.DEFAULT_MEMBERSHIP_PACKAGE_SELECTION, 1)
        if (packageId == 0) {
            finish()
            return
        }
        val packageEnum = PackageEnum.entries.find { it.packageModel.id == packageId } ?: return

        if (packageEnum.packageModel.type == "Membership") {
            when (packageEnum) {
                PackageEnum.MEMBERSHIP_MONTHLY -> {
                    binding.includePackagePosting.apply {
                        tvTitle.text = "Gói Thành Viên Unwind"
                        tvPackagePrice.text = "${formatPrice(packageEnum.packageModel.price)} VND"
                        tvPackageName.text = packageEnum.packageModel.name.toString()
                        llTypePackage.setBackgroundResource(R.drawable.lite_gradient)
                        bindDataPaymentInfo(packageEnum)

                    }
                }

                PackageEnum.MEMBERSHIP_YEARLY -> {
                    binding.includePackagePosting.apply {
                        tvTitle.text = "Gói Thành Viên Unwind"
                        tvPackagePrice.text = "${formatPrice(packageEnum.packageModel.price)} VND"
                        tvPackageName.text = packageEnum.packageModel.name.toString()
                        llTypePackage.setBackgroundResource(R.drawable.pro_gradient)
                        bindDataPaymentInfo(packageEnum)
                    }
                }

                PackageEnum.BASIC_SERVICE -> TODO()
                PackageEnum.ADVANCED_SERVICE -> TODO()
                PackageEnum.PREMIUM_SERVICE -> TODO()
                PackageEnum.DELEGATED_SERVICE -> TODO()
            }
        }

    }

    private fun bindDataPaymentInfo(packageEnum: PackageEnum) {
        // Hide Unessary Views
        binding.includePackagePosting.rvFeatures.visibility = View.GONE
        binding.includePackagePosting.tvPackageDescription.visibility = View.GONE

        binding.apply {
            tvServiceName.text = packageEnum.packageModel.name
            tvPriceService.text = "${formatPrice(packageEnum.packageModel.price)} VND"
            tvDuration.text = "${packageEnum.packageModel.duration} Tháng"
            tvFeeService.text = "Miễn Phí"
            tvTotalFee.text = "${formatPrice(packageEnum.packageModel.price)} VND"
            tvTotalAmount.text = "${formatPrice(packageEnum.packageModel.price)} VND"
        }

    }

    private fun onRequestButtonClicked() {
        binding.ctrRequestButton.setOnClickListener {
            val packageEnum = PackageEnum.entries.find { it.packageModel.id == packageId }
                ?: return@setOnClickListener
            paymentPackageViewModel.getResponsePaymentUrl(
                packageEnum.packageModel.price,
                packageEnum.packageModel.name
            )
        }
    }

    private fun intentToVNPAYActivity(url: String) {
        val intent = Intent(this, VNPayActivity::class.java)
        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.DEFAULT_MEMBERSHIP_PACKAGE_SELECTION, packageId)
        paymentResultLauncher.launch(intent)
    }

    private fun initActivityResultLauncher() {
        paymentResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    finish()
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }


}