package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyDashboardActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
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

@AndroidEntryPoint
class MyDashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityMyDashboardBinding
    private val dashboardDataViewmodel: MyDashboardViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

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
        val token = TokenManager(this)
        if (token.isLoggedIn() && token.getAccessToken() != null) {
            observeData()
        } else {
            showErrorToast("Bạn chưa đăng nhập", "Vui lòng đăng nhập để xem thông tin")
        }
        binding.customToolbar.onStartIconClick = {
            finish()
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
}