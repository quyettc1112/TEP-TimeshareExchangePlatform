package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestDetailActivity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.VNPayActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity.ExchangeRequestOnPostActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangeOption
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyExchangeRequestStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentType
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyExchangeRequestDetailBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogExchangePriceValuationBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogPaymentExchangePriceValuationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MyExchangeRequestDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityMyExchangeRequestDetailBinding
    private lateinit var imagePostingAdapter: ImagePostingAdapter
    private val viewModel: MyExchangeRequestDetailViewModel by viewModels()
    private lateinit var paymentResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var tokenManager: TokenManager
    private var selectedExchangeOption: ExchangeOption? = null
    private var selectedCard: MaterialCardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyExchangeRequestDetailBinding.inflate(layoutInflater)
        // Sử dụng ViewBinding để inflate layout

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getIntentValue()
        initAdapter()
        observeMyExchangeRequestDetail()

        binding.customToolbar5.onStartIconClick = {
            finish()
        }
        evenClickApproveExchangeRequest()
        eventClickRejectExchangeRequest()
        eventClickPriceValuation()
        eventClickToolbar()
        eventClickReload()
        eventClickPayment()

        initActivityResultLauncher()


    }

    private fun getIntentValue() {
        val intent = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
        tokenManager = TokenManager(this)
        if (tokenManager.isLoggedIn() && tokenManager.getAccessToken() != null) {
            Log.d("MyExchangeRequestDasadsetail", intent.toString())
            viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
        } else {
            showWarningToast("Bạn chưa đăng nhập", "Vui lòng đăng nhập để xem thông tin")
        }
    }

    private fun observeMyExchangeRequestDetail() {
        // Get Detail
        viewModel.myExchangeRequestDetail.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                    Log.d("MyExchangeRequestDetail", it.data.ownerId.toString())
                    Log.d("MyExchangeRequestDetail", tokenManager.getProfileInfo()?.id.toString())
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi lấy thông tin yêu cầu trao đổi")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Approve Exchange Request
        viewModel.approveExchangeRequest.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        "Duyệt yêu cầu trao đổi thành công",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val intent = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
                                viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
                            }

                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi duyệt yêu cầu trao đổi")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Reject Exchange Request
        viewModel.rejectExchangeRequest.observe(this) { data ->
            when (data.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        "Từ chối yêu cầu trao đổi thành công",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val intent = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
                                viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
                            }

                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi từ chối yêu cầu trao đổi")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Price Valuation
        viewModel.exchangePriceValuation.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        "Đề xuất giá chênh lệch thành công",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val intent = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
                                viewModel.getCustomerExchangeDetail(tokenManager.getAccessToken().toString(), intent)
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi đề xuất giá chênh lệch")
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Payment Exchange Request Wallet
        viewModel.paymentExchangeRequest.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this,
                        getString(R.string.msg_payment_successful),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                setResult(RESULT_OK)
                                finish()
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast("Thất Bại", "Lỗi khi thanh toán")
                    Log.d("PaymentExchangeRequest", it.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        viewModel.responseVNPAYUrl.observe(this) {
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
                    showErrorToast("Thao tác thất bại", "Thanh Toán Không Thành Công")
                }
            }
        }

    }

    private fun bindData(myExchangeRequestDetail: MyExchangeRequestDetailResponse) {

        // Unit Type
        bindDataUnitType(myExchangeRequestDetail)

        // Resort Info
        binding.apply {
            tvResortName.text =
                myExchangeRequestDetail.exchangePosting.roomInfoResortResortName
            tvRoomCode.text = myExchangeRequestDetail.roomInfo.location.displayName
            Glide.with(this@MyExchangeRequestDetailActivity)
                .load(myExchangeRequestDetail.roomInfo.unitType.photos)
                .into(imImageTimeshare)
            tvNights.text = myExchangeRequestDetail.exchangePosting.nights.toString() + " đêm"
            if (myExchangeRequestDetail.exchangePosting.isVerify) {
                llVerify.visibility = View.VISIBLE
            } else {
                llVerify.visibility = View.GONE
            }
        }

        // Exchanger
        binding.apply {
            tvOwnerFullName.text = myExchangeRequestDetail.ownerFullName
            Glide.with(this@MyExchangeRequestDetailActivity)
                .load(myExchangeRequestDetail.ownerAvatar)
                .placeholder(R.drawable.ic_image_tmp_holder)
                .error(R.drawable.ic_image_tmp_holder)
                .into(ivOwnerAvatar)
        }

        // Check in Date, Check out Date
        binding.apply {
            tvCheckInDate.text = Constant.formatDateByLocale(
                myExchangeRequestDetail.startDate,
                this@MyExchangeRequestDetailActivity
            )
            tvCheckOutDate.text =
                Constant.formatDateByLocale(
                    myExchangeRequestDetail.endDate,
                    this@MyExchangeRequestDetailActivity
                )
        }

        // Price Valuation
        binding.apply {
            if (myExchangeRequestDetail.priceValuation != null) {
                etPriceInput.setText(Constant.formatPriceLongAbs(myExchangeRequestDetail.priceValuation))
            } else {
                etPriceInput.setText("Không Có Đề Xuất Giá Chênh Lệch")
            }
        }

        // Note
        binding.apply {
            if (myExchangeRequestDetail.note != null) {
                etNote.setText(myExchangeRequestDetail.note.toString())
            } else {
                etNote.setText("Người Gửi Không Để Lại Lời Nhắn")
            }
        }

        // Status
        bindDataStatus(myExchangeRequestDetail)

        // Price Valuation
        bindDataPriceValuation(myExchangeRequestDetail)

        // Check Owner
        val customerProfile = tokenManager.getProfileInfo()
        val status = MyExchangeRequestStatus.fromApiStatus(myExchangeRequestDetail.status)
       /* // Owner Side
        if (customerProfile?.id != myExchangeRequestDetail.ownerId && status == MyExchangeRequestStatus.PENDING_OWNER) {
            binding.llRequestAction.visibility = View.VISIBLE
        } else {
            binding.llRequestAction.visibility = View.GONE
        }
        if(customerProfile?.id != myExchangeRequestDetail.ownerId && status == MyExchangeRequestStatus.PENDING_OWNER_PAYMENT){
            binding.llPaymentMethod.visibility = View.VISIBLE
        } else {
            binding.llPaymentMethod.visibility = View.GONE
        }

        // Exchanger Side
        if (customerProfile?.id == myExchangeRequestDetail.ownerId && status == MyExchangeRequestStatus.PENDING_RENTER_PRICING) {
            binding.llRequestAction.visibility = View.VISIBLE
        } else {
            binding.llRequestAction.visibility = View.GONE
        }
        if (customerProfile?.id == myExchangeRequestDetail.ownerId && status == MyExchangeRequestStatus.PENDING_RENTER_PAYMENT) {
            binding.llPaymentMethod.visibility = View.VISIBLE
        } else {
            binding.llPaymentMethod.visibility = View.GONE
        }
*/

        val isOwner = customerProfile?.id == myExchangeRequestDetail.ownerId

        binding.llRequestAction.visibility = when {
            !isOwner && status == MyExchangeRequestStatus.PENDING_OWNER -> View.VISIBLE
            isOwner && status == MyExchangeRequestStatus.PENDING_RENTER_PRICING -> View.VISIBLE
            !isOwner && status == MyExchangeRequestStatus.PENDING_OWNER_PAYMENT -> View.VISIBLE
            isOwner && status == MyExchangeRequestStatus.PENDING_RENTER_PAYMENT -> View.VISIBLE
            else -> View.GONE
        }

        binding.llPaymentMethod.visibility = when {
            !isOwner && status == MyExchangeRequestStatus.PENDING_OWNER_PAYMENT -> View.VISIBLE
            isOwner && status == MyExchangeRequestStatus.PENDING_RENTER_PAYMENT -> View.VISIBLE
            else -> View.GONE
        }

        checkPricingAble(myExchangeRequestDetail)
    }

    private fun bindDataStatus(item: MyExchangeRequestDetailResponse) {
        // Show Status
        when (MyExchangeRequestStatus.fromApiStatus(item.status)) {
            MyExchangeRequestStatus.PENDING_OWNER -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.blue_full
                )

            }

            MyExchangeRequestStatus.PENDING_APPROVAL -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.PENDING_RENTER_PRICING -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.PENDING_RENTER_PAYMENT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.PENDING_OWNER_PAYMENT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
            }

            MyExchangeRequestStatus.COMPLETED -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.green_verify
                )
            }

            MyExchangeRequestStatus.REJECT_APPROVAL -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            MyExchangeRequestStatus.RENTER_REJECT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            MyExchangeRequestStatus.OWNER_REJECT -> {
                applyStatusStyle(
                    this,
                    R.color.white,
                    R.color.status_rejected_text
                )
            }

            else -> {
                // Default or unknown status case
                applyStatusStyle(
                    this,
                    R.color.status_unknown_bg,
                    R.color.status_unknown_text
                )
            }
        }

        binding.tvStatus.text =
            MyExchangeRequestStatus.fromApiStatus(item.status)?.getDescription(this)
                ?: ""
    }

    private fun bindDataUnitType(data: MyExchangeRequestDetailResponse) {
        // Set Unit Type Of Posting
        binding.includeUnitType.apply {
            btnViewDetail.visibility = View.GONE
            tvRoomCode.text = data.roomInfo.roomInfoCode
            tvRoomType.text = data.roomInfo.unitType.title
            llRoomName.visibility = View.GONE
            llUnityTypeBaseAmeniites.visibility = View.GONE
            root.visibility = View.VISIBLE

        }
    }

    private fun bindDataPriceValuation(data: MyExchangeRequestDetailResponse) {
        if (data.priceValuation == null) {
            return
        }
        if (data.priceValuation == 0L) {
            binding.tvPriceLabel.text = "Không có đề xuất giá chênh lệch"
            binding.tvNoPaymentNeededDescription.text =
                "Cả hai bên không cần thanh toán thêm khoản tiền nào để thực hiện trao đổi."
        }

        if (data.priceValuation < 0L) {
            binding.tvPriceLabel.text = "Chủ sở hửu sẽ bù tiền cho người gửi yêu cầu trao đổi"
            binding.tvNoPaymentNeededDescription.text =
                "Chủ sở hữu đề xuất bạn thanh toán số bù để hoàn tất trao đổi."
        }

        if (data.priceValuation > 0L) {
            binding.tvPriceLabel.text = "Người gửi yêu cầu trao đổi sẽ bù tiền cho chủ sở hữu"
            binding.tvNoPaymentNeededDescription.text =
                "Người gửi yêu cầu trao đổi sẽ thanh toán số tiền bù để hoàn tất trao đổi."
        }

    }

    private fun checkPricingAble(data: MyExchangeRequestDetailResponse) {
        val statusEP = data?.exchangePosting!!.status
        val statusRE = data?.status
        // Trường hợp bài đăng dđã được chấp nhân. Không thể thực hiện chấp nhận/ từ chối hay thay đổi mức giá
        if (statusEP == MyPostingStatus.ACCEPTED.apiStatus && statusRE != MyExchangeRequestStatus.PENDING_APPROVAL.apiStatus) {
            binding.llRequestAction.visibility = View.GONE
            showInfoDialog(
                this,
                "Bạn đang có yêu cầu đã được chấp nhận, không thể thực hiện Chấp Nhận/ Từ chối hay thay đổi mức giá",
                object : View.OnClickListener {
                    override fun onClick(v: View?) {

                    }
                }
            )
        }

        if (statusEP == MyPostingStatus.COMPLETED.apiStatus){
            binding.llRequestAction.visibility = View.GONE
        }



    }

    private fun evenClickApproveExchangeRequest() {
        binding.btnApproval.setOnClickListener {
            callApproveExchangeRequest()
            /*// Check Owner
            val customerProfile = tokenManager.getProfileInfo()
            val response = viewModel.myExchangeRequestDetail.value?.data
            val status = MyExchangeRequestStatus.fromApiStatus(response?.status ?: "")
            // Owner
            if (customerProfile?.id != response?.ownerId && status == MyExchangeRequestStatus.PENDING_OWNER) {
                callApproveExchangeRequest()
            }

            if (customerProfile?.id == response?.ownerId && status == MyExchangeRequestStatus.PENDING_RENTER_PRICING) {
                val priceValuation = response?.priceValuation ?: 0L
                when {
                    priceValuation == 0L || priceValuation < 0L -> {
                        showConfirmDialog(
                            title = "Chấp Nhận",
                            message = "Bạn có chắc chắn muốn chấp nhận yêu cầu trao đổi này không?",
                            positiveButtonTitle = "Chấp Nhận",
                            negativeButtonTitle = "Hủy",
                            textButton = "",
                            callback = object : ConfirmDialog.ConfirmCallback {
                                override fun negativeAction() {}
                                override fun positiveAction() {
                                    callApproveExchangeRequest()
                                }
                            },
                        )
                    }
                    priceValuation > 0L -> {
                        Toast.makeText(this, "Call Payment", Toast.LENGTH_SHORT).show()
                        showPaymentDialog()
                    }
                }
            }*/


        }
    }

    private fun eventClickRejectExchangeRequest() {
        binding.btnReject.setOnClickListener {
            showConfirmDialog(
                title = "Từ Chối",
                message = "Bạn có chắc chắn muốn từ chối yêu cầu trao đổi này không?",
                positiveButtonTitle = "Từ Chối",
                negativeButtonTitle = "Hủy",
                "",
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {}
                    override fun positiveAction() {
                        callRejectExchangeRequest()
                    }
                },
            )
        }
    }

    private fun eventClickPriceValuation() {
        binding.btnPriceValuation.setOnClickListener {
            showExchangeDialog()
        }
    }

    private fun eventClickToolbar() {
        binding.customToolbar5.onStartIconClick = {
            setResult(RESULT_OK)
            onBackPressed()
        }

    }

    private fun eventClickReload() {
        binding.customToolbar5.onEndIconClick = {
            getIntentValue()
        }
    }

    private fun eventClickPayment() {
        binding.btnPayment.setOnClickListener {
            showPaymentDialog()
        }
    }

    private fun callApproveExchangeRequest() {
        val requestId = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
        viewModel.approveExchangeRequest(tokenManager.getAccessToken().toString(), requestId)
    }

    private fun callRejectExchangeRequest() {
        val requestId = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
        viewModel.rejectExchangeRequest(tokenManager.getAccessToken().toString(), requestId)
    }

    private fun callExchangePriceValuation(requestId: Int, priceValuation: Long, note: String) {
        viewModel.exchangePriceValuation(
            tokenManager.getAccessToken().toString(),
            requestId,
            priceValuation,
            note
        )
    }

    private fun callPaymentExchangeRequestWallet() {
        showConfirmDialog(
            title = "Xác Nhận Thanh Toán",
            message = "Bạn có chắc chắn muốn thanh toán bằng ví Unwind",
            positiveButtonTitle = "Thanh Toán",
            negativeButtonTitle = "Hủy",
            textButton = "",
            callback = object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {}
                override fun positiveAction() {
                    val requestId = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
                    viewModel.paymentExchangeRequest(
                        tokenManager.getAccessToken().toString(),
                        requestId,
                    )
                }
            },
        )
    }

    private fun callPaymentExchangeRequestVNPAY(priceValuation: Long) {
        viewModel.getResponsePaymentUrl(
            priceValuation,
            "Payment Exchangev Request"
        )
    }

    private fun showExchangeDialog() {
        // Sử dụng View Binding
        val binding_dialog = DialogExchangePriceValuationBinding.inflate(layoutInflater)

        // Tạo AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(binding_dialog.root)
            .create()

        // Lắng nghe sự thay đổi trong RadioGroup
        binding_dialog.radioGroupExchangeOptions.setOnCheckedChangeListener { _, checkedId ->
            selectedExchangeOption = when (checkedId) {
                binding_dialog.radioPayDifferenceToOwner.id -> ExchangeOption.PAY_DIFFERENCE_TO_OWNER
                binding_dialog.radioOwnerPaysDifference.id -> ExchangeOption.OWNER_PAYS_DIFFERENCE
                binding_dialog.radioNoPaymentNeeded.id -> ExchangeOption.NO_PAYMENT_NEEDED
                else -> null
            }
            selectedExchangeOption?.let {
                when (it) {
                    ExchangeOption.PAY_DIFFERENCE_TO_OWNER -> {
                        binding_dialog.llPriceInput.visibility = View.VISIBLE
                    }

                    ExchangeOption.OWNER_PAYS_DIFFERENCE -> {
                        binding_dialog.llPriceInput.visibility = View.VISIBLE
                    }

                    ExchangeOption.NO_PAYMENT_NEEDED -> {
                        viewModel.updatePrice(0)
                        binding_dialog.llPriceInput.visibility = View.GONE
                    }
                }
                binding_dialog.btnSave.visibility = View.VISIBLE

            }
        }
        binding_dialog.etRoomPrice.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable?) {


                // Loại bỏ TextWatcher tạm thời để tránh loop
                binding_dialog.etRoomPrice.removeTextChangedListener(this)

                val input =
                    s.toString().replace("[^\\d]".toRegex(), "") // Loại bỏ các ký tự không phải số

                if (input.isNotEmpty()) {
                    // Kiểm tra và loại bỏ số 0 đầu tiên nếu có
                    var cleanedInput = input
                    if (cleanedInput.startsWith("0")) {
                        cleanedInput = cleanedInput.substring(1) // Loại bỏ số 0 đầu tiên
                    }
                    val numericValue = input.toLongOrNull() ?: 0
                    when {
                        numericValue < 10000 -> {
                            // Hiển thị helper text nếu số tiền nhỏ hơn 100.000
                            binding_dialog.tilRoomPrice.helperText =
                                "Số tiền tối thiểu là 10.000"
                        }

                        numericValue > 100_000_000 -> {
                            // Hiển thị helper text nếu số tiền lớn hơn 100 Triệu
                            binding_dialog.tilRoomPrice.helperText =
                                "Số tiền tối đa cho 1 đêm là 100 triệu"
                        }

                        else -> {
                            // Ẩn helper text khi số tiền đạt yêu cầu
                            binding_dialog.tilRoomPrice.helperText = null
                        }
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " đ"
                    current = formatted
                    binding_dialog.etRoomPrice.setText(formatted)
                    binding_dialog.etRoomPrice.setSelection(formatted.length - 2) // Đặt con trỏ vào vị trí trước "đ"
                    val amount = binding_dialog.etRoomPrice.text.toString()
                        .replace("[^\\d]".toRegex(), "").toLongOrNull()
                    if (amount != null) {
                        if (amount > 0) {
                            viewModel.updatePrice(amount)
                        }
                    }

                } else {
                    binding_dialog.etTotalPrice.setText(null)
                }

                // Thêm lại TextWatcher sau khi cập nhật văn bản
                binding_dialog.etRoomPrice.addTextChangedListener(this)
            }


            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding_dialog.btnSave.setOnClickListener {
            var inputPrice: Long = 0
            when (selectedExchangeOption) {
                ExchangeOption.NO_PAYMENT_NEEDED -> {
                    viewModel.updatePrice(0)
                    inputPrice = viewModel.price.value!!
                }

                ExchangeOption.OWNER_PAYS_DIFFERENCE -> {
                    inputPrice = -viewModel.price.value!!
                }

                ExchangeOption.PAY_DIFFERENCE_TO_OWNER -> {
                    inputPrice = viewModel.price.value!!
                }

                else -> {
                    showErrorToast("Lỗi", "Vui lòng chọn phương thức trao đổi")
                    return@setOnClickListener
                }
            }

            if (selectedExchangeOption != ExchangeOption.NO_PAYMENT_NEEDED && inputPrice == 0L) {
                showWarningToast("Lỗi", "Vui lòng nhập giá trị hợp lệ")
                return@setOnClickListener
            }

            if (selectedExchangeOption != ExchangeOption.NO_PAYMENT_NEEDED && abs(inputPrice) < 10000) {
                showWarningToast("Lỗi", "Số tiền tối thiểu là 10.000")
                return@setOnClickListener
            }

            if (selectedExchangeOption != ExchangeOption.NO_PAYMENT_NEEDED && abs(inputPrice) > 100_000_000) {
                showWarningToast("Lỗi", "Số tiền tối đa là 100 triệu")
                return@setOnClickListener
            }

            val requestId = intent.getIntExtra(Constant.DEFAULT_MY_EXCHANGE_REQUEST_ID, 0)
            val note =
                selectedExchangeOption!!.description + " :" + binding_dialog.etNote.text.toString()
            val priceValuation = inputPrice
            Log.d("PriceValuatisasdon", priceValuation.toString() + " " + requestId + " " + note)
            callExchangePriceValuation(requestId, priceValuation, note)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showPaymentDialog() {
        var binding_dialog = DialogPaymentExchangePriceValuationBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(binding_dialog.root)

        // Bind data
        val customerProfile = tokenManager.getProfileInfo()
        if (customerProfile != null) {
            if(customerProfile.walletAvailableMoney < abs(viewModel.price.value!!)){
                binding_dialog.cardUnwind.visibility = View.GONE
            } else {
                binding_dialog.cardUnwind.visibility = View.VISIBLE
                binding_dialog.tvWalletBalance.text = "${Constant.formatPriceLong(customerProfile.walletAvailableMoney)} VNĐ"
            }
        }

        binding_dialog.cardUnwind.setOnClickListener {
            selectedCard = binding_dialog.cardUnwind
            viewModel.selectPaymentMethod(PaymentMethod.UNWIND)
            callPaymentExchangeRequestWallet()
        }
        binding_dialog.cardVnpay.setOnClickListener {
            selectedCard = binding_dialog.cardVnpay
            viewModel.selectPaymentMethod(PaymentMethod.VNPAY)
            val priceValuation = viewModel.myExchangeRequestDetail.value?.data?.priceValuation ?: 0
            callPaymentExchangeRequestVNPAY(abs(priceValuation))
        }

        // Hiển thị BottomSheetDialog
        bottomSheetDialog.show()
    }

    private fun initAdapter() {
        imagePostingAdapter = ImagePostingAdapter()
    }

    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            llStatusContainer.visibility = View.VISIBLE
            llStatusContainer.backgroundTintList =
                ResourcesCompat.getColorStateList(context.resources, backgroundColorRes, null)
            tvStatus.setTextColor(context.getColor(textColorRes))
            cardStatus.setStrokeColor(context.getColor(textColorRes))
        }
    }

    private fun intentToVNPAYActivity(url: String) {
        val requestId  = viewModel.myExchangeRequestDetail.value?.data?.id
        val intent = Intent(this, VNPayActivity::class.java)
        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.GENERAL_ID_PAYMENT, requestId)
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.PAYMENT_EXCHANGE_REQUEST)
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
        setResult(RESULT_OK)
        finish()
    }

}