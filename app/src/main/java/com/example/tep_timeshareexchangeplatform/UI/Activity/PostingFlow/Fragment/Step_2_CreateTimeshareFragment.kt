package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.UnitTypeDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.AmenitiesAdaper
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.RoomResultAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.UnitTypeAdapterPosting
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimeshareBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimesharePostingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar


class Step_2_CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare_posting) {

    private lateinit var binding: FragmentCreateTimesharePostingBinding
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>
    private var unitTypeAdapterPosting = UnitTypeAdapterPosting(true)
    private var amenitiesAdapter = AmenitiesAdaper()
    private var amenitiesEntertamentAdapter = AmenitiesAdaper()
    private var policyAmentitiesAdapter = AmenitiesAdaper()
    private val postingFlowViewModel: PostingFlowViewModel by activityViewModels()

    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        tokenManager = TokenManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTimesharePostingBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        observeViewModel()
        initActivityLauncher()

        // Event CLick
        eventClickSelectResort()
        eventClickChangeDay()

        return binding.root
    }

    private fun observeViewModel() {
        // KEY FUNCTION
        // Tracking Step And Show UI of Step
        postingFlowViewModel.stepCreateTimeshare.observe(viewLifecycleOwner) { currentTask ->
            changeUIBaseOnStep(currentTask)
        }

        // Step 0- Get Resort Info
        postingFlowViewModel.resortModelResponse.observe(viewLifecycleOwner) { resortModel ->
            if (resortModel != null) {
                // Show Progress Step 1
                postingFlowViewModel.updateTaskProgress(1)
                bindDataResort(resortModel)

                // Call API to get Room List
                if (tokenManager != null && tokenManager.isLoggedIn()) {
                    postingFlowViewModel.getRoomListByResortId(
                        tokenManager.getAccessToken().toString(), resortModel.id
                    )
                    postingFlowViewModel.getUnitTypeListByResortId(
                        tokenManager.getAccessToken().toString(), resortModel.id
                    )
                } else {
                    Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Step 1 - Get Room List By Resort Id
        // YES
        postingFlowViewModel.roomList.observe(viewLifecycleOwner) { roomList ->
            when (roomList.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    bindDataRoomCodeSpinner(roomList.data)
                    bindDataRoomCodeBySearch(roomList.data)
                }

                Status.ERROR -> {
                    showErrorToast("${roomList.message}")
                }
            }
        }
        postingFlowViewModel.unitTypeDetail.observe(viewLifecycleOwner) { unitType ->
            when (unitType.status) {
                Status.LOADING -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.GONE
                    bindDataUnitTypeYesOption(unitType.data!!)
                    postingFlowViewModel.updateTaskProgress(3)
                }

                Status.ERROR -> {
                    showErrorToast("${unitType.message}")
                }
            }

        }

        // Step 2 -


        /*// Tracking Data of Step Create Timeshare
        postingFlowViewModel.stepCreateTimeshare.observe(viewLifecycleOwner) { currentTask ->
            showStepEventHandle(currentTask)
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
                    showErrorToast("${listUnitType.message}")
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
                    showErrorToast("${roomList.message}")
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
                    showSuccessToast("Create Timeshare Success")
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
        }*/


    }

    /**
     * BASE CONFIG LOGIC
     *
     * A
     * Show Step Event Handle,
     * TRACKING STEP OF CREATE TIMESHARE
     * CHANGE UI BASE ON STEP
     *
     * B
     * INIT ACTIVITY LAUNCHER
     * GET VALUE OF RESORT
     */
    private fun changeUIBaseOnStep(currentTask: Int) {
        when (currentTask) {
            1 -> {
                // Off UI Resort Selection
                binding.llSelectResortLocationContainer.visibility = View.GONE
                binding.includeUnitTypeNo.root.visibility = View.GONE
                binding.crlDayCheckIn.visibility = View.GONE
                binding.crlContentAmenities.visibility = View.GONE
                binding.btnNext.visibility = View.GONE

                // Show UI Step 1
                binding.includeUnitTypeYes.root.visibility = View.VISIBLE
                binding.llSelectRoomContainer.visibility = View.VISIBLE


            }

            2 -> {
                binding.crlDayCheckIn.visibility = View.GONE
                binding.crlContentAmenities.visibility = View.GONE
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
                binding.btnNext.visibility = View.VISIBLE
            }
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


    /**
     * 0. Step 0 - Get Resort Info
     */
    private fun eventClickSelectResort() {
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
    /**
     * 2. Step 2 - Get Room List By Resort Id
     */
    private fun eventClickChangeDay() {
        binding.llCheckInCheckOutDate.setOnClickListener {
            showStylishYearPickerDialog(requireContext()) { selectedYear ->
                // Xử lý năm được chọn
                Toast.makeText(requireContext(), "Năm được chọn: $selectedYear", Toast.LENGTH_SHORT).show()
            }



        }
    }


    /**
     * BINDING DATA FUNTION
     *
     *
     */
    // 0. Binding Data of Resort
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
    // 1. Binding Data of Room Distribution, Yes or No
    // YES
    private fun bindDataRoomCodeSpinner(roomList: List<RoomModel>?) {
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
                    postingFlowViewModel.updateTaskProgress(2)
                    postingFlowViewModel.updateCurrentRoomInfo(roomList?.get(position - 1)!!.id)
                    spinnerBinding.etRoomSearch.setText("")
                    // Lấy RoomModel tương ứng từ roomList dựa trên position đã chọn
                    val selectedRoom = roomList?.get(position - 1)

                    binding.includeUnitTypeYes.tvRoomName.text =
                        selectedRoom?.roomInfoName ?: "Unknown Name"
                    binding.includeUnitTypeYes.tvRoomCode.text =
                        selectedRoom?.roomInfoCode ?: "Unknown Code"

                    // Lấy thông tin id của RoomModel tương ứng
                    val unitTypeID = selectedRoom?.unitTypeId
                    callGetUnitTypeDetailByID(unitTypeID!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có gì được chọn
                }
            }
    }
    private fun bindDataRoomCodeBySearch(roomList: List<RoomModel>?) {
        val bindingInclude = binding.includeUnitTypeYes
        bindingInclude.etRoomSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.llSelectRoomContainer.top)
                }
            }
        }
        bindingInclude.etRoomSearch.setOnClickListener {
            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, binding.llSelectRoomContainer.top)
            }
        }
        // Binding Data For Search Room
        // Adapter cho RecyclerView
        val adapter = RoomResultAdapter()
        bindingInclude.rvRoomResults.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        adapter.onItemClick = {
            postingFlowViewModel.updateCurrentRoomInfo(it.id)
            callGetUnitTypeDetailByID(it.unitTypeId)
            bindingInclude.llRoomSearchResults.visibility = View.GONE
            bindingInclude.tvRoomName.text = it?.roomInfoName ?: "Unknown Name"
            bindingInclude.tvRoomCode.text = it?.roomInfoCode ?: "Unknown Code"
            bindingInclude.etRoomSearch.clearFocus()
            postingFlowViewModel.updateTaskProgress(2)

            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(bindingInclude.etRoomSearch.windowToken, 0)
        }


        bindingInclude.llRoomSearchResults.visibility = View.GONE
        bindingInclude.etRoomSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Không cần xử lý ở đây
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()

                // Lọc danh sách dựa trên từ khóa
                val filteredList =
                    roomList?.filter { it.roomInfoCode.contains(query, ignoreCase = true) }
                        ?: emptyList()

                // Cập nhật danh sách cho RecyclerView
                adapter.submitList(filteredList)

                if (query.length == 0) {
                    bindingInclude.etRoomSearch.clearFocus()
                }

                // Hiển thị RecyclerView nếu có kết quả, ngược lại ẩn và hiển thị thông báo
                if (filteredList.isNotEmpty() && query.isNotEmpty() && query.length > 0) {
                    bindingInclude.llRoomSearchResults.visibility = View.VISIBLE
                } else {
                    bindingInclude.llRoomSearchResults.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Không cần xử lý ở đây
            }
        })
    }
    private fun bindDataUnitTypeYesOption(unitType: UnitTypeModel) {
        val binding = binding.includeUnitTypeYes
        // Hide Unnecessary View
        binding.includeItemUnitType.llAmennities.visibility = View.GONE
        binding.includeItemUnitType.tvPrice.visibility = View.GONE
        binding.includeItemUnitType.btnViewRoom.setOnClickListener {
            val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitType)
            unitTypeDataDialog.show(parentFragmentManager, "UnitTypeDataDialog")
        }


        // Bind data to item unit type
        binding.includeItemUnitType.root.visibility = View.VISIBLE
        binding.llUnityTypeDetail.visibility = View.VISIBLE
        binding.includeItemUnitType.apply {
            tvRoomName.text = "Loại Phòng: " + unitType.title
            tvNumBathroom.text = unitType.bathrooms.toString()
            tvNumKitchen.text = 1.toString()
            tvKitchen.text = unitType.kitchen
            tvNumBed.text = "${unitType.bedrooms}"
            tvBed.text =
                "${unitType.bedsQueen} Queen, ${unitType.bedsKing} King, ${unitType.bedsTwin} Twin"
            tvNumPerson.text = unitType.sleeps.toString()

            Glide.with(requireContext())
                .load(unitType.photos)
                .placeholder(R.drawable.ripple_effect)
                .into(imRoomTypeImage)

        }

        /*binding.includeItemUnitType.btnViewRoom.setOnClickListener {
            bindDataUnitTypeDetailDialog(unitType)
        }*/

    }


    /**
     * CALL API FUNTION
     *
     *
     */
    // 1. Call API Get Room Info by id
    private fun callGetUnitTypeDetailByID(int: Int) {
        postingFlowViewModel.getUnitTypeDetailByID(tokenManager.getAccessToken().toString(), int)
    }


    private fun initAdapter() {
        unitTypeAdapterPosting.submitList(listOf())
        amenitiesAdapter.submitList(Constant.listAmenities)
        amenitiesEntertamentAdapter.submitList(Constant.listEntertament)
        policyAmentitiesAdapter.submitList(Constant.listPolicy)
    }
    /*



  
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
                showInfoToast("Please fill in all required fields")
                return@setOnClickListener
            }

            if (postingFlowViewModel.unitTypeSelectionOptionNo.value == null) {
                showErrorToast("Please select a unit type")
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

    private fun callRequestCreateTimeshare() {
        binding.btnNext.setOnClickListener {
            val checkYN: Boolean = postingFlowViewModel.isYesOrNoSelected.value!!
            if (checkYN) {
                sendRequestOptionYes()
            } else {
                // Call API to create Room
                if (!tokenManager.isLoggedIn()) {
                    Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val roomDTO: RoomDTO = RoomDTO(
                    roomInfoCode = binding.includeUnitTypeNo.edtRoomCode.text.toString(),
                    isActive = true,
                    resortId = postingFlowViewModel.resortModelResponse.value?.id!!,
                    status = "Available",
                    unitTypeId = postingFlowViewModel.unitTypeSelectionOptionNo.value?.id!!,
                    roomName = binding.includeUnitTypeNo.edtRoomName.text.toString(),
                    roomAmenities = listOf()
                )
                postingFlowViewModel.postRoom(tokenManager.getAccessToken().toString(), roomDTO)

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
            endYear = 2027,
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

    // Location Activity Result
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
*/

    private fun showErrorToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Error",
            string,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
        )
    }

    private fun showSuccessToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Success",
            string,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
        )
    }

    private fun showInfoToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Failed",
            string,
            MotionToastStyle.INFO,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
        )
    }

    fun showStylishYearPickerDialog(
        context: Context,
        initialYear: Int = 2023,
        onYearSelected: (Int) -> Unit
    ) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        val numberPicker = NumberPicker(context).apply {
            minValue = currentYear
            maxValue = 2100
            value = initialYear.coerceIn(minValue, maxValue)
            wrapSelectorWheel = false  // Tắt vòng lặp
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Chọn năm")
            .setView(numberPicker)
            .setPositiveButton("OK") { _, _ ->
                onYearSelected(numberPicker.value)
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }





}