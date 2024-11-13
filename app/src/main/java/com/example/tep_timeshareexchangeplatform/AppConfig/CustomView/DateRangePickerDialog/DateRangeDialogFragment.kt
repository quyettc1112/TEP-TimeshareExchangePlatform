package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.DateRangePickerDialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.FragmentDateRangeDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateRangeDialogFragment(private val onDateSelected: (Pair<Long, Long>) -> Unit) : DialogFragment() {

    private var startDate: Long? = null
    private var endDate: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDateRangeDialogBinding.inflate(inflater, container, false)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            if (startDate == null) {
                startDate = calendar.timeInMillis
                binding.tvStartDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            } else {
                endDate = calendar.timeInMillis
                binding.tvEndDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }
        }

        binding.btnConfirm.setOnClickListener {
            if (startDate != null && endDate != null) {
                onDateSelected(Pair(startDate!!, endDate!!))
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn đủ ngày", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}