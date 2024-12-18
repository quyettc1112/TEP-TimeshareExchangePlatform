package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangeRequestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.ValidYearResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.MyTimeshareActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangeOption
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRequestExchangeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class RequestExchangeActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestExchangeBinding
    private val viewModel: RequestExchangeViewModel by viewModels()
    private lateinit var selectMyTimeshareActivityResult: ActivityResultLauncher<Intent>
    private lateinit var tokenManager: TokenManager
    private var exchangePostingId: Int = 0
    private var selectedExchangeOption: ExchangeOption? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRequestExchangeBinding.inflate(layoutInflater)
        tokenManager = TokenManager(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getIntentData()
        clickIntentToGetMyTimeshare()
        selectMyTimeshareActivityResult = registerSelectMyTimeshareActivityResult()
        setupTextWatchers()
        eventClickToolbar()

    }

    private fun getIntentData() {
        val postingId = intent.getIntExtra(Constant.DEFAULT_POSTING_ID, 0)
        if (postingId == 0) {
            finish()
        }
        exchangePostingId = postingId
        observeViewModel()
        viewModel.callGetExchangePostingDetail(postingId)

    }

    private fun observeViewModel() {
        // Get Exchange Posting Detail By ID
        viewModel.exchangePostingDetail.observe(this) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.animationViewExchange.visibility = View.GONE
                    resources.data?.let {
                        bindDatsPostingExchange(it)
                    }
                    Log.d(
                        "ExchangePoasdasdstingDetail",
                        resources.data?.exchangePostingId.toString()
                    )
                }

                Status.ERROR -> {
                    showErrorToast(resources.message.toString())
                    binding.animationViewExchange.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.animationViewExchange.visibility = View.VISIBLE
                }
            }
        }

        // Get My Timeshare Detail Selected
        viewModel.myTimeshareDetail.observe(this) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.llAddMyTimeshare.visibility = View.GONE
                    binding.animationViewMyTimeshare.visibility = View.GONE
                    binding.llValidYear.visibility = View.VISIBLE
                    binding.scrollView.post {
                        binding.scrollView.smoothScrollTo(0, binding.llMyTimeshare.top)
                    }
                    resources.data?.let {
                        bindDataMyTimeshare(it)
                    }
                }

                Status.ERROR -> {
                    binding.llAddMyTimeshare.visibility = View.GONE
                    binding.animationViewMyTimeshare.visibility = View.VISIBLE
                    showErrorToast(resources.message.toString())
                }

                Status.LOADING -> {
                    binding.animationViewMyTimeshare.visibility = View.VISIBLE
                    binding.llAddMyTimeshare.visibility = View.GONE
                }
            }
        }

        // Call API get valid year timeshare of Customer
        viewModel.validYearTimeshare.observe(this) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    resources.data?.let {
                        if (it.isEmpty()) {
                            showInfoDialog(this,
                                "Timeshare của bạn hiện không có năm hợp lệ để cho thuê, Xin vui lòng kiem tra lại",
                                object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        val intent = Intent(
                                            this@RequestExchangeActivity,
                                            MyTimeshareActivity::class.java
                                        )
                                        intent.putExtra(
                                            Constant.REQUEST_GET_MY_TIMESHARE,
                                            Constant.REQUEST_GET_MY_TIMESHARE
                                        )
                                        selectMyTimeshareActivityResult.launch(intent)
                                    }
                                })
                        } else {
                            callGetMyTimeshareDetail(viewModel.getCurrentTimeshareIdSelected()!!)
                            bindDataSpinnerValidYear(it)
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast(resources.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Call API to Send Exchange Request
        viewModel.exchangeRequestResponse.observe(this) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    resources.data?.let {
                        showSuccessToast("Gửi yêu cầu trao đổi thành công, Chờ phản hồi từ chủ bài đăng")
                        finish()
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast(resources.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Observer Price Per Night
        viewModel.price.observe(this) { price ->
            if (viewModel.price.value != null) {
                val totalPrice = price
                val value =
                    "${Constant.formatPriceLong(totalPrice)} VNĐ"
                binding.etTotalPrice.setText(value)
            }
        }
    }

    // Function to bind data
    private fun bindDatsPostingExchange(exchangeDetailResponse: ExchangeDetailResponse) {
        if (exchangeDetailResponse == null) {
            binding.includeExchangeTimehare.root.visibility = View.GONE
        } else {
            binding.includeExchangeTimehare.root.visibility = View.VISIBLE
            binding.includeExchangeTimehare.apply {
                // Hide button
                btnSelect.visibility = View.GONE
                tvTitle.text = "Loại phòng: "
                tvResortName.text =
                    exchangeDetailResponse.resortName + " | " + exchangeDetailResponse.roomCode
                tvRoomType.text = exchangeDetailResponse.unitType.title
                tvCheckinDate.text =
                    Constant.formatDateByLocale(
                        exchangeDetailResponse.checkinDate,
                        this@RequestExchangeActivity
                    )
                tvCheckOutDate.text =
                    Constant.formatDateByLocale(
                        exchangeDetailResponse.checkoutDate,
                        this@RequestExchangeActivity
                    )
                Glide.with(binding.root.context).load(exchangeDetailResponse.unitType.photos)
                    .into(imImageTimeshare)
            }

            bindDataPreferExchange(exchangeDetailResponse)
        }
    }

    private fun bindDataMyTimeshare(myTimeshareResponse: MyTimeshareDetailResponse) {
        if (myTimeshareResponse == null) {
            binding.includeMyTimeshare.root.visibility = View.GONE
        } else {
            binding.includeMyTimeshare.root.visibility = View.VISIBLE
            binding.includeMyTimeshare.btnSelect.visibility = View.GONE
            binding.includeMyTimeshare.apply {
                tvResortName.text = myTimeshareResponse.resortName
                tvRoomType.text = myTimeshareResponse.roomCode
                tvCheckinDate.text =
                    Constant.formatDateByLocale(
                        myTimeshareResponse.startDate,
                        this@RequestExchangeActivity
                    )
                tvCheckOutDate.text =
                    Constant.formatDateByLocale(
                        myTimeshareResponse.endDate,
                        this@RequestExchangeActivity
                    )
                Glide.with(binding.root.context).load(myTimeshareResponse.unitType.photos)
                    .into(imResortImage)
            }
        }
    }

    private fun bindDataSpinnerValidYear(validYear: ValidYearResponse) {
        // Chuyển đổi danh sách các năm thành chuỗi (String)
        val yearList = validYear.map { it.toString() }
        Log.d("YearList", yearList.toString())

        // Tạo ArrayAdapter để kết nối dữ liệu với Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yearList)
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

                    // Lấy startDateString và endDateString từ ExchangeOfResortViewModel
                    val startDateString =
                        viewModel.myTimeshareDetail.value?.data?.startDate
                    val endDateString =
                        viewModel.myTimeshareDetail.value?.data?.endDate

                    // Kiểm tra xem startDateString và endDateString có null hay không
                    bindDataCheckInCheckOut(startDateString, endDateString, selectedYear)

                    checkExchangePackage()

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
                val newStartDateString = formatDateByLocale(calendarStart.time, this)
                val startDayOfWeek =
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(calendarStart.time)

                // Lấy ngày tháng và thứ trong tuần cho endDate
                val newEndDateString = formatDateByLocale(calendarEnd.time, this)
                val endDayOfWeek =
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(calendarEnd.time)

                // Định dạng ngày theo kiểu "yyyy-MM-dd"
                val dateFormatDTO = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val checkinDate = dateFormatDTO.format(calendarStart.time)
                val checkoutDate = dateFormatDTO.format(calendarEnd.time)
                viewModel.setCheckinDate(checkinDate)
                viewModel.setCheckoutDate(checkoutDate)

                // Cập nhật UI
                binding.tvCheckInDate.text = newStartDateString
                binding.tvCheckOutDate.text = newEndDateString
                binding.tvCheckInDayOfWeek.text = startDayOfWeek
                binding.tvCheckOutDayOfWeek.text = endDayOfWeek
                binding.etNightsCount.text = "$totalDays"
                viewModel.updateNumberOfNights(totalDays)
            } catch (e: ParseException) {
                e.printStackTrace()
                // Xử lý lỗi khi chuỗi ngày không đúng định dạng
            }
        }
    }

    private fun bindDataExchangePriceOption() {
        // Lắng nghe sự thay đổi trong RadioGroup
        binding.radioGroupExchangeOptions.setOnCheckedChangeListener { _, checkedId ->
            selectedExchangeOption = when (checkedId) {
                binding.radioPayDifferenceToOwner.id -> ExchangeOption.PAY_DIFFERENCE_TO_OWNER
                binding.radioOwnerPaysDifference.id -> ExchangeOption.OWNER_PAYS_DIFFERENCE
                binding.radioNoPaymentNeeded.id -> ExchangeOption.NO_PAYMENT_NEEDED
                else -> null
            }
            selectedExchangeOption?.let {
                handleExchangeOption(it)
                binding.btnNext.visibility = View.VISIBLE
                sendButtonNextClick()
            }
        }

    }

    private fun bindDataPreferExchange(exchangeDetailResponse: ExchangeDetailResponse) {
        binding.apply {
            llPreferExchange.visibility = View.VISIBLE
            tvPreferExchangLocation.text =
                "Tỉnh/Thành Phố: " + exchangeDetailResponse.preferLocation ?: ""
            tvPreferCheckinDate.text =
                exchangeDetailResponse.preferCheckinDate?.let {
                    Constant.getFormattedDate(
                        it,
                        this@RequestExchangeActivity
                    )
                }
            tvPreferCheckoutDate.text =
                exchangeDetailResponse.preferCheckoutDate?.let {
                    Constant.getFormattedDate(
                        it,
                        this@RequestExchangeActivity
                    )
                }
            tvPreferCheckinDayOfWeek.text =
                exchangeDetailResponse.preferCheckinDate?.let {
                    Constant.getDayOfWeek(
                        it,
                        this@RequestExchangeActivity
                    )
                }
            tvPreferCheckoutDayOfWeek.text =
                exchangeDetailResponse.preferCheckoutDate?.let {
                    Constant.getDayOfWeek(
                        it,
                        this@RequestExchangeActivity
                    )
                }
        }
    }

    private fun eventClickToolbar() {
        binding.customToolbar2.onStartIconClick = {
            onBackPressed()
        }

    }

    private fun callGetValidYearTimeshare(timeShareId: Int) {
        viewModel.getValidYearTimeshare(tokenManager.getAccessToken().toString(), timeShareId)
    }

    private fun callGetMyTimeshareDetail(timeShareId: Int) {
        Log.d("TimeSasdasdashareID", timeShareId.toString())
        viewModel.getMyTimeshareDetail(tokenManager.getAccessToken().toString(), timeShareId)
    }

    private fun callSendExchangeRequest(exchangePostingId: Int) {
        var inputPrice: Long = 0
        when (selectedExchangeOption) {
            ExchangeOption.NO_PAYMENT_NEEDED -> {
                viewModel.updatePrice(0)
                inputPrice = viewModel.price.value!!
            }

            ExchangeOption.OWNER_PAYS_DIFFERENCE -> {
                inputPrice = -viewModel.price.value!!
            }

            ExchangeOption.PAY_DIFFERENCE_TO_OWNER -> {
                inputPrice = viewModel.price.value!!
            }

            else -> {
                showErrorToast("Vui lòng chọn phương thức trao đổi")
                return
            }
        }

        if (selectedExchangeOption != ExchangeOption.NO_PAYMENT_NEEDED && inputPrice == 0L) {
            showWarningToast("Lỗi", "Vui lòng nhập giá trị hợp lệ")
            return
        }


        val exchangeRequestDTO = ExchangeRequestDTO(
            timeshareId = viewModel.getCurrentTimeshareIdSelected()!!,
            startDate = viewModel.checkinDate.value.toString(),
            endDate = viewModel.checkoutDate.value.toString(),
            priceValuation = inputPrice,
            note = binding.etNote.text.toString(),
        )
        Log.d("ExchangeRequestDTOValud", exchangeRequestDTO.toString())
        viewModel.callCreateExchangeRequest(
            tokenManager.getAccessToken().toString(),
            exchangePostingId,
            exchangeRequestDTO
        )
    }

    private fun clickIntentToGetMyTimeshare() {
        binding.btnAddMyTimeshare.setOnClickListener {
            val intent = Intent(this, MyTimeshareActivity::class.java)
            intent.putExtra(Constant.REQUEST_GET_MY_TIMESHARE, Constant.REQUEST_GET_MY_TIMESHARE)
            intent.putExtra(Constant.DEFAULT_EXCHANGE_POSTING_ID, exchangePostingId)
            selectMyTimeshareActivityResult.launch(intent)
        }
    }

    private fun registerSelectMyTimeshareActivityResult(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedTimeshareID =
                    data?.getIntExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, 0)
                if (selectedTimeshareID == 0) {
                    showErrorToast("Error")
                } else {
                    callGetValidYearTimeshare(selectedTimeshareID!!)
                    viewModel.setCurrentTimeshareSelected(selectedTimeshareID)
                }
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

    private fun handleExchangeOption(option: ExchangeOption) {
        when (option) {
            ExchangeOption.PAY_DIFFERENCE_TO_OWNER -> {
                binding.llPriceInput.visibility = View.VISIBLE
            }

            ExchangeOption.OWNER_PAYS_DIFFERENCE -> {
                binding.llPriceInput.visibility = View.VISIBLE
            }

            ExchangeOption.NO_PAYMENT_NEEDED -> {
                viewModel.updatePrice(0)
                binding.llPriceInput.visibility = View.GONE
            }
        }
    }

    private fun sendButtonNextClick() {
        binding.btnNext.setOnClickListener {
            callSendExchangeRequest(viewModel.exchangePostingDetail.value?.data?.exchangePostingId!!)
        }
    }

    private fun setupTextWatchers() {
        // Username TextWatcher
        binding.etRoomPrice.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable?) {


                // Loại bỏ TextWatcher tạm thời để tránh loop
                binding.etRoomPrice.removeTextChangedListener(this)

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
                        numericValue < 10000 -> {
                            // Hiển thị helper text nếu số tiền nhỏ hơn 100.000
                            binding.tilRoomPrice.helperText =
                                "Số tiền tối thiểu là 10.000"
                        }

                        numericValue > 100_000_000 -> {
                            // Hiển thị helper text nếu số tiền lớn hơn 100 Triệu
                            binding.tilRoomPrice.helperText =
                                "Số tiền tối đa cho 1 đêm là 100 triệu"
                        }

                        else -> {
                            // Ẩn helper text khi số tiền đạt yêu cầu
                            binding.tilRoomPrice.helperText = null
                        }
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " đ"
                    current = formatted
                    binding.etRoomPrice.setText(formatted)
                    binding.etRoomPrice.setSelection(formatted.length - 2) // Đặt con trỏ vào vị trí trước "đ"
                    val amount = binding.etRoomPrice.text.toString()
                        .replace("[^\\d]".toRegex(), "").toLongOrNull()
                    if (amount != null) {
                        if (amount > 0) {
                            viewModel.updatePrice(amount)
                            viewModel.updatePriceForRequest(amount)
                        }
                    }

                } else {
                    binding.etTotalPrice.setText(null)
                }

                // Thêm lại TextWatcher sau khi cập nhật văn bản
                binding.etRoomPrice.addTextChangedListener(this)
            }


            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    private fun checkExchangePackage() {
        val exchangePackageEnum =
            ExchangePackageEnum.getPackageById(viewModel.exchangePostingDetail.value?.data?.exchangePackageId!!)
        when (exchangePackageEnum) {
            ExchangePackageEnum.BASIC_SERVICE.packageModel -> {
                binding.llExchangeMethod.visibility = View.GONE
                binding.llPriceInput.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
                viewModel.updatePrice(0)
                selectedExchangeOption = ExchangeOption.NO_PAYMENT_NEEDED
                sendButtonNextClick()
            }

            ExchangePackageEnum.ADVANCED_SERVICE.packageModel -> {
                binding.llExchangeMethod.visibility = View.VISIBLE
                bindDataExchangePriceOption()
            }
        }
    }

    private fun showErrorToast(message: String) {
        MotionToast.Companion.createColorToast(
            this,
            "Thất Bại",
            message,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        )
    }

    private fun showSuccessToast(message: String) {
        MotionToast.Companion.createColorToast(
            this,
            "Thành Công",
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}