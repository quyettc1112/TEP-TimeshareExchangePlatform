package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.PackagePostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.ExchangePackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.databinding.FragmentSelectPackageBinding

class Step_4_SelectPackageFragment : BaseFragment(R.layout.fragment_select_package) {

    private lateinit var binding: FragmentSelectPackageBinding
    private var packagePostingAdapter = PackagePostingAdapter()
    private val postingFlowViewModel: PostingFlowViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPackageBinding.inflate(layoutInflater, container, false)
        onButtonSelectPackageClick()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        // Check Posting Flow Type. Use Package Posting Adapter to show the package
        postingFlowViewModel.typeOfPostingFlow.observe(viewLifecycleOwner) {
            when (it) {
                Constant.RENTAL_POSTING_FLOW -> {
                    packagePostingAdapter.submitList(
                        listOf(
                            RentalPackageEnum.BASIC_SERVICE.packageModel,
                            RentalPackageEnum.ADVANCED_SERVICE.packageModel,
                            RentalPackageEnum.PREMIUM_SERVICE.packageModel,
                            RentalPackageEnum.DELEGATED_SERVICE.packageModel
                        )
                    )
                }

                Constant.EXCHANGER_POSTING_FLOW -> {
                    packagePostingAdapter.submitList(
                        listOf(
                            ExchangePackageEnum.BASIC_SERVICE.packageModel,
                            ExchangePackageEnum.ADVANCED_SERVICE.packageModel,
                        )
                    )
                }
            }
            setViewPagerPackage()
        }
    }

    private fun setViewPagerPackage() {
        binding.vpPackagePosting.adapter = packagePostingAdapter
        binding.indicator.apply {
            setViewPager(binding.vpPackagePosting)
            tintIndicator(ContextCompat.getColor(requireContext(), R.color.blue_full))
        }

    }

    private fun onButtonSelectPackageClick() {
        binding.btnNext.setOnClickListener {
            (activity as PostingFlowActivity).showConfirmDialog(
                title = "Xác nhận",
                message = "Chọn gói dịch vụ này?",
                positiveButtonTitle = "Chọn",
                negativeButtonTitle = "Không",
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
        val packagePosition = binding.vpPackagePosting.currentItem
        when (postingFlowViewModel.typeOfPostingFlow.value) {
            Constant.RENTAL_POSTING_FLOW -> {
                saveRentalPackageSelected(packagePosition)
            }
            Constant.EXCHANGER_POSTING_FLOW -> {
                saveExchangePackageSelected(packagePosition)
            }
        }

        postingFlowViewModel.updateStep(5)
    }

    private fun saveRentalPackageSelected(packagePosition: Int) {
        when (packagePosition) {
            0 -> {
                postingFlowViewModel.updatePackageStep4(RentalPackageEnum.BASIC_SERVICE.packageModel)
            }

            1 -> {
                postingFlowViewModel.updatePackageStep4(RentalPackageEnum.ADVANCED_SERVICE.packageModel)
            }

            2 -> {
                postingFlowViewModel.updatePackageStep4(RentalPackageEnum.PREMIUM_SERVICE.packageModel)
            }

            3 -> {
                postingFlowViewModel.updatePackageStep4(RentalPackageEnum.DELEGATED_SERVICE.packageModel)
            }

            else -> {
                // Do Nothing
            }
        }
    }

    private fun saveExchangePackageSelected(packagePosition: Int) {
        when (packagePosition) {
            0 -> {
                postingFlowViewModel.updatePackageStep4(ExchangePackageEnum.BASIC_SERVICE.packageModel)
            }

            1 -> {
                postingFlowViewModel.updatePackageStep4(ExchangePackageEnum.ADVANCED_SERVICE.packageModel)
            }
            else -> {
                // Do Nothing
            }
        }
    }


}