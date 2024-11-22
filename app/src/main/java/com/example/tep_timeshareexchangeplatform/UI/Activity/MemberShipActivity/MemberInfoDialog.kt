package com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.DialogFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CreateCustomerDialog.DialogUpdateCustomer.ConfirmCallback
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatDateByLocale
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.databinding.DialogMemberInfoBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateCustomerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MemberInfoDialog constructor(
    context: Context,
    private val callback: ConfirmCallback?,
) : Dialog(context) {
    private lateinit var binding: DialogUpdateCustomerBinding
    private var gender: String = ""
    private var dob: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUpdateCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Làm cho nền trong suốt
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.ivUserAvt.visibility = View.GONE

        eventClick()
        setListSpinner()
    }

    private fun eventClick() {
        binding.tvDob.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSaveButtonm.setOnClickListener {
            if (validateFields()) {
                // Nếu tất cả các trường hợp lệ, trả về dữ liệu
                val customerDTO = CustomerDTO(
                    fullName = binding.tvFullNameIn.text.toString().trim(),
                    dob = dob,
                    address = binding.tvAddress.text.toString().trim(),
                    gender = gender,
                    phone = binding.tvPhone.text.toString().trim()
                )
                callback?.positiveAction(customerDTO)
                dismiss()
            }
        }
        binding.imClose.setOnClickListener {
            dismiss()
        }

    }

    private fun setListSpinner() {
        // Set up the spinner with the string array
        val adapter = ArrayAdapter.createFromResource(
            context,
            R.array.spinner_gender_items,
            android.R.layout.simple_expandable_list_item_1
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spGender.adapter = adapter


        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Cập nhật giá trị giới tính
                gender = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Không làm gì nếu không chọn
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val minDateCalendar = Calendar.getInstance().apply {
            set(1900, Calendar.JANUARY, 1)
        }

        // Tạo một Calendar để đặt ngày mặc định hiển thị là năm 2000
        val defaultDateCalendar = Calendar.getInstance().apply {
            set(2000, Calendar.JANUARY, 1) // Ngày mặc định hiển thị
        }
        // Hiển thị DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Định dạng ngày được chọn thành chuỗi "dd-MM-yyyy"
                val selectedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)

                // Lưu giá trị dob dưới dạng "yyyy-MM-dd"
                dob = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)

                // Áp dụng hàm formatDateByLocale để định dạng ngày theo ngôn ngữ
                val formattedDate = formatDateByLocale(selectedDate, context)
                binding.tvDob.setText(formattedDate)
            },
            defaultDateCalendar.get(Calendar.YEAR), // Năm mặc định 2000
            defaultDateCalendar.get(Calendar.MONTH), // Tháng mặc định Tháng 1
            defaultDateCalendar.get(Calendar.DAY_OF_MONTH) // Ngày mặc định 1
        )

        datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun validateFields(): Boolean {
        val fullName = binding.tvFullNameIn.text.toString().trim()
        val phone = binding.tvPhone.text.toString().trim()
        val address = binding.tvAddress.text.toString().trim()
        val dob = binding.tvDob.text.toString().trim()
        val gender = binding.spGender.selectedItem.toString().trim()

        if (fullName.isBlank()) {
            showToast("Tên đầy đủ không được để trống hoặc chỉ chứa khoảng trắng")
            return false
        }

        if (phone.isBlank()) {
            showToast("Số điện thoại không được để trống hoặc chỉ chứa khoảng trắng")
            return false
        } else if (!phone.matches(Regex("^[0-9]{10,11}$"))) {
            showToast("Số điện thoại không hợp lệ (chỉ nhận 10-11 chữ số)")
            return false
        }

        if (address.isBlank()) {
            showToast("Địa chỉ không được để trống hoặc chỉ chứa khoảng trắng")
            return false
        }

        if (dob.isBlank() && dob == "Nhập Ngày Sinh") {
            showToast("Ngày sinh không được để trống hoặc chỉ chứa khoảng trắng")
            return false
        }

        if (gender.isBlank() || gender == "Chọn giới tính") {
            showToast("Vui lòng chọn giới tính hợp lệ")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    interface OnClickRequestButton {
        fun onClickRequestButton(customerDTO: CustomerDTO)
    }


    interface ConfirmCallback {
        fun positiveAction(customerDTO: CustomerDTO)
    }
}