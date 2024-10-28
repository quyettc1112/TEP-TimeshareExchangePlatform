package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.PaymentPackageActivity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment.Adapter.IntroSliderAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.Until.JwtDetach.JwtDecoder
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

    private val viewModel: PostingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introSliderAdapter.submitList(Constant.listIntroSlider)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostingBinding.inflate(inflater, container, false)
        setIntroSlider()
        //observeData()

        return binding.root
    }

   /* private fun observeData() {
        // Check Call Is Customer Exist
        viewModel.isCustomerExist.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    (activity as MainActivity).showLoadingWaiting(true)
                }
                Status.SUCCESS -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    // User is Customer, Already Member, Active
                    if (it.data!!.isMember && it.data.isActive ) {
                        startActivity(Intent(requireContext(), RentalPostingActivity::class.java))
                    } else {
                        startActivity(Intent(requireContext(), MemberShipActivity::class.java))
                    }
                }

                Status.ERROR -> {
                    // 404 when User is not Customer, Intent to MemberShipActivity to Create Customer Info
                    (activity as MainActivity).hideLoadingWaiting()
                    if (it.message.toString().contains("404")) {
                        startActivity(Intent(requireContext(), MemberShipActivity::class.java))
                    } else {
                        Log.d("CheckError", it.message.toString() + " " + it.message.toString())
                        MotionToast.createColorToast(
                            requireActivity(),
                            "Error",
                            it.message.toString(),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )
                    }
                }
            }
        }

    }*/

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
            binding.vpPostingIntroduction.currentItem = binding.vpPostingIntroduction.currentItem + 1
            if (binding.vpPostingIntroduction.currentItem == introSliderAdapter.itemCount - 1) {
                showPostingOptionDialog()
            }
        }
    }

    private fun showPostingOptionDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_posting_bottom_nav, null)
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
       /* binding.llLayoutRentTimeshare.setOnClickListener {
            val tokenManager = TokenManager(requireContext())
            val user = JwtDecoder().parseJwtUsingGson(tokenManager.getAccessToken().toString())
            viewModel.callIsCustomerExist(
                tokenManager.getAccessToken().toString(),
                user?.userId!!
            )
        }*/

        // Exchange Click
        binding.llLayoutExchangeTimeshare.setOnClickListener {
            startActivity(Intent(requireContext(), MemberShipActivity::class.java))
        }


    }

    private fun convertDpToPx(dp: Int): Int {
        val density = requireContext().resources.displayMetrics.density
        return (dp * density).toInt()
    }


}