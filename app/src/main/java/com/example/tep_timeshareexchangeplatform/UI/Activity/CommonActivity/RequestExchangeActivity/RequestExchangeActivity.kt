package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.ExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRequestExchangeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestExchangeActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestExchangeBinding
    private val viewModel: RequestExchangeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRequestExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getIntentData()

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
        viewModel.exchangePostingDetail.observe(this) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.animationView.visibility = View.GONE
                    resources.data?.let {
                        bindDatsPostingExchange(it)
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    showErrorToast(resources.message.toString())
                    binding.animationView.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.animationView.visibility = View.VISIBLE
                }
            }
        }
    }

    // Function to bind data
    private fun bindDatsPostingExchange(myTimeshareResponse: ExchangeDetailResponse) {
        if (myTimeshareResponse == null) {
            binding.includeMyTimeshare.root.visibility = View.GONE
        } else {
            binding.includeMyTimeshare.root.visibility = View.VISIBLE
            binding.includeMyTimeshare.apply {
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

}