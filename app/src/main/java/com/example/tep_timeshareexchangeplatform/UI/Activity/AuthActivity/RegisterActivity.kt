package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RegisterDTO
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel.AuthViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.Validator.Validator
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val validator = Validator()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupTextWatchers()
        customToolbarEvent()
        clickHandler()
        requestRegister()
    }


    private fun observeViewModel() {
        authViewModel.registerResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Đăng ký thành công",
                        "Chúc mừng bạn đã đăng ký thành công",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Đăng ký thất bại",
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

    private fun requestRegister() {
        binding.btnLogin.setOnClickListener {
            if (validateAllFields()) {
                // Auto Set Create as Customer
                val registerDTO = RegisterDTO(
                    binding.edtUserName.text.toString().trim(),
                    binding.edtEmail.text.toString().trim(),
                    binding.edtPassword.text.toString().trim(),
                    1
                )

                // Call API Register
                authViewModel.register(registerDTO)
            }
        }
    }

    private fun setupTextWatchers() {
        // Username TextWatcher
        binding.edtUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val message = validator.validateUserName(s.toString().trim())
                binding.userNameContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Email TextWatcher
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val message = validator.validateEmail(s.toString().trim())
                binding.emailContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Password TextWatcher
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val message = validator.validatePassword(s.toString().trim())
                binding.passwordContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun validateAllFields(): Boolean {
        val isUserNameValid =
            validator.validateUserName(binding.edtUserName.text.toString().trim()) == null
        val isEmailValid = validator.validateEmail(binding.edtEmail.text.toString().trim()) == null
        val isPasswordValid =
            validator.validatePassword(binding.edtPassword.text.toString().trim()) == null

        return isUserNameValid && isEmailValid && isPasswordValid
    }

    private fun clickHandler() {
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun customToolbarEvent() {
        binding.customToolbar.onStartIconClick = {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}