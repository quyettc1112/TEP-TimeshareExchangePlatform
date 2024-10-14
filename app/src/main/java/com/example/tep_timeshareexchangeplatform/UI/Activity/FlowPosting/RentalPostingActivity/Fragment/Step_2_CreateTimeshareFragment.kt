package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.LocationModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.AmenitiesAdaper
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.ImageUploadAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.UnitTypeAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimeshareBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Step_2_CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare) {

    private lateinit var binding: FragmentCreateTimeshareBinding
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>
    private var unitTypeAdapter = UnitTypeAdapter(false)
    private var amenitiesAdapter = AmenitiesAdaper()
    private var amenitiesEntertamentAdapter = AmenitiesAdaper()
    private var imageUploadAdapter = ImageUploadAdapter()
    private var policyAmentitiesAdapter = AmenitiesAdaper()
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()
    private var isUnitTypeExpanded = false
    private var isDayCheckInExpanded = false
    private var isAmenitiesExpanded = false
    private var isImageInfoExpanded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTimeshareBinding.inflate(inflater, container, false)
        initActivityLauncher()
        observeViewModel()
        setEventChangeLocation()
        setValueUnitRoom()
        expanedViewHandle()
        getImageFromGallery()
        sendRequestCreateTimeshare()
        setEventChangeDate()

        return binding.root
    }

    private fun initAdapter() {
        unitTypeAdapter.submitList(listOf())
        amenitiesAdapter.submitList(Constant.listAmenities)
        amenitiesEntertamentAdapter.submitList(Constant.listEntertament)
        policyAmentitiesAdapter.submitList(Constant.listPolicy)
        imageUploadAdapter.submitList(listOf())
        imageUploadAdapter.onDeleteClick = {
            imageUploadAdapter.removeItem(it)
        }

    }
    // Observe Location Model
    private fun observeViewModel() {
        // Bind Data of Location Model
        rentalPostingViewModel.locationModel.observe(viewLifecycleOwner) { locationModel ->
            if (locationModel != null) {
                binding.let {
                    it.tvResortName.text = locationModel.name
                    it.tvLocation.text = locationModel.location
                    it.ivResortImage.setImageResource(locationModel.image)
                    binding.llResortLocation.visibility = View.VISIBLE
                    binding.btnSelectResortLocation.visibility = View.GONE
                    isUnitTypeExpanded = true
                    handleViewVisibility(binding.llContentUnitType, isUnitTypeExpanded)
                }
            } else {
                binding.llResortLocation.visibility = View.GONE
                binding.btnSelectResortLocation.visibility = View.VISIBLE

            }
        }
    }

    // Click to Open or Close View
    private fun expanedViewHandle() {
        binding.crlUnitTypeInfo.setOnClickListener {
            isUnitTypeExpanded = !isUnitTypeExpanded
            handleViewVisibility(binding.llContentUnitType, isUnitTypeExpanded)
        }
        binding.crlDayCheckInInfo.setOnClickListener {
            isDayCheckInExpanded = !isDayCheckInExpanded
            handleViewVisibility(binding.llCheckInCheckOut, isDayCheckInExpanded)
        }
        binding.crlAmenitiesInfo.setOnClickListener {
            isAmenitiesExpanded = !isAmenitiesExpanded
            handleViewVisibility(binding.llContentAmenities, isAmenitiesExpanded)
        }
        binding.crlImageInfo.setOnClickListener {
            isImageInfoExpanded = !isImageInfoExpanded
            handleViewVisibility(binding.llImage, isImageInfoExpanded)
        }

    }
    private fun handleViewVisibility(view: View, isExpanded: Boolean) {
        view.visibility = if (isExpanded) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // User click to change location of Resort
    private fun setEventChangeLocation() {
        binding.tvChangeLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtras(Bundle().apply {
                putString(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, "getResortLocation")
            })
            locationResultLauncher.launch(intent)
        }
        binding.btnSelectResortLocation.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtras(Bundle().apply {
                putString(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, "getResortLocation")
            })
            locationResultLauncher.launch(intent)
        }
    }

    // Set Value for of Resort Location
    private fun setValueUnitRoom() {
        // Set Spinner
        var adapterSpiner = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items, android.R.layout.simple_spinner_item
        )
        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.customSpinnerBed.adapter = adapterSpiner
        binding.customSpinnerViewDiretion.adapter = adapterSpiner

        // Set Unit Type
        binding.rvBedType.apply {
            adapter = unitTypeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // Set List Amenities
        binding.rvAmenities.apply {
            adapter = amenitiesAdapter
            layoutManager = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
        }

        // Set List Amenities Entertament
        binding.rvAmenitiesEntertainment.apply {
            adapter = amenitiesEntertamentAdapter
            layoutManager = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
        }

        // Set List Policy
        binding.rvPolicy.apply {
            adapter = policyAmentitiesAdapter
            layoutManager = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
        }

        // Set List Image
        binding.rvImage.apply {
            adapter = imageUploadAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }


    }

    // Event to Change Date
    private fun setEventChangeDate() {
        binding.llCheckInCheckOutDate.setOnClickListener {
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


    // Get Image Form Gallery
    private fun getImageFromGallery() {
        binding.btnAddImage.setOnClickListener {
            openGallery()
        }
    }
    private fun initActivityLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation: LocationModel? = data?.getParcelableExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW)
                    selectedLocation?.let {
                        rentalPostingViewModel.updateLocationModel(it)
                    }
                }
            }

    }
    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            val listImage = mutableListOf<ImageUploadModel>()
            for (uri in uris) {
                listImage.add(ImageUploadModel.create(uri))
            }
            imageUploadAdapter.addImage(listImage)
        }
    }
    fun openGallery() {
        pickImagesLauncher.launch("image/*")
    }


    private fun sendRequestCreateTimeshare() {
        binding.btnNext.setOnClickListener {
            showLoading("Đang tạo yêu cầu tạo mới Timeshare", "Vui lòng chờ", true, object : (DialogInterface) -> Unit {
                override fun invoke(dialog: DialogInterface) {
                    dialog.dismiss()
                    rentalPostingViewModel.updateStep(3)
                }
            })
        }

    }


}