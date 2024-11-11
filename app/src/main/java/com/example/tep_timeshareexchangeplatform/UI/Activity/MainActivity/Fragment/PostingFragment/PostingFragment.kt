package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment.Adapter.IntroSliderAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.DialogPostingBottomNavBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentPostingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostingFragment : BaseFragment(R.layout.fragment_posting) {
    private lateinit var binding: FragmentPostingBinding
    private var introSliderAdapter = IntroSliderAdapter()
    private lateinit var tokenManager: TokenManager

    private val viewModel: PostingViewModel by viewModels()
    private val viewModelMain: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introSliderAdapter.submitList(Constant.listIntroSlider)
        tokenManager = TokenManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostingBinding.inflate(inflater, container, false)
        setIntroSlider()
        observeData()

        return binding.root
    }

    private fun observeData() {
        viewModel.isCustomerExist.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    if (it.data!!.isMember) {
                        tokenManager.saveCustomerInfo(it.data)
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
                        intentToRentalPostingActivity()
                    } else {
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
                        intentToMemberShipActivity()
                    }
                }

                Status.ERROR -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    if(it.message!!.contains("404")) {
                        intentToMemberShipActivity()
                    }

                }

                Status.LOADING -> {
                    (activity as MainActivity).showLoadingWaiting(true)
                }
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun setIntroSlider() {
        binding.vpPostingIntroduction.adapter = introSliderAdapter
        // Set up indicator
        binding.indicator.apply {
            setViewPager(binding.vpPostingIntroduction)
            tintIndicator(R.color.blue_full)
        }
        // Event next Button
        binding.btnNext.setOnClickListener {
            binding.vpPostingIntroduction.currentItem =
                binding.vpPostingIntroduction.currentItem + 1
            if (binding.vpPostingIntroduction.currentItem == introSliderAdapter.itemCount - 1) {
                showPostingOptionDialog()
            }
        }
    }

    private fun showPostingOptionDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme)
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_posting_bottom_nav, null)
        val binding = DialogPostingBottomNavBinding.bind(view)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            convertDpToPx(200)
        )
        // Gắn view vào dialog
        dialog.setContentView(view)

        // Hiển thị dialog
        dialog.show()

        // Event Dialog

        // Rental click
        binding.llLayoutRentTimeshare.setOnClickListener {
            if (tokenManager.getAccessToken() != null) {
                callIsCustomerExist()
            } else {
                dialog.dismiss()
                (activity as MainActivity).binding.vp2Main.currentItem = 4
                MotionToast.Companion.createColorToast(
                    requireActivity(),
                    "Lỗi",
                    "Bạn cần đăng nhập để thực hiện chức năng này",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }

        // Exchange Click
        binding.llLayoutExchangeTimeshare.setOnClickListener {
            startActivity(Intent(requireContext(), MemberShipActivity::class.java))
        }


    }

    private fun intentToRentalPostingActivity() {
        startActivity(Intent(requireContext(), RentalPostingActivity::class.java))
    }

    private fun intentToMemberShipActivity() {
        startActivity(Intent(requireContext(), MemberShipActivity::class.java))
    }

    private fun callIsCustomerExist() {
        viewModel.callIsCustomerExist(tokenManager.getAccessToken().toString())
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = requireContext().resources.displayMetrics.density
        return (dp * density).toInt()
    }


}