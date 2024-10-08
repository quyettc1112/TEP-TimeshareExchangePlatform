package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.LocationModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.FaqAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.TimeshareCompanyAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCheckTimeshareBinding


class Step_1_CheckTimeshareFragment : BaseFragment(R.layout.fragment_check_timeshare) {
    private lateinit var binding: FragmentCheckTimeshareBinding
    private var timeshareCompanyAdapter = TimeshareCompanyAdapter()
    private var faqAdapter = FaqAdapter()
    private val rentalPostingViewModel: RentalPostingViewModel by activityViewModels()
    private lateinit var locationResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeshareCompanyAdapter.submitList(Constant.listTimeshareCompany)
        faqAdapter.submitList(Constant.listFaq)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckTimeshareBinding.inflate(inflater, container, false)
        initActivityLauncher()
        setRecyclerView()
        setScrollToEvent()
        setEventInputAction()

        return binding.root
    }

    // Button or Search Click Event
    private fun setEventInputAction(){
        binding.btnYes.setOnClickListener {
            // Go to My Timeshare List
            // (activity as RentalPostingActivity).goToCreateTimeshare()
            rentalPostingViewModel.updateStep(3)
        }

        binding.btnNo.setOnClickListener {
            // Go to Create Timeshare
            // (activity as RentalPostingActivity).goToCreateTimeshare()
            rentalPostingViewModel.updateStep(2)
        }

        binding.searchInput.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            intent.putExtras(Bundle().apply {
                putString(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, "getResortLocation")
            })
            locationResultLauncher.launch(intent)
        }

    }


    private fun setRecyclerView() {
        // Timeshare Company
        binding.rcTimeshareCompany.adapter = timeshareCompanyAdapter

        // FAQ
        binding.rvFaq.adapter = faqAdapter

    }

    private fun setScrollToEvent() {
        binding.btnSeeMore.setOnClickListener {
            // Scroll to FAQ
            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, binding.cslInputSection.top)
            }
        }

    }

    private fun initActivityLauncher() {
        locationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedLocation: LocationModel? = data?.getParcelableExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW)
                    selectedLocation?.let {
                        rentalPostingViewModel.updateLocationModel(selectedLocation)
                        rentalPostingViewModel.updateStep(2)
                    }
                }
            }

    }



}

