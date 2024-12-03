package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment.BookingDetailActivity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.UpdateExchangeBookingDTO
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberInfoDialog.ConfirmCallback
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateCustomerBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateExchangeBookingBinding

class UpdateExchangeBookingDialog constructor(
    context: Context,
    private val tokenManager: TokenManager,
    private val callback: ConfirmCallback?,
) : Dialog(context) {
    private lateinit var binding: DialogUpdateExchangeBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUpdateExchangeBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Làm cho nền trong suốt
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        bindDefaultData()
        eventClickCloseDismiss()
        eventClickSaveInfo()
    }

    private fun eventClickCloseDismiss() {
        binding.imClose.setOnClickListener {
            dismiss()
        }
    }

    private fun eventClickSaveInfo() {
        binding.btnSaveInfo.setOnClickListener {
            if (validateProfileInfo()) {
                val updateExchangeBookingDTO = UpdateExchangeBookingDTO(
                    binding.tvFullNameIn.text.toString(),
                    binding.tvPhone.text.toString(),
                    binding.tvEmail.text.toString(),
                    "",
                    "",
                    "",
                    "",
                    ""
                )
                callback?.positiveAction(updateExchangeBookingDTO)
                dismiss()
            }
        }
    }

    private fun bindDefaultData() {
        val profileInfo = tokenManager.getProfileInfo()
        if (profileInfo == null) {
            dismiss()
            return
        }
        binding.tvFullNameIn.setText(profileInfo.fullName)
        binding.tvEmail.setText(profileInfo.userEmail)
        binding.tvPhone.setText(profileInfo.phone)
    }


    private fun validateProfileInfo(): Boolean {
        if (binding.tvFullNameIn.text.isNullOrEmpty()) {
            showToast("Tên không được để trống!")
            return false
        }

        if (binding.tvEmail.text.isNullOrEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                binding.tvEmail.text
            ).matches()
        ) {
            showToast("Email không hợp lệ!")
            return false
        }

        if (binding.tvPhone.text.isNullOrEmpty() || !binding.tvPhone.text.matches(Regex("^[0-9]{10,15}$"))) {
            showToast("Số điện thoại không hợp lệ!")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    interface ConfirmCallback {
        fun positiveAction(updateExchangeBookingDTO: UpdateExchangeBookingDTO)
    }


}