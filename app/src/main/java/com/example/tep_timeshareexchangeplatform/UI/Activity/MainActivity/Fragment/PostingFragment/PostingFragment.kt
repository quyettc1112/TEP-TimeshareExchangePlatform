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
import com.example.tep_timeshareexchangeplatform.UI.Activity.PaymentPackageActivity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.PostingFragment.Adapter.IntroSliderAdapter
import com.example.tep_timeshareexchangeplatform.databinding.DialogPostingBottomNavBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentPostingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        return binding.root
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
        binding.llLayoutRentTimeshare.setOnClickListener {
            startActivity(Intent(requireContext(), MemberShipActivity::class.java))
        }

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