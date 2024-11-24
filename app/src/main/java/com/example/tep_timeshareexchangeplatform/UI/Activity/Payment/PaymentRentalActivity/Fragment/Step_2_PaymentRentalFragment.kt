package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.GuestDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.PublicPostingDetailResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPrice
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.formatPriceLong
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.VNPayActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentRentalActivity.PaymentRentalViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentStep2PaymentRentalBinding
import com.google.android.material.card.MaterialCardView

class Step_2_PaymentRentalFragment : BaseFragment(R.layout.fragment_step_2__payment_rental) {
    private lateinit var binding: FragmentStep2PaymentRentalBinding
    private val viewModel: PaymentRentalViewModel by activityViewModels()
    private var selectedCard: MaterialCardView? = null
    private lateinit var paymentResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStep2PaymentRentalBinding.inflate(inflater, container, false)
        setToolBarEvent()
        checkTokenValid()
        observeData()
        onPaymentMethodSelected()
        requestButtonClick()
        initActivityResultLauncher()
        return binding.root
    }

    private fun observeData() {
        // Get Data Posting Detail
        viewModel.postingDetail.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as PaymentRentalActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    Log.d("CheckPostingDetailData", "observePostingDetail: ${it.data}")
                    bindDataPostingDetail(it.data!!)
                }

                Status.ERROR -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        // Observe Selected Payment Method
        viewModel.selectedPaymentMethod.observe(viewLifecycleOwner) { method ->
            when (method) {
                PaymentMethod.VNPAY -> {
                    updateCardViewAppearance(binding.cardVnpay, true)
                    updateCardViewAppearance(binding.cardUnwind, false)
                }

                PaymentMethod.UNWIND -> {
                    updateCardViewAppearance(binding.cardUnwind, true)
                    updateCardViewAppearance(binding.cardVnpay, false)
                }
            }
        }

        // Call Payment API by VNPAY
        viewModel.responseVNPAYUrl.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as PaymentRentalActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    intentToVNPAYActivity(it.data?.url.toString())
                }

                Status.ERROR -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        // Call API Create Booking
        viewModel.myBookingResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as PaymentRentalActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    viewModel.getCustomerInfo(tokenManager.getAccessToken().toString())
                }

                Status.ERROR -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        // Call API Booking By Wallet
        viewModel.walletPurchaseResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as PaymentRentalActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thành Công",
                        "Thanh Toán thành công",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    callAPICreateBooking()


                }

                Status.ERROR -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        "${it.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        viewModel.customerInfoResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as PaymentRentalActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                    if (it.data!!.isMember) {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                        tokenManager.saveProfileInfo(it.data)
                    } else {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                        tokenManager.saveProfileInfo(it.data)
                    }

                    (activity as PaymentRentalActivity).showSuccessDialog(
                        requireContext(),
                        "Chúc mừng bạn đã đặt phòng thành công. Vui lòng kiểm tra thông tin đặt phòng trong mục lịch sử đặt phòng",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                (activity as PaymentRentalActivity).finish()
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                            }
                        }
                    )
                }

                Status.ERROR -> {
                    (activity as PaymentRentalActivity).hideLoadingWaiting()
                }
            }
        }

    }

    private fun checkTokenValid() {
        if (!tokenManager.isLoggedIn()) {
            MotionToast.Companion.createColorToast(
                requireActivity(),
                "Thất Bại",
                "Vui lòng đăng nhập để thực hiện chức năng này",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
            requireActivity().finish()
        }
        val profile = tokenManager.getProfileInfo()
        binding.tvWalletBalance.text = "${formatPriceLong(profile?.walletAvailableMoney!!)} đ"

    }

    private fun bindDataPostingDetail(postingDetail: PublicPostingDetailResponse) {
        // Custom Toolbar Data
        binding.customToolbar.apply {
            setTitle("${postingDetail.unitType.title}")
            setTitleDetail("${postingDetail.checkinDate} đến ${postingDetail.checkoutDate}")
        }

        // Cancel Policy
        binding.apply {
            if (postingDetail.cancelType.toString() == "null") {
                tvCancelPolicyDtb.text = "Không có"
            } else {
                val refundPolicy = RefundPolicy.getShortDescriptionFromName(
                    requireContext(),
                    postingDetail.cancelType.toString()
                )
                tvCancelPolicyDtb.text = refundPolicy
            }
        }

        // UI DTB
        binding.apply {
            tvCheckInDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkinDate, requireContext())
            tvCheckOutDateDtb.text =
                Constant.formatDateByLocale(postingDetail.checkoutDate, requireContext())
            tvNightDtb.text = "${postingDetail.nights} đêm"
            tvRoomPricePerNight.text =
                "${Constant.formatPriceLong(postingDetail.pricePerNights)} đ / 1 đêm"
            tvEstimatedTotalPrice.text =
                "${Constant.formatPriceLong(postingDetail.totalPrice)} đ / ${postingDetail.nights} đêm"

            tvResortNameDtb.text = postingDetail.resortName + " | " + postingDetail.unitType.title

        }

        // Data for Request
        binding.apply {
            tvPrice.text =
                "${Constant.formatPriceLong(postingDetail.totalPrice)} đ / ${postingDetail.nights} đêm"

        }


    }

    private fun requestButtonClick() {
        binding.ctrRequestButton.setOnClickListener {
            val isFormValid = validateGuestInfo(
                binding.etFullName,
                binding.etPhoneNumber,
                binding.etEmail,
                binding.etNotes
            )

            if (isFormValid && binding.cbAgreeTerms.isChecked) {
                paymentMethodProcess()
            } else {
                // Show error message or keep focus on the invalid field
                Toast.makeText(
                    requireContext(),
                    "Vui lòng kiểm tra lại thông tin!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun intentToVNPAYActivity(url: String) {
        val intent = Intent(requireContext(), VNPayActivity::class.java)
        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(
            Constant.GENERAL_ID_PAYMENT,
            viewModel.postingDetail.value?.data!!.rentalPostingId
        )
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.BOOKING_RENTAL_PAYMENT)
        paymentResultLauncher.launch(intent)
    }

    private fun paymentMethodProcess() {
        when (viewModel.selectedPaymentMethod.value) {
            PaymentMethod.VNPAY -> {
                viewModel.getResponsePaymentUrl(
                    viewModel.postingDetail.value?.data!!.totalPrice,
                    viewModel.postingDetail.value?.data!!.rentalPackageName
                )
            }

            PaymentMethod.UNWIND -> {
                viewModel.bookingByWallet(
                    tokenManager.getAccessToken().toString(),
                    viewModel.postingDetail.value?.data!!.rentalPostingId
                )
            }

            else -> {
                // Show error message or keep focus on the invalid field
                Toast.makeText(
                    requireContext(),
                    "Vui lòng chọn phương thức thanh toán!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onPaymentMethodSelected() {
        binding.cardUnwind.setOnClickListener {
            selectedCard = binding.cardUnwind
            viewModel.selectPaymentMethod(PaymentMethod.UNWIND)
        }
        binding.cardVnpay.setOnClickListener {
            selectedCard = binding.cardVnpay
            viewModel.selectPaymentMethod(PaymentMethod.VNPAY)
        }
    }

    // Hàm để cập nhật giao diện của CardView
    private fun updateCardViewAppearance(cardView: MaterialCardView, isSelected: Boolean) {
        cardView.apply {
            strokeWidth = if (isSelected) 4 else 0
            strokeColor = ContextCompat.getColor(
                requireContext(),
                if (isSelected) R.color.blue_see_more else R.color.white
            )
        }
    }

    private fun setToolBarEvent() {
        binding.customToolbar.onStartIconClick = {
            viewModel.setCurrentViewPager(0)
        }
    }

    private fun initActivityResultLauncher() {
        paymentResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    callAPICreateBooking()
                }
            }
    }

    private fun callAPICreateBooking() {
        viewModel.createBooking(
            tokenManager.getAccessToken().toString(),
            viewModel.postingDetail.value?.data!!.rentalPostingId,
            GuestDTO(
                binding.etFullName.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etEmail.text.toString()
            )
        )
    }


    fun validateGuestInfo(
        fullName: EditText,
        phoneNumber: EditText,
        email: EditText,
        notes: EditText
    ): Boolean {
        var isValid = true

        // Reset all errors before validating
        fullName.error = null
        phoneNumber.error = null
        email.error = null
        notes.error = null

        // Check full name
        if (fullName.text.toString().trim().isEmpty()) {
            fullName.error = "Họ tên không được để trống"
            isValid = false
        }

        // Check phone number (ensure it's numeric and has the correct length)
        val phonePattern = Regex("^[0-9]{9,12}$") // Example: Vietnamese phone numbers (9-12 digits)
        if (phoneNumber.text.toString().trim().isEmpty()) {
            phoneNumber.error = "Số điện thoại không được để trống"
            isValid = false
        } else if (!phonePattern.matches(phoneNumber.text.toString())) {
            phoneNumber.error = "Số điện thoại không hợp lệ"
            isValid = false
        }

        // Check email (basic email pattern validation)
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        if (email.text.toString().trim().isEmpty()) {
            email.error = "Email không được để trống"
            isValid = false
        } else if (!emailPattern.matcher(email.text.toString()).matches()) {
            email.error = "Email không hợp lệ"
            isValid = false
        }

        // Notes are optional, but you can add specific checks if required (e.g., character limit)
        if (notes.text.toString().length > 500) {
            notes.error = "Ghi chú không được dài quá 500 ký tự"
            isValid = false
        }

        return isValid
    }


}