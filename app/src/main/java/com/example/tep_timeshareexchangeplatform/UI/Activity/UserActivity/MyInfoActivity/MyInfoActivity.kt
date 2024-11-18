package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
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

        // Bind User Info
        val userJWTPayloadModel =
            JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
        val userProfileResponse = viewModel.customerProfile.value!!.data
        binding.apply {
            tvEmail.text = userJWTPayloadModel!!.email
            tvFullNameOut.text = userJWTPayloadModel!!.email
            tvUserName.text = userJWTPayloadModel!!.sub
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
            tvFullNameOut.text = userProfileResponse!!.userEmail
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


}