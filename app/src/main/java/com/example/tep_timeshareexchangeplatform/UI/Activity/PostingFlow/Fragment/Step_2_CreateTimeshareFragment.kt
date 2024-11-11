package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

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
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.AmenitiesAdaper
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.ImageUploadAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.UnitTypeAdapterPosting
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
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
    private val postingFlowViewModel: PostingFlowViewModel by activityViewModels()


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
        routeRoomDistribution()
        setEventSaveAmenities()
        return binding.root
    }
    private fun observeViewModel() {
        // Tracking Data of Step Create Timeshare
        postingFlowViewModel.stepCreateTimeshare.observe(viewLifecycleOwner) { currentTask ->
            showStepEventHandle(currentTask)
        }

        // API Value - Resort Model Selected
        postingFlowViewModel.resortModelResponse.observe(viewLifecycleOwner) { resortModel ->
            bindDataResort(resortModel)
            if (resortModel != null) {
                // Show Progress Step 1
                postingFlowViewModel.updateTaskProgress(1)
                postingFlowViewModel.resetDateRange()

                // Call API to get Room List
                val token = TokenManager(requireContext()).getAccessToken()
                if (token != null) {
                    postingFlowViewModel.getRoomListByResortId(token, resortModel.id)
                    postingFlowViewModel.getUnitTypeListByResortId(token, resortModel.id)
                } else {
                    Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // API Value - Unit Type List By Resort Id
        postingFlowViewModel.unitTypeList.observe(viewLifecycleOwner) { listUnitType ->
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

        // API Value - Room List By Resort Id
        postingFlowViewModel.roomList.observe(viewLifecycleOwner) { roomList ->
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

        // API Value - Unit Type Detail By UnitTypeId
        postingFlowViewModel.unitTypeDetail.observe(viewLifecycleOwner) { unitType ->
            when (unitType.status) {
                Status.LOADING -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.GONE
                    bindDataUnitTypeYesOption(unitType.data!!)
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



        // Tracking Date Range
        postingFlowViewModel.dateRange.observe(viewLifecycleOwner) { dateRange ->
            if (postingFlowViewModel.getNumberOfNights() > 0) {
                // Update Step 4
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlDayCheckIn.top)
                }
                postingFlowViewModel.updateTaskProgress(4)
            } else binding.crlContentAmenities.visibility = View.GONE
        }

        // Create Timeshare
        postingFlowViewModel.timeshareDTO.observe(viewLifecycleOwner) { timeshareDTO ->
            when (timeshareDTO.status) {
                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "${timeshareDTO.status}",
                        "${timeshareDTO.data}",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                    );
                    postingFlowViewModel.updateTaskProgress(1)
                    postingFlowViewModel.updateStep(3)
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).showFailedDialog(
                        requireContext(),
                        "${timeshareDTO.message}",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                binding.scrollView.post {
                                    binding.scrollView.smoothScrollTo(0, binding.crlDayCheckIn.top)
                                }
                            }

                        }
                    )
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                }
            }
        }

        // Create Room
        postingFlowViewModel.roomModel.observe(viewLifecycleOwner) { roomModel ->
            when (roomModel.status) {
                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    val token = TokenManager(requireContext()).getAccessToken()
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val startDateFormatted =
                        postingFlowViewModel.dateRange.value?.first?.let { dateFormatter.format(it) }
                            ?: ""
                    val endDateFormatted =
                        postingFlowViewModel.dateRange.value?.second?.let {
                            dateFormatter.format(
                                it
                            )
                        }
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
                        postingFlowViewModel.postTimeshareDTO(token, timeshareDTO)
                    } else {
                        Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                    }
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).showErrorDialog(
                        "${roomModel.message}",
                        "Back"
                    )
                    (activity as PostingFlowActivity).hideLoadingWaiting()
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

    // 1. Binding Data of Resort
    private fun bindDataResort(resortModelResponse: ResortModelResponse.Content) {
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

    // 2. Binding Data of Room Distribution, Yes or No
    private fun routeRoomDistribution() {
        // Yes
        binding.btnYesRoomDistribution.setOnClickListener {
            binding.includeUnitTypeYes.root.visibility = View.VISIBLE
            binding.includeUnitTypeNo.root.visibility = View.GONE

            postingFlowViewModel.updateTaskProgress(2)
            postingFlowViewModel.updateIsYesOrNo(true)

            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeYes.root.top)
            }
        }

        // No
        binding.btnNoRoomDistribution.setOnClickListener {
            binding.includeUnitTypeYes.root.visibility = View.GONE
            binding.includeUnitTypeNo.root.visibility = View.VISIBLE

            postingFlowViewModel.updateTaskProgress(2)
            postingFlowViewModel.updateIsYesOrNo(false)

            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeNo.root.top)
            }
        }

    }
    private fun bindDataUnitTypeDetailDialog(unitType: UnitTypeModel) {
        val unitTypeDetail = CustomDialog(requireContext())
        unitTypeDetail.show()
    }
    // Yes. User Have Room Type
    private fun bindDataUnitTypeYesOption(unitType: UnitTypeModel) {
        val binding = binding.includeUnitTypeYes
        // Hide Unnecessary View
        binding.includeItemUnitType.llAmennities.visibility = View.GONE
        binding.includeItemUnitType.tvPrice.visibility = View.GONE


        // Bind data to item unit type
        binding.includeItemUnitType.root.visibility = View.VISIBLE
        binding.includeItemUnitType.apply {
            tvRoomName.text = "Loại Phòng: " + unitType.title
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
    private fun bindDataSpinner(roomList: List<RoomModel>?) {
        val spinnerBinding = binding.includeUnitTypeYes

        // Thêm mục mặc định vào danh sách
        val roomDisplayList: List<String> = listOf("Chọn Mã Phòng") +
                (roomList?.map { "Mã Phòng: ${it.roomInfoCode}" } ?: emptyList())

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomDisplayList)
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
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
                    if (position == 0) {
                        // Nếu người dùng chọn "Chọn Mã Phòng", không làm gì
                        binding.includeUnitTypeYes.includeItemUnitType.root.visibility = View.GONE
                        binding.includeUnitTypeYes.tvRoomName.text = ""
                        binding.includeUnitTypeYes.tvRoomCode.text = ""
                        postingFlowViewModel.updateTaskProgress(2)
                        return
                    }

                    binding.scrollView.post {
                        binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeYes.root.top)
                    }
                    postingFlowViewModel.updateTaskProgress(3)
                    postingFlowViewModel.updateCurrentRoomInfo(roomList?.get(position - 1)!!.id)

                    // Lấy RoomModel tương ứng từ roomList dựa trên position đã chọn
                    val selectedRoom = roomList?.get(position - 1)

                    binding.includeUnitTypeYes.tvRoomName.text =
                        selectedRoom?.roomInfoName ?: "Unknown Name"
                    binding.includeUnitTypeYes.tvRoomCode.text =
                        selectedRoom?.roomInfoCode ?: "Unknown Code"

                    // Lấy thông tin id của RoomModel tương ứng
                    val unitTypeID = selectedRoom?.unitTypeId

                    // Call API to get Unit Type Detail
                    val token = TokenManager(requireContext()).getAccessToken()
                    if (token != null && unitTypeID != null) {
                        postingFlowViewModel.getUnitTypeDetail(token, unitTypeID)
                    } else {
                        MotionToast.createColorToast(
                            requireActivity(),
                            "Error",
                            "Token is null",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có gì được chọn
                }
            }
    }
    // No. User Don't Have Room Type, Create New Room
    private fun bindDataUnitTypeNoOption(listUnitType: List<UnitTypeModel>) {
        // Lấy danh sách duy nhất cho view và bedrooms
        val viewList = listUnitType.map { it.view }.distinct()
        val bedroomsList = listUnitType.map { it.bedrooms.toString() }.distinct()

        // Gán dữ liệu cho customSpinnerViewDiretion
        val viewAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewList)
        viewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.includeUnitTypeNo.customSpinnerViewDiretion.adapter = viewAdapter

        // Gán dữ liệu cho customSpinnerBed
        val bedroomsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bedroomsList)
        bedroomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.includeUnitTypeNo.customSpinnerBed.adapter = bedroomsAdapter

        // Lọc dữ liệu dựa trên giá trị được chọn
        binding.includeUnitTypeNo.customSpinnerViewDiretion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterUnitTypes(listUnitType)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không làm gì nếu không có gì được chọn
                }
            }

        binding.includeUnitTypeNo.customSpinnerBed.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterUnitTypes(listUnitType)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không làm gì nếu không có gì được chọn
                }
            }

        // Hiển thị danh sách ban đầu
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
            postingFlowViewModel.updateUnitTypeSelectionOptionNo(it)
        }

        binding.includeUnitTypeNo.btnSaveRoomInfo.setOnClickListener {
            if (!verifyDataAndSendRequest()) {
                MotionToast.createColorToast(
                    requireActivity(),
                    "Error",
                    "Please fill in all required fields",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                )
                return@setOnClickListener
            }

            if (postingFlowViewModel.unitTypeSelectionOptionNo.value == null) {
                MotionToast.createColorToast(
                    requireActivity(),
                    "Error",
                    "Please select a room type",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
                )
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeNo.rvUnitType.top)
                }
                return@setOnClickListener
            }


            postingFlowViewModel.updateTaskProgress(3)
            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, binding.crlDayCheckIn.top)
            }

        }
    }
    private fun filterUnitTypes(listUnitType: List<UnitTypeModel>) {
        val selectedView =
            binding.includeUnitTypeNo.customSpinnerViewDiretion.selectedItem?.toString()
        val selectedBedrooms =
            binding.includeUnitTypeNo.customSpinnerBed.selectedItem?.toString()?.toIntOrNull()

        val filteredList = listUnitType.filter {
            (selectedView == null || it.view == selectedView) &&
                    (selectedBedrooms == null || it.bedrooms == selectedBedrooms)
        }

        unitTypeAdapterPosting.submitList(filteredList)
        if (filteredList.isEmpty() || filteredList.size == 0) {
            binding.includeUnitTypeNo.lottieAnimationView.visibility = View.VISIBLE
        } else {
            binding.includeUnitTypeNo.lottieAnimationView.visibility = View.GONE
        }
    }


    // 3. Display Day Check In - Check Out
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
                    postingFlowViewModel.setDateRange(startDate, endDate)
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


    // 4. Display Amenities
    private fun bindDataAmenities(unitType: UnitTypeModel) {

    }
    private fun setEventSaveAmenities() {
        binding.btnSaveAmenities.setOnClickListener {
            postingFlowViewModel.updateTaskProgress(5)
        }

    }


    // 5. Display Image Upload
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
                        postingFlowViewModel.updateResortModel(it)
                    }
                }
            }

    }
    fun openGallery() {
        pickImagesLauncher.launch("image/*")
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


    // Send Request
    private fun sendRequestCreateTimeshare() {
        binding.btnNext.setOnClickListener {
            val checkYN: Boolean = postingFlowViewModel.isYesOrNoSelected.value!!
            if (checkYN) {
                sendRequestOptionYes()
            } else {
                // Call API to create Room
                val token = TokenManager(requireContext()).getAccessToken()
                val roomDTO: RoomDTO = RoomDTO(
                    roomInfoCode = binding.includeUnitTypeNo.edtRoomCode.text.toString(),
                    isActive = true,
                    resortId = postingFlowViewModel.resortModelResponse.value?.id!!,
                    status = "Available",
                    unitTypeId = postingFlowViewModel.unitTypeSelectionOptionNo.value?.id!!,
                    roomName = binding.includeUnitTypeNo.edtRoomName.text.toString(),
                    roomAmenities = listOf()
                )
                if (token != null) {
                    postingFlowViewModel.postRoom(token, roomDTO)
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
            postingFlowViewModel.dateRange.value?.first?.let { dateFormatter.format(it) }
                ?: ""
        val endDateFormatted =
            postingFlowViewModel.dateRange.value?.second?.let { dateFormatter.format(it) }
                ?: ""
        val timeshareDTO = TimeshareDTO(
            status = "Available",
            startYear = 2024,
            endYear = 2025,
            startDate = startDateFormatted,
            endDate = endDateFormatted,
            roomInfoId = postingFlowViewModel.currentRoomInfo.value!!
        )
        Log.d("CheckTImesahreModel", "sendRequestCreateTimeshare: $timeshareDTO")
        if (token != null && timeshareDTO != null) {
            postingFlowViewModel.postTimeshareDTO(token, timeshareDTO)
        } else {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    // Order of the steps
    private fun showStepEventHandle(currentTask: Int) {
        when (currentTask) {
            1 -> {
                binding.crlRoomDistribution.visibility = View.VISIBLE
                binding.includeUnitTypeYes.root.visibility = View.GONE
                binding.includeUnitTypeNo.root.visibility = View.GONE
                binding.crlDayCheckIn.visibility = View.GONE
                binding.crlContentAmenities.visibility = View.GONE
                binding.crlContentImage.visibility = View.GONE
                binding.btnNext.visibility = View.GONE

            }

            2 -> {
                binding.crlDayCheckIn.visibility = View.GONE
                binding.crlContentAmenities.visibility = View.GONE
                binding.crlContentImage.visibility = View.GONE
                binding.btnNext.visibility = View.GONE
            }

            3 -> {
                binding.crlDayCheckIn.visibility = View.VISIBLE
            }

            4 -> {
                binding.crlContentAmenities.visibility = View.VISIBLE
                val checkYN: Boolean = postingFlowViewModel.isYesOrNoSelected.value!!
                if (checkYN) {
                    binding.btnSaveAmenities.visibility = View.VISIBLE
                } else {
                    binding.btnSaveAmenities.visibility = View.GONE
                    postingFlowViewModel.updateTaskProgress(5)

                }
            }

            5 -> {
                binding.crlContentImage.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlContentImage.top)
                }
            }
        }
    }
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
    private fun verifyDataAndSendRequest(): Boolean {
        val checkYN: Boolean = postingFlowViewModel.isYesOrNoSelected.value ?: false

        if (!checkYN) {
            val roomCode = binding.includeUnitTypeNo.edtRoomCode.text.toString().trim()
            val roomName = binding.includeUnitTypeNo.edtRoomName.text.toString().trim()

            if (roomCode.isEmpty()) {
                binding.includeUnitTypeNo.edtRoomCode.error = "Mã phòng không được để trống"
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeNo.edtRoomCode.top)
                }
                return false
            }

            if (roomName.isEmpty()) {
                binding.includeUnitTypeNo.edtRoomName.error = "Tên phòng không được để trống"
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeNo.edtRoomCode.top)
                }
                return false
            }
        }

        // Nếu tất cả các kiểm tra đều vượt qua
        return true
    }




}