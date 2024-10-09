package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class  DayPickerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_day_picker)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Giới hạn ngày (ví dụ: chỉ cho phép chọn từ ngày 5 đến ngày 25 của tháng 9)
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(object : CalendarConstraints.DateValidator {
                override fun isValid(date: Long): Boolean {
                    // Định dạng ngày để kiểm tra các ngày không hợp lệ
                    val calendar = Calendar.getInstance().apply { timeInMillis = date }
                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                    val month = calendar.get(Calendar.MONTH)
                    val year = calendar.get(Calendar.YEAR)
                    return true
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
        dateRangePicker.show(supportFragmentManager, "DateRangePicker")


        // Lắng nghe sự kiện khi người dùng chọn ngày
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection?.first
            val endDate = selection?.second

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString = startDate?.let { dateFormat.format(Date(it)) } ?: "N/A"
            val endDateString = endDate?.let { dateFormat.format(Date(it)) } ?: "N/A"

            intentExtraValueToHome( "$startDateString - $endDateString")

        }
    }

    private fun checkIntenFrom() {
        val value = intent.getStringExtra(Constant.DEFAULT_SELECTION_DATE_KEY)
        if (value != null) {
            when (value) {
                "A1" -> {
                    // Handle the case where A1 sent the Intent
                }
                "A3" -> {
                    // Handle the case where A3 sent the Intent
                }
            }
        }

    }


    private fun intentExtraValueToHome(value : String) {
        val intent = Intent()
        // Replace "locationName" with the actual selected location
        intent.putExtra(Constant.DEFAULT_SELECTION_DATE_KEY, value)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}