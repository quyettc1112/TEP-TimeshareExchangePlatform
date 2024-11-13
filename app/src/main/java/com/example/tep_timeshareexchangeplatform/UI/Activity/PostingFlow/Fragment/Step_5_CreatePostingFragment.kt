package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.DateRangePickerDialog.DateRangeDialogFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreatePostingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Step_5_CreatePostingFragment : BaseFragment(R.layout.fragment_create_posting) {

    private lateinit var binding: FragmentCreatePostingBinding
    private val postingFlowViewModel: PostingFlowViewModel by activityViewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostingBinding.inflate(inflater, container, false)
        binding.includeMyTimeshare.btnSelect.visibility = View.GONE
        observeViewModel()
        setEventChangeMyTimeshare()
        bindDataSpinnerCancellationPolicy()
        setupTextWatchers()
        return binding.root
    }

    // Funtion to observe view model
    private fun observeViewModel() {
        // Observe myTimeshareModelSelected
        postingFlowViewModel.myTimeshareModelSelected.observe(viewLifecycleOwner) { myTimeshareModel ->
            postingFlowViewModel.getValidYearTimeshare(
                tokenManager.getAccessToken().toString(), myTimeshareModel.timeShareId
            )
            bindDataMyTimeshare(myTimeshareModel)

        }

        // Observer Valid Year Timeshare of Customer
        postingFlowViewModel.validYearTimeshare.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    resources.data?.let {
                        if (it.isEmpty()) {
                            (activity as PostingFlowActivity).showInfoDialog(requireContext(),
                                "Timeshare của bạn hiện không có năm hợp lệ để cho thuê, Xin vui lòng kiem tra lại",
                                object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        postingFlowViewModel.resetSteps()
                                        postingFlowViewModel.updateStep(3)
                                    }
                                })
                        } else {
                            bindDataSpinnerValidYear(it)
                        }
                    }
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    MotionToast.Companion.createToast(
                        requireActivity(),
                        "Error",
                        "Error ${resources.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        // Observer Price Per Night
        postingFlowViewModel.pricePerNight.observe(viewLifecycleOwner) { pricePerNight ->
            if (postingFlowViewModel.numberOfNights.value != null) {
                val totalPrice = pricePerNight * postingFlowViewModel.numberOfNights.value!!
                val value =
                    "${Constant.formatPrice(totalPrice.toInt())} đ/${postingFlowViewModel.numberOfNights.value!!} đêm"
                binding.includePaymentMethod12.etTotalPrice.setText(value)
            }

        }

        // Observer Package Selected
        postingFlowViewModel.packageStep4.observe(viewLifecycleOwner) { packageModel ->
            when (postingFlowViewModel.typeOfPostingFlow.value) {
                Constant.RENTAL_POSTING_FLOW -> {
                    rentalPackageHandleUI(packageModel)
                }

                Constant.EXCHANGER_POSTING_FLOW -> {
                    exchangePackageHandleUI(packageModel)
                }
            }
        }
    }

    // Function to set event change my timeshare
    private fun setEventChangeMyTimeshare() {
        binding.btnChangeMyTimeshare.setOnClickListener {
            postingFlowViewModel.updateStep(3)
        }

    }


    // Function to bind data
    private fun bindDataMyTimeshare(myTimeshareResponse: MyTimeshareResponse.Content) {
        if (myTimeshareResponse == null) {
            binding.includeMyTimeshare.root.visibility = View.GONE
        } else {
            binding.includeMyTimeshare.root.visibility = View.VISIBLE
            binding.includeMyTimeshare.apply {
                tvResortName.text = myTimeshareResponse.resortName
                tvRoomType.text = myTimeshareResponse.roomName
                tvCheckinDate.text =
                    Constant.formatDateByLocale(myTimeshareResponse.startDate, requireContext())
                tvCheckOutDate.text =
                    Constant.formatDateByLocale(myTimeshareResponse.endDate, requireContext())
                /*Glide.with(binding.root.context).load(myTimeshareModel.image).into(imResortImage)*/
            }
        }
    }

    private fun bindDataSpinnerCancellationPolicy() {
        val refundPolicies = RefundPolicy.entries.toTypedArray() // Lấy danh sách tất cả các enum
        val spinnerAdapter = object :
            ArrayAdapter<RefundPolicy>(requireContext(), R.layout.spinner_item, refundPolicies) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val refundPolicy = getItem(position)
                (view as TextView).text = refundPolicy?.getShortDescription(context)
                return view
            }

            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val refundPolicy = getItem(position)
                (view as TextView).text = refundPolicy?.getShortDescription(context)
                return view
            }

        }
        binding.customSpinnerViewDiretion.adapter = spinnerAdapter

        binding.customSpinnerViewDiretion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    val selectedPolicy = parent.getItemAtPosition(position) as RefundPolicy
                    val policyId = selectedPolicy.id
                    val longDescription =
                        Html.fromHtml(selectedPolicy.getLongDescription(requireContext()))
                    binding.tvCancellationPolicyDescription.text = longDescription
                    postingFlowViewModel.updateCancelPolicy(policyId)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có mục nào được chọn
                }
            }


    }

    private fun bindDataSpinnerValidYear(validYear: ValidYearResponse) {
        // Chuyển đổi danh sách các năm thành chuỗi (String)
        val yearList = validYear.map { it.toString() }

        // Tạo ArrayAdapter để kết nối dữ liệu với Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, yearList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Gán adapter cho Spinner
        binding.customSpinnerYearValid.adapter = adapter

        // Bắt sự kiện khi người dùng chọn năm
        binding.customSpinnerYearValid.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    // Lấy năm được chọn từ Spinner
                    val selectedYear = parent.getItemAtPosition(position).toString().toInt()

                    // Lấy startDateString và endDateString từ ViewModel
                    val startDateString =
                        postingFlowViewModel.myTimeshareModelSelected.value?.startDate
                    val endDateString =
                        postingFlowViewModel.myTimeshareModelSelected.value?.endDate

                    // Kiểm tra xem startDateString và endDateString có null hay không
                    bindDataCheckInCheckOut(startDateString, endDateString, selectedYear)


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có item nào được chọn
                }
            }
    }

    private fun bindDataCheckInCheckOut(
        startDateString: String?, endDateString: String?, selectedYear: Int
    ) {
        if (startDateString != null && endDateString != null) {
            // Định dạng chuỗi ngày tháng từ dạng "dd-MM-yyyy"
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            try {
                // Chuyển đổi startDateString và endDateString thành Date
                val startDate = dateFormat.parse(startDateString)
                val endDate = dateFormat.parse(endDateString)

                // Cập nhật năm mới cho startDate và endDate dựa trên năm đã chọn
                val calendarStart = Calendar.getInstance().apply {
                    time = startDate
                    set(Calendar.YEAR, selectedYear)
                }
                val calendarEnd = Calendar.getInstance().apply {
                    time = endDate
                    set(Calendar.YEAR, selectedYear)
                }


                // Tính toán số đêm
                val totalDays =
                    ((calendarEnd.timeInMillis - calendarStart.timeInMillis) / (1000 * 60 * 60 * 24)).toInt() + 1

                // Lấy ngày tháng và thứ trong tuần cho startDate
                val newStartDateString = formatDateByLocale(calendarStart.time, requireContext())
                val startDayOfWeek =
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(calendarStart.time)

                // Lấy ngày tháng và thứ trong tuần cho endDate
                val newEndDateString = formatDateByLocale(calendarEnd.time, requireContext())
                val endDayOfWeek =
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(calendarEnd.time)

                // Định dạng ngày theo kiểu "yyyy-MM-dd"
                val dateFormatDTO = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val checkinDate = dateFormatDTO.format(calendarStart.time)
                val checkoutDate = dateFormatDTO.format(calendarEnd.time)
                postingFlowViewModel.setCheckinDate(checkinDate)
                postingFlowViewModel.setCheckoutDate(checkoutDate)

                // Cập nhật UI
                binding.tvCheckInDate.text = newStartDateString
                binding.tvCheckOutDate.text = newEndDateString
                binding.tvCheckInDayOfWeek.text = startDayOfWeek
                binding.tvCheckOutDayOfWeek.text = endDayOfWeek
                binding.etNightsCount.text = "$totalDays"
                postingFlowViewModel.updateNumberOfNights(totalDays)
            } catch (e: ParseException) {
                e.printStackTrace()
                // Xử lý lỗi khi chuỗi ngày không đúng định dạng
            }
        }
    }

    private fun bindDataSpinnerProvince() {
        val spinner: Spinner = binding.includeExchangeMethod12.customSpinnerProvince
        val provinces = resources.getStringArray(R.array.vietnam_provinces)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            provinces
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

// Xử lý khi người dùng chọn một tỉnh
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedProvince = provinces[position]
                if (selectedProvince != "Chọn Tỉnh Thành") {
                    val provinceId = position + 1
                    postingFlowViewModel.updateCurrentProvinceSelected(provinceId)
                } else {
                    postingFlowViewModel.updateCurrentProvinceSelected(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Không làm gì
            }
        }
    }

    private fun formatDateByLocale(date: Date, context: Context): String {
        // Sử dụng PreferenceHelper để lấy ngôn ngữ đã lưu
        val preferenceHelper = PreferenceHelper(context)
        val languageCode = preferenceHelper.getLanguage()

        // Định dạng ngày tháng dựa trên ngôn ngữ đã lưu
        val dateFormat = if (languageCode == "vi") {
            // Định dạng cho Tiếng Việt
            SimpleDateFormat("dd, 'Tháng' M, yyyy", Locale.forLanguageTag("vi"))
        } else {
            // Định dạng cho Tiếng Anh hoặc ngôn ngữ khác
            SimpleDateFormat("dd, MMMM, yyyy", Locale.ENGLISH)
        }

        return dateFormat.format(date)
    }

    private fun rentalPackageHandleUI(packageModel: PackageModel) {
        when (packageModel) {
            RentalPackageEnum.BASIC_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.VISIBLE
                binding.includePaymentMethod34.root.visibility = View.GONE
                binding.includeExchangeMethod12.root.visibility = View.GONE
                rentalPackage12ButtonClick()
            }

            RentalPackageEnum.ADVANCED_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.VISIBLE
                binding.includePaymentMethod34.root.visibility = View.GONE
                binding.includeExchangeMethod12.root.visibility = View.GONE
                rentalPackage12ButtonClick()
            }

            RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.GONE
                binding.includePaymentMethod34.root.visibility = View.VISIBLE
                binding.includeExchangeMethod12.root.visibility = View.GONE
                postingFlowViewModel.updatePricePerNight(0)
                rentalPackage34ButtonClick()
            }

            RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                binding.includePaymentMethod12.root.visibility = View.GONE
                binding.includePaymentMethod34.root.visibility = View.VISIBLE
                binding.includeExchangeMethod12.root.visibility = View.GONE
                postingFlowViewModel.updatePricePerNight(0)
                rentalPackage34ButtonClick()
            }
        }
        binding.titleTypePosting.text = "Giá phòng và chính sách hủy phòng"
        binding.llCancellationPolicy.visibility = View.VISIBLE

    }

    private fun exchangePackageHandleUI(packageModel: PackageModel) {
        when (packageModel) {
            ExchangePackageEnum.BASIC_SERVICE.packageModel -> {
                binding.includeExchangeMethod12.root.visibility = View.VISIBLE
                binding.includeExchangeMethod12.llBenefit.visibility = View.GONE
                binding.includePaymentMethod12.root.visibility = View.GONE
                binding.includePaymentMethod34.root.visibility = View.GONE
            }

            ExchangePackageEnum.ADVANCED_SERVICE.packageModel -> {
                binding.includeExchangeMethod12.root.visibility = View.VISIBLE
                binding.includeExchangeMethod12.llBenefit.visibility = View.VISIBLE
                binding.includePaymentMethod12.root.visibility = View.GONE
                binding.includePaymentMethod34.root.visibility = View.GONE
            }
        }

        exchangePackage12ButtonClick()
        //
        binding.titleTypePosting.text = "Thông tin Timeshare mong muốn trao đổi"
        binding.llCancellationPolicy.visibility = View.GONE

        // Bind data Spinner Province
        bindDataSpinnerProvince()

        // Change Date
        binding.includeExchangeMethod12.llCheckInCheckOut.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Chọn khoảng thời gian")
                .build()

            dateRangePicker.show(parentFragmentManager, "DATE_RANGE_PICKER")


            dateRangePicker.addOnPositiveButtonClickListener {
                val startDate = dateRangePicker.selection?.first
                val endDate = dateRangePicker.selection?.second
                if (startDate != null && endDate != null) {
                    val startDateString = formatDateByLocale(Date(startDate), requireContext())
                    val endDateString = formatDateByLocale(Date(endDate), requireContext())

                    val endDateOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(endDate)
                    val startDateOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(startDate)

                    postingFlowViewModel.setExchangeDateRange(startDate, endDate)

                    binding.includeExchangeMethod12.tvCheckInDate.apply {
                        text = startDateString
                        setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black))
                    }
                    binding.includeExchangeMethod12.tvCheckInDayOfWeek.text = startDateOfWeek
                    binding.includeExchangeMethod12.tvCheckOutDayOfWeek.text = endDateOfWeek
                    binding.includeExchangeMethod12.tvCheckOutDate.apply {
                        text = endDateString
                        setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black))
                    }

                    // Night
                    val totalDays = ((endDate - startDate) / (1000 * 60 * 60 * 24)).toInt() + 1
                    binding.includeExchangeMethod12.etNightsCount.text = "$totalDays"
                }
            }
        }
    }


    // Function to set event next
    private fun rentalPackage12ButtonClick() {
        binding.btnNext.setOnClickListener {
            if (postingFlowViewModel.pricePerNight.value == 0.toLong()) {
                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    "Error",
                    "Vui lòng nhập gia phong",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlPricePerNight.top)
                }
            } else {
                postingFlowViewModel.updateStep(6)
            }
        }
    }

    private fun rentalPackage34ButtonClick() {
        binding.btnNext.setOnClickListener {
            postingFlowViewModel.updateStep(6)
        }
    }

    private fun exchangePackage12ButtonClick() {
        binding.btnNext.setOnClickListener {
            val numberOfNights = postingFlowViewModel.getNumberOfExchangeNights()
            val provinceId = postingFlowViewModel.getCurrentProvinceSelected()

            if (numberOfNights == 0) {
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlPricePerNight.top)
                }
                Toast.makeText(requireContext(), "Vui lòng chọn ngày trao đổi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (provinceId == 0) {
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.crlPricePerNight.top)
                }
                Toast.makeText(requireContext(), "Vui lòng chọn tỉnh thành mong muốn trao đổi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            postingFlowViewModel.updateStep(6)
        }
    }


    // Function to validate all fields
    private fun setupTextWatchers() {
        // Username TextWatcher
        binding.includePaymentMethod12.etRoomPrice.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable?) {


                // Loại bỏ TextWatcher tạm thời để tránh loop
                binding.includePaymentMethod12.etRoomPrice.removeTextChangedListener(this)

                val input =
                    s.toString().replace("[^\\d]".toRegex(), "") // Loại bỏ các ký tự không phải số

                if (input.isNotEmpty()) {
                    // Kiểm tra và loại bỏ số 0 đầu tiên nếu có
                    var cleanedInput = input
                    if (cleanedInput.startsWith("0")) {
                        cleanedInput = cleanedInput.substring(1) // Loại bỏ số 0 đầu tiên
                    }
                    val numericValue = input.toLongOrNull() ?: 0
                    when {
                        numericValue < 100000 -> {
                            // Hiển thị helper text nếu số tiền nhỏ hơn 100.000
                            binding.includePaymentMethod12.tilRoomPrice.helperText =
                                "Số tiền tối thiểu là 100.000"
                        }

                        numericValue > 10_000_000_000 -> {
                            // Hiển thị helper text nếu số tiền lớn hơn 10 tỷ
                            binding.includePaymentMethod12.tilRoomPrice.helperText =
                                "Số tiền tối đa là 10 tỷ"
                        }

                        else -> {
                            // Ẩn helper text khi số tiền đạt yêu cầu
                            binding.includePaymentMethod12.tilRoomPrice.helperText = null
                        }
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " đ"
                    current = formatted
                    binding.includePaymentMethod12.etRoomPrice.setText(formatted)
                    binding.includePaymentMethod12.etRoomPrice.setSelection(formatted.length - 2) // Đặt con trỏ vào vị trí trước "đ"
                    val amount = binding.includePaymentMethod12.etRoomPrice.text.toString()
                        .replace("[^\\d]".toRegex(), "").toLongOrNull()
                    if (amount != null) {
                        if (amount > 0) {
                            postingFlowViewModel.updatePricePerNight(amount)
                        }
                    }

                } else {
                    binding.includePaymentMethod12.etTotalPrice.setText(null)
                }

                // Thêm lại TextWatcher sau khi cập nhật văn bản
                binding.includePaymentMethod12.etRoomPrice.addTextChangedListener(this)
            }


            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    override fun onResume() {
        super.onResume()
    }

}