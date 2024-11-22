package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CreateCustomerDialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatDateByLocale
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateCustomerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DialogUpdateCustomer constructor(
    context: Context,
    private val pickSingleImageLauncherDialog: ActivityResultLauncher<String>,
    private val callback: ConfirmCallback?,
    private val currentProfile: ProfileDTO
) : Dialog(context) {
    private lateinit var binding: DialogUpdateCustomerBinding
    private var gender: String = ""
    private var avatar: String = ""
    private var dob: String = ""
    private var avatarUri: Uri? = null
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
        populateData() // Hiển thị thông tin hiện tại
        eventClick()
        setListSpinner(currentProfile.gender)
    }

    private fun populateData() {
        // Điền thông tin hiện tại vào các trường giao diện
        binding.tvFullNameIn.setText(currentProfile.fullName)
        binding.tvPhone.setText(currentProfile.phone)
        binding.tvAddress.setText(currentProfile.address)
        binding.tvDob.setText(Constant.formatDateByLocale(currentProfile.dob, context))
        val formattedDob = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(currentProfile.dob)!!
        )
        dob = formattedDob

        // Cập nhật avatar
        avatarUri = if (currentProfile.avatar.isNotBlank()) Uri.parse(currentProfile.avatar) else null
        Glide.with(context)
            .load(avatarUri)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_placeholder)
            .into(binding.ivUserAvt)
        if(currentProfile.avatar != null){
            avatar = currentProfile.avatar!!
        }



    }

    private fun eventClick() {
        binding.tvDob.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSaveButtonm.setOnClickListener {
            if (validateFields()) {
                // Nếu tất cả các trường hợp lệ, trả về dữ liệu
                val profileDTO = ProfileDTO(
                    fullName = binding.tvFullNameIn.text.toString().trim(),
                    avatar = avatar,
                    dob = dob,
                    address = binding.tvAddress.text.toString().trim(),
                    gender = gender,
                    phone = binding.tvPhone.text.toString().trim()
                )
                callback?.positiveAction(profileDTO)
                dismiss()
            }
        }

        binding.ivUserAvt.setOnClickListener {
            pickSingleImageLauncherDialog.launch("image/*")
        }
        binding.imClose.setOnClickListener {
            dismiss()
        }

    }

    fun setAvatar(uri: Uri) {
        avatarUri = uri
        binding.ivUserAvt.setImageURI(uri) // Cập nhật ImageView với URI mới
    }
    fun saveImageResponse(image: String){
        avatar = image
    }

    private fun setListSpinner(currentGender: String) {
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

        // Tìm vị trí của giá trị hiện tại trong Spinner
        val genderPosition = if (currentGender.isBlank()) {
            0 // Giá trị mặc định khi không có giới tính
        } else {
            (0 until adapter.count).firstOrNull {
                adapter.getItem(it).toString().equals(currentGender, ignoreCase = true)
            } ?: 0 // Nếu không tìm thấy, trả về giá trị mặc định
        }

        // Set giá trị cho Spinner
        binding.spGender.setSelection(genderPosition)

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


    interface ConfirmCallback {
        fun positiveAction(profileDTO: ProfileDTO)
    }

}