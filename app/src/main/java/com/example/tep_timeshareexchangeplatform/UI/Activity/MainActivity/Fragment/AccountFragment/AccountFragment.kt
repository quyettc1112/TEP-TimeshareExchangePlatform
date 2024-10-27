package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.AccountFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.AuthActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.MyOrderActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.MyPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionActivity
import com.example.tep_timeshareexchangeplatform.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

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
        mainViewModel.user.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvUserName.text = it.sub
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
        }

    }

    private fun logoutDialog() {
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }
    }


}