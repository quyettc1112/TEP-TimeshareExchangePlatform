package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.ViewModel.PaymentPackageViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentType
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityPaymentPackageBinding
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentPackageActivity : BaseActivity() {
    private lateinit var binding: ActivityPaymentPackageBinding
    private val paymentPackageViewModel: PaymentPackageViewModel by viewModels()
    private lateinit var paymentResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var tokenManager: TokenManager
    private var selectedCard: MaterialCardView? = null

    private var packageId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        packageId = intent.getIntExtra(Constant.DEFAULT_PACKAGE_SELECTION, 0)
        checkTokenValid()
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
        onPaymentMethodSelected()
    }

    private fun checkTokenValid() {
        tokenManager = TokenManager(this)
        if (!tokenManager.isLoggedIn()) {
            MotionToast.Companion.createColorToast(
                this,
                "Thất Bại",
                "Vui lòng đăng nhập để thực hiện chức năng này",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
            finish()
        }
        val customerInfo = tokenManager.getCustomerInfo()
        binding.tvWalletBalance.text = "${formatPrice(customerInfo?.walletAvailableMoney!!)} đ"

    }

    private fun observeData() {
        // Observe Extend Membership By VNPAY
        paymentPackageViewModel.responseVNPAYUrl.observe(this) {
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

        // Observe Extend Membership By Wallet
        paymentPackageViewModel.memberShipResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    val intent = Intent(this, PaymentResultActivity::class.java)
                    intent.putExtra(Constant.PAYMENT_SUCCESS_PACKAGE, it.data)
                    paymentResultLauncher.launch(intent)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this,
                        "${it.message}",
                        null
                    )
                }
            }
        }

        // Observe Selected Payment Method
        paymentPackageViewModel.selectedPaymentMethod.observe(this, Observer { method ->
            when (method) {
                PaymentMethod.VNPAY -> {
                    updateCardViewAppearance(binding.cardVnpay, true)
                    updateCardViewAppearance(binding.cardUnwind, false)
                }

                PaymentMethod.UNWIND -> {
                    updateCardViewAppearance(binding.cardUnwind, true)
                    updateCardViewAppearance(binding.cardVnpay, false)
                }
            }
        })


    }

    private fun setCustomToolBar() {
        binding.customToolbar6.onStartIconClick = {
            finish()
        }
    }

    private fun getIntentData() {
        packageId = intent.getIntExtra(Constant.DEFAULT_PACKAGE_SELECTION, 0)
        if (packageId == 0) {
            finish()
            return
        }
        val rentalPackageEnum = RentalPackageEnum.entries.find { it.packageModel.id == packageId } ?: return

        if (rentalPackageEnum.packageModel.type == "Membership") {
            when (rentalPackageEnum) {
                RentalPackageEnum.MEMBERSHIP_MONTHLY -> {
                    binding.includePackagePosting.apply {
                        tvTitle.text = "Gói Thành Viên Unwind"
                        tvPackagePrice.text =
                            "${formatPrice(rentalPackageEnum.packageModel.price)} VND"
                        tvPackageName.text = rentalPackageEnum.packageModel.name.toString()
                        llTypePackage.setBackgroundResource(R.drawable.lite_gradient)
                        bindDataPaymentInfo(rentalPackageEnum)

                    }
                }

                RentalPackageEnum.MEMBERSHIP_YEARLY -> {
                    binding.includePackagePosting.apply {
                        tvTitle.text = "Gói Thành Viên Unwind"
                        tvPackagePrice.text =
                            "${formatPrice(rentalPackageEnum.packageModel.price)} VND"
                        tvPackageName.text = rentalPackageEnum.packageModel.name.toString()
                        llTypePackage.setBackgroundResource(R.drawable.pro_gradient)
                        bindDataPaymentInfo(rentalPackageEnum)
                    }
                }

                RentalPackageEnum.BASIC_SERVICE -> TODO()
                RentalPackageEnum.ADVANCED_SERVICE -> TODO()
                RentalPackageEnum.PREMIUM_SERVICE -> TODO()
                RentalPackageEnum.DELEGATED_SERVICE -> TODO()
            }
        }

        if (tokenManager.getCustomerInfo()?.walletAvailableMoney!! < rentalPackageEnum.packageModel.price) {
            binding.cardUnwind.isEnabled = false }



    }

    private fun bindDataPaymentInfo(rentalPackageEnum: RentalPackageEnum) {
        // Hide Unessary Views
        binding.includePackagePosting.rvFeatures.visibility = View.GONE
        binding.includePackagePosting.tvPackageDescription.visibility = View.GONE

        binding.apply {
            tvServiceName.text = rentalPackageEnum.packageModel.name
            tvPriceService.text = "${formatPrice(rentalPackageEnum.packageModel.price)} VND"
            tvDuration.text = "${rentalPackageEnum.packageModel.duration} Tháng"
            tvFeeService.text = "Miễn Phí"
            tvTotalFee.text = "${formatPrice(rentalPackageEnum.packageModel.price)} VND"
            tvTotalAmount.text = "${formatPrice(rentalPackageEnum.packageModel.price)} VND"
        }

    }

    private fun onRequestButtonClicked() {
        binding.ctrRequestButton.setOnClickListener {

            // Get Token
            val token = TokenManager(this@PaymentPackageActivity)
            if (token.getAccessToken() == null) {
                MotionToast.Companion.createColorToast(
                    this,
                    "Thất Bại",
                    "Vui lòng đăng nhập để thực hiện chức năng này",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
                return@setOnClickListener
            }

            // Get Payment Method
            val paymentMethod = paymentPackageViewModel.selectedPaymentMethod.value

            // Get Package Enum
            val rentalPackageEnum = RentalPackageEnum.entries.find { it.packageModel.id == packageId }
                ?: return@setOnClickListener

            // Check Payment Method, Call API to get Payment URL or Check Wallet Balance
            when (paymentMethod) {
                // Call API to check Wallet Balance, Intent to PaymentResultActivity
                PaymentMethod.UNWIND -> {
                    paymentPackageViewModel.extendMembershipByWallet(
                        token.getAccessToken().toString(), packageId
                    )
                }

                // Call API to get Payment URL, Intent to VNPayActivity
                PaymentMethod.VNPAY -> {
                    paymentPackageViewModel.getResponsePaymentUrl(
                        rentalPackageEnum.packageModel.price,
                        rentalPackageEnum.packageModel.name
                    )
                }

                else -> {
                    MotionToast.Companion.createColorToast(
                        this,
                        "Thất Bại",
                        "Vui lòng chọn phương thức thanh toán",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }
    }

    private fun intentToVNPAYActivity(url: String) {
        val intent = Intent(this, VNPayActivity::class.java)
        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.GENERAL_ID_PAYMENT, packageId)
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.PURCHASE_PACKAGE_MEMBER)
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

    private fun onPaymentMethodSelected() {
        binding.cardUnwind.setOnClickListener {
            selectedCard = binding.cardUnwind
            paymentPackageViewModel.selectPaymentMethod(PaymentMethod.UNWIND)
        }
        binding.cardVnpay.setOnClickListener {
            selectedCard = binding.cardVnpay
            paymentPackageViewModel.selectPaymentMethod(PaymentMethod.VNPAY)
        }
    }

    // Hàm để cập nhật giao diện của CardView
    private fun updateCardViewAppearance(cardView: MaterialCardView, isSelected: Boolean) {
        cardView.apply {
            strokeWidth = if (isSelected) 4 else 0
            strokeColor = ContextCompat.getColor(
                this@PaymentPackageActivity,
                if (isSelected) R.color.blue_see_more else R.color.white
            )
        }
    }

    fun formatPrice(price: Long): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
    }


}