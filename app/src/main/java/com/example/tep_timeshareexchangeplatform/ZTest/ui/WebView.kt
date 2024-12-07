package com.example.tep_timeshareexchangeplatform.ZTest.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityWebViewBinding

class WebView : BaseActivity() {
    private lateinit var binding: ActivityWebViewBinding

    companion object {
        const val UNWIND_OAUTH2 = "https://unwind.id.vn/oauth2/authorization/google"
        const val OAUTH_SUCCESS = "http://35.247.160.131/api/auth/oauth2-success"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Xử lý Deep Link từ Intent
        handleDeepLink(intent)

    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val data: Uri? = intent.data
        data?.let {
            // Lấy giá trị token từ query parameter
            val token = it.getQueryParameter("token")
            if (token != null) {
                Toast.makeText(this, "Access Token: $token", Toast.LENGTH_LONG).show()
                // Thực hiện các hành động với token, ví dụ: lưu token hoặc gọi API
            } else {
                Toast.makeText(this, "No token received", Toast.LENGTH_LONG).show()
            }
        }
    }
}