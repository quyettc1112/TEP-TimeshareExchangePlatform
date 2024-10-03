package com.example.tep_timeshareexchangeplatform.UI.Fragment.PostingFragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.PostingBottomNavDialog.PostOptionsBottomSheet
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Fragment.PostingFragment.Adapter.IntroSliderAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentPostingBinding

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
               showBottomNavDialog()
            }
        }
    }

    private fun showBottomNavDialog() {
        val postOptionsBottomSheet = PostOptionsBottomSheet()
        postOptionsBottomSheet.show(requireActivity().supportFragmentManager, "PostOptionsBottomSheet")
    }
}