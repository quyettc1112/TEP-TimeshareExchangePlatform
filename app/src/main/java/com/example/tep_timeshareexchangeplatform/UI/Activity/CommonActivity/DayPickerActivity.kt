package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity

import android.os.Bundle
import android.os.Parcel
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DayPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_day_picker)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.date_picker_button)

        // Giới hạn ngày (ví dụ: chỉ cho phép chọn từ ngày 5 đến ngày 25 của tháng 9)
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
                    TODO("Not yet implemented")
                }

            })

        // Tạo DateRangePicker với CalendarConstraints
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Chọn khoảng thời gian")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        // Hiển thị DateRangePicker khi nhấn nút
        button.setOnClickListener {
            dateRangePicker.show(supportFragmentManager, "DateRangePicker")
        }

        // Lắng nghe sự kiện khi người dùng chọn ngày
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection?.first
            val endDate = selection?.second

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString = startDate?.let { dateFormat.format(Date(it)) } ?: "N/A"
            val endDateString = endDate?.let { dateFormat.format(Date(it)) } ?: "N/A"

            button.text = "Từ: $startDateString - Đến: $endDateString"
        }
    }
}