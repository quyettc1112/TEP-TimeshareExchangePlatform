package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.PostingTimeshareDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.Adapter.BenefitAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.PaymentResultActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.PaymentScreen.VNPayActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentMethod
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PaymentType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentPaymentPostingBinding
import com.google.android.material.card.MaterialCardView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Step_6_PaymentPostingFragment : BaseFragment(R.layout.fragment_payment_posting) {

    private lateinit var binding: FragmentPaymentPostingBinding
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()
    private var selectedCard: MaterialCardView? = null
    private lateinit var tokenManager: TokenManager
    private lateinit var paymentResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentPostingBinding.inflate(layoutInflater, container, false)
        bindDataWalletInfo()
        observeViewModel()
        setEventChangePackage()
        requestButtonClick()
        onPaymentMethodSelected()
        initActivityResultLauncher()


        return binding.root
    }

    // Observe ViewModel
    private fun observeViewModel() {
        rentalPostingViewModel.packageStep4.observe(viewLifecycleOwner) { packageModel ->
            if (packageModel != null) {
                bindDataPackagePosting(packageModel)
            }
        }

        rentalPostingViewModel.pricePerNight.observe(viewLifecycleOwner) { pricePerNight ->
            if (pricePerNight.toInt() != 0) {
                val totalPrice = pricePerNight * rentalPostingViewModel.numberOfNights.value!!
                val value =
                    "${formatPrice(totalPrice.toInt())} đ/${rentalPostingViewModel.numberOfNights.value!!} đêm"
                Toast.makeText(requireContext(), "Có Tiền", Toast.LENGTH_SHORT).show()
                binding.includeDetailBilling.tvEstimatedTotalPrice.text = value
                binding.includeDetailBilling.tvRoomPricePerNight.text =
                    "${formatPrice(pricePerNight.toInt())} đ"
            } else {
                binding.includeDetailBilling.tvEstimatedTotalPrice.setText("Đang Chờ Định Giá")
                binding.includeDetailBilling.tvRoomPricePerNight.setText("Đang Chờ Định Giá")
                Toast.makeText(requireContext(), "0 Tiền", Toast.LENGTH_SHORT).show()
            }
        }

        rentalPostingViewModel.numberOfNights.observe(viewLifecycleOwner) { numberOfNights ->
            binding.includeDetailBilling.tvNumberNight.text = "${numberOfNights} đêm"
        }

        rentalPostingViewModel.checkinDate.observe(viewLifecycleOwner) { checkinDate ->
            if (checkinDate != null) {
                try {
                    // Định dạng ban đầu từ ViewModel là "yyyy-MM-dd"
                    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = inputDateFormat.parse(checkinDate)

                    // Sử dụng hàm formatDateByLocale để định dạng ngày theo ngôn ngữ đã lưu
                    val formattedDate = formatDateByLocale(date, requireContext())

                    // Gán vào TextView
                    binding.includeDetailBilling.tvCheckInDate.text = formattedDate
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Xử lý lỗi khi định dạng sai
                }
            }
        }

        rentalPostingViewModel.checkoutDate.observe(viewLifecycleOwner) { checkoutDate ->
            if (checkoutDate != null) {
                try {
                    // Định dạng ban đầu từ ViewModel là "yyyy-MM-dd"
                    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = inputDateFormat.parse(checkoutDate)

                    // Sử dụng hàm formatDateByLocale để định dạng ngày theo ngôn ngữ đã lưu
                    val formattedDate = formatDateByLocale(date, requireContext())

                    // Gán vào TextView
                    binding.includeDetailBilling.tvCheckOutDate.text = formattedDate
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Xử lý lỗi khi định dạng sai
                }
            }
        }

        rentalPostingViewModel.myTimeshareModelSelected.observe(viewLifecycleOwner) { myTimeshareResponse ->
            bindDataTimeshareInfo(myTimeshareResponse)
        }

        rentalPostingViewModel.cancelPolicy.observe(viewLifecycleOwner) { cancelPolicy ->
            when (cancelPolicy) {
                RefundPolicy.FULL_REFUND.id -> {
                    binding.includeDetailBilling.tvCancellationPolicy.text =
                        "Hoàn Tiền toàn bộ 100%"
                }

                RefundPolicy.PARTIAL_REFUND.id -> {
                    binding.includeDetailBilling.tvCancellationPolicy.text = "Hoàn Tiền 50%"
                }

                RefundPolicy.NO_REFUND.id -> {
                    binding.includeDetailBilling.tvCancellationPolicy.text = "Không Hoàn Tiền"
                }
            }
        }

        // Observe Selected Payment Method
        rentalPostingViewModel.selectedPaymentMethod.observe(viewLifecycleOwner) { paymentMethod ->
            when (paymentMethod) {
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

        // Observe Purchase Package By VNPAY
        rentalPostingViewModel.responseVNPAYUrl.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    response.data?.let {
                        (activity as RentalPostingActivity).hideLoading()
                        intentToVNPAYActivity(it.url.toString())
                    }
                }

                Status.ERROR -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        response.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    (activity as RentalPostingActivity).showLoadingWaiting(true)
                }
            }
        }

        // Observe Purchase Package By Wallet
        rentalPostingViewModel.walletPurchaseResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thành Công",
                        "Mua gói thành công",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    rentalPostingViewModel.getCustomerInfo(tokenManager.getAccessToken().toString())
                    val packageEnum =
                        PackageEnum.getPackageByName(rentalPostingViewModel.packageStep4.value?.name.toString())
                    val postingTimeshareDTO = PostingTimeshareDTO(
                        description = "String",
                        nights = rentalPostingViewModel.numberOfNights.value!!.toInt(),
                        pricePerNights = rentalPostingViewModel.pricePerNight.value!!.toInt(),
                        timeshareId = rentalPostingViewModel.myTimeshareModelSelected.value?.timeShareId!!,
                        cancellationTypeId = rentalPostingViewModel.cancelPolicy.value!!,
                        checkinDate = rentalPostingViewModel.checkinDate.value!!,
                        checkoutDate = rentalPostingViewModel.checkoutDate.value!!,
                        rentalPackageId = packageEnum?.id!!
                    )
                    rentalPostingViewModel.createPosting(
                        tokenManager.getAccessToken().toString(),
                        postingTimeshareDTO
                    )


                }

                Status.ERROR -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        response.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    (activity as RentalPostingActivity).showLoadingWaiting(true)
                }
            }
        }

        // Observe Call Get New Available Balance
        rentalPostingViewModel.newBalanceInfoResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    (activity as RentalPostingActivity).showSuccessDialog(
                        requireContext(),
                        "Bạn đã đăng bài thành công",
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val intent = Intent(requireContext(), MyPostingActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                    )
                    response.data?.let {
                        tokenManager.saveCustomerInfo(it)
                        bindDataWalletInfo()
                    }
                }

                Status.ERROR -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        response.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    (activity as RentalPostingActivity).showLoadingWaiting(true)
                }
            }
        }

        // Observe Create Posting
        rentalPostingViewModel.postingTimeshareResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    startActivity(Intent(requireContext(), MyPostingActivity::class.java))
                    requireActivity().finish()
                }

                Status.ERROR -> {
                    (activity as RentalPostingActivity).hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        response.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    (activity as RentalPostingActivity).showLoadingWaiting(true)
                }
            }
        }


    }

    // Funtion to Change Pakage
    private fun setEventChangePackage() {
        binding.btnChangeMyPackage.setOnClickListener {
            rentalPostingViewModel.updateStep(4)
        }
    }

    // Funtion to done Payment
    private fun requestButtonClick() {
        binding.ctrRequestButton.setOnClickListener {
            if (!binding.cbAgreement.isChecked) {
                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    "Thông Báo",
                    "Vui lòng đồng ý với điều khoản sử dụng",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
                return@setOnClickListener
            }
            // Get Payment Method
            val paymentMethod = rentalPostingViewModel.selectedPaymentMethod.value

            // Get Package Enum
            val packageEnum =
                PackageEnum.getPackageByName(rentalPostingViewModel.packageStep4.value?.name.toString())

            // Check Payment Method, Call API to get Payment URL or Check Wallet Balance
            when (paymentMethod) {
                // Call API to check Wallet Balance, Intent to PaymentResultActivity
                PaymentMethod.UNWIND -> {
                    rentalPostingViewModel.purchasePackagePostingWallet(
                        tokenManager.getAccessToken().toString(), packageEnum!!.id
                    )
                }

                // Call API to get Payment URL, Intent to VNPayActivity
                PaymentMethod.VNPAY -> {
                    rentalPostingViewModel.getResponsePaymentUrl(
                        packageEnum!!.price,
                        packageEnum.name
                    )
                }

                else -> {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Thất Bại",
                        "Vui lòng chọn phương thức thanh toán",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

    }

    // Funtion to Bind data to UI
    private fun bindDataPackagePosting(packageModel: PackageModel) {
        val benefitAdapter = BenefitAdapter().apply {
            submitList(packageModel.listBenefit)
        }

        binding.includePackegePosting.apply {
            // Thay đổi Layout
            clContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

            // Ẩn các UI không cần thiết
            tvTitle.visibility = View.GONE
            tvPackageDescription.visibility = View.GONE
            rvFeatures.visibility = View.GONE

            // Gán dữ liệu cho các TextView
            tvPackageName.text = packageModel.name
            tvPackagePrice.text = "${formatPrice(packageModel.price)} đ"
            tvPackageDescription.text = packageModel.description

            // Gán dữ liệu cho RecyclerView
            rvFeatures.apply {
                adapter = benefitAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
        }

        binding.apply {
            tvPostingFee.text = "${formatPrice(packageModel.price)} đ"
            tvTotalAmount.text = "${formatPrice(packageModel.price)} đ"

            // Lấy ngày hiện tại
            val calendar = Calendar.getInstance()

            // Cộng thêm số tháng trong duration
            calendar.add(Calendar.MONTH, packageModel.duration)

            val expirationDate = formatDateByLocale(calendar.time, requireContext())

            // Hiển thị thời hạn tới
            tvDurationTime.text = expirationDate
        }
    }

    // Funtion to Bind Timeshare Info Data to UI
    private fun bindDataTimeshareInfo(
        myTimeshareResponse: MyTimeshareResponse.Content,
    ) {

        binding.includeDetailBilling.apply {
            // Hide Unnecessary UI
            llLocation.visibility = View.GONE
            llPostingBy.visibility = View.GONE

            /*  // Image
              Glide.with(requireContext())
                  .load(myTimeshareResponse.image)
                  .into(imImageTimeshare)*/

            // Title
            tvResortNameDtb.text =
                "${myTimeshareResponse.resortName} | ${myTimeshareResponse.roomName}"


        }
    }

    private fun bindDataWalletInfo() {
        if (tokenManager.isLoggedIn()) {
            val availableMoney = tokenManager.getCustomerInfo()?.walletAvailableMoney
            binding.tvWalletBalance.text = "${availableMoney?.let { formatPrice(it) }} đ"
            availableMoney?.let { money ->
                rentalPostingViewModel.packageStep4.value?.price?.let { price ->
                    if (money < price) {
                        binding.cardUnwind.isEnabled = false
                    }
                }
            }
        }


    }

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(price)
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

    private fun onPaymentMethodSelected() {
        binding.cardUnwind.setOnClickListener {
            selectedCard = binding.cardUnwind
            rentalPostingViewModel.selectPaymentMethod(PaymentMethod.UNWIND)
        }
        binding.cardVnpay.setOnClickListener {
            selectedCard = binding.cardVnpay
            rentalPostingViewModel.selectPaymentMethod(PaymentMethod.VNPAY)
        }
    }

    private fun intentToVNPAYActivity(url: String) {

        val intent = Intent(requireContext(), VNPayActivity::class.java)
        val packageEnum =
            PackageEnum.getPackageByName(rentalPostingViewModel.packageStep4.value?.name.toString())

        val postingTimeshareDTO = PostingTimeshareDTO(
            description = "String",
            nights = rentalPostingViewModel.numberOfNights.value!!.toInt(),
            pricePerNights = rentalPostingViewModel.pricePerNight.value!!.toInt(),
            timeshareId = rentalPostingViewModel.myTimeshareModelSelected.value?.timeShareId!!,
            cancellationTypeId = rentalPostingViewModel.cancelPolicy.value!!,
            checkinDate = rentalPostingViewModel.checkinDate.value!!,
            checkoutDate = rentalPostingViewModel.checkoutDate.value!!,
            rentalPackageId = packageEnum?.id!!
        )
        Log.d("CheckDTO", postingTimeshareDTO.toString())

        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.DEFAULT_PACKAGE_SELECTION, packageEnum.id)
        intent.putExtra(Constant.POSTING_TIMESHARE_DTO, postingTimeshareDTO)
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.PURCHASE_PACKAGE_POSTING)
        paymentResultLauncher.launch(intent)
    }

    private fun initActivityResultLauncher() {
        paymentResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    requireActivity().finish()
                }
            }
    }


}