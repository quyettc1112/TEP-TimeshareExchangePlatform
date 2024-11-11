package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.PackagePostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.RentalPostingActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.PackageEnum
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

    @SuppressLint("ResourceAsColor")
    private fun setViewPagerPackage() {
        binding.vpPackagePosting.apply {
            adapter = packagePostingAdapter
            offscreenPageLimit = 3
        }

        binding.indicator.apply {
            setViewPager(binding.vpPackagePosting)
            tintIndicator(R.color.blue_full)
        }

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
                        savePackageSelected()
                    }
                }
            )
        }

    }

    private fun savePackageSelected() {
        Toast.makeText(requireContext(), "Selected", Toast.LENGTH_SHORT).show()
        val packagePosition = binding.vpPackagePosting.currentItem
        when (packagePosition) {
            0 -> {
                rentalPostingViewModel.updatePackageStep4(PackageEnum.BASIC_SERVICE.packageModel)
            }

            1 -> {
                rentalPostingViewModel.updatePackageStep4(PackageEnum.ADVANCED_SERVICE.packageModel)
            }

            2 -> {
                rentalPostingViewModel.updatePackageStep4(PackageEnum.PREMIUM_SERVICE.packageModel)
            }

            3 -> {
                rentalPostingViewModel.updatePackageStep4(PackageEnum.DELEGATED_SERVICE.packageModel)
            }

            else -> {
                // Do Nothing
            }
        }
        rentalPostingViewModel.updateStep(5)
    }


}