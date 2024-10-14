package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.LoginDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.LoginResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel.AuthViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityLoginScreen2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity_screen2 : BaseActivity() {
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
                    showLoading(true)
                }
                Status.SUCCESS -> {
                    // Handle success, e.g., navigate to another screen
                    hideLoading()
                    resource.data?.let { loginResponse ->
                        handleLoginSuccess(loginResponse)
                    }
                }
                Status.ERROR -> {
                    // Handle error, e.g., show a toast with error message
                    hideLoading()
                    showErrorDialog(resource.message ?: getString(R.string.error_unknown), "Quay lại")
                }
            }
        }
    }

    // Handle login success
    private fun handleLoginSuccess(loginResponse: LoginResponse) {
        // Handle login success, e.g., save tokens, navigate to another screen
        val accessToken = loginResponse.accessToken
        val refreshToken = loginResponse.refreshToken
        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
    }

    // Call loginProcess() function when user click on login button
    private fun loginProcess() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog(getString(R.string.error_empty_email_password), "Quay lại")
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