package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.PricingSupportActivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityPricingSupportBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogAcceptPricingBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogPriceInputBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PricingSupportActivity : BaseActivity() {
    private lateinit var binding: ActivityPricingSupportBinding
    lateinit var postingData: PostingData
    private val viewModel: PricingSupportViewModel by viewModels()
    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPricingSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)


        binding.btnChangePrice.setOnClickListener {
            handleChangePriceSupport()
        }

        binding.btnAcceptPrice.setOnClickListener {
            showAcceptPriceSupport()
        }

        binding.customToolbar5.onStartIconClick = {
            finish()
        }

        bindDataPriceSupport()
        observeData()


    }

    private fun observeData() {
        viewModel.pricingSupportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        "Thay Đổi Mức Giá Thành Công",
                        object : View.OnClickListener {
                            override fun onClick(v: android.view.View?) {
                                val intent = Intent()
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                        }
                    )

                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
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

    private fun bindDataPriceSupport() {
        postingData = getPostingDataFromIntent()
        binding.tvResortName.text = postingData.resortName
        binding.tvRoomName.text = postingData.roomName
        binding.tvCheckInDate.text = postingData.checkInDate
        binding.tvCheckOutDate.text = postingData.checkOutDate

        val rentalPackageEnum = RentalPackageEnum.getPackageByName(postingData.packageSelection)
        when (rentalPackageEnum) {
            RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                binding.etPriceInput.setText(Constant.formatPriceLong(postingData.staffRefinementPrice!!) + "VND")
                binding.btnChangePrice.text = "Thay Đổi Mức Giá"
            }

            RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                binding.etPriceInput.setText(Constant.formatPriceLong(postingData.priceValuation!!) + "VND")
                binding.btnChangePrice.text = "Từ Chối Mức Giá"
            }
        }
    }

    private fun getPostingDataFromIntent(): PostingData {
        // Lấy các giá trị truyền qua Intent
        val rentalPostingId = intent.getIntExtra(Constant.DEFAULT_MY_POSTING_ID, 0)
        val packageSelection = intent.getStringExtra(Constant.DEFAULT_PACKAGE_SELECTION) ?: ""
        val resortName = intent.getStringExtra(Constant.DEFAULT_MY_POSTING_RESORT_NAME) ?: ""
        val roomName = intent.getStringExtra(Constant.DEFAULT_MY_POSTING_ROOM_NAME) ?: ""
        val checkInDate = intent.getStringExtra(Constant.DEFAULT_MY_POSTING_CHECK_IN_DATE) ?: ""
        val checkOutDate = intent.getStringExtra(Constant.DEFAULT_MY_POSTING_CHECK_OUT_DATE) ?: ""
        val nights = intent.getIntExtra(Constant.DEFAULT_MY_POSTING_NIGHT, 0)

        // Kiểm tra và lấy dữ liệu theo Package
        val staffRefinementPrice = intent.getLongExtra(Constant.staffRefinementPrice, 0)
        val priceValuation = intent.getLongExtra(Constant.priceValuation, 0)

        // Trả về đối tượng chứa tất cả dữ liệu
        return PostingData(
            rentalPostingId,
            packageSelection,
            resortName,
            roomName,
            checkInDate,
            checkOutDate,
            staffRefinementPrice,
            priceValuation,
            nights

        )
    }

    private fun handleChangePriceSupport() {
        val rentalPackageEnum = RentalPackageEnum.getPackageByName(postingData.packageSelection)

        when (rentalPackageEnum) {
            RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                showChangePriceDialog()
            }

            RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                // Call To Reject
                if (tokenManager.isLoggedIn()) {
                    viewModel.acceptPricingSupport(
                        tokenManager.getAccessToken().toString(),
                        postingData.rentalPostingId.toInt(),
                        postingData.priceValuation!!.toFloat(),
                        false
                    )
                } else {
                    MotionToast.Companion.createColorToast(
                        this,
                        "Error",
                        "Vui lòng đăng nhập để thực hiện chức năng này",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }


    }

    private fun showChangePriceDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_price_input, null)
        val binding = DialogPriceInputBinding.bind(dialogView)

        binding.etPriceInput.setText(Constant.formatPriceLong(postingData.staffRefinementPrice!!) + " đ")

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setMoneyInputLogic(binding)
        binding.btnAcceptPrice.setOnClickListener {
            val priceString = binding.etPriceInput.text.toString().replace(" đ", "").replace(".", "").replace(",", "")
            if (priceString.isNotEmpty() && binding.moneyContainer.helperText.isNullOrEmpty()) {
                viewModel.acceptPricingSupport(
                    tokenManager.getAccessToken().toString(),
                    postingData.rentalPostingId.toInt(),
                    priceString.toFloat(),
                    null
                )
                dialog.dismiss()
            } else {
                MotionToast.createColorToast(
                    this,
                    "Error",
                    "Vui lòng nhập giá",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }
        dialog.show()
    }

    private fun setMoneyInputLogic(binding: DialogPriceInputBinding) {
        binding.etPriceInput.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                binding.etPriceInput.removeTextChangedListener(this)

                val input = editable.toString()
                    .replace("[^\\d]".toRegex(), "") // Loại bỏ các ký tự không phải số

                if (input.isNotEmpty()) {
                    // Loại bỏ số 0 đầu tiên nếu có
                    var cleanedInput = input
                    if (cleanedInput.startsWith("0")) {
                        cleanedInput = cleanedInput.substring(1)
                    }

                    // Kiểm tra số tiền tối thiểu 10.000
                    val numericValue = cleanedInput.toLongOrNull() ?: 0
                    if (numericValue < 10000) {
                        // Hiển thị helper text nếu số tiền nhỏ hơn 10.000
                        binding.moneyContainer.helperText = "Số tiền tối thiểu là 10.000"
                    } else {
                        // Ẩn helper text khi số tiền đạt yêu cầu
                        binding.moneyContainer.helperText = null
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " đ"
                    current = formatted
                    binding.etPriceInput.setText(formatted)
                    binding.etPriceInput.setSelection(formatted.length - 2)
                }

                binding.etPriceInput.addTextChangedListener(this)
            }

            // Hàm format để chèn dấu chấm vào các số (ví dụ: 100000 -> 100.000)
            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }
        })

    }

    private fun showAcceptPriceSupport() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_accept_pricing, null)
        val bindingDialog = DialogAcceptPricingBinding.bind(dialogView)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Set dialog window background to transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val rentalPackageEnum = RentalPackageEnum.getPackageByName(postingData.packageSelection)


        bindingDialog.apply {
            tvResortNameDtb.text = postingData.resortName + ", " + postingData.roomName
            tvCheckInDate.text = postingData.checkInDate
            tvCheckOutDate.text = postingData.checkOutDate
            tvNumberNight.text = postingData.nights.toString()

            if (rentalPackageEnum == RentalPackageEnum.PREMIUM_SERVICE.packageModel) {
                tvRoomPricePerNight.text =
                    Constant.formatPriceLong(postingData.staffRefinementPrice!!) + "VND"
                tvEstimatedTotalPrice.text =
                    Constant.formatPriceLong(postingData.staffRefinementPrice!! * postingData.nights) + " VND"
            } else {
                tvRoomPricePerNight.text = Constant.formatPriceLong(postingData.priceValuation!!) + "VND"
                tvEstimatedTotalPrice.text =
                    Constant.formatPriceLong(postingData.priceValuation!! * postingData.nights) + " VND"
            }
        }

        bindingDialog.btnAcceptPrice.setOnClickListener {
            if (tokenManager.isLoggedIn()) {
                when (rentalPackageEnum) {
                    RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                        viewModel.acceptPricingSupport(
                            tokenManager.getAccessToken().toString(),
                            postingData.rentalPostingId.toInt(),
                            postingData.staffRefinementPrice!!.toFloat(),
                            null
                        )
                    }

                    RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                        viewModel.acceptPricingSupport(
                            tokenManager.getAccessToken().toString(),
                            postingData.rentalPostingId.toInt(),
                            postingData.priceValuation!!.toFloat(),
                            true
                        )
                    }
                }
                dialog.dismiss()
            } else {
                MotionToast.Companion.createColorToast(
                    this,
                    "Error",
                    "Vui lòng đăng nhập để thực hiện chức năng này",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }
        dialog.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // Data class để lưu trữ dữ liệu
    data class PostingData(
        val rentalPostingId: Int,
        val packageSelection: String,
        val resortName: String,
        val roomName: String,
        val checkInDate: String,
        val checkOutDate: String,
        val staffRefinementPrice: Long?,
        val priceValuation: Long?,
        val nights: Int
    )
}