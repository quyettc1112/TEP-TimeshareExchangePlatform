package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CreateCustomerDialog.DialogUpdateCustomer
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity.ViewModel.MyInfoViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private val viewModel: MyInfoViewModel by viewModels()
    private lateinit var tokenManager: TokenManager
    private var dialogUpdateCustomer: DialogUpdateCustomer? = null
    private var image: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)
        observeViewModel()
        eventClickUpdateCustomerInfo()
        binding.customToolbar.onStartIconClick = {
            finish()
        }

    }

    private fun observeViewModel() {
        // Check User State Log
        viewModel.userLogState.observe(this, {
            bindDataBaseOnUserLogState(it)
        })

        // Get Customer Profile
        viewModel.customerProfile.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    if (it.data!!.isMember) {
                        viewModel.setUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                    } else {
                        viewModel.setUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    // Nếu Người dùng Ko phải là Customer
                    if (it.message!!.contains("404")) {
                        // Log As User
                        viewModel.setUserLogState(UserLogState.LOGGED_IN_AS_USER)
                    } else {
                        showErrorToast(it.message)
                    }
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        })

        // Call Update Profile
        viewModel.updateCustomerProfile.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessToast("Cập nhật thông tin thành công")
                    it.data?.let { it1 -> tokenManager.saveProfileInfo(it1) }
                    val response = it.data
                    binding.apply {
                        tvEmail.text = response!!.userEmail
                        tvFullNameOut.text = response!!.userUserName
                        tvUserName.text = response!!.userUserName
                        tvFullNameIn.text = response!!.fullName
                        tvDob.text = Constant.formatDateByLocale(response.dob, this@MyInfoActivity)
                        tvAddress.text = response.address
                        tvGender.text = response.gender
                        tvPhone.text = response.phone
                        Glide.with(this@MyInfoActivity)
                            .load(response.avatar)
                            .error(R.drawable.ic_image_placeholder)
                            .into(ivUserAvt)
                        btnEditButton.visibility = View.VISIBLE
                        btnEditButton.text = "Cập nhật thông tin !"
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast(it.message!!)
                    Log.d("CheckValue", it.message!!)
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        })

        // Get Image Response
        viewModel.uploadImageResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    val response = it.data
                    if (response != null) {
                        dialogUpdateCustomer?.saveImageResponse(response[0])
                        Log.d("CheckValue", image)
                    }
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    showErrorToast(it.message!!)
                }

                Status.LOADING -> {
                    showLoadingWaiting(true)
                }
            }
        })


    }

    private fun bindDataBaseOnUserLogState(userLogState: UserLogState) {
        when (userLogState) {
            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                binding.apply {
                    bindDataLogAsCustomerMember()
                }
            }

            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                binding.apply {
                    bindDataLogAsCustomer()
                }
            }

            UserLogState.LOGGED_IN_AS_USER -> {
                binding.apply {
                    bindDataLogAsUser()
                }
            }

            UserLogState.LOGGED_OUT -> {
                binding.apply {
                    finish()
                }
            }

            else -> { /* nothing to do */
            }
        }
    }

    private fun ActivityMyInfoBinding.bindDataLogAsUser() {
        llMembershipContainer.visibility = View.GONE

        // Chang Title
        tvTitle.text = "Thông tin cá nhân"
        tvTitleDescription.text =
            "Lưu thông tin của Quý Khách để sử dụng dịch vụ tốt nhất"

        // Clear Customer Info
        clearCustomerInfo()

        // Bind User Info
        val userJWTPayloadModel =
            JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
        binding.apply {
            tvEmail.text = userJWTPayloadModel!!.email
            tvFullNameOut.text = userJWTPayloadModel!!.email
            tvUserName.text = userJWTPayloadModel!!.sub
            btnEditButton.visibility = View.GONE
            btnEditButton.text = "Cập nhật thông tin !"
        }
    }

    private fun ActivityMyInfoBinding.bindDataLogAsCustomer() {
        llMembershipContainer.visibility = View.GONE

        // Chang Title
        tvTitle.text = "Thông tin cá nhân"
        tvTitleDescription.text =
            "Lưu thông tin của Quý Khách để sử dụng dịch vụ tốt nhất"

        // Clear Customer Info
        clearCustomerInfo()
        val userProfileResponse = viewModel.customerProfile.value!!.data
        binding.apply {
            tvEmail.text = userProfileResponse?.userEmail
            tvFullNameOut.text = userProfileResponse?.fullName
            tvUserName.text = userProfileResponse?.userUserName
            tvFullNameIn.text = userProfileResponse?.fullName
            tvDob.text = userProfileResponse?.let { Constant.formatDateByLocale(it.dob, this@MyInfoActivity) }
            tvAddress.text = userProfileResponse?.address
            tvGender.text = userProfileResponse?.gender
            tvPhone.text = userProfileResponse?.phone
            Glide.with(this@MyInfoActivity)
                .load(userProfileResponse?.avatar)
                .error(R.drawable.ic_image_placeholder)
                .into(ivUserAvt)
            btnEditButton.visibility = View.VISIBLE
            btnEditButton.text = "Cập nhật thông tin !"
        }
    }

    private fun ActivityMyInfoBinding.bindDataLogAsCustomerMember() {
        llMembershipContainer.visibility = View.VISIBLE

        // Chang Title
        tvTitle.text = "Chứng nhận thành viên"
        tvTitleDescription.text =
            "Bạn đã trở thành thành viên của TEP, hãy cùng chúng tôi khám phá những trải nghiệm tuyệt vời nhất"

        // Clear Customer Info
        clearCustomerInfo()

        // Bind User Info
        val userProfileResponse = viewModel.customerProfile.value!!.data
        binding.apply {
            tvEmail.text = userProfileResponse!!.userEmail
            tvFullNameOut.text = userProfileResponse!!.fullName
            tvUserName.text = userProfileResponse!!.userUserName
            tvFullNameIn.text = userProfileResponse!!.fullName
            tvDob.text = Constant.formatDateByLocale(userProfileResponse.dob, this@MyInfoActivity)
            tvAddress.text = userProfileResponse.address
            tvGender.text = userProfileResponse.gender
            tvPhone.text = userProfileResponse.phone
            Glide.with(this@MyInfoActivity)
                .load(userProfileResponse.avatar)
                .error(R.drawable.ic_image_placeholder)
                .into(ivUserAvt)
            btnEditButton.visibility = View.VISIBLE
            btnEditButton.text = "Cập nhật thông tin !"
        }
        binding.apply {
            tvMemberCode.text = userProfileResponse!!.membershipId.toString()
            tvBecomeMemberDate.text = Constant.formatDateByLocale(
                userProfileResponse.memberPurchaseDate,
                this@MyInfoActivity
            )

            tvExpireDate.text =
                Constant.formatDateByLocale(
                    userProfileResponse.memberExpiryDate,
                    this@MyInfoActivity
                )

            animMembership.visibility = View.VISIBLE
        }

    }

    private fun eventClickUpdateCustomerInfo() {
        binding.btnEditButton.setOnClickListener {
            val currentProfileDTo = ProfileDTO(
                fullName = viewModel.customerProfile.value?.data?.fullName ?: "",
                avatar = viewModel.customerProfile.value?.data?.avatar ?: "",
                dob = viewModel.customerProfile.value?.data?.dob ?: "",
                address = viewModel.customerProfile.value?.data?.address ?: "",
                gender = viewModel.customerProfile.value?.data?.gender ?: "",
                phone = viewModel.customerProfile.value?.data?.phone ?: ""
            )
            dialogUpdateCustomer = DialogUpdateCustomer(this,
                pickSingleImageLauncherDialog,
                object : DialogUpdateCustomer.ConfirmCallback {
                    override fun positiveAction(profileDTO: ProfileDTO) {
                        if (profileDTO != null) {
                            callUpdateCustomerProfile(profileDTO)
                            Log.d("CheckValue", profileDTO.toString())
                        }
                    }
                },
                currentProfileDTo
            )
            dialogUpdateCustomer!!.show()

        }
    }

    private fun clearCustomerInfo() {
        binding.apply {
            tvFullNameIn.text = ""
            tvFullNameOut.text = ""
            tvDob.text = ""
            tvAddress.text = ""
            tvGender.text = ""
            tvPhone.text = ""
            Glide.with(this@MyInfoActivity)
                .load(R.drawable.ic_image_placeholder)
                .into(ivUserAvt)
        }
    }

    private fun callGetCustomerProfile() {
        if (tokenManager.getAccessToken() == null && !tokenManager.isLoggedIn()) {
            return
        }
        viewModel.getCustomerProfile(tokenManager.getAccessToken().toString())
    }

    private fun callUpdateCustomerProfile(profileDTO: ProfileDTO) {
        if (tokenManager.getAccessToken() == null && !tokenManager.isLoggedIn()) {
            return
        }
        viewModel.updateCustomerProfile(tokenManager.getAccessToken().toString(), profileDTO)
    }

    private fun showErrorToast(message: String) {
        // Show Error Toast
        MotionToast.createColorToast(
            this,
            "Error",
            message,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null
        )
    }

    private fun showSuccessToast(message: String) {
        // Show Success Toast
        MotionToast.createColorToast(
            this,
            "Success",
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null
        )
    }



    override fun onResume() {
        super.onResume()
        callGetCustomerProfile()
    }

    private val pickSingleImageLauncherDialog =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                dialogUpdateCustomer?.setAvatar(it) // Cập nhật ảnh avatar trong Dialog
                viewModel.setMainImage(ImageUploadModel.create(uri))
                viewModel.callUploadImages(tokenManager.getAccessToken().toString())
            }
        }

}