package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.CustomeDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ExchangePostingUpdateDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter.ImageUPAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailViewModel
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
    private lateinit var pickImagesUPLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using View Binding
        _binding = DialogUpdateRentalPostingBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        initializeLaunchers()
        initAdapter()
        observeViewModel()
        bindDataImages()
        bindDataDescription()
        eventClickAddImage()
        eventClickSaveUpdate()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun observeViewModel() {
        // UpLoad Image
        myRentalDetailViewModel.uploadImageResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    (activity as MyExchangeDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                    }
                    myRentalDetailViewModel.addListImageForPut(it.data ?: emptyList())
                    imageUploadAdapter.submitList(myRentalDetailViewModel.getImagesForPut())

                }

                Status.ERROR -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Không thể tải ảnh lên")
                    }
                }
            }
        })

        // Update Exchange Posting
        myRentalDetailViewModel.updateExchangeResponse.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    (activity as MyExchangeDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                        showSuccessToast("Thành công", "Cập nhật bài đăng thành công")
                        (activity as MyExchangeDetailActivity).apply {
                          /*  myExchangeDetailViewModel.getCustomerExchangeDetail(
                                tokenManager.getAccessToken().toString(),
                                myExchangeDetailViewModel.myExchangeDetail.value?.data?.exchangePostingId ?: 0
                            )*/
                        }
                        dismiss()
                    }
                }

                Status.ERROR -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Không thể cập nhật bài đăng")
                    }
                }

                null -> {

                }
            }
        })

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
        pickImagesUPLauncher =
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
            pickImagesUPLauncher.launch("image/*")
        }
    }

    private fun eventClickSaveUpdate() {
        binding.btnSaveUpdatePosting.setOnClickListener {
            if (!isImageValid()) {
                return@setOnClickListener
            }
            val description = binding.etNote.text.toString()

            val exchangePostingUpdateDTO = myRentalDetailViewModel.getImagesForPut()?.let { it1 ->
                ExchangePostingUpdateDTO(
                    description = description,
                    imageUrls = it1
                )
            }

          /*  callUpdateExchangePosting(
                myExchangeDetailViewModel.postingDetailResponse.value?.data?.exchangePostingId ?: 0,
                exchangePostingUpdateDTO!!
            )*/
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

    private fun callUploadImages() {
        if (!tokenManager.isLoggedIn()) {
            return
        }
        myRentalDetailViewModel.callUploadImages(tokenManager.getAccessToken().toString())
    }

/*    private fun callUpdateExchangePosting(postingId: Int, exchangePostingUpdateDTO: ExchangePostingUpdateDTO) {
        if (!tokenManager.isLoggedIn()) {
            return
        }
        myExchangeDetailViewModel.callUpdateExchangePosting(
            tokenManager.getAccessToken().toString(),postingId, exchangePostingUpdateDTO
        )
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
        myRentalDetailViewModel.clearListImageForPut()



    }

    override fun getTheme(): Int {
        return R.style.MyBottomSheetDialogTheme // Use custom theme
    }


    private fun isImageValid(): Boolean {
        val subImages = myRentalDetailViewModel.getImagesForPut()?.size
            ?: 0 // Ảnh phụ là các ảnh sau ảnh chính
        return when {
            subImages == 0 || subImages!! < 6 -> {
                (activity as MyExchangeDetailActivity).showWarningToast(
                    "Thiếu Ảnh",
                    "Vui lòng chọn ít nhất 6 ảnh"
                )
                false
            }

            else -> true
        }
    }


}