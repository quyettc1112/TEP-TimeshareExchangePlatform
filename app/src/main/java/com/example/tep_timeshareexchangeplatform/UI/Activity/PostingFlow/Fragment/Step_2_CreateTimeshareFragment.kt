package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.AmenitiesBottomSheetFragment.AmenitiesBottomSheetFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RoomDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort.ResortModelResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.mapRoomAmenitiesToAmenitiesModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.AmenitiesAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.RoomResultAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.UnitTypeAdapterPosting
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.AmenityType
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimesharePostingBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.datepicker.MaterialDatePicker.Builder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Step_2_CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare_posting) {

    private lateinit var binding: FragmentCreateTimesharePostingBinding
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>
    private var unitTypeAdapterPosting = UnitTypeAdapterPosting(true)
    private var kitchenAmenitiesAdapter = AmenitiesAdapter()
    private var entertainmentAmenitiesAdapter = AmenitiesAdapter()
    private var policyAmenitiesAdapter = AmenitiesAdapter()
    private var featuresAmenitiesAdapter = AmenitiesAdapter()
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
        postingFlowViewModel.updateIsYesOrNo(true)

        // Binding Data
        bindDataAmenities()


        // Event CLick
        eventClickSelectResort()
        eventClickChangeDay()
        eventSaveDayClick()
        eventClickSaveAmenities()
        eventClickCreateTimeshare()
        eventCLickChangeToOptionNo()

        return binding.root
    }

    private fun initAdapter() {
        unitTypeAdapterPosting.submitList(listOf())


        kitchenAmenitiesAdapter.apply {
            submitList(Constant.listAmenities)
            onItemChecked = {
                postingFlowViewModel.updateAmenitiesForType(AmenityType.KITCHEN, getCheckedItems())
            }
        }
        entertainmentAmenitiesAdapter.apply {
            submitList(Constant.listEntertament)
            onItemChecked = {
                postingFlowViewModel.updateAmenitiesForType(
                    AmenityType.ENTERTAINMENT,
                    getCheckedItems()
                )

            }
        }
        policyAmenitiesAdapter.apply {
            submitList(Constant.listPolicy)
            onItemChecked = {
                postingFlowViewModel.updateAmenitiesForType(AmenityType.POLICY, getCheckedItems())
            }
        }
        featuresAmenitiesAdapter.apply {
            submitList(Constant.listFeatures)
            onItemChecked = {
                postingFlowViewModel.updateAmenitiesForType(AmenityType.FEATURES, getCheckedItems())

            }
        }

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
        postingFlowViewModel.unitTypeList.observe(viewLifecycleOwner) { listUnitType ->
            when (listUnitType?.status) {
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

                else -> {}
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
                }

                Status.ERROR -> {
                    showErrorToast("${unitType.message}")
                }
            }

        }
        postingFlowViewModel.roomDetailResponse.observe(viewLifecycleOwner) { roomDetail ->
            when (roomDetail?.status) {
                Status.LOADING -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    roomDetail?.data?.roomAmenities?.let { amenitiesList ->
                        Log.d("RoomDetailLis", "RoomDetail: $amenitiesList")
                        val featuresList =
                            mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.FEATURES)
                        val entertainmentList = mapRoomAmenitiesToAmenitiesModel(
                            amenitiesList,
                            AmenityType.ENTERTAINMENT
                        )
                        val policyList =
                            mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.POLICY)
                        val kitchenList =
                            mapRoomAmenitiesToAmenitiesModel(amenitiesList, AmenityType.KITCHEN)



                        kitchenAmenitiesAdapter.updateCheckedItemsFromList(kitchenList)
                        entertainmentAmenitiesAdapter.updateCheckedItemsFromList(entertainmentList)
                        policyAmenitiesAdapter.updateCheckedItemsFromList(policyList)
                        featuresAmenitiesAdapter.updateCheckedItemsFromList(featuresList)

                        postingFlowViewModel.updateAmenitiesForType(
                            AmenityType.FEATURES,
                            featuresList
                        )
                        postingFlowViewModel.updateAmenitiesForType(
                            AmenityType.ENTERTAINMENT,
                            entertainmentList
                        )
                        postingFlowViewModel.updateAmenitiesForType(AmenityType.POLICY, policyList)
                        postingFlowViewModel.updateAmenitiesForType(
                            AmenityType.KITCHEN,
                            kitchenList
                        )


                    }
                }

                Status.ERROR -> {
                    showErrorToast("${roomDetail?.message}")
                }

                else -> {}
            }
        }
        // No - Create Room
        postingFlowViewModel.roomModel.observe(viewLifecycleOwner) { roomModel ->
            when (roomModel.status) {
                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    val startDateFormatted = postingFlowViewModel.getTimeshareDateRange().first
                    val endDateFormatted = postingFlowViewModel.getTimeshareDateRange().second
                    val timeshareDTO = TimeshareDTO(
                        status = "Available",
                        startYear = postingFlowViewModel.getYearRange().first,
                        endYear = postingFlowViewModel.getYearRange().second,
                        startDate = startDateFormatted.toString(),
                        endDate = endDateFormatted.toString(),
                        roomInfoId = roomModel.data!!.roomId
                    )
                    callCreateTimeshare(timeshareDTO)
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

        // Step 2 - Day Check In
        postingFlowViewModel.numberOfNightsTimeshare.observe(viewLifecycleOwner) { numberOfNights ->
            if (numberOfNights > 0) {
                binding.etNightsCount.setText(numberOfNights.toString())
            }
        }

        // Step 3- Amennities


        // Final Step - Create Timeshare
        postingFlowViewModel.createTimeshareResponse.observe(viewLifecycleOwner) { timeshareDTO ->
            when (timeshareDTO.status) {
                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    showSuccessToast("Create Timeshare Success")
                    postingFlowViewModel.updateTaskProgress(1)
                    postingFlowViewModel.updateTaskProgress(0)
                    postingFlowViewModel.resetTimeshareDateRange()
                    postingFlowViewModel.resetData()
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
            0 -> {
                binding.llSelectResortLocationContainer.visibility = View.VISIBLE
                postingFlowViewModel.apply {
                    clearAllAmenities()
                    clearCurrentMyTimeshareList()
                    cleanCheckInDay()
                }
            }


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
                binding.crlDayCheckIn.visibility = View.VISIBLE
                binding.crlContentAmenities.visibility = View.GONE
                binding.btnNext.visibility = View.GONE

            }

            3 -> {
                binding.crlContentAmenities.visibility = View.VISIBLE
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
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.btnNext.bottom)
                }
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
     * EVENT CLICK FUNCTION
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

    private fun eventClickChangeDay() {
        bindDataSpinnerValidYear()
        binding.btnSelectCheckInOutDate.setOnClickListener {
            val selectedStartYear = binding.spValidStarYear.selectedItem as Int
            showRangeDayPickerDialog(requireContext(), selectedStartYear) { dateRange ->

            }
        }
    }

    private fun eventSaveDayClick() {
        binding.btnSaveDay.setOnClickListener {
            val night = postingFlowViewModel.getNumberOfNightsTimeshare()
            if (night == null || night == 0) {
                showErrorToast("Please select number of nights")
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlDayCheckIn.top)
                }
                return@setOnClickListener
            } else {
                postingFlowViewModel.updateTaskProgress(3)
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlContentAmenities.top)
                }
            }
        }
    }

    private fun eventClickSaveAmenities() {
        binding.btnSaveAmenities.setOnClickListener {
            if (postingFlowViewModel.checkEachTypeHasMinTwoSelected()) {
                (activity as PostingFlowActivity).showConfirmDialog(
                    "Lưu Tiện Ích",
                    "Bạn có chắc chắn muốn lưu tiện ích này?",
                    "Đồng Ý",
                    "Hủy",
                    "",
                    object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {

                        }

                        override fun positiveAction() {
                            postingFlowViewModel.updateTaskProgress(5)
                        }

                    }
                )
            } else {
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlContentAmenities.top)
                }
                showWarningToast("Mỗi loại tiện ích cần chọn ít nhất 2 mục")
            }
        }
    }

    private fun eventClickCreateTimeshare() {
        binding.btnNext.setOnClickListener {
            val checkYN: Boolean = postingFlowViewModel.isYesOrNoSelected.value!!
            if (checkYN) {
                sendRequestOptionYes()
            } else {
                // Call API to create Room
                senRequestOptionNO()
            }
        }
    }

    private fun eventCLickChangeToOptionNo() {
        binding.btnChangeOption.setOnClickListener {
            cleanAmenities()
            cleanCheckInDay()
            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, binding.llSelectRoomContainer.top)
            }
            if (postingFlowViewModel.isYesOrNoSelected.value == true) {

                postingFlowViewModel.updateIsYesOrNo(false)
                postingFlowViewModel.updateTaskProgress(1)
                postingFlowViewModel.resetTimeshareDateRange()
                binding.includeUnitTypeYes.root.visibility = View.GONE
                binding.includeUnitTypeNo.root.visibility = View.VISIBLE

                binding.tvOptionNo.text = "Chọn Mã Phòng Có Sẵn Từ Resort"
                binding.btnChangeOption.text = "Chọn Mã Phòng"
            } else {
                postingFlowViewModel.updateIsYesOrNo(true)
                postingFlowViewModel.updateTaskProgress(1)
                postingFlowViewModel.resetTimeshareDateRange()
                binding.includeUnitTypeYes.root.visibility = View.VISIBLE
                binding.includeUnitTypeNo.root.visibility = View.GONE

                binding.tvOptionNo.text =
                    "Không có phòng của bạn?\nKhai báo thông tin phòng với chúng tôi"
                binding.btnChangeOption.text = "Khai Báo"
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

                    // Lấy Room Detail Amenities
                    callGetRoomDetailById(selectedRoom.id)
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

            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
            val unitTypeDataDialog = UnitTypeDataDialog.newInstance(it)
            unitTypeDataDialog.show(parentFragmentManager, "UnitTypeDataDialog")
        }

        unitTypeAdapterPosting.onItemClick = { it ->
            postingFlowViewModel.updateUnitTypeSelectionOptionNo(it)
            binding.includeUnitTypeNo.edtRoomCode.clearFocus()
            binding.includeUnitTypeNo.edtRoomName.clearFocus()
        }

        binding.includeUnitTypeNo.btnSaveRoomInfo.setOnClickListener {
            if (!verifyDataAndSendRequest()) {
                showWarningToast("Please fill in all required fields")
                return@setOnClickListener
            }

            if (postingFlowViewModel.unitTypeSelectionOptionNo.value == null) {
                showErrorToast("Please select a unit type")
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.includeUnitTypeNo.rvUnitType.top)
                }
                return@setOnClickListener
            }
            binding.includeUnitTypeNo.edtRoomCode.clearFocus()
            binding.includeUnitTypeNo.edtRoomName.clearFocus()
            postingFlowViewModel.updateTaskProgress(2)
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

    // 2. Binding Data of Day Check In
    private fun bindDataSpinnerValidYear() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val yearList = (currentYear..currentYear + 100).toList()

        val yearAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, yearList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spValidStarYear.adapter = yearAdapter
        binding.spValidEndYear.adapter = yearAdapter

        binding.spValidStarYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedStartYear = yearList[position]
                    postingFlowViewModel.resetTimeshareDateRange()
                    binding.apply {
                        tvCheckinDate.text = ""
                        tvCheckinDayOfWeek.text = ""
                        tvCheckoutDate.text = ""
                        tvCheckoutDayOfWeek.text = ""
                        etNightsCount.setText("0")
                    }

                    val validEndYearList = yearList.filter { it >= selectedStartYear }
                    val endYearAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        validEndYearList
                    )
                    endYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.spValidEndYear.adapter = endYearAdapter

                    val currentEndYearPosition = validEndYearList.indexOf(
                        binding.spValidEndYear.selectedItem ?: selectedStartYear
                    )
                    binding.spValidEndYear.setSelection(if (currentEndYearPosition >= 0) currentEndYearPosition else 0)

                    // Lưu cặp giá trị năm Start và End
                    val selectedEndYear =
                        validEndYearList.getOrNull(currentEndYearPosition) ?: selectedStartYear
                    postingFlowViewModel.setYearRange(selectedStartYear, selectedEndYear)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Không cần xử lý
                }
            }

        binding.spValidEndYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedStartYear = binding.spValidStarYear.selectedItem as? Int
                    val selectedEndYear = parent?.getItemAtPosition(position) as? Int

                    if (selectedStartYear != null && selectedEndYear != null) {
                        postingFlowViewModel.setYearRange(selectedStartYear, selectedEndYear)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Không cần xử lý
                }
            }
    }

    // 3. Amennities
    private fun bindDataAmenities() {
        // Set List Kitchen
        binding.rvKitchen.apply {
            adapter = kitchenAmenitiesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Amenities Entertament
        binding.rvAmenitiesEntertainment.apply {
            adapter = entertainmentAmenitiesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Policy
        binding.rvPolicy.apply {
            adapter = policyAmenitiesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

        // Set List Features
        binding.rvFeatures.apply {
            adapter = featuresAmenitiesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }

    }


    /**
     * CALL API FUNTION
     *
     *
     */
    private fun sendRequestOptionYes() {
        val startDateFormatted = postingFlowViewModel.getTimeshareDateRange().first
        val endDateFormatted = postingFlowViewModel.getTimeshareDateRange().second
        val timeshareDTO = TimeshareDTO(
            status = "Available",
            startYear = postingFlowViewModel.getYearRange().first,
            endYear = postingFlowViewModel.getYearRange().second,
            startDate = startDateFormatted.toString(),
            endDate = endDateFormatted.toString(),
            roomInfoId = postingFlowViewModel.currentRoomInfo.value!!
        )
        Log.d("CheckTImesahreModel", "sendRequestCreateTimeshare: $timeshareDTO")
        if (tokenManager.getAccessToken().toString() != null && timeshareDTO != null) {
            callCreateTimeshare(timeshareDTO)
        } else {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun senRequestOptionNO() {
        if (!tokenManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show()
            return
        }
        val roomDTO: RoomDTO = RoomDTO(
            roomInfoCode = binding.includeUnitTypeNo.edtRoomCode.text.toString(),
            isActive = true,
            resortId = postingFlowViewModel.resortModelResponse.value?.id!!,
            status = "Available",
            unitTypeId = postingFlowViewModel.unitTypeSelectionOptionNo.value?.id!!,
            roomName = binding.includeUnitTypeNo.edtRoomName.text.toString(),
            roomAmenities = postingFlowViewModel.getSelectedAmenitiesForPost()
        )
        callCreateRoom(roomDTO)
    }

    private fun callGetUnitTypeDetailByID(int: Int) {
        postingFlowViewModel.getUnitTypeDetailByID(tokenManager.getAccessToken().toString(), int)
    }

    private fun callCreateTimeshare(timeshareDTO: TimeshareDTO) {
        postingFlowViewModel.postTimeshareDTO(
            tokenManager.getAccessToken().toString(),
            timeshareDTO
        )
    }

    private fun callCreateRoom(roomDTO: RoomDTO) {
        postingFlowViewModel.postRoom(tokenManager.getAccessToken().toString(), roomDTO)
    }

    private fun callGetRoomDetailById(roomId: Int) {
        postingFlowViewModel.getRoomDetailById(tokenManager.getAccessToken().toString(), roomId)
    }

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

    private fun showWarningToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Failed",
            string,
            MotionToastStyle.WARNING,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        )
    }

    private fun cleanAmenities() {
        kitchenAmenitiesAdapter.submitList(Constant.listAmenities)
        entertainmentAmenitiesAdapter.submitList(Constant.listEntertament)
        policyAmenitiesAdapter.submitList(Constant.listPolicy)
        featuresAmenitiesAdapter.submitList(Constant.listFeatures)
        postingFlowViewModel.clearAllAmenities()
    }

    private fun cleanCheckInDay() {
        binding.apply {
            tvCheckinDate.text = ""
            tvCheckinDayOfWeek.text = ""
            tvCheckoutDate.text = ""
            tvCheckoutDayOfWeek.text = ""
            etNightsCount.setText("0")
        }
        postingFlowViewModel.resetTimeshareDateRange()

    }


    fun showRangeDayPickerDialog(
        context: Context,
        startYear: Int,
        onDateRangeSelected: (String) -> Unit
    ) {
        // Tạo Calendar để giới hạn ngày trong năm Start Year
        val calendarStart = Calendar.getInstance().apply {
            set(Calendar.YEAR, startYear)
            set(Calendar.DAY_OF_YEAR, 1) // Ngày đầu tiên trong năm Start Year
        }
        val calendarEnd = Calendar.getInstance().apply {
            set(Calendar.YEAR, startYear)
            set(
                Calendar.DAY_OF_YEAR,
                getActualMaximum(Calendar.DAY_OF_YEAR)
            ) // Ngày cuối cùng trong năm
        }

        // Cấu hình ràng buộc khoảng thời gian
        val constraints = CalendarConstraints.Builder()
            .setStart(calendarStart.timeInMillis) // Bắt đầu từ ngày đầu năm Start Year
            .setEnd(calendarEnd.timeInMillis)     // Kết thúc ngày cuối năm Start Year
            .build()

        // Tạo Range Date Picker
        val datePicker = Builder.dateRangePicker()
            .setTitleText("Chọn khoảng ngày trong năm $startYear")
            .setCalendarConstraints(constraints)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection?.first
            val endDate = selection?.second

            postingFlowViewModel.setTimeshareDateRange(startDate, endDate)

            val preferenceHelper = PreferenceHelper(context)
            val languageCode = preferenceHelper.getLanguage()
            val locale = if (languageCode == "vi") Locale.forLanguageTag("vi") else Locale.ENGLISH

            val displayDateFormat = SimpleDateFormat("dd 'Tháng' M, yyyy", locale)
            val dayOfWeekFormat = SimpleDateFormat("EEEE", locale)

            binding.apply {
                tvCheckinDate.text = startDate?.let { displayDateFormat.format(Date(it)) }
                tvCheckinDayOfWeek.text = startDate?.let { dayOfWeekFormat.format(Date(it)) }

                tvCheckoutDate.text = endDate?.let { displayDateFormat.format(Date(it)) }
                tvCheckoutDayOfWeek.text = endDate?.let { dayOfWeekFormat.format(Date(it)) }
            }
        }

        datePicker.show((context as AppCompatActivity).supportFragmentManager, "RANGE_DATE_PICKER")
    }


}