package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment

import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreatePostingBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Step_5_CreatePostingFragment : BaseFragment(R.layout.fragment_create_posting) {

    private lateinit var binding: FragmentCreatePostingBinding
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostingBinding.inflate(inflater, container, false)
        binding.includeMyTimeshare.btnSelect.visibility = View.GONE
        observeViewModel()
        setEventChangeMyTimeshare()
        setEventNext()
        setEventChangeDate()
        return binding.root
    }

    // Funtion to observe view model
    private fun observeViewModel() {
        // Observe myTimeshareModelSelected
        rentalPostingViewModel.myTimeshareModelSelected.observe(viewLifecycleOwner) { myTimeshareModel ->
            bindDataMyTimeshare(myTimeshareModel)
        }

        // Observe Date Range Picker
        rentalPostingViewModel.dateRange.observe(viewLifecycleOwner) { dateRangePicker ->
            bindDataDateRange(dateRangePicker)
        }
    }

    // Function to set event change my timeshare
    private fun setEventChangeMyTimeshare() {
        binding.btnChangeMyTimeshare.setOnClickListener {
            rentalPostingViewModel.updateStep(3)
        }

    }

    // Function to set event next
    private fun setEventNext() {
        binding.btnNext.setOnClickListener {
            rentalPostingViewModel.updateStep(6)
        }
    }

    // Function to set event change date
    private fun setEventChangeDate() {
        binding.llCheckInCheckOutComp.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        // Optional: Add logic to validate the selected date
                        return true
                    }
                    override fun describeContents(): Int = 0
                    override fun writeToParcel(dest: Parcel, flags: Int) {
                        // Required to implement DateValidator
                    }
                })

            // Create DateRangePicker with CalendarConstraints
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText(getString(R.string.date_range_picker))
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()

            // Show DateRangePicker when button is clicked
            dateRangePicker.show(requireActivity().supportFragmentManager, "DateRangePicker")

            // Listen for positive button clicks (when a date is selected)
            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val startDate = selection?.first
                val endDate = selection?.second

                if (startDate != null && endDate != null) {
                    rentalPostingViewModel.setDateRange(startDate, endDate)
                    // Calculate total days between startDate and endDate
                    val totalDays = ((endDate - startDate) / (1000 * 60 * 60 * 24)).toInt() + 1

                    // Update UI with the total number of days
                    binding.etNightsCount.text = " $totalDays "

                    // Format and display start date and end date
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val startDateString = dateFormat.format(Date(startDate))
                    val endDateString = dateFormat.format(Date(endDate))

                    binding.tvCheckInDate.text = startDateString
                    binding.tvCheckOutDate.text = endDateString

                    // Get day of the week for start date
                    val startDayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(startDate))
                    binding.tvCheckInDayOfWeek.text = startDayString

                    // Get day of the week for end date
                    val endDayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(endDate))
                    binding.tvCheckOutDayOfWeek.text = endDayString
                }
            }

        }
    }

    // Function to bind data
    private fun bindDataMyTimeshare(myTimeshareResponse: MyTimeshareResponse) {
        if (myTimeshareResponse == null) {
            binding.includeMyTimeshare.root.visibility = View.GONE
            binding.ivAddMyTimeshare.visibility = View.VISIBLE
        } else {
            binding.includeMyTimeshare.root.visibility = View.VISIBLE
            binding.ivAddMyTimeshare.visibility = View.GONE

            binding.includeMyTimeshare.tvNumberOfNight.visibility = View.GONE
            binding.includeMyTimeshare.tvPrice.visibility = View.GONE
            binding.includeMyTimeshare.apply {
                tvResortName.text = myTimeshareResponse.resortName
                tvRoomType.text = myTimeshareResponse.roomName
                tvCheckinDate.text = "${myTimeshareResponse.startDate} - ${myTimeshareResponse.endDate}"
                /*Glide.with(binding.root.context).load(myTimeshareModel.image).into(imResortImage)*/
            }
        }
    }
    private fun bindDataDateRange(dateRangePicker: Pair<Long?, Long?>) {
        val startDate = dateRangePicker.first ?: return
        val endDate = dateRangePicker.second ?: return
        if (startDate != 0L && endDate != 0L) {
            // Calculate total days between startDate and endDate
            val totalDays = ((endDate - startDate) / (1000 * 60 * 60 * 24)).toInt() + 1

            // Update UI with the total number of days
            binding.etNightsCount.text = " $totalDays "

            // Format and display start date and end date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString = dateFormat.format(Date(startDate))
            val endDateString = dateFormat.format(Date(endDate))

            binding.tvCheckInDate.text = startDateString
            binding.tvCheckOutDate.text = endDateString

            // Get day of the week for start date
            val startDayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(startDate))
            binding.tvCheckInDayOfWeek.text = startDayString

            // Get day of the week for end date
            val endDayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(endDate))
            binding.tvCheckOutDayOfWeek.text = endDayString
        }
    }

}