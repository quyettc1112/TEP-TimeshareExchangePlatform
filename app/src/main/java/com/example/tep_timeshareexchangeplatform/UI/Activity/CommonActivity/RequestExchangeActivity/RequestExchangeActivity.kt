package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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

    }
    private fun getIntentData() {
        val postingId = intent.getIntExtra(Constant.DEFAULT_POSTING_ID, 0)
        if (postingId == 0) {
            finish()
        }
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
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
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
                                        val intent = Intent(this@RequestExchangeActivity, MyTimeshareActivity::class.java)
                                        intent.putExtra(Constant.REQUEST_GET_MY_TIMESHARE, Constant.REQUEST_GET_MY_TIMESHARE)
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

    }

    // Function to bind data
    private fun bindDatsPostingExchange(myTimeshareResponse: ExchangeDetailResponse) {
        if (myTimeshareResponse == null) {
            binding.includeExchangeTimehare.root.visibility = View.GONE
        } else {
            binding.includeExchangeTimehare.root.visibility = View.VISIBLE
            binding.includeExchangeTimehare.apply {
                // Hide button
                btnSelect.visibility = View.GONE

                tvResortName.text = myTimeshareResponse.resortName
                tvRoomType.text = myTimeshareResponse.roomName
                tvCheckinDate.text =
                    Constant.formatDateByLocale(
                        myTimeshareResponse.checkinDate,
                        this@RequestExchangeActivity
                    )
                tvCheckOutDate.text =
                    Constant.formatDateByLocale(
                        myTimeshareResponse.checkoutDate,
                        this@RequestExchangeActivity
                    )
                Glide.with(binding.root.context).load(myTimeshareResponse.unitType.photos)
                    .into(imResortImage)
            }
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
                tvRoomType.text = myTimeshareResponse.roomName
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

                    // Lấy startDateString và endDateString từ ViewModel
                    val startDateString =
                        viewModel.myTimeshareDetail.value?.data?.startDate
                    val endDateString =
                        viewModel.myTimeshareDetail.value?.data?.endDate

                    // Kiểm tra xem startDateString và endDateString có null hay không
                    bindDataCheckInCheckOut(startDateString, endDateString, selectedYear)
                    binding.btnNext.visibility = View.VISIBLE
                    sendButtonNextClick()


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

    private fun callGetValidYearTimeshare(timeShareId: Int) {
        viewModel.getValidYearTimeshare(tokenManager.getAccessToken().toString(), timeShareId)
    }
    private fun callGetMyTimeshareDetail(timeShareId: Int) {
        viewModel.getMyTimeshareDetail(tokenManager.getAccessToken().toString(), timeShareId)
    }
    private fun callSendExchangeRequest(exchangePostingId : Int) {
        val exchangeRequestDTO = ExchangeRequestDTO (
            timeshareId = viewModel.getCurrentTimeshareIdSelected()!!,
            startDate = viewModel.checkinDate.value.toString(),
            endDate = viewModel.checkoutDate.value.toString(),
            exchangePostingId = exchangePostingId
        )
        Log.d("ExchangeRequestDTOValud", exchangeRequestDTO.toString())
        viewModel.callExchangeRequest(tokenManager.getAccessToken().toString(), exchangePostingId, exchangeRequestDTO)
    }
    private fun clickIntentToGetMyTimeshare() {
        binding.btnAddMyTimeshare.setOnClickListener {
            val intent = Intent(this, MyTimeshareActivity::class.java)
            intent.putExtra(Constant.REQUEST_GET_MY_TIMESHARE, Constant.REQUEST_GET_MY_TIMESHARE)
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

    private fun sendButtonNextClick() {
        binding.btnNext.setOnClickListener {
            callSendExchangeRequest(viewModel.exchangePostingDetail.value?.data?.exchangePostingId!!)
        }
    }


    private fun showErrorToast(message: String) {
        MotionToast.Companion.createColorToast(
            this,
            "Error",
            message,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null
        )
    }
    private fun showSuccessToast(message: String) {
        MotionToast.Companion.createColorToast(
            this,
            "Success",
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null
        )
    }

}