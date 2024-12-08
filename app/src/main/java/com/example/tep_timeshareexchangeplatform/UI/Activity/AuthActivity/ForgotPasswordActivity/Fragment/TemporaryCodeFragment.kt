package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.ForgotPasswordActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.ForgotPasswordViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.LoginActivity
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTemporyCodeBinding
import `in`.aabhasjindal.otptextview.OTPListener

class TemporaryCodeFragment : BaseFragment(R.layout.fragment_tempory_code) {
    private lateinit var binding: FragmentTemporyCodeBinding
    private val viewModel: ForgotPasswordViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemporyCodeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment\
        setOTPView()
        eventClickSendAgain()
        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.resetPasswordResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as ForgotPasswordActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as ForgotPasswordActivity).hideLoadingWaiting()
                    (activity as ForgotPasswordActivity).showSuccessToast(
                        "Thành Công",
                        "Thay đổi mật khẩu thành công"
                    )
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }

                Status.ERROR -> {
                    (activity as ForgotPasswordActivity).hideLoadingWaiting()
                    (activity as ForgotPasswordActivity).showWarningToast(
                        "Mã OTP không chính xác",
                        "Vui lòng thử lại"
                    )
                    Log.d("EmailFragmenasdasdt", "observeViewModel: ${it.message}")
                }
            }
        }
    }


    private fun setOTPView() {
       binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                Log.d("EmailFragment", "onInteractionListener: ")
            }

            override fun onOTPComplete(otp: String) {
                viewModel.setToken(otp)
                callAPIVerifyCode(otp)
            }
        }
    }

    private fun eventClickSendAgain() {
        binding.tvResend.setOnClickListener {
            callAPISendEmail()
        }
    }

    private fun callAPIVerifyCode(token: String) {
        val email = viewModel.email.value
        val newPassword = viewModel.newPassword.value
        if (email != null && newPassword != null) {
            viewModel.callResetPassword(email, token, newPassword)
        }
    }

    private fun callAPISendEmail() {
        val email = viewModel.email.value
        if (email != null) {
            viewModel.callSendEmailForgotPassword(email)
        }
    }

}