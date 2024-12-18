package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment.BookingDetailActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomFeedbackDialog.CustomFeedbackDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.UpdateExchangeBookingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingExchangeDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberInfoDialog
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyBookingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.NotificationHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityBookingDetailBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogCancellcationPolicyBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class BookingDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityBookingDetailBinding
    private lateinit var token: TokenManager
    private lateinit var notificationHelper: NotificationHelper
    private val viewModel: BookingDetailViewModel by viewModels()
    private var exchangeBookingId = 0

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
        notificationHelper = NotificationHelper(this)
        getIntentData()
        eventClickCancelBooking()
        eventClickUpdateBookingExchange()

        binding.toolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun getIntentData() {

        val rentalBookingId = intent.getIntExtra(Constant.DEFAULT_MY_BOOKING_RENTAL, 0)
        Log.d("Check Data Booking Detail", rentalBookingId.toString())
        exchangeBookingId = intent.getIntExtra(Constant.DEFAULT_MY_BOOKING_EXCHANGE, 0)

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
                    Log.d("Check Data Booking Detail", resources.data.status.toString())
                }

                Status.ERROR -> {
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.shimmerViewContainer.stopShimmer()
                    Log.d("Check Data Booking Detail", resources.message.toString())
                    MotionToast.createToast(
                        this,
                        "Lỗi Tải Dữ Liệu",
                        resources.message.toString(),
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_bold)
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
                }

                Status.ERROR -> {
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.shimmerViewContainer.stopShimmer()
                    MotionToast.createToast(
                        this,
                        "Lỗi Tải Dữ Liệu",
                        resources.message.toString(),
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_bold)
                    )
                }
            }
        }

        // Check Cancel Booking
        viewModel.cancelBookingResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    viewModel.callGetCustomerProfile(token.getAccessToken().toString())
                }
                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailToast(it.message.toString())
                    Log.d("Chgeckasfasda", it.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Fetch Customer Profile
        viewModel.getCustomerProfileResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    token.saveProfileInfo(it.data!!)
                    showSuccessToast("Hủy đặt phòng thành công")
                    Log.d("Checkasfasda", it.data.toString())
                    notificationHelper.makeNotification(
                        this,
                        "Hủy đặt phòng thành công",
                        "Đặt phòng của bạn đã được hủy thành công"
                    )
                    finish()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailToast(it.message.toString())
                    Log.d("Chgeckasfasda", it.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
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

        viewModel.feedbackExchangeResponse.observe(this) {
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
                        showFailToast(it)
                    }
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }

        // Update Exchange Booking
        viewModel.updateExchangeBookingInfoResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessToast("Cập nhật thông tin thành công")
                    viewModel.getMyBookingExchangeDetail(
                        token.getAccessToken().toString(),
                        exchangeBookingId
                    )
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showFailToast(it.message.toString())
                    Log.d("Chgeckasfasda", it.message.toString())
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        }
    }

    // Event Click
    private fun eventClickCancelBooking() {
        binding.btnCancelBooking.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                this,
                R.style.MyBottomSheetDialogTheme
            ) // Use `requireContext()` if in Fragment

            // Inflate the bottom sheet layout using View Binding
            val bottomSheetBinding = DialogCancellcationPolicyBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)

            // Set up actions for buttons inside the bottom sheet
            bottomSheetBinding.btnCancelBooking.setOnClickListener {
                val bookingId = viewModel.getMyBookingRentalDetailResponse.value?.data?.id
                callCancelBooking(bookingId!!)
                bottomSheetDialog.dismiss()
            }

            // Bind Cancellation Type
            val data = viewModel.getMyBookingRentalDetailResponse.value
            if (data?.data?.rentalPosting?.cancellationType == null) {
                bottomSheetBinding.tvContentCancellationPolicy.text = "Không có"
            } else {
                val refundPolicy = RefundPolicy.getShortDescriptionFromName(
                    this@BookingDetailActivity,
                    data?.data?.rentalPosting?.cancellationType.name.toString()
                )
                bottomSheetBinding.tvContentCancellationPolicy.text = refundPolicy
            }



            bottomSheetBinding.btnNone.setOnClickListener {
                // Dismiss the dialog
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()
        }
    }

    private fun eventClickUpdateBookingExchange() {
        binding.btnUpdateBookingExchange.setOnClickListener {
            val dialogUpdateCustomer =
                UpdateExchangeBookingDialog(this, token,
                    object : UpdateExchangeBookingDialog.ConfirmCallback {
                        override fun positiveAction(updateExchangeBookingDTO: UpdateExchangeBookingDTO) {
                            Log.d("CheckUpdateBooking", updateExchangeBookingDTO.toString())
                            callUpdateExchangeInfo(updateExchangeBookingDTO)
                        }
                    })
            dialogUpdateCustomer.show()
        }
    }

    // Bind Data Section
    private fun bindDataRental(data: MyBookingRentalDetailResponse) {
        // Bind Data Room Reservation
        binding.apply {
            tvRoomReservationCode.text = "Mã đặt phòng: ${data.id}"

            // Check in Date
            tvCheckinDate.text =
                data.checkinDate?.let { Constant.getFormattedDate(it, this@BookingDetailActivity) }
            tvCheckinDayOfWeek.text =
                data.checkinDate?.let { Constant.getDayOfWeek(it, this@BookingDetailActivity) }

            // Check Out date
            tvCheckoutDate.text =
                data.checkoutDate?.let { Constant.getFormattedDate(it, this@BookingDetailActivity) }
            tvCheckoutDayOfWeek.text =
                data.checkoutDate?.let { Constant.getDayOfWeek(it, this@BookingDetailActivity) }

            // Booking Type
            binding.tvBookingTupe.text =
                MyBookingStatus.fromApiStatus(data.source)?.getDescription(binding.root.context)

            // Status
            bindBookingStatus(data.id, data.status, data.isFeedback, data.source)

        }

        // Bind Data Guest Information
        binding.apply {
            etFullName.setText(data.primaryGuestName)
            etPhoneNumber.setText(data.primaryGuestPhone)
            etEmail.setText(data.primaryGuestEmail)
        }

        // Cancellatioon
        val cancellationType: RefundPolicy? =
            RefundPolicy.getRefundPolicyById(data.rentalPosting.cancellationType.id)
        Log.d("Check Cancellation Type", cancellationType.toString())
        if (cancellationType?.let {
                val formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy") // Định dạng chuỗi tùy chỉnh
                val checkInDate =
                    LocalDate.parse(data.checkinDate, formatter) // Chuyển String thành LocalDate
                canCancelBooking(checkInDate, it)
            } == true) {
            binding.btnCancelBooking.visibility = View.VISIBLE
            // Show Cancel Booking
            showCancelBooking(data.status)
        } else {
            binding.btnCancelBooking.visibility = View.GONE
        }


        // Type Booking
        if (data.source == "rental") {
            Glide.with(binding.root.context).load(R.drawable.ic_rental_booking)
                .into(binding.imBookingType)
        } else {
            Glide.with(binding.root.context).load(R.drawable.ic_exchange_booking)
                .into(binding.imBookingType)
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
                data.rentalPosting.roomInfo.unitType.resortResortName + " - Mã Phòng: " + data.rentalPosting.roomInfo.roomInfoCode
            tvLocation.text = data.rentalPosting.roomInfo.unitType.location.displayName
            tvNumberNight.text = data.totalNights.toString()
            tvCheckInDate.text =
                data.checkinDate?.let {
                    Constant.formatDateByLocale(
                        it,
                        this@BookingDetailActivity
                    )
                }
            tvCheckOutDate.text =
                data.checkoutDate?.let {
                    Constant.formatDateByLocale(
                        it,
                        this@BookingDetailActivity
                    )
                }

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

            tvRoomPricePerNight.text =
                data.pricePerNights?.let { Constant.formatPriceLong(it) } + " VNĐ"
            tvEstimatedTotalPrice.text =
                data.totalPrice?.let { Constant.formatPriceLong(it) } + " VNĐ (${data.totalNights} đêm)"
            tvFeePrice.text = data.serviceFee?.let { Constant.formatPriceLong(it) } + " VNĐ"

            // Image
            Glide.with(this@BookingDetailActivity)
                .load(data.rentalPosting.roomInfo.unitType.photos)
                .error(R.drawable.im_material_mn)
                .placeholder(R.drawable.ripple_effect_white)
                .into(imImageTimeshare)
            binding.includeDetailBilling.llRoomPricing.visibility = View.VISIBLE
        }

        // Status
        bindBookingStatus(data.id, data.status, data.isFeedback, data.source)

        binding.llTypeBookingContainer.visibility = View.VISIBLE
    }

    private fun bindDataExchange(data: MyBookingExchangeDetailResponse) {
        // Bind Data Room Reservation
        Log.d("Check Data Exchange", data.status.toString())
        Log.d("Check Data Exchange", data.id.toString())
        binding.apply {
            tvRoomReservationCode.text = "Mã đặt phòng: ${data.id}"

            // Check in Date
            tvCheckinDate.text =
                data.checkinDate?.let { Constant.getFormattedDate(it, this@BookingDetailActivity) }
            tvCheckinDayOfWeek.text =
                data.checkinDate?.let { Constant.getDayOfWeek(it, this@BookingDetailActivity) }

            // Check Out date
            tvCheckoutDate.text =
                data.checkoutDate?.let { Constant.getFormattedDate(it, this@BookingDetailActivity) }
            tvCheckoutDayOfWeek.text =
                data.checkoutDate?.let { Constant.getDayOfWeek(it, this@BookingDetailActivity) }

            // Booking Type
            binding.tvBookingTupe.text =
                MyBookingStatus.fromApiStatus(data.source)?.getDescription(binding.root.context)

            // Status
            bindBookingStatus(data.id, data.status, data.isFeedback ?: false, data.source)


        }
        if (data.source == "rental") {
            Glide.with(binding.root.context).load(R.drawable.ic_rental_booking)
                .into(binding.imBookingType)
        } else {
            Glide.with(binding.root.context).load(R.drawable.ic_exchange_booking)
                .into(binding.imBookingType)
        }

        // Bind Data Guest Information
        binding.apply {
            etFullName.setText(data.primaryGuestName)
            etPhoneNumber.setText(data.primaryGuestPhone)
            etEmail.setText(data.primaryGuestEmail)
        }


        // Bind Data Detail Billing
        binding.includeDetailBilling.root.visibility = View.VISIBLE
        val binding_detail_billing = binding.includeDetailBilling
        binding_detail_billing.apply {
            // Hide Unnecessary View
            llPostingBy.visibility = View.GONE
            // Show Necessary View
            llFeePrice.visibility = View.VISIBLE

            // Bind Data
            tvResortNameDtb.text =
                data.roomInfo.unitType.resortName + " - " + data.roomInfo.unitType.title
            tvLocation.text = data.roomInfo.unitType.location.displayName
            tvNumberNight.text = ""
            tvCheckInDate.text =
                data.checkinDate?.let {
                    Constant.formatDateByLocale(
                        it,
                        this@BookingDetailActivity
                    )
                }
            tvCheckOutDate.text =
                data.checkoutDate?.let {
                    Constant.formatDateByLocale(
                        it,
                        this@BookingDetailActivity
                    )
                }

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


        // Check Is Primary Guest
        if (data.isPrimaryGuest) {
            binding.btnUpdateBookingExchange.visibility = View.GONE
        } else {
            binding.btnUpdateBookingExchange.visibility = View.VISIBLE
        }


        binding.llTypeBookingContainer.visibility = View.VISIBLE
    }

    private fun bindBookingStatus(
        bookingId: Int,
        status: String,
        isFeedback: Boolean,
        source: String
    ) {
        Log.d("Check Status", status)
        when (MyBookingStatus.fromApiStatus(status)) {
            MyBookingStatus.BOOKED -> {
                applyStatusStyle(
                    binding.root.context,
                    R.color.primaryColor,
                    R.color.white
                )

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
                    R.color.status_rejected_text,
                    R.color.white
                )
                binding.llFeedbackContainer.visibility = View.GONE
                binding.btnCancelBooking.visibility = View.GONE
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
        binding.tvStatus.text =
            MyBookingStatus.fromApiStatus(status)?.getDescription(binding.root.context)

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

    private fun callUpdateExchangeInfo(updateExchangeBookingDTO: UpdateExchangeBookingDTO) {
        val bookingId = viewModel.getMyBookingExchangeDetailResponse.value?.data?.id ?: 0
        viewModel.updateExchangeBookingInfo(
            token.getAccessToken().toString(),
            bookingId,
            updateExchangeBookingDTO
        )
    }

    private fun callCancelBooking(bookingId: Int) {
        if (!token.isLoggedIn()) {
            showFailToast("Bạn cần đăng nhập để thực hiện chức năng này")
            return
        }
        viewModel.cancelBooking(token.getAccessToken().toString(), bookingId)

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

    private fun callSendFeedBackExchange(rating: Int, feedback: String, bookingId: Int) {
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

    private fun canCancelBooking(checkInDate: LocalDate, refundPolicy: RefundPolicy): Boolean {
        val today = LocalDate.now() // Ngày hiện tại
        val allowedCancelDate =
            checkInDate.minusDays(refundPolicy.duration.toLong()) // Ngày cuối cùng cho phép hủy

        // Kiểm tra nếu hôm nay trước hoặc bằng ngày được phép hủy
        return today.isBefore(allowedCancelDate) || today.isEqual(allowedCancelDate)
    }

    private fun showCancelBooking(status: String) {
        when (MyBookingStatus.fromApiStatus(status)) {
            MyBookingStatus.BOOKED -> {
                binding.btnCancelBooking.visibility = View.VISIBLE
            }

            else -> {
                binding.btnCancelBooking.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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