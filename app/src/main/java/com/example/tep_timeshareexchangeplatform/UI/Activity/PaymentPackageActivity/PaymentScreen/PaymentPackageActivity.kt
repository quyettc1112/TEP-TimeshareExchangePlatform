package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.PaymentScreen

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.databinding.ActivityPaymentPackageBinding
import java.text.DecimalFormat

class PaymentPackageActivity : BaseActivity() {
    private lateinit var binding: ActivityPaymentPackageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setCustomToolBar()
        getIntentData()
    }

    private fun setCustomToolBar() {
        binding.customToolbar6.onStartIconClick = {
            finish()
        }
    }
    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }

    private fun getIntentData() {
        val packageId: Int = intent.getIntExtra(Constant.DEFAULT_MEMBERSHIP_PACKAGE_SELECTION, 0)
        if(packageId == 0) {
            finish()
            return
        }
        val packageEnum = PackageEnum.entries.find { it.packageModel.id == packageId } ?: return

        if (packageEnum.packageModel.type == "Membership") {
            when(packageEnum) {
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}