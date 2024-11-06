package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBookingDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityBookingDetailBinding
    private lateinit var token: TokenManager
    private val viewModel: BookingDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        token = TokenManager(this)
        getIntentData()

        binding.toolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun getIntentData() {
        val bookingId = intent.getIntExtra(Constant.DEFAULT_MY_BOOKING_SELECTED_ID, 0)
        if (bookingId == 0 || !token.isLoggedIn()) {
            finish()
        } else {
            viewModel.getMyBookingDetail(token.getAccessToken().toString(), bookingId)
            observeData()
        }
    }

    private fun observeData() {
        viewModel.getMyBookingDetailResponse.observe(this) { resources ->
            when(resources.status) {
                Status.LOADING -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                }

                Status.SUCCESS -> {
                    binding.shimmerViewContainer.hideShimmer()
                    bindData(resources.data!!)
                    Log.d("Check Data Booking Detail", resources.data.toString())
                }

                Status.ERROR -> {
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.shimmerViewContainer.stopShimmer()
                    MotionToast.createToast(
                        this,
                        "Error",
                        resources.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }
    }

    // Bind Data Section
    private fun bindData(data: MyBookingDetailResponse) {
        // Bind Data Room Reservation
        binding.apply {
            tvRoomReservationCode.text = "Mã đặt phòng: ${data.id}"

            // Check in Date
            tvCheckinDate.text = Constant.getFormattedDate(data.checkinDate, this@BookingDetailActivity)
            tvCheckinDayOfWeek.text = Constant.getDayOfWeek(data.checkinDate, this@BookingDetailActivity)

            // Check Out date
            tvCheckoutDate.text = Constant.getFormattedDate(data.checkoutDate, this@BookingDetailActivity)
            tvCheckoutDayOfWeek.text = Constant.getDayOfWeek(data.checkoutDate, this@BookingDetailActivity)
        }

        // Bind Data Guest Information
        binding.apply {
            etFullName.setText(data.primaryGuestName)
            etPhoneNumber.setText(data.primaryGuestPhone)
            etEmail.setText(data.primaryGuestEmail)
        }

        // Bind Data Detail Billing
        val binding_detail_billing = binding.includeDetailBilling
        binding_detail_billing.apply {
            // Hide Unnecessary View
            llPostingBy.visibility = View.GONE
            // Show Necessary View
            llFeePrice.visibility = View.VISIBLE

            // Bind Data
            tvResortNameDtb.text = data.rentalPosting.roomInfo.unitType.resortResortName + " - " + data.rentalPosting.roomInfo.unitType.title
            tvLocation.text = data.rentalPosting.roomInfo.unitType.resortAddress
            tvNumberNight.text = data.totalNights.toString()
            tvCheckInDate.text = Constant.formatDateByLocale(data.checkinDate, this@BookingDetailActivity)
            tvCheckOutDate.text = Constant.formatDateByLocale(data.checkoutDate, this@BookingDetailActivity)

            // Cancel Policy
            binding_detail_billing.apply {
                if (data.rentalPosting.cancellationType == null) {
                    tvCancellationPolicy.text = "Không có"
                } else {
                    val refundPolicy = RefundPolicy.getShortDescriptionFromName(
                        this@BookingDetailActivity,
                        data.rentalPosting.cancellationType.name.toString()
                    )
                    tvCancellationPolicy.text = refundPolicy
                }
            }

            tvRoomPricePerNight.text = Constant.formatPrice(data.pricePerNights)
            tvEstimatedTotalPrice.text = Constant.formatPrice(data.totalPrice)
            tvFeePrice.text = Constant.formatPrice(data.serviceFee)


        }



    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}