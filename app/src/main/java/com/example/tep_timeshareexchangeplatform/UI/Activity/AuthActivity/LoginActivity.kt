package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.LoginResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel.AuthViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityLoginScreen2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginScreen2Binding

    // Inject AuthViewModel using Hilt
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.customToolbar.onStartIconClick = {
            finish()
        }

        // Observe login response LiveData
        observeLoginResponse()

        // Handle login button click
        clickLoginButton()
    }

    // Observe login response LiveData
    private fun observeLoginResponse() {
        authViewModel.loginResponse.observe(this) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    // Show a loading spinner
                    showLoadingWaiting(true)
                }
                Status.SUCCESS -> {
                    // Handle success, e.g., navigate to another screen
                    hideLoadingWaiting()
                    resource.data?.let { loginResponse ->
                        handleLoginSuccess(loginResponse)
                    }


                }
                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(this,
                        "${resource.status}",
                        "${resource.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_thin));
                }
            }
        }
    }

    // Handle login success
    private fun handleLoginSuccess(loginResponse: LoginResponse) {
        // Handle login success, e.g., save tokens, navigate to another screen
        val accessToken = loginResponse.accessToken
        val refreshToken = loginResponse.refreshToken
        MotionToast.Companion.createColorToast(this,
            "${getString(R.string.success_login)}",
            "${getString(R.string.success_login_description)}",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_thin));

        val token: TokenManager = TokenManager(this)
        token.saveTokens(accessToken, refreshToken)
        startActivity(Intent(this, MainActivity::class.java))
        Log.d("CheckToken", "Access" + accessToken)
        finish()


    }

    // Call loginProcess() function when user click on login button
    private fun loginProcess() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {

            MotionToast.Companion.createColorToast(this,
                "${getString(R.string.error_empty_email_password)}",
                "Nhập lại thông tin",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.inter_thin));
           // showErrorDialog(getString(R.string.error_empty_email_password), "Quay lại")
            return
        }
        val loginDTO = LoginDTO(email, password)
        authViewModel.login(loginDTO)
    }




    /// Group function here for click event
    // Group function here for click event
    private fun clickLoginButton() {
        binding.btnLogin.setOnClickListener {
            loginProcess()
        }
    }

}