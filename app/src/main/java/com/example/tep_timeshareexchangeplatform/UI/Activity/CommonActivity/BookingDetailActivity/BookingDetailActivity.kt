package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetailActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomFeedbackDialog.CustomFeedbackDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ImagePostingAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyBookingStatus
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

        // Posting feedback
        viewModel.feedbackResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                   hideLoadingWaiting()
                    showDoneFeedbackDialog(this,
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                binding.llFeedbackContainer.visibility = View.GONE
                            }
                        })
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    Log.d("Check asdasdasd", it.message.toString())
                    it.message?.let {
                        MotionToast.Companion.createColorToast(
                            this,
                            "Error",
                            it,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )
                    }
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
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

            // Booking Type
            binding.tvBookingTupe.text = MyBookingStatus.fromApiStatus(data.source)?.getDescription(binding.root.context)

            // Status
            bindStatus(data)


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

            // Image
            Glide.with(this@BookingDetailActivity)
                .load(data.rentalPosting.roomInfo.unitType.photos)
                .error(R.drawable.im_material_mn)
                .placeholder(R.drawable.ripple_effect_white)
                .into(imImageTimeshare)
        }



    }

    private fun bindStatus(data: MyBookingDetailResponse) {
        when (MyBookingStatus.fromApiStatus(data.status)) {
            MyBookingStatus.BOOKED -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.primaryColor,
                    R.color.white
                )
                binding.llFeedbackContainer.visibility = View.GONE
            }

            MyBookingStatus.CHECK_IN -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.blue_btn_search,
                    R.color.white
                )
                binding.llFeedbackContainer.visibility = View.GONE
            }

            MyBookingStatus.CHECKOUT -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.green_verify,
                    R.color.white
                )
                // Feedback
                if (!data.isFeedback) {
                    binding.llFeedbackContainer.visibility = View.VISIBLE
                } else {
                    binding.llFeedbackContainer.visibility = View.GONE
                }
                onFeedbackClick(data)
                binding.llFeedbackContainer.visibility = View.VISIBLE
            }

            MyBookingStatus.NO_SHOW -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.status_unknown_bg,
                    R.color.status_unknown_text
                )
                binding.llFeedbackContainer.visibility = View.GONE
            }

            MyBookingStatus.CANCELED -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.white,
                    R.color.status_rejected_text
                )
                binding.llFeedbackContainer.visibility = View.GONE
            }

            MyBookingStatus.REFUND -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.white,
                    R.color.status_rejected_text
                )
                binding.llFeedbackContainer.visibility = View.GONE
            }

            MyBookingStatus.PAYMENT_COMPLETED -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.white,
                    R.color.status_pending_approval_text
                )
                binding.llFeedbackContainer.visibility = View.GONE
            }

            else -> {
                // Default or unknown status case
                applyStatusStyle(
                    binding.root.context,
                    R.color.status_unknown_bg,
                    R.color.status_unknown_text
                )
            }
        }
    }

    private fun onFeedbackClick(data: MyBookingDetailResponse) {
        binding.llFeedbackContainer.setOnClickListener {
            val feedbackDialog = CustomFeedbackDialog(this) { rating, feedback ->
                callSendFeedBack(rating, feedback, data.id) // Gọi hàm xử lý feedback
            }

            feedbackDialog.show()
        }
    }

    private fun callSendFeedBack(rating: Int, feedback: String, bookingId: Int) {
        if (!token.isLoggedIn()) {
            MotionToast.Companion.createColorToast(
                this,
                "Error",
                "Bạn cần đăng nhập để thực hiện chức năng này",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
            return
        }

        val feedbackDTO = FeedbackDTO(rating, feedback, bookingId)
        if (feedbackDTO.bookingId !== 0 && feedbackDTO.ratingPoint !== 0) {
            viewModel.postFeedback(token.getAccessToken().toString(), feedbackDTO)
        } else {
            MotionToast.Companion.createColorToast(
                this,
                "Error",
                "Vui lòng nhập đầy đủ thông tin",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
        }
    }
    private fun applyStatusStyle(context: Context, backgroundColorRes: Int, textColorRes: Int) {
        binding.apply {
            // Nền
            llStatusContainer.backgroundTintList = context.getColorStateList(backgroundColorRes)

            // TExt
            tvStatus.setTextColor(context.getColor(textColorRes))

            // Stroke
            cardStatus.setStrokeColor(context.getColorStateList(R.color.white))

            // Background
            cardStatus.backgroundTintList = (context.getColorStateList(backgroundColorRes))
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}