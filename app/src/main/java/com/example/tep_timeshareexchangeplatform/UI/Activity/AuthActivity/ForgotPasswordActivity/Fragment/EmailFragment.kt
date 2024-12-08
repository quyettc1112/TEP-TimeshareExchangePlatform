package com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.ForgotPasswordActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.ForgotPasswordActivity.ForgotPasswordViewModel
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentEmailBinding

class EmailFragment : BaseFragment(R.layout.fragment_email) {

    private lateinit var binding: FragmentEmailBinding
    private val viewModel : ForgotPasswordViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailBinding.inflate(inflater, container, false)
        observeViewModel()
        eventClickNext()
        eventClickSendEmail()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.forgotPasswordResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as ForgotPasswordActivity).showLoadingWaiting(true)
                }
                Status.SUCCESS -> {
                    (activity as ForgotPasswordActivity).hideLoadingWaiting()
                    (activity as ForgotPasswordActivity).showSuccessToast("Gửi email thành công", "Vui lòng kiểm tra email của bạn")
                    binding.btnNext.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    (activity as ForgotPasswordActivity).hideLoadingWaiting()
                    (activity as ForgotPasswordActivity).showErrorToast("Gửi email thất bại", "Vui lòng thử lại")
                    Log.d("EmailFragmenasdasdt", "observeViewModel: ${it.message}")
                }
            }
        }
    }

    private fun eventClickNext() {
        binding.btnNext.setOnClickListener {
            viewModel.setViewPagerPosition(1)

        }
    }
    private fun eventClickSendEmail() {
        binding.btnSend.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            if (validEmail(email)) {
                callSendEmailForgotPassword(email)
            } else {
                return@setOnClickListener
            }
        }
    }


    private fun validEmail(email: String?): Boolean {
        return when {
            email.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Email không được để trống", Toast.LENGTH_SHORT).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(requireContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun callSendEmailForgotPassword(email: String) {
        viewModel.setEmail(email)
        viewModel.callSendEmailForgotPassword(email)
    }





}