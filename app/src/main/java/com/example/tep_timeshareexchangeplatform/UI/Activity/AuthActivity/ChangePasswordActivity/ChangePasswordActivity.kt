package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ChangePasswordActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ChangePasswordDTO
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthViewModel.AuthViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.Until.Validator.Validator
import com.example.tep_timeshareexchangeplatform.databinding.ActivityChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val authViewModel : AuthViewModel by viewModels()
    private val validator = Validator()
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        tokenManager = TokenManager(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(!tokenManager.isLoggedIn()){
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show()
            finish()
        }


        setupTextWatchers()
        validateAllFields()
        eventClickNext()
        eventClickToolbar()
        observeData()

    }

    private fun observeData() {
        authViewModel.changePasswordResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessToast("Thành Công", "Đổi Mật Khẩu Thành Công")
                    finish()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message!!.contains("401")) {
                        showWarningToast("Có lỗi xảy ra", "Không thể đổi mật khẩu")
                    } else {
                        showWarningToast("Có lỗi xảy ra", it.message.toString())
                    }
                    Log.d("ChangePasswordActiviasdty", "observeData: ${it.message}")
                }
            }
        }
    }


    private fun eventClickNext() {
        binding.btnNext.setOnClickListener {
            if (validateAllFields()) {
                val changePasswordDTO = ChangePasswordDTO(
                    binding.edtPasswordCurrent.text.toString().trim(),
                    binding.edtPasswordNew.text.toString().trim(),
                    binding.edtPasswordNewConfirm.text.toString().trim()
                )

                callChangePasswordAPI(changePasswordDTO)
            }
        }
    }

    private fun eventClickToolbar() {
        binding.customToolbar.onStartIconClick =  {
            finish()
        }
    }

    private fun setupTextWatchers() {
        // Password TextWatcher
        binding.edtPasswordCurrent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                val message = if (password.isEmpty()) {
                    "Mật khẩu không được để trống"
                } else {
                     ""
                }
                binding.passwordCurrentContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        binding.edtPasswordNew.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                val message = if (password.isEmpty()) {
                    "Mật khẩu không được để trống"
                } else {
                    validator.validatePassword(password) ?: ""
                }
                binding.passwordNewContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Confirm Password TextWatcher
        binding.edtPasswordNewConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val confirmPassword = s.toString().trim()
                val newPassword = binding.edtPasswordNew.text.toString().trim()

                val message = when {
                    confirmPassword.isEmpty() -> "Mật khẩu xác nhận không được để trống"
                    confirmPassword != newPassword -> "Mật khẩu không khớp"
                    else -> ""
                }
                binding.passwordNewConfirmContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun validateAllFields(): Boolean {
        val isCurrentPasswordValid =
            binding.edtPasswordCurrent.text.toString().trim().isNotEmpty()
        val isPasswordValid =
            validator.validatePassword(binding.edtPasswordNew.text.toString().trim()) == null
        val isConfirmPasswordValid =
            binding.edtPasswordNew.text.toString()
                .trim() == binding.edtPasswordNewConfirm.text.toString().trim()

        return isPasswordValid && isConfirmPasswordValid && isCurrentPasswordValid
    }

    private fun callChangePasswordAPI(changePasswordDTO: ChangePasswordDTO) {
        Log.d("ChangePasswordActivityasasd", "callChangePasswordAPI: $changePasswordDTO")
        authViewModel.changePassword(tokenManager.getAccessToken().toString(), changePasswordDTO)
    }
}