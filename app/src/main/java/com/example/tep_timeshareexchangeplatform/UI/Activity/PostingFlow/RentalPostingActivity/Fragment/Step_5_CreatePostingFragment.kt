package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
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
        bindDataSpinner()
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

        // Observer Package Type
        rentalPostingViewModel.packageStep4.observe(viewLifecycleOwner) { packageType ->
            checkPackageType(packageType)
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
    private fun bindDataSpinner() {
        val refundPolicies = RefundPolicy.entries.toTypedArray() // Lấy danh sách tất cả các enum
        val spinnerAdapter = object : ArrayAdapter<RefundPolicy>(requireContext(), R.layout.spinner_item, refundPolicies) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val refundPolicy = getItem(position)
                (view as TextView).text = refundPolicy?.getShortDescription(context)
                return view
            }
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val refundPolicy = getItem(position)
                (view as TextView).text = refundPolicy?.getShortDescription(context)
                return view
            }

        }
        binding.customSpinnerViewDiretion.adapter = spinnerAdapter

        binding.customSpinnerViewDiretion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPolicy = parent.getItemAtPosition(position) as RefundPolicy
                val policyId = selectedPolicy.id
                val longDescription =
                    Html.fromHtml(selectedPolicy.getLongDescription(requireContext()))
                binding.tvCancellationPolicyDescription.text = longDescription
                Toast.makeText(requireContext(), "Selected ID: $policyId", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Không có mục nào được chọn
            }
        }


    }
    private fun checkPackageType(packageModel: PackageModel) {
        when(packageModel) {
            PackageEnum.BASIC_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.VISIBLE
                binding.includePaymentMethod34.root.visibility = View.GONE
            }
            PackageEnum.ADVANCED_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.VISIBLE
                binding.includePaymentMethod34.root.visibility = View.GONE
            }
            PackageEnum.PREMIUM_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.GONE
                binding.includePaymentMethod34.root.visibility = View.VISIBLE
            }
            PackageEnum.DELEGATED_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.GONE
                binding.includePaymentMethod34.root.visibility = View.VISIBLE
            }
        }
    }


}