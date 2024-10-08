package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.PackagePostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentSelectPackageBinding

class Step_4_SelectPackageFragment : BaseFragment(R.layout.fragment_select_package) {

    private lateinit var binding: FragmentSelectPackageBinding
    private var packagePostingAdapter = PackagePostingAdapter()
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()

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
        onButtonSelectPackageClick()
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

    private fun onButtonSelectPackageClick() {
        binding.btnNext.setOnClickListener {
            (activity as RentalPostingActivity).showConfirmDialog(
                title = "Confirm",
                message = "Selected this package?",
                positiveButtonTitle = "Yes",
                negativeButtonTitle = "No",
                textButton = null,
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {

                    }
                    override fun positiveAction() {
                        Toast.makeText(requireContext(), "Selected", Toast.LENGTH_SHORT).show()
                        rentalPostingViewModel.updatePackageStep4(packagePostingAdapter.differ.currentList[binding.vpPackagePosting.currentItem])
                        rentalPostingViewModel.updateStep(5)
                    }
                }
            )
        }

    }




}