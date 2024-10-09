package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.MyTimeshareModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreatePostingBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

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

    private fun setEventChangeMyTimeshare() {
        binding.btnChangeMyTimeshare.setOnClickListener {
            rentalPostingViewModel.updateStep(3)
        }

    }

    private fun setEventNext() {
        binding.btnNext.setOnClickListener {
            rentalPostingViewModel.updateStep(6)
        }
    }


    private fun bindDataMyTimeshare(myTimeshareModel: MyTimeshareModel) {
        if (myTimeshareModel == null) {
            binding.includeMyTimeshare.root.visibility = View.GONE
            binding.ivAddMyTimeshare.visibility = View.VISIBLE
        } else {
            binding.includeMyTimeshare.root.visibility = View.VISIBLE
            binding.ivAddMyTimeshare.visibility = View.GONE
            binding.includeMyTimeshare.apply {
                tvResortName.text = myTimeshareModel.name
                tvRoomType.text = myTimeshareModel.roomName
                tvCheckinDate.text = "${myTimeshareModel.checkInDate} - ${myTimeshareModel.checkOutDate}"
                tvNumberOfNight.text =  " | ${myTimeshareModel.numberOfNight.toString()} đêm"
                tvPrice.text = myTimeshareModel.price.toString()
                Glide.with(binding.root.context).load(myTimeshareModel.image).into(imResortImage)
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