package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog.UnitTypeDataDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.TimeshareUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Room.RoomModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.UnitType.UnitTypeModel
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.mapUnitTypeModelToUnitTypeBase
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateTimeshareBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker.Builder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateTimeshareBottomDialog(
    private val myTimeshareDetailViewModel: MyTimeshareDetailViewModel
) : BottomSheetDialogFragment() {

    private var _binding: DialogUpdateTimeshareBinding? = null
    private val binding get() = _binding!!
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using View Binding
        _binding = DialogUpdateTimeshareBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        callGetRoomListByResortId()
        observeViewModel()
        bindDataSpinnerValidYear()
        eventClickSaveUpdate()
        eventClickChangeDay()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeViewModel() {
        myTimeshareDetailViewModel.roomList.observe(viewLifecycleOwner) { roomList ->
            when (roomList?.status) {
                Status.LOADING -> {
                    (activity as MyTimeshareDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyTimeshareDetailActivity).hideLoadingWaiting()
                    bindDataRoomCodeSpinner(roomList.data, myTimeshareDetailViewModel.myTimeshareDetail.value?.data?.roomId)
                }

                Status.ERROR -> {
                    (activity as MyTimeshareDetailActivity).hideLoadingWaiting()
                    Log.d("RoomList", "RoomList: ${roomList.message}")
                    (activity as MyTimeshareDetailActivity).showErrorToast("Lỗi khi lấy danh sách phòng của Resort","Danh sách phòng trống")
                }

                null -> {}
            }
        }

        myTimeshareDetailViewModel.unitTypeDetail.observe(viewLifecycleOwner) { unitType ->
            when (unitType?.status) {
                Status.LOADING -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.includeUnitTypeYes.llProcessbar.visibility = View.GONE
                    bindDataUnitTypeYesOption(unitType.data!!)
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).showFailedDialog(
                        requireContext(),
                        "Lỗi Khi Lấy Thông Tin Loại Phòng",
                        "${unitType.message}",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                dismiss()
                            }
                        }
                    )
                }

                null -> {}
            }

        }

        myTimeshareDetailViewModel.numberOfNightsTimeshare.observe(viewLifecycleOwner) { numberOfNights ->
            if (numberOfNights != null) {
                if (numberOfNights > 0) {
                    binding.etNightsCount.setText(numberOfNights.toString())
                }
            }
        }

        myTimeshareDetailViewModel.updateTimeshare.observe(viewLifecycleOwner) { updateTimeshare ->
            when (updateTimeshare?.status) {
                Status.LOADING -> {
                    (activity as MyTimeshareDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyTimeshareDetailActivity).apply {
                        hideLoadingWaiting()
                        showSuccessToast("Cập nhật thành công", "Cập nhật thông tin thành công")
                        val myTimeshareId = myTimeshareDetailViewModel.myTimeshareDetail.value?.data?.timeShareId ?: 0
                        myTimeshareDetailViewModel.getMyTimeshareDetail(tokenManager.getAccessToken().toString(), myTimeshareId)
                        dismiss()
                    }

                    dismiss()
                }

                Status.ERROR -> {
                    (activity as MyTimeshareDetailActivity).hideLoadingWaiting()
                    (activity as MyTimeshareDetailActivity).showErrorToast("Lỗi cập nhật", "Lỗi khi cập nhật thông tin")
                    Log.d("UpdateTimeshasasdare", "UpdateTimeshare: ${updateTimeshare.message}")
                }

                null -> {}
            }
        }
    }

    private fun bindDataRoomCodeSpinner(roomList: List<RoomModel>?, selectedRoomId: Int?) {
        val spinnerBinding = binding.includeUnitTypeYes

        spinnerBinding.etRoomSearch.visibility = View.GONE

        // Thêm mục mặc định vào danh sách
        val roomDisplayList: List<String> = listOf("Chọn Mã Phòng") +
                (roomList?.map { "Mã Phòng: ${it.roomInfoCode}" } ?: emptyList())

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomDisplayList)
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        spinnerBinding.spUnitType.adapter = adapter

        // Tìm vị trí tương ứng với RoomId
        val selectedPosition = selectedRoomId?.let { id ->
            roomList?.indexOfFirst { it.id == id }?.let { it + 1 } // +1 để tính cả mục "Chọn Mã Phòng"
        } ?: 0 // Nếu không tìm thấy hoặc selectedRoomId là null, mặc định là 0

        // Đặt selection cho Spinner
        spinnerBinding.spUnitType.setSelection(selectedPosition)

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

                    val selectedRoom = roomList?.get(position - 1) // Trừ 1 để bỏ qua mục "Chọn Mã Phòng"

                    // Cập nhật ViewModel
                    myTimeshareDetailViewModel.updateCurrentRoomInfo(selectedRoom!!.id)

                    // Hiển thị thông tin phòng
                    binding.includeUnitTypeYes.tvRoomName.text =
                        selectedRoom.roomInfoName ?: "Unknown Name"
                    binding.includeUnitTypeYes.tvRoomCode.text =
                        selectedRoom.roomInfoCode ?: "Unknown Code"

                    // Lấy thông tin id của RoomModel tương ứng
                    val unitTypeID = selectedRoom.unitTypeId
                    callGetUnitTypeDetailByID(unitTypeID!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có gì được chọn
                }
            }
    }

    private fun bindDataUnitTypeYesOption(unitType: UnitTypeModel) {
        val binding = binding.includeUnitTypeYes
        // Hide Unnecessary View
        binding.includeItemUnitType.llAmennities.visibility = View.GONE
        binding.includeItemUnitType.tvPrice.visibility = View.GONE
        binding.includeItemUnitType.btnViewRoom.setOnClickListener {
            val unitTypeBase = mapUnitTypeModelToUnitTypeBase(unitType)
            val unitTypeDataDialog = UnitTypeDataDialog.newInstance(unitTypeBase)
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
                .placeholder(R.drawable.ic_image_tmp_holder)
                .error(R.drawable.ic_image_tmp_holder)
                .into(imRoomTypeImage)

        }

        /*binding.includeItemUnitType.btnViewRoom.setOnClickListener {
            bindDataUnitTypeDetailDialog(unitType)
        }*/

    }

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
                    myTimeshareDetailViewModel.resetTimeshareDateRange()
                    binding.apply {
                        tvCheckinDate.text = ""
                        tvCheckinDayOfWeek.text = ""
                        tvCheckoutDate.text = ""
                        tvCheckoutDayOfWeek.text = ""
                        etNightsCount.setText("0")
                    }

                    val validEndYearList = yearList.filter { it > selectedStartYear }
                    val endYearAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        validEndYearList
                    )
                    endYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.spValidEndYear.adapter = endYearAdapter

                    /*val currentEndYearPosition = validEndYearList.indexOf(
                        binding.spValidEndYear.selectedItem ?: selectedStartYear
                    )
                    binding.spValidEndYear.setSelection(if (currentEndYearPosition >= 0) currentEndYearPosition else 0)*/
                    binding.spValidEndYear.adapter = endYearAdapter

                    // Set the default selection to the first item in the validEndYearList
                    binding.spValidEndYear.setSelection(0)

                    /*// Lưu cặp giá trị năm Start và End
                    val selectedEndYear =
                        validEndYearList.getOrNull(currentEndYearPosition) ?: selectedStartYear*/
                    val selectedEndYear = validEndYearList.firstOrNull() ?: (selectedStartYear + 1)
                    myTimeshareDetailViewModel.setYearRange(selectedStartYear, selectedEndYear)
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
                        myTimeshareDetailViewModel.setYearRange(selectedStartYear, selectedEndYear)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Không cần xử lý
                }
            }
    }

    private fun eventClickSaveUpdate() {
        binding.btnSaveUpdatePosting.setOnClickListener {
            if(!isValidDateRange()) {
                return@setOnClickListener
            }

            val timeshareUpdateDTO = TimeshareUpdateDTO(
                startYear = myTimeshareDetailViewModel.yearRange.value!!.first,
                endYear = myTimeshareDetailViewModel.yearRange.value!!.second,
                startDate = myTimeshareDetailViewModel.getTimeshareDateRange().first.toString(),
                endDate = myTimeshareDetailViewModel.getTimeshareDateRange().second.toString(),
                roomInfoId = myTimeshareDetailViewModel.currentRoomInfo.value!!
            )
            Log.d("UpdateTimeshasasdare", "UpdateTimeshare: $timeshareUpdateDTO")
            callUpdateTimeshare(timeshareUpdateDTO)
        }
    }

    private fun eventClickChangeDay() {
        binding.btnSelectCheckInOutDate.setOnClickListener {
            val selectedStartYear = binding.spValidStarYear.selectedItem as Int
            showRangeDayPickerDialog(requireContext(), selectedStartYear) { dateRange ->

            }
        }
    }

    private fun callGetUnitTypeDetailByID(int: Int) {
        myTimeshareDetailViewModel.getUnitTypeDetailByID(
            tokenManager.getAccessToken().toString(),
            int
        )
    }

    private fun callGetRoomListByResortId() {
        val resortID = myTimeshareDetailViewModel.myTimeshareDetail.value?.data?.resortId ?: 0
        myTimeshareDetailViewModel.getRoomListByResortId(
            tokenManager.getAccessToken().toString(), resortID
        )
    }

    private fun callUpdateTimeshare(timeshareUpdateDTO: TimeshareUpdateDTO) {
        Log.d("UpdateTimeshasasdare", "UpdateTimeshare: $timeshareUpdateDTO, ${myTimeshareDetailViewModel.myTimeshareDetail.value?.data?.timeShareId}")
        myTimeshareDetailViewModel.callUpdateTimeshare(
            tokenManager.getAccessToken().toString(),
            myTimeshareDetailViewModel.myTimeshareDetail.value?.data?.timeShareId ?: 0,
            timeshareUpdateDTO
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    override fun getTheme(): Int {
        return R.style.MyBottomSheetDialogTheme // Use custom theme
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

            myTimeshareDetailViewModel.setTimeshareDateRange(startDate, endDate)

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

    private fun isValidDateRange(): Boolean {
        // Lấy khoảng ngày từ ViewModel
        val dateRange = myTimeshareDetailViewModel.getTimeshareDateRange()
        val startDate = dateRange.first
        val endDate = dateRange.second

        // Kiểm tra null hoặc trống
        if (startDate.isEmpty() || endDate.isEmpty()) {
            (activity as MyTimeshareDetailActivity).showErrorToast(
                "Ngày không hợp lệ",
                "Ngày bắt đầu hoặc ngày kết thúc bị thiếu"
            )
            return false
        }
        // Tất cả điều kiện hợp lệ
        return true
    }

}