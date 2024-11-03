package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.AccountFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.DepositActivity.DepositActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyInfoActivity.MyInfoActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.MyOrderActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.MyTimeshareActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(requireContext())

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

        mainViewModel.customerInfo.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvBalance.text = Constant.formatPrice(it.walletAvailableMoney) + " đ"
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

                }
            }

            UserLogState.LOGGED_OUT -> {
                binding.apply {
                    // Change Button Text
                    btnLogout.text = getString(R.string.login)
                    btnLogout.setTextColor(resources.getColor(R.color.white))
                    btnLogout.backgroundTintList = resources.getColorStateList(R.color.blue_btn_search)

                    // Hide Customer Container
                    llCustomerContainer.visibility = View.GONE
                }
            }
            else -> { /* nothing to do */ }
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

            // Bài Đăng của tôi
            llMyPosting.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        MyPostingActivity::class.java
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
                startActivity(
                    Intent(
                        requireContext(),
                        MyInfoActivity::class.java
                    )
                )
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


}