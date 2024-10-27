package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.CustomerResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.User.LoginResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel.AuthViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityLoginScreen2Binding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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
                    MotionToast.Companion.createColorToast(
                        this,
                        "${resource.status}",
                        "${resource.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_thin)
                    );
                }
            }
        }

        authViewModel.customerResponse.observe(this) { response ->
            when (response.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    handelCheckCustomer(response)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (response.message?.contains("404") == true) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra(Constant.USER_LOGIN_STATE, UserLogState.LOGGED_IN_AS_USER)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun handelCheckCustomer(response: Resource<CustomerResponse>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now()
            val expiryDate = LocalDate.parse(
                response.data?.memberExpiryDate.toString(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            )

            if (expiryDate.isAfter(currentDate)) {
                // Ngày hết hạn còn hiệu lực so với ngày hiện tại
                hideLoadingWaiting()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constant.USER_LOGIN_STATE, UserLogState.LOGGED_IN_AS_CUSTOMER)
                startActivity(intent)
            } else {
                // Ngày hết hạn đã qua
                hideLoadingWaiting()
                // Giả sử bạn có một hàm hiển thị thông báo hết hạn
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constant.USER_LOGIN_STATE, UserLogState.LOGGED_IN_AS_USER)
                startActivity(intent)

                MotionToast.Companion.createColorToast(
                    this,
                    "Hêt hạn Gói thành viên",
                    "Gia hạn gói thành viên để sử dụng dịch vụ",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.inter_thin)
                );
            }
        } else {
            // Sử dụng SimpleDateFormat cho các phiên bản API thấp hơn
            val currentDate = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val expiryDate = dateFormat.parse(response.data?.memberExpiryDate.toString())

            if (expiryDate != null && expiryDate.after(currentDate)) {
                // Ngày hết hạn còn hiệu lực so với ngày hiện tại
                hideLoadingWaiting()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constant.USER_LOGIN_STATE, UserLogState.LOGGED_IN_AS_CUSTOMER)
                startActivity(intent)
            } else {
                // Ngày hết hạn đã qua
                hideLoadingWaiting()
                Toast.makeText(this, "Hết hạn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constant.USER_LOGIN_STATE, UserLogState.LOGGED_IN_AS_USER)
                startActivity(intent)
                MotionToast.Companion.createColorToast(
                    this,
                    "Hêt hạn Gói thành viên",
                    "Gia hạn gói thành viên để sử dụng dịch vụ",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.inter_thin)
                );
            }
        }
    }

    // Handle login success
    private fun handleLoginSuccess(loginResponse: LoginResponse) {
        // Handle login success, e.g., save tokens, navigate to another screen
        val accessToken = loginResponse.accessToken
        val refreshToken = loginResponse.refreshToken
        MotionToast.Companion.createColorToast(
            this,
            "${getString(R.string.success_login)}",
            "${getString(R.string.success_login_description)}",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_thin)
        );

        val token: TokenManager = TokenManager(this)
        token.saveTokens(accessToken, refreshToken)

        val userJWTPayloadModel = JwtDecoder().parseJwtUsingGson(accessToken)

        userJWTPayloadModel?.let { authViewModel.getIsCustomerExist(accessToken, it.userId) }

    }

    // Call loginProcess() function when user click on login button
    private fun handleLoginInput() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {

            MotionToast.Companion.createColorToast(
                this,
                "${getString(R.string.error_empty_email_password)}",
                "Nhập lại thông tin",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.inter_thin)
            );
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
            handleLoginInput()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}