package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    private fun requestRegister() {
        binding.btnLogin.setOnClickListener {
            if (validateAllFields()) {
                MotionToast.Companion.createColorToast(
                    this,
                    "Đăng ký thành công",
                    "Chúc mừng bạn đã đăng ký thành công",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }
    }

    private fun setupTextWatchers() {
        // Username TextWatcher
        binding.edtUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateUserName()  // Validate username as the user types
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Email TextWatcher
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateEmail()  // Validate email as the user types
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Password TextWatcher
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validatePassword()  // Validate password as the user types
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun validateUserName(): Boolean {
        val userName = binding.edtUserName.text.toString().trim()
        return if (userName.isEmpty()) {
            binding.userNameContainer.helperText = "Tên người dùng không được để trống"
            false
        } else {
            binding.userNameContainer.helperText = null  // Clear helperText if valid
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.edtEmail.text.toString().trim()
        return if (email.isEmpty()) {
            binding.emailContainer.helperText = "Email không được để trống"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailContainer.helperText = "Email không hợp lệ"
            false
        } else {
            binding.emailContainer.helperText = null  // Clear helperText if valid
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.edtPassword.text.toString().trim()
        return when {
            password.isEmpty() -> {
                binding.passwordContainer.helperText = "Mật khẩu không được để trống"
                false
            }
            password.length < 6 -> {
                binding.passwordContainer.helperText = "Mật khẩu phải chứa ít nhất 6 ký tự"
                false
            }
            !password.matches(".*[A-Z].*".toRegex()) -> {
                binding.passwordContainer.helperText = "Mật khẩu phải chứa ít nhất một chữ cái viết hoa"
                false
            }
            !password.matches(".*[@#\$%^&+=!].*".toRegex()) -> {
                binding.passwordContainer.helperText = "Mật khẩu phải chứa ít nhất một ký tự đặc biệt"
                false
            }
            !password.matches(".*[0-9].*".toRegex()) -> {
                binding.passwordContainer.helperText = "Mật khẩu phải chứa ít nhất một chữ số"
                false
            }
            else -> {
                binding.passwordContainer.helperText = null  // Clear helperText if valid
                true
            }
        }
    }

    private fun validateAllFields(): Boolean {
        val isUserNameValid = validateUserName()
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()

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