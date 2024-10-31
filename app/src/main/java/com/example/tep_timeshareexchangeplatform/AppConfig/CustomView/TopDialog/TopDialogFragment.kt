package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.TopDialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcel
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.RoomSelectionDialog
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.databinding.DialogSearchComponentBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TopDialogFragment : DialogFragment() {

    lateinit var _binding: DialogSearchComponentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MyMaterialDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSearchComponentBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.TOP)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Apply the logic from your Fragment here
        setupClickListeners()
    }
    private fun setupClickListeners() {
        // Location Click Event
        _binding.llLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
           // locationResultLauncher.launch(intent)
        }

        _binding.llTourist.setOnClickListener {
            val roomSelectionDialog = RoomSelectionDialog.newInstance()
            roomSelectionDialog.show(parentFragmentManager, "RoomSelectionDialog")
        }

        _binding.llDate.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        // Định dạng ngày để kiểm tra các ngày không hợp lệ
                        val calendar = Calendar.getInstance().apply { timeInMillis = date }
                        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                        val month = calendar.get(Calendar.MONTH)
                        val year = calendar.get(Calendar.YEAR)

                        // Ví dụ: Chỉ cho phép chọn ngày từ 5/9/2024 đến 25/9/2024
                        return if (year == 2024 && month == Calendar.SEPTEMBER) {
                            dayOfMonth in 5..25
                        } else {
                            false // Không hợp lệ cho các ngày ngoài phạm vi trên
                        }
                    }
                    override fun describeContents(): Int = 0
                    override fun writeToParcel(dest: Parcel, flags: Int) {
                        // You can implement this if needed or leave it unimplemented
                    }

                })

            // Tạo DateRangePicker với CalendarConstraints
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.date_range_picker))
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

            // Hiển thị DateRangePicker khi nhấn nút
            dateRangePicker.show(requireActivity().supportFragmentManager, "DateRangePicker")
            // Lắng nghe sự kiện khi người dùng chọn ngày
            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val startDate = selection?.first
                val endDate = selection?.second

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val startDateString = startDate?.let { dateFormat.format(Date(it)) } ?: "N/A"
                val endDateString = endDate?.let { dateFormat.format(Date(it)) } ?: "N/A"

                _binding.tvDate.text = "$startDateString - $endDateString"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}