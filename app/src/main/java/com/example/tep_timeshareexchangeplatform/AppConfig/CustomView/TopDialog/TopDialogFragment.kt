package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.TopDialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcel
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.RoomSelectionDialog
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.databinding.DialogSearchComponentBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TopDialogFragment : DialogFragment() {

    lateinit var _binding: DialogSearchComponentBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MyMaterialDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSearchComponentBinding.inflate(inflater, container, false)
        initActivityResultLauncher()
        observeData()
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

    private fun observeData() {
        mainViewModel.location.observe(viewLifecycleOwner) { location ->
            _binding.tvLocation.text = location
        }
        mainViewModel.dateRange.observe(viewLifecycleOwner) { dateRange ->
            _binding.tvDate.text = dateRange
        }

        // Observe data from ViewModel Resort
        mainViewModel.roomCount.observe(viewLifecycleOwner) {
            _binding.tvTourist.text = mainViewModel.getRoomCount()
        }



    }

    private fun setupClickListeners() {
        // Location Click Event
        _binding.llLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            locationResultLauncher.launch(intent)
        }

        _binding.llTourist.setOnClickListener {
            val roomSelectionDialog = RoomSelectionDialog.newInstance()
            roomSelectionDialog.show(parentFragmentManager, "RoomSelectionDialog")
        }

        _binding.llDate.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                      return true
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
                mainViewModel.updateDateRange("$startDateString - $endDateString")
            }
        }
    }
    companion object {
        fun newInstance(): TopDialogFragment {
            return TopDialogFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initActivityResultLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation =
                        data?.getStringExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY)
                    selectedLocation?.let {
                        mainViewModel.updateLocation(selectedLocation)
                    }
                }
            }
    }
}