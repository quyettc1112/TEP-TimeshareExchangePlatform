package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.PaymentScreen

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.databinding.ActivityVnpayBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class VNPayActivity : BaseActivity() {
    private lateinit var binding: ActivityVnpayBinding

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

        getIntentData()
    }

    private fun getIntentData() {
        val urlIntent = intent.getStringExtra(Constant.PAYMENT_URL)

        // Set WebViewClient to prevent opening external browser
        binding.webView.webViewClient = WebViewClient()

        // Enable JavaScript if needed (optional)
        binding.webView.settings.javaScriptEnabled = true

        // Load the URL in the WebView

        binding.webView.settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)


        binding.webView.loadUrl(urlIntent.toString())


        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (!urlIntent!!.contains("https://sandbox.vnpayment.vn/paymentv2")) {
                    view.loadUrl("about:blank")
                    return true
                } else {
                    val url = request.url.toString()
                    view.loadUrl(url);
                    MotionToast.Companion.createToast(
                        this@VNPayActivity,
                        "shouldOverrideUrlLoading",
                        "Đang chuyển hướng đến trang thanh toán...",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )

                    return super.shouldOverrideUrlLoading(view, url)
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                MotionToast.Companion.createToast(
                    this@VNPayActivity,
                    "onPageFinished",
                    "Đang chuyển hướng đến trang thanh toán...",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )

                // Kiểm tra URL để xử lý kết quả thanh toán nếu cần
                if (url.contains("https://fams-management.tech/api/payment/payment-infor")) {
                    // Sử dụng evaluateJavascript để lấy nội dung JSON từ trang
                    view.evaluateJavascript(
                        "(function() { return document.body.innerText; })();"
                    ) { jsonResult ->
                        // Xử lý chuỗi JSON trả về từ trang
                        if (jsonResult != null && jsonResult.contains("responseCode")) {
                            try {
                                // Loại bỏ dấu ngoặc kép dư thừa (do evaluateJavascript trả về chuỗi có dấu "")
                                val json = jsonResult.replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}")

                                // Parse JSON
                                val jsonObject = JSONObject(json)
                                val amount = jsonObject.getString("amount")
                                val responseCode = jsonObject.getString("responseCode")
                                val transactionTime = jsonObject.getString("transactionTime")
                                val orderDetail = jsonObject.getString("orderDetail")

                                // In ra thông tin hoặc xử lý logic tùy ý
                                Log.d("PaymentResult", "Amount: $amount, ResponseCode: $responseCode, TransactionTime: $transactionTime, OrderDetail: $orderDetail")

                                // Bạn có thể cập nhật UI, lưu thông tin hoặc thực hiện các xử lý khác ở đây
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
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
}