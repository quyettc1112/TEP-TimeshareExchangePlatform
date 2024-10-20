package com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.PaymentScreen

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Payment.VNPayResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.VnpResponseCode
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.databinding.ActivityVnpayBinding
import com.google.gson.Gson
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

        webViewLoadSetup()
    }

    private fun webViewLoadSetup() {
        val urlIntent = intent.getStringExtra(Constant.PAYMENT_URL)
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
                                val cleanedJson = jsonResult.replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}")
                                val vnPayResponse = gson.fromJson(cleanedJson, VNPayResponse::class.java)
                                // Check response code
                                val responseCodeEnum: VnpResponseCode = VnpResponseCode.fromCode(vnPayResponse.responseCode)!!
                                if (responseCodeEnum!!.equals(VnpResponseCode.SUCCESS)) {
                                    hideLoadingWaiting()
                                    showSuccess(responseCodeEnum)
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

    private fun showSuccess(responseCodeEnum: VnpResponseCode) {
        MotionToast.Companion.createToast(
            this@VNPayActivity,
            "PaymentResult ${responseCodeEnum.code}",
            responseCodeEnum.getString(this@VNPayActivity),
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_TOP,
            MotionToast.LONG_DURATION,
            null
        )
        showSuccessDialog(
            this@VNPayActivity,
            responseCodeEnum.getString(this@VNPayActivity),
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    finish()
                }
            })
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
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    finish()
                }
            })
    }
}