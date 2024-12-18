package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RentalPostingDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.PackageModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.Adapter.BenefitAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.VNPayActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingListActivity.MyRentalPostingListActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
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
    private val postingFlowViewModel: PostingFlowViewModel by activityViewModels()
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

    // Observe ExchangeOfResortViewModel
    private fun observeViewModel() {
        // Update Data Here
        postingFlowViewModel.packageStep4.observe(viewLifecycleOwner) { packageModel ->
            if (packageModel != null) {
                bindDataPackagePosting(packageModel)
            }
        }
        postingFlowViewModel.pricePerNight.observe(viewLifecycleOwner) { pricePerNight ->
            bindDataPriceOfTimeshare(pricePerNight)
        }
        postingFlowViewModel.numberOfNights.observe(viewLifecycleOwner) { numberOfNights ->
            binding.includeDetailBilling.tvNumberNight.text = "${numberOfNights} đêm"
        }
        postingFlowViewModel.myTimeshareModelSelected.observe(viewLifecycleOwner) { myTimeshareResponse ->
            bindDataTimeshareInfo(myTimeshareResponse)
        }
        postingFlowViewModel.cancelPolicy.observe(viewLifecycleOwner) { cancelPolicy ->
            when (cancelPolicy) {
                RefundPolicy.FULL_REFUND.id -> {
                    binding.includeDetailBilling.tvCancellationPolicy.text =
                        RefundPolicy.FULL_REFUND.getShortDescription(
                            requireContext()
                        )

                }

                RefundPolicy.PARTIAL_REFUND.id -> {
                    binding.includeDetailBilling.tvCancellationPolicy.text =
                        RefundPolicy.PARTIAL_REFUND.getShortDescription(
                            requireContext()
                        )
                }

                RefundPolicy.NO_REFUND.id -> {
                    binding.includeDetailBilling.tvCancellationPolicy.text =
                        RefundPolicy.NO_REFUND.getShortDescription(
                            requireContext()
                        )
                }
            }
        }
        postingFlowViewModel.selectedPaymentMethod.observe(viewLifecycleOwner) { paymentMethod ->
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
        postingFlowViewModel.checkinDateValid.observe(viewLifecycleOwner) { checkinDate ->
            Log.d("CheckDate", checkinDate.toString())
            binding.includeDetailBilling.tvCheckInDate.text =
                Constant.formatDateByLocaleYMD(checkinDate, requireContext())
        }
        postingFlowViewModel.checkoutDateValid.observe(viewLifecycleOwner) { checkoutDate ->
            Log.d("CheckDate", checkoutDate.toString())
            binding.includeDetailBilling.tvCheckOutDate.text =
                Constant.formatDateByLocaleYMD(checkoutDate, requireContext())
        }


        // FIRST CHECK TYPE POSTING
        postingFlowViewModel.typeOfPostingFlow.observe(viewLifecycleOwner) { typePosting ->
            when (typePosting) {
                Constant.RENTAL_POSTING_FLOW -> {
                    binding.cvExchangeTime.visibility = View.GONE
                    binding.includeDetailBilling.tvCancellationPolicy.visibility = View.VISIBLE
                }

                Constant.EXCHANGER_POSTING_FLOW -> {
                    binding.cvExchangeTime.visibility = View.VISIBLE
                    binding.includeDetailBilling.tvCancellationPolicy.visibility = View.GONE
                    observeViewModelExchange()
                }
            }
        }


        // Call API Payment With VN PAY
        // 1. Observe Purchase Package By VNPAY
        postingFlowViewModel.responseVNPAYUrl.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    //routingToVNPayByTypePosting(response)
                    // Call Upload Image, After that Call Create Posting
                    callRequestCreateImageList()
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
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
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }
            }
        }
        // 2. UPLOAD IMAGE -> CREATE POSTING


        // Call API Payment With Wallet
        // 1. Observe Purchase Package By Wallet. Call Wallet Purchase
        postingFlowViewModel.createRentalPostingTransactionByWallet.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    showSuccessToast("Thanh toán thành công")
                    // Payment Success, Call Create Upload Image and get new Balance same time
                    callRequestCreateImageList()
                    callGetNewBalance()
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
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
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }
            }
        }
        postingFlowViewModel.createExchangePostingTransactionByWallet.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    showSuccessToast("Thanh toán thành công")
                    // Payment Success, Call Create Upload Image and get new Balance same time
                    callRequestCreateImageList()
                    callGetNewBalance()
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    showErrorToast(response.message.toString())
                }

                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }
            }
        }

        // 2.1. UPDATE BALANCE WALLET
        postingFlowViewModel.newBalanceInfoResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        tokenManager.saveProfileInfo(it)
                        bindDataWalletInfo()
                    }
                }

                Status.ERROR -> {
                    showErrorToast(response.message.toString() + " Update Balance")
                }

                Status.LOADING -> {
                    // Do nothing
                }
            }
        }
        // 2.2. UPLOAD IMAGE -> CREATE POSTING

        // 3. CREATE POSTING
        postingFlowViewModel.createRentalPosting.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    (activity as PostingFlowActivity).showSuccessDialog(
                        requireContext(),
                        getString(R.string.msg_post_successful),
                        View.OnClickListener {
                            startActivity(Intent(requireContext(), MyRentalPostingListActivity::class.java))
                            requireActivity().finish()
                        }
                    )

                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
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
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }
            }
        }
        postingFlowViewModel.createExchangePosting.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    (activity as PostingFlowActivity).showSuccessDialog(
                        requireContext(),
                        getString(R.string.msg_post_successful),
                        View.OnClickListener {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    MyExchangePostingActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }
                    )

                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    Log.d("CheckkDOO-Create Exchange", response.message.toString())
                    showErrorToast("Lỗi tạo bài đăng trao đổi")
                }

                Status.LOADING -> {
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }
            }
        }

        // UPLOAD IMAGE -> CREATE POSTING
        postingFlowViewModel.uploadImageResponse.observe(viewLifecycleOwner) { uploadImageResponse ->
            when (uploadImageResponse.status) {
                Status.LOADING -> {
                    Log.d("CheckkDOO - Create Image", "Loading")
                    (activity as PostingFlowActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                    when (postingFlowViewModel.getSelectedPaymentMethod()) {
                        PaymentMethod.VNPAY -> {
                            routingToVNPayByTypePosting()
                        }

                        PaymentMethod.UNWIND -> {
                            routingWalletByTypePosting()
                        }
                    }
                }

                Status.ERROR -> {
                    (activity as PostingFlowActivity).showErrorDialog(
                        "${uploadImageResponse.message}",
                        "Back"
                    )
                    (activity as PostingFlowActivity).hideLoadingWaiting()
                }
            }
        }

    }

    // Observe ExchangeOfResortViewModel of Exchange Posting
    private fun observeViewModelExchange() {
        // Exchange Posting Date Range
        binding.includeExchangeTime.customSpinnerProvince.isEnabled = false

        postingFlowViewModel.exchangeDateRange.observe(viewLifecycleOwner) { checkinDate ->
            bindDataExchangeTime(checkinDate)
        }

        // Exchange Posting Note
        postingFlowViewModel.note.observe(viewLifecycleOwner) { noteExchange ->
            binding.includeExchangeTime.etNote.setText(noteExchange)
        }

        postingFlowViewModel.currentProvinceSelected.observe(viewLifecycleOwner) { provinceId ->
            val spinner: Spinner = binding.includeExchangeTime.customSpinnerProvince
            val provinces = resources.getStringArray(R.array.vietnam_provinces)

            // Tạo ArrayAdapter và áp dụng cho Spinner
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                provinces
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Vô hiệu hóa tương tác của Spinner
            spinner.isEnabled = false
            spinner.isClickable = false

            // Kiểm tra nếu provinceId hợp lệ
            if (provinceId.first != 0) {
                // Cập nhật Spinner đến vị trí tương ứng với provinceId
                binding.includeExchangeTime.customSpinnerProvince.setSelection(provinceId.first - 1)
            } else {
                // Nếu không hợp lệ, đặt về vị trí mặc định (ví dụ: vị trí 0)
                binding.includeExchangeTime.customSpinnerProvince.setSelection(0)
            }
        }

    }

    // Funtion to Change Pakage
    private fun setEventChangePackage() {
        binding.btnChangeMyPackage.setOnClickListener {
            postingFlowViewModel.updateStep(4)
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
            tvPostingFee.text = "${formatPrice(packageModel.price)} VNĐ"
            tvTotalAmount.text = "${formatPrice(packageModel.price)} VNĐ"

            // Lấy ngày hiện tại
            val calendar = Calendar.getInstance()

            // Cộng thêm số tháng trong duration
            calendar.add(Calendar.MONTH, packageModel.duration)

            val expirationDate = formatDateByLocale(calendar.time, requireContext())

            // Hiển thị thời hạn tới
            tvDurationTime.text = expirationDate
        }
    }

    private fun bindDataTimeshareInfo(
        myTimeshareResponse: MyTimeshareResponse.Content,
    ) {

        binding.includeDetailBilling.apply {
            // Hide Unnecessary UI
            llLocation.visibility = View.GONE
            llPostingBy.visibility = View.GONE

            // Image
            Glide.with(requireContext())
                .load(myTimeshareResponse.resortImage)
                .error(R.drawable.ic_image_tmp_holder)
                .placeholder(R.drawable.ic_image_tmp_holder)
                .into(imImageTimeshare)

            // Titlew
            tvResortNameDtb.text =
                "${myTimeshareResponse.resortName} | ${myTimeshareResponse.roomCode}"

            Log.d("CheckDateCCC", myTimeshareResponse.startDate.toString())
            Log.d("CheckDateCCC", myTimeshareResponse.endDate.toString())
            tvCheckInDate.text =
                Constant.formatDateByLocale(myTimeshareResponse.startDate, requireContext())
            tvCheckOutDate.text =
                Constant.formatDateByLocale(myTimeshareResponse.endDate, requireContext())


        }
    }

    private fun bindDataWalletInfo() {
        if (tokenManager.isLoggedIn()) {
            val availableMoney = tokenManager.getProfileInfo()?.walletAvailableMoney
            binding.tvWalletBalance.text = "${availableMoney?.let { formatPrice(it) }} VNĐ"
            availableMoney?.let { money ->
                postingFlowViewModel.packageStep4.value?.price?.let { price ->
                    if (money < price) {
                        binding.cardUnwind.isEnabled = false
                    }
                }
            }
        }


    }

    private fun bindDataExchangeTime(range: Pair<Long?, Long?>) {
        val startDate = range.first
        val endDate = range.second

        if (startDate != null && endDate != null) {
            val startDateString = formatDateByLocale(Date(startDate), requireContext())
            val endDateString = formatDateByLocale(Date(endDate), requireContext())

            val endDateOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(endDate)
            val startDateOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(startDate)

            binding.includeExchangeTime.tvCheckInDate.apply {
                text = startDateString
                setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black))
            }
            binding.includeExchangeTime.tvCheckInDayOfWeek.text = startDateOfWeek
            binding.includeExchangeTime.tvCheckOutDayOfWeek.text = endDateOfWeek
            binding.includeExchangeTime.tvCheckOutDate.apply {
                text = endDateString
                setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black))
            }

            binding.includeExchangeTime.etNightsCount.text =
                "${postingFlowViewModel.getNumberOfExchangeNights()}"
        }


    }

    private fun bindDataPriceOfTimeshare(pricePerNight: Long) {
        if (pricePerNight.toInt() != 0) {
            val totalPrice = pricePerNight * postingFlowViewModel.numberOfNights.value!!.toInt()
            binding.includeDetailBilling.tvEstimatedTotalPrice.text =
                "${Constant.formatPriceLong(totalPrice)} VNĐ / ${postingFlowViewModel.numberOfNights.value} đêm"
            binding.includeDetailBilling.tvRoomPricePerNight.text =
                "${Constant.formatPriceLong(pricePerNight)} VNĐ / 1 đêm"
        } else {
            binding.includeDetailBilling.tvEstimatedTotalPrice.setText("Đang Chờ Định Giá")
            binding.includeDetailBilling.tvRoomPricePerNight.setText("Đang Chờ Định Giá")
        }
    }


    fun formatPrice(price: Long): String {
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
            postingFlowViewModel.selectPaymentMethod(PaymentMethod.UNWIND)
        }
        binding.cardVnpay.setOnClickListener {
            selectedCard = binding.cardVnpay
            postingFlowViewModel.selectPaymentMethod(PaymentMethod.VNPAY)
        }
    }

    private fun routingToVNPayByTypePosting() {
        val url = postingFlowViewModel.getVNPAYUrl()
        when (postingFlowViewModel.typeOfPostingFlow.value) {
            Constant.RENTAL_POSTING_FLOW -> {
                intentToVNPAYActivity_RentalPosting(url.toString())
            }

            Constant.EXCHANGER_POSTING_FLOW -> {
                intentToVNPAYActivity_ExchangePosting(url.toString())
            }
        }
    }

    private fun routingWalletByTypePosting() {
        // Call Create Posting

        when (postingFlowViewModel.typeOfPostingFlow.value) {
            Constant.RENTAL_POSTING_FLOW -> {
                val rentalPackageEnum =
                    RentalPackageEnum.getPackageByName(postingFlowViewModel.packageStep4.value?.name.toString())
                val rentalPostingDTO = RentalPostingDTO(
                    description = "",
                    nights = postingFlowViewModel.numberOfNights.value!!.toInt(),
                    pricePerNights = postingFlowViewModel.pricePerNight.value!!.toInt(),
                    timeshareId = postingFlowViewModel.myTimeshareModelSelected.value?.timeShareId!!,
                    cancellationTypeId = postingFlowViewModel.cancelPolicy.value!!,
                    checkinDate = postingFlowViewModel.checkinDateValid.value!!,
                    checkoutDate = postingFlowViewModel.checkoutDateValid.value!!,
                    rentalPackageId = rentalPackageEnum?.id!!,
                    imageUrls = postingFlowViewModel.getUploadedImageUrls()
                )
                callCreateRentalPosting(rentalPostingDTO)
            }

            Constant.EXCHANGER_POSTING_FLOW -> {
                val exchangePackageEnum =
                    ExchangePackageEnum.getPackageByName(postingFlowViewModel.packageStep4.value?.name.toString())
                val exchangePostingDTO = ExchangePostingDTO(
                    description = postingFlowViewModel.note.value ?: "",
                    nights = postingFlowViewModel.numberOfNights.value!!.toInt(),
                    exchangePackageId = exchangePackageEnum?.id!!,
                    timeshareId = postingFlowViewModel.myTimeshareModelSelected.value?.timeShareId!!,
                    checkinDate = postingFlowViewModel.checkinDateValid.value!!,
                    checkoutDate = postingFlowViewModel.checkoutDateValid.value!!,
                    preferLocation = postingFlowViewModel.getCurrentProvinceSelected().second.toString(),
                    preferCheckinDate = Constant.formatDateFromLong(
                        postingFlowViewModel.exchangeDateRange.value?.first!!
                    ),
                    preferCheckoutDate = Constant.formatDateFromLong(
                        postingFlowViewModel.exchangeDateRange.value?.second!!
                    ),
                    imageUrls = postingFlowViewModel.getUploadedImageUrls()
                )
                Log.d("CheckDTO", exchangePostingDTO.toString())
                callCreateExchangePosting(exchangePostingDTO)
            }
        }
    }

    private fun intentToVNPAYActivity_RentalPosting(url: String) {
        val intent = Intent(requireContext(), VNPayActivity::class.java)
        val rentalPackageEnum =
            RentalPackageEnum.getPackageByName(postingFlowViewModel.packageStep4.value?.name.toString())

        val rentalPostingDTO = RentalPostingDTO(
            description = "",
            nights = postingFlowViewModel.numberOfNights.value!!.toInt(),
            pricePerNights = postingFlowViewModel.pricePerNight.value!!.toInt(),
            timeshareId = postingFlowViewModel.myTimeshareModelSelected.value?.timeShareId!!,
            cancellationTypeId = postingFlowViewModel.cancelPolicy.value!!,
            checkinDate = postingFlowViewModel.checkinDateValid.value!!,
            checkoutDate = postingFlowViewModel.checkoutDateValid.value!!,
            rentalPackageId = rentalPackageEnum?.id!!,
            imageUrls = postingFlowViewModel.getUploadedImageUrls()
        )
        Log.d("CheckDTO", rentalPostingDTO.toString())

        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.GENERAL_ID_PAYMENT, rentalPackageEnum.id)
        intent.putExtra(Constant.POSTING_TIMESHARE_DTO, rentalPostingDTO)
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.PURCHASE_PACKAGE_RENTAL_POSTING)
        paymentResultLauncher.launch(intent)
    }

    private fun intentToVNPAYActivity_ExchangePosting(url: String) {
        val intent = Intent(requireContext(), VNPayActivity::class.java)
        val rentalPackageEnum =
            ExchangePackageEnum.getPackageById(postingFlowViewModel.packageStep4.value?.id!!)

        val postingTimeshareDTO = ExchangePostingDTO(
            description = postingFlowViewModel.note.value ?: "",
            nights = postingFlowViewModel.numberOfNights.value!!.toInt(),
            exchangePackageId = rentalPackageEnum?.id!!,
            timeshareId = postingFlowViewModel.myTimeshareModelSelected.value?.timeShareId!!,
            checkinDate = postingFlowViewModel.checkinDateValid.value!!,
            checkoutDate = postingFlowViewModel.checkoutDateValid.value!!,
            preferLocation = postingFlowViewModel.currentProvinceSelected.value.toString(),
            preferCheckinDate =Constant.formatDateFromLong( postingFlowViewModel.exchangeDateRange.value?.first!!),
            preferCheckoutDate = Constant.formatDateFromLong(postingFlowViewModel.exchangeDateRange.value?.second!!),
            imageUrls = postingFlowViewModel.getUploadedImageUrls()
        )
        Log.d("CheckDTO", postingTimeshareDTO.toString())

        intent.putExtra(Constant.PAYMENT_URL, url)
        intent.putExtra(Constant.GENERAL_ID_PAYMENT, rentalPackageEnum.id)
        intent.putExtra(Constant.POSTING_TIMESHARE_DTO, postingTimeshareDTO)
        intent.putExtra(Constant.PAYMENT_METHOD_TYPE, PaymentType.PURCHASE_PACKAGE_EXCHANGE_POSTING)
        paymentResultLauncher.launch(intent)
    }

    // Send Request
    // Send Request To Create Image List
    private fun callRequestCreateImageList() {
        postingFlowViewModel.callUploadImages(tokenManager.getAccessToken().toString())
    }

    private fun callCreateRentalPosting(rentalPostingDTO: RentalPostingDTO) {
        postingFlowViewModel.createRentalPosting(
            tokenManager.getAccessToken().toString(),
            rentalPostingDTO
        )
    }

    private fun callCreateExchangePosting(exchangePostingDTO: ExchangePostingDTO) {
        postingFlowViewModel.createExchangePosting(
            tokenManager.getAccessToken().toString(),
            exchangePostingDTO
        )
    }

    private fun callGetNewBalance() {
        postingFlowViewModel.getCustomerInfo(tokenManager.getAccessToken().toString())
    }

    // Funtion to done Payment
    private fun requestButtonClick() {
        binding.ctrRequestButton.setOnClickListener {
            if (!binding.cbAgreement.isChecked) {
                "Vui Lòng Chấp Nhận Điều Khoản"
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, binding.llAgreement.top)
                }
                return@setOnClickListener
            }
            // Get Payment Method
            val paymentMethod = postingFlowViewModel.selectedPaymentMethod.value

            when (postingFlowViewModel.typeOfPostingFlow.value) {
                Constant.RENTAL_POSTING_FLOW -> {
                    // Get Package Enum
                    val rentalPackageEnum =
                        RentalPackageEnum.getPackageByName(postingFlowViewModel.packageStep4.value?.name.toString())
                    // Check Payment Method, Call API to get Payment URL or Check Wallet Balance
                    paymentTypeHanldeFlow(paymentMethod, rentalPackageEnum)
                }

                Constant.EXCHANGER_POSTING_FLOW -> {
                    // Get Package Enum
                    val exchangePackageEnum =
                        ExchangePackageEnum.getPackageByName(postingFlowViewModel.packageStep4.value?.name.toString())
                    // Check Payment Method, Call API to get Payment URL or Check Wallet Balance
                    paymentTypeHanldeFlow(paymentMethod, exchangePackageEnum)
                }
            }


        }

    }

    private fun paymentTypeHanldeFlow(
        paymentMethod: PaymentMethod?,
        packageEnum: PackageModel?
    ) {
        when (paymentMethod) {
            // Call API to check Wallet Balance, Intent to PaymentResultActivity
            PaymentMethod.UNWIND -> {
                when (postingFlowViewModel.typeOfPostingFlow.value) {
                    Constant.RENTAL_POSTING_FLOW -> {
                        postingFlowViewModel.createRentalPostingTransactionByWallet(
                            tokenManager.getAccessToken().toString(), packageEnum!!.id
                        )
                    }

                    Constant.EXCHANGER_POSTING_FLOW -> {
                        postingFlowViewModel.createExchangePostingTransactionByWallet(
                            tokenManager.getAccessToken().toString(), packageEnum!!.id
                        )
                    }
                }
            }

            // Call API to get Payment URL, Intent to VNPayActivity
            PaymentMethod.VNPAY -> {
                postingFlowViewModel.getResponsePaymentUrl(
                    packageEnum!!.price,
                    packageEnum.name
                )
            }

            else -> {
                showErrorToast("Chưa chọn phương thức thanh toán")
            }
        }
    }

    private fun initActivityResultLauncher() {
        paymentResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    requireActivity().finish()
                }
            }
    }

    private fun showErrorToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Thất Bại",
            string,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        )
    }

    private fun showSuccessToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Thành Công",
            string,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        )
    }

    private fun showInfoToast(string: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Failed",
            string,
            MotionToastStyle.INFO,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.inter_thin)
        )
    }


}