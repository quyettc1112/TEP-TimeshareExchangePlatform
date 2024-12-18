package com.example.tep_timeshareexchangeplatform.ZTest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel.AuthViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityWebViewBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebView : BaseActivity() {
    private lateinit var binding: ActivityWebViewBinding

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

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
        tokenManager = TokenManager(this)
        getFCMToken()
        // Xử lý Deep Link từ Intent
        handleDeepLink(intent)

        observeLoginResponse()

    }

    private fun observeLoginResponse() {
        authViewModel.profileCustomerInfoResponse.observe(this) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    // Show a loading spinner
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    // Handle success, e.g., navigate to another screen
                    hideLoadingWaiting()
                    resource.data?.let { customerInfoResponse ->
                        handleCheckCustomerExist(customerInfoResponse)
                    }
                }
                Status.ERROR -> {
                    hideLoadingWaiting()
                    // SAve user log state, Intent to main
                    tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_USER)
                    intentToMain()
                }
            }
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (task.isSuccessful) {
                tokenManager.saveFCMToken(task.result)
                Log.d("CheckTokenCurrentasdasdasdasd", task.result.toString())
            }
        }
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
                handleLoginSuccess(token, "")
                callGetCustomerInfo()
            } else {
                Toast.makeText(this, "Không nhận được Token", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Handle login success
    private fun handleLoginSuccess(accessToken: String, refreshToken: String) {
        MotionToast.Companion.createColorToast(
            this,
            "${getString(R.string.success_login)}",
            "${getString(R.string.success_login_description)}",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        );

        // Save tokens
        tokenManager.saveTokens(accessToken, refreshToken)
        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_USER)

    }

    private fun handleCheckCustomerExist(customerInfoResponse : CustomerProfileResponse) {
        tokenManager.saveProfileInfo(customerInfoResponse)
        if (customerInfoResponse.isMember) {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
        } else {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
        }
        intentToMain()
    }

    private fun callGetCustomerInfo() {
        authViewModel.getProfileCustomerInfo(tokenManager.getAccessToken().toString())
    }

    private fun intentToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }



}