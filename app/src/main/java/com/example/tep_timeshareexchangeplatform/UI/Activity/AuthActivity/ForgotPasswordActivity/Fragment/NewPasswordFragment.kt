package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.ForgotPasswordViewModel
import com.example.tep_timeshareexchangeplatform.Until.Validator.Validator
import com.example.tep_timeshareexchangeplatform.databinding.FragmentNewPasswordBinding

class NewPasswordFragment : BaseFragment(R.layout.fragment_new_password) {

    private lateinit var binding: FragmentNewPasswordBinding
    private val validator = Validator()
    private val viewModel : ForgotPasswordViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        setupTextWatchers()
        eventClickNext()
        return binding.root
    }
    private fun eventClickNext() {
        binding.btnNext.setOnClickListener {
            if (validateAllFields()) {
                viewModel.setNewPassword(binding.edtPassword.text.toString().trim())
                viewModel.setViewPagerPosition(2)
            }
        }
    }

    private fun setupTextWatchers() {

        // Password TextWatcher
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val message = validator.validatePassword(s.toString().trim())
                binding.passwordContainer.helperText = message
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Confirm Password TextWatcher
        binding.edtPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val message = if (s.toString().trim() != binding.edtPassword.text.toString().trim()) {
                    "Mật khẩu không khớp"
                } else {
                    null
                }
                binding.passwordConfirmContainer.helperText = message
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {}

        })

    }

    private fun validateAllFields(): Boolean {
        val isPasswordValid =
            validator.validatePassword(binding.edtPassword.text.toString().trim()) == null
        val isConfirmPasswordValid =
            binding.edtPassword.text.toString().trim() == binding.edtPasswordConfirm.text.toString().trim()

        return isPasswordValid && isConfirmPasswordValid
    }

}