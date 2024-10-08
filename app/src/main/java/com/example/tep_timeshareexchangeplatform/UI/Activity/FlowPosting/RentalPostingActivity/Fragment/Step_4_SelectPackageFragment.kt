package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.PackagePostingAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentSelectPackageBinding

class Step_4_SelectPackageFragment : BaseFragment(R.layout.fragment_select_package) {

    private lateinit var binding: FragmentSelectPackageBinding
    private var packagePostingAdapter = PackagePostingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPackageBinding.inflate(layoutInflater, container, false)
        setViewPagerPackage()
        return binding.root
    }

    private fun initAdapter() {
        packagePostingAdapter.submitList(Constant.listPackage)
    }

    private fun setViewPagerPackage() {
        binding.vpPackagePosting.apply {
            adapter = packagePostingAdapter
            offscreenPageLimit = 3
        }

        binding.indicator.setViewPager(binding.vpPackagePosting)

    }




}