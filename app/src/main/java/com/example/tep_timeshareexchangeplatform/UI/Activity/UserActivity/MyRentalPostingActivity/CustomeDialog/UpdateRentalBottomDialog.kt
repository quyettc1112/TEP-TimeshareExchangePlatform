package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.CustomeDialog

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.RentalPostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter.ImageUPAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RefundPolicy
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateRentalPostingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UpdateRentalBottomDialog(
    private val myRentalDetailViewModel: MyPostingDetailViewModel
) : BottomSheetDialogFragment() {

    private var _binding: DialogUpdateRentalPostingBinding? = null
    private val binding get() = _binding!!
    private val imageUploadAdapter = ImageUPAdapter()
    private lateinit var tokenManager: TokenManager
    private lateinit var pickImagesUPLauncher_Rental: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using View Binding
        _binding = DialogUpdateRentalPostingBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        imageUploadAdapter.submitList(listOf())


        myRentalDetailViewModel.clearListImageForPut()
        Log.d(
            "UpdateRentalBottomDialog",
            "onCreateView: ${myRentalDetailViewModel.postingDetailResponse.value?.data?.imageUrls?.size}"
        )
        Log.d(
            "UpdateRentalBottomDialog",
            "onCreateView: ${myRentalDetailViewModel.getImageList().size}"
        )
        Log.d(
            "UpdateRentalBottomDialog",
            "onCreateView: ${imageUploadAdapter.differ.currentList?.size}"
        )

        initializeLaunchers()
        initAdapter()
        observeViewModel()
        bindDataImages()
        bindDataDescription()
        bindDataSpinnerCancellationPolicy()
        bindDataPrice()
        eventClickAddImage()
        eventClickSaveUpdate()
        setupTextWatchers()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun observeViewModel() {
        // UpLoad Image
        myRentalDetailViewModel.uploadImageResponse.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    (activity as MyPostingDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyPostingDetailActivity).apply {
                        hideLoadingWaiting()
                    }
                    myRentalDetailViewModel.addListImageForPut(it.data ?: emptyList())
                    imageUploadAdapter.submitList(myRentalDetailViewModel.getImagesForPut())

                }

                Status.ERROR -> {
                    (activity as MyPostingDetailActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Không thể tải ảnh lên")
                    }
                }

                null -> {

                }
            }
        })

        // Update Exchange Posting
        myRentalDetailViewModel.updateRentalResponse.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    (activity as MyPostingDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyPostingDetailActivity).apply {
                        hideLoadingWaiting()
                        showSuccessToast("Thành công", "Cập nhật bài đăng thành công")
                        (activity as MyPostingDetailActivity).apply {
                            myRentalDetailViewModel.getMyPostingDetail(
                                tokenManager.getAccessToken().toString(),
                                myRentalDetailViewModel.postingDetailResponse.value?.data?.rentalPostingId
                                    ?: 0
                            )
                        }
                        dismiss()
                    }
                }

                Status.ERROR -> {
                    (activity as MyPostingDetailActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Không thể cập nhật bài đăng")
                    }
                }

                null -> {

                }
            }
        })

        // Observer Price Per Night
        myRentalDetailViewModel.pricePerNight.observe(viewLifecycleOwner) { pricePerNight ->
            val numberOfNight =
                myRentalDetailViewModel.postingDetailResponse.value?.data?.nights?.toInt() ?: 0
            val totalPrice = pricePerNight * numberOfNight
            val value =
                "${Constant.formatPriceLong(totalPrice)} VNĐ/${numberOfNight} đêm"
            binding.includePaymentMethod12.etTotalPrice.setText(value)
        }
    }

    private fun initAdapter() {
        binding.rvImage.apply {
            adapter = imageUploadAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
        imageUploadAdapter.onDeleteClick = {
            imageUploadAdapter.removeItem(it)
            myRentalDetailViewModel.deleteImageForPut(it)
        }
    }

    private fun initializeLaunchers() {
        pickImagesUPLauncher_Rental =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
                if (uris.isNotEmpty()) {
                    val listImage = mutableListOf<ImageUploadModel>()
                    for (uri in uris) {
                        listImage.add(ImageUploadModel.create(uri))
                    }
                    // Save to ViewModel
                    myRentalDetailViewModel.addImagesFromDevice(listImage)
                    callUploadImages()
                }
            }

    }

    private fun eventClickAddImage() {
        binding.btnAddImage.setOnClickListener {
            pickImagesUPLauncher_Rental.launch("image/*")
        }
    }

    private fun eventClickSaveUpdate() {
        binding.btnSaveUpdatePosting.setOnClickListener {
            if (!isImageValid()) {
                return@setOnClickListener
            }
            val packageModel =
                myRentalDetailViewModel.postingDetailResponse.value?.data?.rentalPackageName ?: ""

            val rentalPackageEnum = RentalPackageEnum.getPackageByName(packageModel)
            if (rentalPackageEnum != RentalPackageEnum.DELEGATED_SERVICE.packageModel && !isPriceInputvalid()) {
                return@setOnClickListener
            }

            val description = binding.etNote.text.toString()
            val rentalPostingUpdateDTO = myRentalDetailViewModel.getImagesForPut()?.let { it1 ->
                RentalPostingUpdateDTO(
                    description = description,
                    pricePerNights = myRentalDetailViewModel.pricePerNight.value ?: 0,
                    cancellationTypeId = myRentalDetailViewModel.cancelPolicy.value ?: 0,
                    imageUrls = it1
                )
            }
            Log.d("RentalPostingUpdateDTO", rentalPostingUpdateDTO!!.imageUrls.size.toString())

            callUpdateRentalPosting(
                myRentalDetailViewModel.postingDetailResponse.value?.data?.rentalPostingId ?: 0,
                rentalPostingUpdateDTO!!
            )
        }
    }

    private fun bindDataDescription() {
        binding.etNote.setText(myRentalDetailViewModel.postingDetailResponse.value?.data?.description)
    }

    private fun bindDataImages() {
        myRentalDetailViewModel.addListImageForPut(
            myRentalDetailViewModel.postingDetailResponse.value?.data?.imageUrls ?: emptyList()
        )
        imageUploadAdapter.submitList(myRentalDetailViewModel.getImagesForPut())
    }

    private fun bindDataSpinnerCancellationPolicy() {
        val selectedPolicyId: Int =
            myRentalDetailViewModel.postingDetailResponse.value?.data?.cancelTypeId ?: 0
        val refundPolicies = RefundPolicy.entries.toTypedArray() // Lấy danh sách tất cả các enum
        val spinnerAdapter = object :
            ArrayAdapter<RefundPolicy>(requireContext(), R.layout.spinner_item, refundPolicies) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val refundPolicy = getItem(position)
                (view as TextView).text = refundPolicy?.getShortDescription(context)
                return view
            }

            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val refundPolicy = getItem(position)
                (view as TextView).text = refundPolicy?.getShortDescription(context)
                return view
            }
        }

        binding.customSpinnerViewDiretion.adapter = spinnerAdapter

        // Tìm vị trí tương ứng với selectedPolicyId
        val selectedPosition = refundPolicies.indexOfFirst { it.id == selectedPolicyId }
        if (selectedPosition >= 0) {
            binding.customSpinnerViewDiretion.setSelection(selectedPosition)
            val selectedPolicy = refundPolicies[selectedPosition]
            val longDescription =
                Html.fromHtml(selectedPolicy.getLongDescription(requireContext()))
            binding.tvCancellationPolicyDescription.text = longDescription
            binding.customSpinnerViewDiretion.setSelection(selectedPosition)
        }

        binding.customSpinnerViewDiretion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    val selectedPolicy = parent.getItemAtPosition(position) as RefundPolicy
                    val policyId = selectedPolicy.id
                    val longDescription =
                        Html.fromHtml(selectedPolicy.getLongDescription(requireContext()))
                    binding.tvCancellationPolicyDescription.text = longDescription
                    myRentalDetailViewModel.updateCancelPolicy(policyId)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không có mục nào được chọn
                }
            }
    }

    private fun bindDataPrice() {
        val packageModel =
            myRentalDetailViewModel.postingDetailResponse.value?.data?.rentalPackageName ?: ""
        if (packageModel != null && packageModel.isNotEmpty()) {
            val rentalPackageEnum = RentalPackageEnum.getPackageByName(packageModel)
            when (rentalPackageEnum) {
                RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                    binding.includePaymentMethod12.root.visibility = View.GONE
                    binding.includePaymentMethod34.root.visibility = View.VISIBLE
                    myRentalDetailViewModel.pricePerNight.value = 0
                }

                else -> {
                    binding.includePaymentMethod12.root.visibility = View.VISIBLE
                    binding.includePaymentMethod34.root.visibility = View.GONE
                    val price =
                        myRentalDetailViewModel.postingDetailResponse.value?.data?.pricePerNights
                    myRentalDetailViewModel.pricePerNight.value = price
                    binding.includePaymentMethod12.etRoomPrice.setText(
                        Constant.formatPriceLong(
                            price ?: 0
                        )
                    )
                }
            }
        }

    }

    private fun callUploadImages() {
        if (!tokenManager.isLoggedIn()) {
            return
        }
        myRentalDetailViewModel.callUploadImages(tokenManager.getAccessToken().toString())
    }

    private fun callUpdateRentalPosting(
        postingId: Int,
        rentalPostingUpdateDTO: RentalPostingUpdateDTO
    ) {
        if (!tokenManager.isLoggedIn()) {
            return
        }
        myRentalDetailViewModel.callUpdateRentalPosting(
            tokenManager.getAccessToken().toString(), postingId, rentalPostingUpdateDTO
        )
    }

    // Function to validate all fields
    private fun setupTextWatchers() {
        // Username TextWatcher
        binding.includePaymentMethod12.etRoomPrice.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun afterTextChanged(s: Editable?) {


                // Loại bỏ TextWatcher tạm thời để tránh loop
                binding.includePaymentMethod12.etRoomPrice.removeTextChangedListener(this)

                val input =
                    s.toString().replace("[^\\d]".toRegex(), "") // Loại bỏ các ký tự không phải số

                if (input.isNotEmpty()) {
                    // Kiểm tra và loại bỏ số 0 đầu tiên nếu có
                    var cleanedInput = input
                    if (cleanedInput.startsWith("0")) {
                        cleanedInput = cleanedInput.substring(1) // Loại bỏ số 0 đầu tiên
                    }
                    val numericValue = input.toLongOrNull() ?: 0
                    when {
                        numericValue < 10000 -> {
                            // Hiển thị helper text nếu số tiền nhỏ hơn 100.000
                            binding.includePaymentMethod12.tilRoomPrice.helperText =
                                "Số tiền tối thiểu là 10.000"
                        }

                        numericValue > 100_000_000 -> {
                            // Hiển thị helper text nếu số tiền lớn hơn 100 Triệu
                            binding.includePaymentMethod12.tilRoomPrice.helperText =
                                "Số tiền tối đa cho 1 đêm là 100 triệu"
                        }

                        else -> {
                            // Ẩn helper text khi số tiền đạt yêu cầu
                            binding.includePaymentMethod12.tilRoomPrice.helperText = null
                        }
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " VNĐ"
                    current = formatted
                    binding.includePaymentMethod12.etRoomPrice.setText(formatted)
                    binding.includePaymentMethod12.etRoomPrice.setSelection(formatted.length - 4) // Đặt con trỏ vào vị trí trước "đ"
                    val amount = binding.includePaymentMethod12.etRoomPrice.text.toString()
                        .replace("[^\\d]".toRegex(), "").toLongOrNull()
                    if (amount != null) {
                        if (amount > 0) {
                            myRentalDetailViewModel.updatePricePerNight(amount)
                        }
                    }

                } else {
                    binding.includePaymentMethod12.etTotalPrice.setText(null)
                }

                // Thêm lại TextWatcher sau khi cập nhật văn bản
                binding.includePaymentMethod12.etRoomPrice.addTextChangedListener(this)
            }


            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
        myRentalDetailViewModel.clearListImageForPut()
        imageUploadAdapter.clearAll()

        Log.d(
            "DasdasdasdadwsA",
            "onDestroyView: ${myRentalDetailViewModel.getImagesForPut()?.size}"
        )
        Log.d("DasdasdasdadwsA", "onDestroyView: ${imageUploadAdapter.differ.currentList?.size}")
    }

    override fun getTheme(): Int {
        return R.style.MyBottomSheetDialogTheme // Use custom theme
    }

    private fun isImageValid(): Boolean {
        val subImages = myRentalDetailViewModel.getImagesForPut()?.size
            ?: 0 // Ảnh phụ là các ảnh sau ảnh chính
        return when {
            subImages == 0 || subImages!! < 6 -> {
                (activity as MyPostingDetailActivity).showWarningToast(
                    "Thiếu Ảnh",
                    "Vui lòng chọn ít nhất 6 ảnh"
                )
                false
            }

            else -> true
        }
    }

    private fun isPriceInputvalid(): Boolean {
        // Lấy giá từ ViewModel
        val pricePerNight = myRentalDetailViewModel.pricePerNight.value

        // Kiểm tra nếu giá tiền là null
        if (pricePerNight == null || pricePerNight < 10000 || pricePerNight > 100_000_000) {
            (activity as MyPostingDetailActivity).showWarningToast(
                "Lỗi",
                "Vui lòng nhập giá phòng từ 10,000 đến 100,000,000"
            )
            binding.includePaymentMethod12.tilRoomPrice.error =
                "Giá phòng phải từ 10,000 đến 100,000,000"
            return false
        }

        // Nếu tất cả các điều kiện đều hợp lệ
        binding.includePaymentMethod12.tilRoomPrice.error = null
        return true
    }


}