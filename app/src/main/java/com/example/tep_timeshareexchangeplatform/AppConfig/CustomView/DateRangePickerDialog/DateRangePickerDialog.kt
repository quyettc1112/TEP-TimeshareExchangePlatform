package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.DateRangePickerDialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import com.example.tep_timeshareexchangeplatform.R
import java.util.Calendar

class DateRangePickerDialog(
    context: Context,
    private val onDateSelected: (startDate: Calendar, endDate: Calendar) -> Unit
) : Dialog(context) {

    private lateinit var datePickerStart: DatePicker
    private lateinit var datePickerEnd: DatePicker
    private lateinit var btnConfirm: Button

    init {
        setContentView(R.layout.dialog_time_picker)

        datePickerStart = findViewById(R.id.datePickerStart)
        datePickerEnd = findViewById(R.id.datePickerEnd)
        btnConfirm = findViewById(R.id.btnConfirm)

        datePickerEnd.visibility = View.GONE // Ẩn DatePicker của ngày kết thúc ban đầu
        btnConfirm.isEnabled = false // Disable button Confirm ban đầu

        setupListeners()
    }

    private fun setupListeners() {
        datePickerStart.minDate = Calendar.getInstance().timeInMillis // Ngày bắt đầu không nhỏ hơn hôm nay

        datePickerStart.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            datePickerEnd.visibility = View.VISIBLE // Hiện DatePicker ngày kết thúc
            val startDate = Calendar.getInstance().apply {
                set(year, monthOfYear, dayOfMonth)
            }

            // Đặt minDate cho ngày kết thúc
            datePickerEnd.minDate = startDate.timeInMillis
        }

        datePickerEnd.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val startDate = Calendar.getInstance().apply {
                set(datePickerStart.year, datePickerStart.month, datePickerStart.dayOfMonth)
            }

            val endDate = Calendar.getInstance().apply {
                set(year, monthOfYear, dayOfMonth)
            }

            // Kiểm tra điều kiện ngày kết thúc không nhỏ hơn ngày bắt đầu
            if (!endDate.before(startDate)) {
                btnConfirm.isEnabled = true
            } else {
                btnConfirm.isEnabled = false
                Toast.makeText(context, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu", Toast.LENGTH_SHORT).show()
            }
        }

        btnConfirm.setOnClickListener {
            val startDate = Calendar.getInstance().apply {
                set(datePickerStart.year, datePickerStart.month, datePickerStart.dayOfMonth)
            }

            val endDate = Calendar.getInstance().apply {
                set(datePickerEnd.year, datePickerEnd.month, datePickerEnd.dayOfMonth)
            }

            onDateSelected(startDate, endDate) // Gọi callback trả về dữ liệu
            dismiss() // Đóng dialog
        }
    }
}