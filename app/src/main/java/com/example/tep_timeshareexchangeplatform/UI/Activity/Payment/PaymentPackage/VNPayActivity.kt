package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.VNPayResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.ViewModel.VNPayViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingList.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.VnpResponseCode
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityVnpayBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class VNPayActivity : BaseActivity() {
    private lateinit var binding: ActivityVnpayBinding
    private val viewModel: VNPayViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVnpayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeData()
        tokenManager = TokenManager(this)
        webViewLoadSetup()
    }

    private fun observeData() {
        // Observe Create Transaction of Purchase Package Member Ship by VN Pay
        viewModel.memberShipResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this@VNPayActivity,
                        "Payment Success",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                // Intent to FInsish Activity
                                val intent = intent
                                setResult(RESULT_OK, intent)
                                // Intent to Billing Activity
                                val intentToBilling =
                                    Intent(this@VNPayActivity, PaymentResultActivity::class.java)
                                intentToBilling.putExtra(Constant.PAYMENT_SUCCESS_PACKAGE, it.data)
                                startActivity(intentToBilling)

                                // Finish Activity
                                finish()
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }
        }

        // Observe Create Transaction of Deposit Wallet by VN Pay
        viewModel.depositByVNPAYResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this@VNPayActivity,
                        "Payment Success",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                // Intent to FInsish Activity
                                val intent = intent
                                setResult(RESULT_OK, intent)
                                val intentToBilling =
                                    Intent(this@VNPayActivity, PaymentResultActivity::class.java)
                                intentToBilling.putExtra(Constant.PAYMENT_SUCCESS_VNPAY, it.data)
                                startActivity(intentToBilling)
                                Log.d("WalletDepositResponseData", it.data.toString())
                                // Finish Activity
                                finish()
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }
        }

        // Create Transaction of Purchase Rental Posting by VN Pay
        viewModel.createRentalPostingTransaction.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    // Create Rental Posting
                    val postingTimeshareDTO =
                        intent.getParcelableExtra<PostingTimeshareDTO>(Constant.POSTING_TIMESHARE_DTO)
                    if (postingTimeshareDTO != null) {
                        viewModel.createRentalPosting(
                            tokenManager.getAccessToken().toString(),
                            postingTimeshareDTO
                        )
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }
        }

        // Create Transaction of Purchase Exchange Posting by VN Pay
        viewModel.createExchangePostingTransaction.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    // Create Rental Posting
                    val postingTimeshareDTO =
                        intent.getParcelableExtra<ExchangeTimeshareDTO>(Constant.POSTING_TIMESHARE_DTO)
                    if (postingTimeshareDTO != null) {
                        viewModel.createExchangePosting(
                            tokenManager.getAccessToken().toString(),
                            postingTimeshareDTO
                        )
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString() + "Create Transaction",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }

        }

        // Create Rental Posting
        viewModel.postingTimeshareResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this@VNPayActivity,
                        "Payment Success",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                // Intent to FInsish Activity
                                val intent = intent
                                setResult(RESULT_OK, intent)
                                // Intent to Billing Activity
                                val intentToBilling =
                                    Intent(this@VNPayActivity, MyPostingActivity::class.java)
                                startActivity(intentToBilling)

                                // Finish Activity
                                finish()
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }
        }

        // Create Exchange Posting
        viewModel.exchangePostingResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessDialog(
                        this@VNPayActivity,
                        "Payment Success",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                // Intent to FInsish Activity
                                val intent = intent
                                setResult(RESULT_OK, intent)
                                // Intent to Billing Activity
                                val intentToBilling =
                                    Intent(this@VNPayActivity, MyExchangePostingActivity::class.java)
                                startActivity(intentToBilling)

                                // Finish Activity
                                finish()
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }
        }

        // Observe Booking Rental
        viewModel.bookingResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    val intent = intent
                    setResult(RESULT_OK, intent)
                    // Finish Activity
                    finish()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailedDialog(
                        this@VNPayActivity,
                        it.message.toString(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                finish()
                            }
                        })
                }
            }


        }

    }

    private fun checkPaymentType(
        paymentType: PaymentType,
        walletTransactionId: String,
        packageId: Int
    ) {
        when (paymentType) {
            PaymentType.BOOKING_RENTAL_PAYMENT -> {
                callAPIBookingRentalTransaction(
                    walletTransactionId,
                    packageId
                )
            }

            PaymentType.DEPOSIT_WALLET -> {
                callAPIDepositWalletTransaction(walletTransactionId)
            }

            PaymentType.PURCHASE_PACKAGE_MEMBER -> {
                callAPIExtendMembershipTransaction(
                    walletTransactionId,
                    packageId
                )
            }

            PaymentType.PURCHASE_PACKAGE_RENTAL_POSTING -> {
                callAPICreateRentalPostingTransaction(walletTransactionId, packageId)
            }

            PaymentType.PURCHASE_PACKAGE_EXCHANGE_POSTING -> {
                callAPICreateExchangePostingTransaction(walletTransactionId, packageId)
            }
        }
    }

    private fun webViewLoadSetup() {
        val urlIntent = intent.getStringExtra(Constant.PAYMENT_URL)
        val packageId = intent.getIntExtra(Constant.GENERAL_ID_PAYMENT, 0)

        // Set WebViewClient to prevent opening external browser
        binding.webView.webViewClient = WebViewClient()
        // Enable JavaScript if needed (optional)
        binding.webView.settings.javaScriptEnabled = true
        // Load the URL in the WebView
        binding.webView.settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        binding.webView.loadUrl(urlIntent.toString())
        showLoadingWaiting(true)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (!urlIntent!!.contains("https://sandbox.vnpayment.vn/paymentv2")) {
                    view.loadUrl("about:blank")
                    finish()
                    return true
                } else {
                    hideLoadingWaiting()
                    val url = request.url.toString()
                    view.loadUrl(url)
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // Kiểm tra URL để xử lý kết quả thanh toán nếu cần
                processPaymentInfo(url, view, packageId)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                var request = request
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Permissions-Policy"] = "geolocation=(self), microphone=()"
                    headers["Content-Security-Policy"] =
                        "default-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:"

                    val finalRequest = request
                    request = object : WebResourceRequest {
                        override fun getUrl(): Uri {
                            return finalRequest.url
                        }

                        override fun isForMainFrame(): Boolean {
                            return finalRequest.isForMainFrame
                        }

                        override fun isRedirect(): Boolean {
                            return finalRequest.isRedirect
                        }

                        override fun hasGesture(): Boolean {
                            return finalRequest.hasGesture()
                        }

                        override fun getMethod(): String {
                            return finalRequest.method
                        }

                        override fun getRequestHeaders(): Map<String, String> {
                            return headers
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }
        }

    }

    private fun processPaymentInfo(url: String, view: WebView, packageId: Int) {
        if (url.contains("https://fams-management.tech/api/payment/payment-infor")) {
            // Sử dụng evaluateJavascript để lấy nội dung JSON từ trang
            showLoadingWaiting(true)
            view.loadUrl("about:blank")
            view.evaluateJavascript(
                "(function() { return document.body.innerText; })();"
            ) { jsonResult ->
                // Xử lý chuỗi JSON trả về từ trang
                if (jsonResult != null && jsonResult.contains("responseCode")) {
                    try {
                        val gson = Gson()
                        val cleanedJson =
                            jsonResult.replace("\\\"", "\"").replace("\"{", "{")
                                .replace("}\"", "}")
                        val vnPayResponse =
                            gson.fromJson(cleanedJson, VNPayResponse::class.java)
                        // Get Response Code
                        val responseCodeEnum: VnpResponseCode =
                            VnpResponseCode.fromCode(vnPayResponse.responseCode)!!

                        // Check Success or Failed
                        if (responseCodeEnum!!.equals(VnpResponseCode.SUCCESS)) {
                            hideLoadingWaiting()
                            // Call API to extend membership
                            val paymentType =
                                intent.getSerializableExtra(Constant.PAYMENT_METHOD_TYPE) as PaymentType
                            checkPaymentType(
                                paymentType,
                                vnPayResponse.walletTransactionId,
                                packageId
                            )

                        } else {
                            hideLoadingWaiting()
                            showFailed(responseCodeEnum)
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun showFailed(responseCodeEnum: VnpResponseCode) {
        MotionToast.Companion.createToast(
            this@VNPayActivity,
            "PaymentResult ${responseCodeEnum.code}",
            responseCodeEnum.getString(this@VNPayActivity),
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_TOP,
            MotionToast.LONG_DURATION,
            null
        )
        showFailedDialog(
            this@VNPayActivity,
            responseCodeEnum.getString(this@VNPayActivity),
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    finish()
                }
            })
    }

    private fun callAPIBookingRentalTransaction(uuid: String, postingId: Int) {
        val token = TokenManager(this).getAccessToken().toString()
        viewModel.bookingRentalTransaction(token, uuid, postingId)
    }

    private fun callAPIExtendMembershipTransaction(uuid: String, membershipId: Int) {
        val token = TokenManager(this).getAccessToken().toString()
        viewModel.extendMembership(token, uuid, membershipId)
    }

    private fun callAPIDepositWalletTransaction(uuid: String) {
        val token = TokenManager(this).getAccessToken().toString()
        viewModel.depositMoney(token, uuid)
    }

    private fun callAPICreateRentalPostingTransaction(uuid: String, packageId: Int) {
        val token = TokenManager(this).getAccessToken().toString()
        viewModel.createRentalPostingTransaction(token, uuid, packageId)
    }

    private fun callAPICreateExchangePostingTransaction(uuid: String, packageId: Int) {
        val token = TokenManager(this).getAccessToken().toString()
        viewModel.createExchangePostingTransaction(token, uuid, packageId)
    }

}