package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.FaqAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.TimeshareCompanyAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCheckTimeshareBinding


class CheckTimeshareFragment : BaseFragment(R.layout.fragment_check_timeshare) {
    private lateinit var binding: FragmentCheckTimeshareBinding
    private var timeshareCompanyAdapter = TimeshareCompanyAdapter()
    private var faqAdapter = FaqAdapter()

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
        setRecyclerView()
        setScrollToEvent()


        return binding.root
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



}

