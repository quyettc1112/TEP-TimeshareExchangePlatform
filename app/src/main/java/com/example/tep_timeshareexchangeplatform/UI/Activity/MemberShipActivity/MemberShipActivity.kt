package com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CreateCustomerDialog.DialogUpdateCustomer
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.ProfileDTO
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.Adapter.MemberShipAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackage.PaymentPackageActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMemberShipBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberShipActivity : BaseActivity() {
    private lateinit var binding: ActivityMemberShipBinding
    private var memberShipAdapter = MemberShipAdapter()
    private val memberShipViewModel: MemberShipViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        tokenManager = TokenManager(this)
        binding = ActivityMemberShipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        initViewPagerView()
        observeData()

        binding.customToolbar4.onStartIconClick = {
            onBackPressed()
            finish()
        }
        binding.imgNext.setOnClickListener {
            startActivity(Intent(this, PostingFlowActivity::class.java))
        }
        clickRequestPaymentButton()

    }

    private fun observeData() {
        // Check Call Create Customer
        memberShipViewModel.createCustomerResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessToast("Tạo tài khoản thành công")
                    memberShipViewModel.getCustomerInfo(tokenManager.getAccessToken()!!)
                }

                Status.ERROR -> {
                    Log.d("CheckErrorCreate", it.message.toString() + " " + it.message.toString())
                    MotionToast.createColorToast(
                        this,
                        "Error",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    hideLoadingWaiting()
                }
            }
        }

        // Check Call Is Customer Exist
        memberShipViewModel.customerInfoResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    tokenManager.saveCustomerInfo(it.data!!)
                    tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                    val intent = Intent(this@MemberShipActivity, PaymentPackageActivity::class.java)
                    intent.putExtra(
                        Constant.DEFAULT_PACKAGE_SELECTION,
                        memberShipViewModel.currentPackage.value!!.id
                    )
                    startActivity(intent)
                    finish()

                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    if (it.message.toString().contains("404")) {
                        val dialogUpdateCustomer = showCustomerDialog()
                        dialogUpdateCustomer.show()
                    } else {
                        Log.d("CheckError", it.message.toString() + " " + it.message.toString())
                        showErrorToast(it.message.toString())
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        memberShipAdapter.submitList(Constant.listMemberShip)
    }

    private fun initViewPagerView() {
        binding.vpMemberShip.adapter = memberShipAdapter
        // Set Indicator
        binding.indicator.apply {
            setViewPager(binding.vpMemberShip)
            tintIndicator(ContextCompat.getColor(context, R.color.blue_full))
        }
        // Lắng nghe sự kiện thay đổi trang
        binding.vpMemberShip.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        memberShipViewModel.updateCurrentPackage(RentalPackageEnum.MEMBERSHIP_MONTHLY.packageModel)
                        Log.d("CheckCurrentPackage", "Updated to MONTHLY")
                    }

                    1 -> {
                        memberShipViewModel.updateCurrentPackage(RentalPackageEnum.MEMBERSHIP_YEARLY.packageModel)
                        Log.d("CheckCurrentPackage", "Updated to YEARLY")
                    }
                }
            }
        })
    }

    private fun callCreateCustomer(customerDTO: CustomerDTO){
        memberShipViewModel.callCreateCustomer(tokenManager.getAccessToken().toString(), customerDTO)
    }
    private fun callGetCustomerInfo() {
        memberShipViewModel.getCustomerInfo(tokenManager.getAccessToken().toString())
    }

    private fun showCustomerDialog(): MemberInfoDialog {
        val dialogUpdateCustomer =
            MemberInfoDialog(this, object : MemberInfoDialog.ConfirmCallback {
                override fun positiveAction(customerDTO: CustomerDTO) {
                   callCreateCustomer(customerDTO)
                }
            })
        return dialogUpdateCustomer
    }

    private fun clickRequestPaymentButton() {
        binding.ctrRequestButton.setOnClickListener {
            val user = JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken()!!)
            if (tokenManager.getAccessToken() != null && user != null) {
                callGetCustomerInfo()
            } else {
                showErrorToast("Token không hợp lệ ")
            }
        }
    }

    private fun showSuccessToast(message: String) {
        MotionToast.createColorToast(
            this,
            "Thành Công",
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        )
    }
    private fun showErrorToast(message: String) {
        MotionToast.createColorToast(
            this,
            "Error",
            message,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.inter_bold)
        )
    }


}