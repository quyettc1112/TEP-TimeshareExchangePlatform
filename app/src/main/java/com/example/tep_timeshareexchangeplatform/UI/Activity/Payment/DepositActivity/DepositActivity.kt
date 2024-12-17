package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.DepositActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.VNPayActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentType
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityDepositBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositActivity : BaseActivity() {
    private lateinit var binding: ActivityDepositBinding
    private val viewModel: DepositViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDepositBinding.inflate(layoutInflater)
        tokenManager = TokenManager(this)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setMoneyInputLogic()
        observeData()
        requestButtonClick()
        checkValidToken()



    }

    private fun checkValidToken() {
        if(!tokenManager.isLoggedIn()) {
            finish()
        }
        binding.tvWalletBalancel.text = tokenManager.getProfileInfo()
            ?.let { Constant.formatPriceLong(it.walletAvailableMoney) } + " VNĐ"
    }


    private fun observeData() {
        viewModel.responseVNPAYUrl.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    if (it.data != null) {
                        // Nếu nhận được URL từ API, chuyển hướng sang trang thanh toán
                        val url = it.data.url
                        if (url != null) {
                            intentToVNPAYActivity(url)
                        } else {
                            // Hiển thị thông báo lỗi
                            MotionToast.Companion.createColorToast(
                                this,
                                "Thất Bại",
                                "Không thể tạo URL thanh toán",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this, R.font.inter_bold)
                            )
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    // Hiển thị thông báo lỗi
                    MotionToast.Companion.createColorToast(
                        this,
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_bold)
                    )
                }
            }
        }

    }

    private fun setMoneyInputLogic() {
        binding.edtMoney.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                binding.edtMoney.removeTextChangedListener(this)

                val input = editable.toString()
                    .replace("[^\\d]".toRegex(), "") // Loại bỏ các ký tự không phải số

                if(input.isNotEmpty()) {
                    binding.moneyContainer.helperText = null

                }

                if (input.isNotEmpty()) {
                    // Loại bỏ số 0 đầu tiên nếu có
                    var cleanedInput = input
                    if (cleanedInput.startsWith("0")) {
                        cleanedInput = cleanedInput.substring(1)
                    }

                    // Kiểm tra số tiền tối thiểu 10.000
                    val numericValue = cleanedInput.toLongOrNull() ?: 0

                    when {
                        numericValue < 10000 -> {
                            binding.moneyContainer.helperText = "Số tiền tối thiểu là 10.000 VNĐ"
                        }
                        numericValue > 100000000 -> {
                            binding.moneyContainer.helperText = "Số tiền tối đa là 100.000.000 VNĐ"
                        }

                        else -> {
                            binding.moneyContainer.helperText = null
                        }
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " VNĐ"
                    current = formatted
                    binding.edtMoney.setText(formatted)
                    binding.edtMoney.setSelection(formatted.length - 4)
                }

                binding.edtMoney.addTextChangedListener(this)
            }

            // Hàm format để chèn dấu chấm vào các số (ví dụ: 100000 -> 100.000)
            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }
        })
    }

    private fun intentToVNPAYActivity(url: String) {
        val intent = Intent(this, VNPayActivity::class.java)
        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.DEPOSIT_WALLET)
        startActivity(intent)
    }

    private fun requestButtonClick() {
        binding.btnRequest.setOnClickListener {
            val priceString = binding.edtMoney.text.toString().replace(" VNĐ", "").replace(".", "").replace(",", "")
            if (priceString.isNotEmpty() && binding.moneyContainer.helperText.isNullOrEmpty()) {
                val amount = binding.edtMoney.text.toString().replace("[^\\d]".toRegex(), "").toLong()
                viewModel.getResponsePaymentUrl(amount, "DEPOSIT")

            } else {
                // Hiển thị thông báo lỗi nếu số tiền rỗng
                MotionToast.Companion.createColorToast(
                    this,
                    "Thất Bại",
                    "Vui lòng nhập số tiền hợp lệ",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.inter_bold)
                )
                return@setOnClickListener
            }


        }
    }
}