package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.AccountFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.MyExchangeRequestActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity.NotificationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.DepositActivity.DepositActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostings.MyExchangePostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity.MyInfoActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.MyOrderActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingList.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.MyTimeshareActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var tokenManager: TokenManager
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(requireContext())
        initActivityLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        depositButtonClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserActivitiesEvent()
        logoutDialog()

        observeViewModel()
    }

    // Observer
    private fun observeViewModel() {
        // Get User Info
        mainViewModel.userJWTPayload.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvUserName.text = it.sub
            }
        }

        mainViewModel.userLogState.observe(viewLifecycleOwner) {
            setUserLoginState(it)
        }

        mainViewModel.customerProfileInfo.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvBalance.text = Constant.formatPriceLong(it.walletAvailableMoney) + " đ"
                Glide.with(requireContext())
                    .load(it.avatar)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(binding.ivUserAvt)
            }
        }
    }

    private fun setUserLoginState(userLogState: UserLogState) {
        when (userLogState) {
            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                binding.apply {
                    // Change Button Text
                    btnLogout.text = getString(R.string.btn_logout)
                    btnLogout.setTextColor(resources.getColor(R.color.black))
                    btnLogout.backgroundTintList = null
                    llCustomerContainer.visibility = View.VISIBLE

                    llMyTimeshare.visibility = View.VISIBLE
                    llMyPosting.visibility = View.VISIBLE
                    llMyExchangePosting.visibility = View.VISIBLE
                    llMyTransaction.visibility = View.VISIBLE
                    llMyExchangeRequest.visibility = View.VISIBLE

                    // Un Hide Wallet
                    cardWalletContainer.visibility = View.VISIBLE

                    // Un Hide is Member
                    tvIsMembership.visibility = View.VISIBLE
                    animMembership.visibility = View.VISIBLE

                }
            }

            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                binding.apply {
                    // Change Button Text
                    btnLogout.text = getString(R.string.btn_logout)
                    btnLogout.setTextColor(resources.getColor(R.color.black))
                    btnLogout.backgroundTintList = null
                    llCustomerContainer.visibility = View.VISIBLE

                    // Un Hide Wallet
                    cardWalletContainer.visibility = View.VISIBLE

                    // Hide is Member
                    tvIsMembership.visibility = View.GONE
                    animMembership.visibility = View.GONE

                    llMyTimeshare.visibility = View.VISIBLE
                    llMyPosting.visibility = View.VISIBLE
                    llMyExchangePosting.visibility = View.VISIBLE
                    llMyTransaction.visibility = View.VISIBLE
                    llMyExchangeRequest.visibility = View.VISIBLE

                }
            }

            UserLogState.LOGGED_IN_AS_USER -> {
                binding.apply {
                    // Change Button Text
                    btnLogout.text = getString(R.string.btn_logout)
                    btnLogout.setTextColor(resources.getColor(R.color.black))
                    btnLogout.backgroundTintList = null
                    llCustomerContainer.visibility = View.VISIBLE

                    // Hide Wallet
                    cardWalletContainer.visibility = View.GONE
                    // Hide is Member
                    tvIsMembership.visibility = View.GONE
                    animMembership.visibility = View.GONE

                    llMyTimeshare.visibility = View.GONE
                    llMyPosting.visibility = View.GONE
                    llMyExchangePosting.visibility = View.GONE
                    llMyTransaction.visibility = View.GONE
                    llMyExchangeRequest.visibility = View.GONE

                }
            }

            UserLogState.LOGGED_OUT -> {
                binding.apply {
                    // Change Button Text
                    btnLogout.text = getString(R.string.login)
                    btnLogout.setTextColor(resources.getColor(R.color.white))
                    btnLogout.backgroundTintList =
                        resources.getColorStateList(R.color.blue_btn_search)

                    // Hide Customer Container
                    llCustomerContainer.visibility = View.GONE
                }
            }

            else -> { /* nothing to do */
            }
        }
    }







    private fun setUserActivitiesEvent() {
        binding.apply {
            // Chỉnh Ngôn ngữ
            llSettingLang.setOnClickListener { (activity as? BaseActivity)?.showLanguageDialog() }

            // Xem List Order
            llMyOrder.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyOrderActivity::class.java
                    )
                )
            }

            // Hỗ trợ
            llHelpCenter.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MemberShipActivity::class.java
                    )
                )
            }

            binding.toolblarCustome.onEndIconClick =  {
                startActivity(Intent(requireContext(), NotificationActivity::class.java))
            }

            // Bài Đăng của tôi
            llMyPosting.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyPostingActivity::class.java
                    )
                )
            }
            llMyExchangePosting.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyExchangePostingActivity::class.java
                    )
                )
            }

            llMyExchangeRequest.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyExchangeRequestActivity::class.java
                    )
                )
            }

            // Giao dịch của tôi
            llMyTransaction.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyTransactionActivity::class.java
                    )
                )
            }

            // Timeshare của tôi
            llMyTimeshare.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyTimeshareActivity::class.java
                    )
                )
            }

            // My Profile
            llMyProfileInfo.setOnClickListener {
                val intent = Intent(requireContext(), MyInfoActivity::class.java)
                resultLauncher.launch(intent)
            }
        }

    }

    private fun depositButtonClick() {
        binding.cardWalletContainer.setOnClickListener {
            startActivity(Intent(requireContext(), DepositActivity::class.java))
        }
        binding.btnDeposit.setOnClickListener {
            startActivity(Intent(requireContext(), DepositActivity::class.java))
        }


    }

    private fun logoutDialog() {
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            mainViewModel.setUserLogState(UserLogState.LOGGED_OUT)
            tokenManager.clearAllToken()
        }
    }

    private fun initActivityLauncher() {
        // Initialize the result launcher
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                }
            }


    }


}