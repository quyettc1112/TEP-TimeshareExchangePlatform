package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetailActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomFeedbackDialog.CustomFeedbackDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
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
        val rentalBookingId = intent.getIntExtra(Constant.DEFAULT_MY_BOOKING_RENTAL, 0)
        val exchangeBookingId = intent.getIntExtra(Constant.DEFAULT_MY_BOOKING_EXCHANGE, 0)

        if (!token.isLoggedIn()) {
            finish()
            return
        }

        when {
            rentalBookingId != 0 -> {
                viewModel.getMyBookingRentalDetail(
                    token.getAccessToken().toString(),
                    rentalBookingId
                )
            }

            exchangeBookingId != 0 -> {
                viewModel.getMyBookingExchangeDetail(
                    token.getAccessToken().toString(),
                    exchangeBookingId
                )
            }

            else -> {
                finish()
            }
        }

        observeData()
    }

    private fun observeData() {
        viewModel.getMyBookingRentalDetailResponse.observe(this) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                }

                Status.SUCCESS -> {
                    binding.shimmerViewContainer.hideShimmer()
                    bindDataRental(resources.data!!)
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

        viewModel.getMyBookingExchangeDetailResponse.observe(this) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                }

                Status.SUCCESS -> {
                    binding.shimmerViewContainer.hideShimmer()
                    bindDataExchange(resources.data!!)
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
        viewModel.feedbackRentalResponse.observe(this) {
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
    private fun bindDataRental(data: MyBookingRentalDetailResponse) {
        // Bind Data Room Reservation
        binding.apply {
            tvRoomReservationCode.text = "Mã đặt phòng: ${data.id}"

            // Check in Date
            tvCheckinDate.text =
                Constant.getFormattedDate(data.checkinDate, this@BookingDetailActivity)
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(data.checkinDate, this@BookingDetailActivity)

            // Check Out date
            tvCheckoutDate.text =
                Constant.getFormattedDate(data.checkoutDate, this@BookingDetailActivity)
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(data.checkoutDate, this@BookingDetailActivity)

            // Booking Type
            binding.tvBookingTupe.text =
                MyBookingStatus.fromApiStatus(data.source)?.getDescription(binding.root.context)

            // Status
            bindBookingStatus(data.id,data.status, data.isFeedback, data.source)


        }

        // Bind Data Guest Information
        binding.apply {
            etFullName.setText(data.primaryGuestName)
            etPhoneNumber.setText(data.primaryGuestPhone)
            etEmail.setText(data.primaryGuestEmail)
        }


        if (data.source == "rental") {
            Glide.with(binding.root.context).load(R.drawable.ic_rental_booking).into(binding.ivBookingType)
        } else {
            Glide.with(binding.root.context).load(R.drawable.ic_exchange_booking).into(binding.ivBookingType)
        }



        // Bind Data Detail Billing
        val binding_detail_billing = binding.includeDetailBilling
        binding_detail_billing.apply {
            // Hide Unnecessary View
            llPostingBy.visibility = View.GONE
            // Show Necessary View
            llFeePrice.visibility = View.VISIBLE

            // Bind Data
            tvResortNameDtb.text =
                data.rentalPosting.roomInfo.unitType.resortResortName + " - " + data.rentalPosting.roomInfo.unitType.title
            tvLocation.text = data.rentalPosting.roomInfo.unitType.resortAddress
            tvNumberNight.text = data.totalNights.toString()
            tvCheckInDate.text =
                Constant.formatDateByLocale(data.checkinDate, this@BookingDetailActivity)
            tvCheckOutDate.text =
                Constant.formatDateByLocale(data.checkoutDate, this@BookingDetailActivity)

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

    private fun bindDataExchange(data: MyBookingExchangeDetailResponse) {
        // Bind Data Room Reservation
        binding.apply {
            tvRoomReservationCode.text = "Mã đặt phòng: ${data.id}"

            // Check in Date
            tvCheckinDate.text =
                Constant.getFormattedDate(data.checkinDate, this@BookingDetailActivity)
            tvCheckinDayOfWeek.text =
                Constant.getDayOfWeek(data.checkinDate, this@BookingDetailActivity)

            // Check Out date
            tvCheckoutDate.text =
                Constant.getFormattedDate(data.checkoutDate, this@BookingDetailActivity)
            tvCheckoutDayOfWeek.text =
                Constant.getDayOfWeek(data.checkoutDate, this@BookingDetailActivity)

            // Booking Type
            binding.tvBookingTupe.text =
                MyBookingStatus.fromApiStatus(data.source)?.getDescription(binding.root.context)

            // Status
            data.isFeedback?.let { bindBookingStatus(data.id,data.status, it, data.source) }


        }


        if (data.source == "rental") {
            Glide.with(binding.root.context).load(R.drawable.ic_rental_booking).into(binding.ivBookingType)
        } else {
            Glide.with(binding.root.context).load(R.drawable.ic_exchange_booking).into(binding.ivBookingType)
        }

        // Bind Data Guest Information
        binding.apply {
            etFullName.setText(data.primaryGuestName)
            etPhoneNumber.setText(data.primaryGuestPhone)
            etEmail.setText(data.primaryGuestEmail)
        }

        binding.includeDetailBilling.root.visibility = View.VISIBLE
        // Bind Data Detail Billing
        val binding_detail_billing = binding.includeDetailBilling
        binding_detail_billing.apply {
            // Hide Unnecessary View
            llPostingBy.visibility = View.GONE
            // Show Necessary View
            llFeePrice.visibility = View.VISIBLE

            // Bind Data
            tvResortNameDtb.text =
                data.roomInfo.unitType.resortName + " - " + data.roomInfo.unitType.title
            tvLocation.text = ""
            tvNumberNight.text = ""
            tvCheckInDate.text =
                Constant.formatDateByLocale(data.checkinDate, this@BookingDetailActivity)
            tvCheckOutDate.text =
                Constant.formatDateByLocale(data.checkoutDate, this@BookingDetailActivity)

            // Cancel Policy
            binding_detail_billing.apply {
                llCancellationPolicy.visibility = View.GONE
                llRoomPricing.visibility = View.GONE
            }

            tvFeePrice.text = data.serviceFee?.let { Constant.formatPriceLong(it) }

            // Image
            Glide.with(this@BookingDetailActivity)
                .load(data.roomInfo.unitType.photos)
                .error(R.drawable.im_material_mn)
                .placeholder(R.drawable.ripple_effect_white)
                .into(imImageTimeshare)
        }


    }

    private fun bindBookingStatus(bookingId: Int, status: String, isFeedback: Boolean, source: String) {
        when (MyBookingStatus.fromApiStatus(status)) {
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
                if (isFeedback) {
                    binding.llFeedbackContainer.visibility = View.GONE
                } else {
                    binding.llFeedbackContainer.visibility = View.VISIBLE
                }
                onFeedbackClick(bookingId, source)
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

    private fun onFeedbackClick(bookingId: Int, source: String) {
        binding.llFeedbackContainer.setOnClickListener {
            if (source == "rental") {
                val feedbackDialog = CustomFeedbackDialog(this) { rating, feedback ->
                    callSendFeedBackRental(rating, feedback, bookingId) // Gọi hàm xử lý feedback
                }
                feedbackDialog.show()
            } else {
                val feedbackDialog = CustomFeedbackDialog(this) { rating, feedback ->
                    callSendFeedBackExchange(rating, feedback, bookingId) // Gọi hàm xử lý feedback
                }
                feedbackDialog.show()
            }


        }
    }

    private fun callSendFeedBackRental(rating: Int, feedback: String, bookingId: Int) {
        if (!token.isLoggedIn()) {
            showFailToast("Bạn cần đăng nhập để thực hiện chức năng này")
            return
        }

        val feedbackDTO = FeedbackDTO(rating, feedback, bookingId)
        if (feedbackDTO.bookingId !== 0 && feedbackDTO.ratingPoint !== 0) {
            viewModel.postFeedbackRental(token.getAccessToken().toString(), feedbackDTO)
        } else {
            showFailToast("Vui lòng nhập đầy đủ thông tin")
        }
    }

    private fun callSendFeedBackExchange(rating: Int, feedback: String, bookingId: Int){
        if (!token.isLoggedIn()) {
           showFailToast("Bạn cần đăng nhập để thực hiện chức năng này")
            return
        }

        val feedbackDTO = FeedbackDTO(rating, feedback, bookingId)
        if (feedbackDTO.bookingId !== 0 && feedbackDTO.ratingPoint !== 0) {
            viewModel.postFeedbackExchange(token.getAccessToken().toString(), feedbackDTO)
        } else {
           showFailToast("Vui lòng nhập đầy đủ thông tin")
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

    private fun showSuccessToast(message: String) {
        MotionToast.createToast(
            this,
            "Thành Công",
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        )
    }

    private fun showFailToast(message: String) {
        MotionToast.createToast(
            this,
            "Lỗi",
            message,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        )
    }

}