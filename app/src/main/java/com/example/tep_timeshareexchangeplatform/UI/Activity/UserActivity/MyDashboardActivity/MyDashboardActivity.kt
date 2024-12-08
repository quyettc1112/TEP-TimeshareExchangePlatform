package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyDashboardActivity

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DailySummaryDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.DashboardDataResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.Adapter.MemberShipAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity.Companion.POSTING_PAGE_SIZE
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMemberShipBinding
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MyDashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityMyDashboardBinding
    private val dashboardDataViewmodel: MyDashboardViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    private var startDate: String? = null
    private var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)
        initializeDates()
        getIntentValue();
        observeData();
        binding.customToolbar.onStartIconClick = {
            finish()
        }
        DatePicker()

    }

    private fun DatePicker() {
        val calendar = Calendar.getInstance()

        val dateSetListenerStart =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                startDate =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                        selectedDate.time
                    )
                binding.startDateEditText.setText(
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
                )
                if (!validateDates()) {
                    startDate = null
                    binding.startDateEditText.text.clear()
                }
            }

        binding.startDateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListenerStart,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.calendarStartIcon.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListenerStart,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set up DatePickerDialog for End Date when EditText or ImageView is clicked
        val dateSetListenerEnd = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            endDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                selectedDate.time
            )

            binding.endDateEditText.setText(
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
            )
            if (!validateDates()) {
                endDate = null
                binding.endDateEditText.text.clear()
            }
            getIntentValue()
        }

        binding.endDateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListenerEnd,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.calendarEndIcon.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListenerEnd,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun initializeDates() {
        val calendar = Calendar.getInstance()

        // Set endDate to today
        val today = calendar.time
        endDate =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(today)

        // Set startDate to 3 days back
        calendar.add(Calendar.DAY_OF_YEAR, -3)
        val threeDaysBack = calendar.time
        startDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
            threeDaysBack
        )

        // Update the EditText fields for UI, if needed
        binding.startDateEditText.setText(
            SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).format(threeDaysBack)
        )
        binding.endDateEditText.setText(
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                today
            )
        )
    }

    private fun getIntentValue() {

        if (tokenManager.isLoggedIn() && tokenManager.getAccessToken() != null) {
            if (validateDates()) {
                dashboardDataViewmodel.getDashboardData(tokenManager.getAccessToken().toString())
                dashboardDataViewmodel.getDailySummaryData(
                    tokenManager.getAccessToken().toString(),
                    startDate!!,
                    endDate!!
                )
            }
        } else {
            showWarningToast("Bạn chưa đăng nhập", "Vui lòng đăng nhập để xem thông tin")
        }
    }

    private fun observeData() {
        dashboardDataViewmodel.dashboardData.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindData(it.data!!)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message.toString().contains("404")) {
                        Log.d("CheckError", it.message.toString() + " " + it.message.toString())
                    }
                }
            }
        }
        dashboardDataViewmodel.dailySummaryData.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    bindDataSummary(it.data!!)
                    setupBarChart(it.data.revenueCostByDateDtos)
                    Log.d("BARDATA: ", it.data.revenueCostByDateDtos.toString())
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message.toString().contains("404")) {
                        Log.d("CheckError", it.message.toString() + " " + it.message.toString())
                    }
                    Log.d("ERROR_CHECK_DATE", it.message.toString())
                }
            }
        }
    }

    private fun bindData(dashboardData: DashboardDataResponse) {
        binding.apply {
            tvTotalPost.text = dashboardData.totalPosting.toString();
            tvTotalRentalRenter.text = dashboardData.totalRentalRenter.toString();
            tvTotalExchangeRenter.text = dashboardData.totalExchangerRenter.toString();
            tvTotalRequest.text = dashboardData.totalRequest.toString();
            tvTotalBooking.text = dashboardData.totalBooking.toString();
        }

    }

    private fun bindDataSummary(dailySummaryData: DailySummaryDataResponse) {
        binding.apply {
            revenueText1.text = Constant.formatPriceLong(dailySummaryData.totalRevenue)
            revenueText2.text = Constant.formatPriceLong(dailySummaryData.totalCosts)
        }

    }

    private fun setupBarChart(revenueCostByDateDtos: List<DailySummaryDataResponse.RevenueCostByDateDto>) {
        val barEntriesRevenue = mutableListOf<BarEntry>()
        val barEntriesCosts = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        revenueCostByDateDtos.forEachIndexed { index, dto ->
            barEntriesRevenue.add(BarEntry(index.toFloat(), dto.revenueByDate.toFloat()))
            barEntriesCosts.add(BarEntry(index.toFloat(), dto.revenueByCosts.toFloat()))
            labels.add(parseDate(dto.date))
        }

        val dataSetRevenue = BarDataSet(barEntriesRevenue, "Doanh thu").apply {
            color = Color.parseColor("#FF29B6F6")
            valueTextColor = Color.BLACK
            valueTextSize = 12f
        }

        val dataSetCosts = BarDataSet(barEntriesCosts, "Chi phí").apply {
            color = Color.parseColor("#ff6c2f")
            valueTextColor = Color.BLACK
            valueTextSize = 12f
        }

        // Combine datasets into BarData
        val barData = BarData(dataSetRevenue, dataSetCosts).apply {
            barWidth = 0.5f
        }

        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.BLACK

        binding.barChart.axisLeft.textColor = Color.BLACK
        binding.barChart.axisRight.isEnabled = false

        binding.barChart.data = barData
        binding.barChart.description.isEnabled = false
        binding.barChart.setFitBars(true)
        binding.barChart.legend.isEnabled = true
        binding.barChart.invalidate()
    }

    private fun parseDate(dateString: String): String {
        return try {

            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            Log.e("ParseDate", "Error parsing date: ${e.message}")
            dateString
        }
    }

    private fun validateDates(): Boolean {
        if (startDate == null || endDate == null) {
            showWarningToast("Ngày nhập không hợp lệ", "Vui lòng nhập ngày bắt đầu và kết thúc")
            return false
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return try {
            val start = dateFormat.parse(startDate!!)
            val end = dateFormat.parse(endDate!!)
            if (start.after(end)) {
                showWarningToast(
                    "Ngày nhập không hợp lệ",
                    "Ngày kết thúc phải lớn hơn ngày bắt đầu"
                )
                false
            } else {
                true
            }
        } catch (e: Exception) {
            Log.e("DateValidation", "Error parsing dates: ${e.message}")
            false
        }
    }



}