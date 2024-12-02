package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.CustomDialog

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
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.Adapter.ImageUPAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailViewModel
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateExchangePostingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdateExchangeBottomDialog(
    private val description: String?,
    private val myExchangeDetailViewModel: MyExchangeDetailViewModel
) : BottomSheetDialogFragment() {

    private var _binding: DialogUpdateExchangePostingBinding? = null
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
        _binding = DialogUpdateExchangePostingBinding.inflate(inflater, container, false)
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
        myExchangeDetailViewModel.uploadImageResponse.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    (activity as MyExchangeDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                    }
                    myExchangeDetailViewModel.addListImageForPut(it.data ?: emptyList())
                    imageUploadAdapter.submitList(myExchangeDetailViewModel.getImagesForPut())

                }

                Status.ERROR -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Không thể tải ảnh lên")
                    }
                }

                null -> {

                }
            }
        })

        // Update Exchange Posting
        myExchangeDetailViewModel.updateExchangeResponse.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    (activity as MyExchangeDetailActivity).showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    (activity as MyExchangeDetailActivity).apply {
                        hideLoadingWaiting()
                        showSuccessToast("Thành công", "Cập nhật bài đăng thành công")
                        (activity as MyExchangeDetailActivity).apply {
                            myExchangeDetailViewModel.getCustomerExchangeDetail(
                                tokenManager.getAccessToken().toString(),
                                myExchangeDetailViewModel.myExchangeDetail.value?.data?.exchangePostingId ?: 0
                            )
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
            myExchangeDetailViewModel.deleteImageForPut(it)
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
                    myExchangeDetailViewModel.addImagesFromDevice(listImage)
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

            val exchangePostingUpdateDTO = myExchangeDetailViewModel.getImagesForPut()?.let { it1 ->
                ExchangePostingUpdateDTO(
                    description = description,
                    imageUrls = it1
                )
            }

            callUpdateExchangePosting(
                myExchangeDetailViewModel.myExchangeDetail.value?.data?.exchangePostingId ?: 0,
                exchangePostingUpdateDTO!!
            )
        }
    }

    private fun bindDataDescription() {
        binding.etNote.setText(description)
    }

    private fun bindDataImages() {
        myExchangeDetailViewModel.addListImageForPut(
            myExchangeDetailViewModel.myExchangeDetail.value?.data?.imageUrls ?: emptyList()
        )
        imageUploadAdapter.submitList(myExchangeDetailViewModel.getImagesForPut())
    }

    private fun callUploadImages() {
        if (!tokenManager.isLoggedIn()) {
            return
        }
        myExchangeDetailViewModel.callUploadImages(tokenManager.getAccessToken().toString())
    }

    private fun callUpdateExchangePosting(postingId: Int, exchangePostingUpdateDTO: ExchangePostingUpdateDTO) {
        if (!tokenManager.isLoggedIn()) {
            return
        }
        myExchangeDetailViewModel.callUpdateExchangePosting(
            tokenManager.getAccessToken().toString(),postingId, exchangePostingUpdateDTO
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
        myExchangeDetailViewModel.clearListImageForPut()



    }

    override fun getTheme(): Int {
        return R.style.MyBottomSheetDialogTheme // Use custom theme
    }


    private fun isImageValid(): Boolean {
        val subImages = myExchangeDetailViewModel.getImagesForPut()?.size
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