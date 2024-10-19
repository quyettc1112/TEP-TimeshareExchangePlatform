package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MemberShipActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.databinding.DialogMemberInfoBinding
import java.util.Calendar

class MemberInfoDialog: DialogFragment() {


    interface OnClickRequestButton {
        fun onClickRequestButton()
    }
    private var onClickRequestButton: OnClickRequestButton? = null
    fun setOnClickRequestButton(onClickRequestButton: OnClickRequestButton) {
        this.onClickRequestButton = onClickRequestButton
    }


    private lateinit var binding: DialogMemberInfoBinding // Assume you're using View Binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogMemberInfoBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup close button
        binding.imClose.setOnClickListener { dismiss() }

        // Set up click listener for the update button
        binding.ctrRequestButton.setOnClickListener {
            handleInput()
        }

        binding.tvDob.setOnClickListener {
            showDatePickerDialog()
        }

        setListSpinner()

    }

    private fun setListSpinner() {
        // Set up the spinner with the string array
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_gender_items,
            android.R.layout.simple_expandable_list_item_1
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spGender.adapter = adapter
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Update the TextView with the selected date
            binding.tvDob.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        }, year, month, day)

        // Show the dialog
        datePickerDialog.show()
    }

    private fun handleInput() {
        val fullName = binding.edtFullname.text.toString()
        val phoneNumber = binding.edtPhone.text.toString()
        val email = binding.edtEmail.text.toString()
        val address = binding.edtAddress.text.toString()

        // Clear previous errors
        binding.edtFullname.error = null
        binding.edtPhone.error = null
        binding.edtEmail.error = null
        binding.edtAddress.error = null

        // Check for null or empty values
        var isValid = true

        if (fullName.isEmpty()) {
            binding.edtFullname.error = "Tên Đầy Đủ không được để trống"
            isValid = false
        }
        if (phoneNumber.isEmpty()) {
            binding.edtPhone.error = "Số Điện Thoại không được để trống"
            isValid = false
        }
        if (email.isEmpty()) {
            binding.edtEmail.error = "Email không được để trống"
            isValid = false
        }
        if (address.isEmpty()) {
            binding.edtAddress.error = "Địa Chỉ không được để trống"
            isValid = false
        }

        // Only proceed if all fields are valid
        if (isValid) {
            // Handle valid input (e.g., save data, send it to the server)
            MotionToast.Companion.createColorToast(requireActivity(),
                "Thành Công",
                "Cập Nhật Thông Tin Thành Công",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null)
            dismiss()

        }
    }


    companion object {
        fun newInstance(): MemberInfoDialog {
            return MemberInfoDialog()
        }
    }
}