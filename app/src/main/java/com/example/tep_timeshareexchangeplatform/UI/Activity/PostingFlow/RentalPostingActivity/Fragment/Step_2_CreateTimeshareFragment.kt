package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter.AmenitiesAdaper
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter.ImageUploadAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Adapter.UnitTypeAdapterPosting
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Custom.CustomDialog
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimeshareBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Step_2_CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare) {

    private lateinit var binding: FragmentCreateTimeshareBinding
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>
    private var unitTypeAdapterPosting = UnitTypeAdapterPosting(true)
    private var amenitiesAdapter = AmenitiesAdaper()
    private var amenitiesEntertamentAdapter = AmenitiesAdaper()
    private var imageUploadAdapter = ImageUploadAdapter()
    private var policyAmentitiesAdapter = AmenitiesAdaper()
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()


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
        getImageFromGallery()
        sendRequestCreateTimeshare()
        setEventChangeDate()
        nextStepHandle()
        return binding.root
    }

    private fun observeViewModel() {
        // Tracking Data of Step Create Timeshare
        rentalPostingViewModel.stepCreateTimeshare.observe(viewLifecycleOwner) { currentTask ->
            showStepEventHandle(currentTask)
        }

        // Bind Data of Location Model
        rentalPostingViewModel.resortModelResponse.observe(viewLifecycleOwner) { resortModel ->
            bindDataResortLocation(resortModel)
            if (resortModel != null) {
                // Show Progress Step 1
                rentalPostingViewModel.updateTaskProgress(1)
                rentalPostingViewModel.resetDateRange()

                // Call API to get Room List
                val token = TokenManager(requireContext()).getAccessToken()
                if (token != null) {
                    rentalPostingViewModel.getRoomListByResortId(token, resortModel.id)
                    rentalPostingViewModel.getUnitTypeListByResortId(token, resortModel.id)
                } else {
                    Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tracking Data of Room List
        rentalPostingViewModel.roomList.observe(viewLifecycleOwner) { roomList ->
            when (roomList.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    bindDataSpinner(roomList.data)
                }

                Status.ERROR -> {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "${roomList.status}",
                        "${roomList.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                    );
                }
            }
        }

        // Tracking Data of Unit Type
        rentalPostingViewModel.unitTypeDetail.observe(viewLifecycleOwner) { unitType ->
            when (unitType.status) {
                Status.LOADING -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.GONE
                    bindDataUnitTypeYesOption(unitType.data!!)

                    // Open Step 3
                    rentalPostingViewModel.updateTaskProgress(3)
                }

                Status.ERROR -> {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "${unitType.status}",
                        "${unitType.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                    );
                }
            }

        }

        // Tracking Data List Unit Type
        rentalPostingViewModel.unitTypeList.observe(viewLifecycleOwner) { listUnitType ->
            when (listUnitType.status) {
                Status.LOADING -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.GONE
                    bindDataUnitTypeNoOption(listUnitType.data!!)
                }

                Status.ERROR -> {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "${listUnitType.status}",
                        "${listUnitType.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                    );
                }
            }
        }

        // Tracking Date Range
        rentalPostingViewModel.dateRange.observe(viewLifecycleOwner) { dateRange ->
            if (rentalPostingViewModel.getNumberOfNights() > 0) {
                // Update Step 4
                rentalPostingViewModel.updateTaskProgress(4)
                rentalPostingViewModel.updateTaskProgress(5)
            } else binding.crlContentAmenities.visibility = View.GONE
        }

        // Tracking Data of Post Timeshare
        rentalPostingViewModel.timeshareDTO.observe(viewLifecycleOwner) { timeshareDTO ->
            when (timeshareDTO.status) {
                Status.LOADING -> {
                    (activity as RentalPostingActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "${timeshareDTO.status}",
                        "${timeshareDTO.data}",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                    );
                    rentalPostingViewModel.updateStep(3)
                }

                Status.ERROR -> {
                    (activity as RentalPostingActivity).showErrorDialog(
                        "${timeshareDTO.message}",
                        "Back"
                    )
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                }
            }
        }

        // Tracking Create Timeshare Progress
        rentalPostingViewModel.roomModel.observe(viewLifecycleOwner) { roomModel ->
            when (roomModel.status) {
                Status.LOADING -> {
                    (activity as RentalPostingActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    val token = TokenManager(requireContext()).getAccessToken()
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val startDateFormatted =
                        rentalPostingViewModel.dateRange.value?.first?.let { dateFormatter.format(it) }
                            ?: ""
                    val endDateFormatted =
                        rentalPostingViewModel.dateRange.value?.second?.let { dateFormatter.format(it) }
                            ?: ""
                    val timeshareDTO = TimeshareDTO(
                        status = "Available",
                        startYear = 2024,
                        endYear = 2025,
                        startDate = startDateFormatted,
                        endDate = endDateFormatted,
                        roomInfoId = roomModel.data!!.roomId
                    )
                    if (token != null) {
                        rentalPostingViewModel.postTimeshareDTO(token, timeshareDTO)
                    } else {
                        Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                    }
                }

                Status.ERROR -> {
                    (activity as RentalPostingActivity).showErrorDialog(
                        "${roomModel.message}",
                        "Back"
                    )
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                }
            }
        }


    }

    private fun initAdapter() {
        unitTypeAdapterPosting.submitList(listOf())
        amenitiesAdapter.submitList(Constant.listAmenities)
        amenitiesEntertamentAdapter.submitList(Constant.listEntertament)
        policyAmentitiesAdapter.submitList(Constant.listPolicy)
        imageUploadAdapter.submitList(listOf())
        imageUploadAdapter.onDeleteClick = {
            imageUploadAdapter.removeItem(it)
        }

    }

    // Function to update the visibility of tasks based on progress
    private fun showStepEventHandle(currentTask: Int) {
        when (currentTask) {
            1 -> {
                binding.crlRoomDistribution.visibility = View.VISIBLE
                binding.includeUnitTypeYes.root.visibility = View.GONE
                binding.crlDayCheckIn.visibility = View.GONE
                binding.crlContentAmenities.visibility = View.GONE
                binding.crlContentImage.visibility = View.GONE
                binding.btnNext.visibility = View.GONE

            }

            2 -> {
                binding.crlDayCheckIn.visibility = View.VISIBLE
                binding.crlContentAmenities.visibility = View.GONE
                binding.crlContentImage.visibility = View.GONE
                binding.btnNext.visibility = View.GONE
            }

            3 -> {
                binding.crlDayCheckIn.visibility = View.VISIBLE
            }

            4 -> {
                binding.crlContentAmenities.visibility = View.VISIBLE
            }

            5 -> {
                binding.crlContentImage.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
        }
    }

    private fun nextStepHandle() {
        binding.btnYesRoomDistribution.setOnClickListener {
            binding.includeUnitTypeYes.root.visibility = View.VISIBLE
            binding.includeUnitTypeNo.root.visibility = View.GONE

            rentalPostingViewModel.updateTaskProgress(2)
            rentalPostingViewModel.updateIsYesOrNo(true)
        }

        binding.btnNoRoomDistribution.setOnClickListener {
            binding.includeUnitTypeYes.root.visibility = View.GONE
            binding.includeUnitTypeNo.root.visibility = View.VISIBLE

            rentalPostingViewModel.updateTaskProgress(2)
            rentalPostingViewModel.updateIsYesOrNo(false)
        }

    }

    // Binding Data of Resort Location
    private fun bindDataResortLocation(resortModelResponse: ResortModelResponse.Content) {
        if (resortModelResponse != null) {
            binding.let {
                it.tvResortName.text = resortModelResponse.resortName
                it.tvLocation.text = resortModelResponse.address
                Glide.with(requireContext())
                    .load(resortModelResponse.logo)
                    .placeholder(R.drawable.ripple_effect)
                    .into(it.ivResortImage)
                binding.llResortLocation.visibility = View.VISIBLE
                binding.btnSelectResortLocation.visibility = View.GONE
            }
        } else {
            binding.llResortLocation.visibility = View.GONE
            binding.btnSelectResortLocation.visibility = View.VISIBLE
        }
    }

    private fun bindDataSpinner(roomList: List<RoomModel>?) {

        val spinnerBinding = binding.includeUnitTypeYes
        val roomDisplayList: List<String> = roomList
            ?.map { "Phòng: ${it.roomInfoName}, Code: ${it.roomInfoCode}" ?: "Unknown Room" }
            ?: emptyList()

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomDisplayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBinding.spUnitType.adapter = adapter

        // Xử lý sự kiện khi chọn một item trong Spinner
        spinnerBinding.spUnitType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Lấy RoomModel tương ứng từ roomList dựa trên position đã chọn
                    val selectedRoom = roomList?.get(position)

                    // Lấy thông tin id của RoomModel tương ứng
                    val unitTypeID = selectedRoom?.unitTypeId

                    // Call API to get Unit Type Detail
                    val token = TokenManager(requireContext()).getAccessToken()
                    if (token != null && unitTypeID != null) {
                        rentalPostingViewModel.getUnitTypeDetail(token, unitTypeID)
                    } else {
                        MotionToast.Companion.createColorToast(
                            requireActivity(),
                            "Error",
                            "Token is null",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                        );
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có gì được chọn
                }
            }
    }

    private fun bindDataUnitTypeYesOption(unitType: UnitTypeModel) {
        val binding = binding.includeUnitTypeYes
        // Hide Unnecessary View
        binding.rvUnitType.visibility = View.GONE
        binding.includeItemUnitType.llAmennities.visibility = View.GONE
        binding.includeItemUnitType.tvPrice.visibility = View.GONE


        // Bind data to item unit type
        binding.includeItemUnitType.root.visibility = View.VISIBLE
        binding.includeItemUnitType.apply {
            tvRoomName.text = unitType.title
            tvNumBathroom.text = unitType.bathrooms.toString()
            tvNumKitchen.text = 1.toString()
            tvKitchen.text = unitType.kitchen
            tvNumBed.text = "${unitType.bedrooms}"
            tvBed.text =
                "${unitType.bedsQueen} Queen, ${unitType.bedsKing} King, ${unitType.bedsTwin} Twin"
            tvNumPerson.text = unitType.sleeps.toString()
            /* Glide.with(requireContext())
                 .load(unitType.photos)
                 .placeholder(R.drawable.ripple_effect)
                 .into(imRoomTypeImage)*/
        }

        binding.includeItemUnitType.btnViewRoom.setOnClickListener {
            bindDataUnitTypeDetailDialog(unitType)
        }

    }

    private fun bindDataUnitTypeNoOption(listUnitType: List<UnitTypeModel>) {
        unitTypeAdapterPosting.submitList(listUnitType)
        binding.includeUnitTypeNo.rvUnitType.apply {
            visibility = View.VISIBLE
            adapter = unitTypeAdapterPosting
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        unitTypeAdapterPosting.onButtonBookClick = {
            bindDataUnitTypeDetailDialog(it)
        }

        unitTypeAdapterPosting.onItemClick = { it ->
            rentalPostingViewModel.updateUnitTypeSelectionOptionNo(it)
        }
    }

    private fun bindDataUnitTypeDetailDialog(unitType: UnitTypeModel) {
        val unitTypeDetail = CustomDialog(requireContext())
        unitTypeDetail.show()
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


        // Set List Amenities
        binding.rvAmenities.apply {
            adapter = amenitiesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Amenities Entertament
        binding.rvAmenitiesEntertainment.apply {
            adapter = amenitiesEntertamentAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Policy
        binding.rvPolicy.apply {
            adapter = policyAmentitiesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
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
                    val startDayString =
                        SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(startDate))
                    binding.tvCheckInDayOfWeek.text = startDayString

                    // Get day of the week for end date
                    val endDayString =
                        SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(endDate))
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
                    val selectedLocation: ResortModelResponse.Content? =
                        data?.getParcelableExtra(Constant.DEFAULT_RESORT_SEARCHED_SELECTION)
                    selectedLocation?.let {
                        rentalPostingViewModel.updateResortModel(it)
                    }
                }
            }

    }

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
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
            val checkYN: Boolean = rentalPostingViewModel.isYesOrNoSelected.value!!
            if (checkYN) {
                sendRequestOptionYes()
            } else {
                // Call API to create Room
                val token = TokenManager(requireContext()).getAccessToken()
                val roomDTO: RoomDTO = RoomDTO(
                    roomInfoCode = binding.includeUnitTypeNo.edtRoomCode.text.toString(),
                    isActive = true,
                    resortId = rentalPostingViewModel.resortModelResponse.value?.id!!,
                    status = "Available",
                    unitTypeId = rentalPostingViewModel.unitTypeSelectionOptionNo.value?.id!!,
                    roomName = binding.includeUnitTypeNo.edtRoomName.text.toString(),
                    roomAmenities = listOf()
                )
                if (token != null) {
                    rentalPostingViewModel.postRoom(token, roomDTO)
                } else {
                    Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                }






            }
        }

    }
    private fun sendRequestOptionYes() {
        val token = TokenManager(requireContext()).getAccessToken()
        // Định dạng theo kiểu yyyy-MM-dd
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val startDateFormatted =
            rentalPostingViewModel.dateRange.value?.first?.let { dateFormatter.format(it) }
                ?: ""
        val endDateFormatted =
            rentalPostingViewModel.dateRange.value?.second?.let { dateFormatter.format(it) }
                ?: ""
        val timeshareDTO = TimeshareDTO(
            status = "Available",
            startYear = 2024,
            endYear = 2025,
            startDate = startDateFormatted,
            endDate = endDateFormatted,
            roomInfoId = rentalPostingViewModel.unitTypeDetail.value?.data?.id!!
        )
        Log.d("CheckTImesahreModel", "sendRequestCreateTimeshare: $timeshareDTO")
        if (token != null) {
            rentalPostingViewModel.postTimeshareDTO(token, timeshareDTO)
        } else {
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
        }
    }


}