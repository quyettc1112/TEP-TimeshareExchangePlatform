package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Adapter.MyTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentSelectTimeshareBinding

class Step_3_SelectTimeshareFragment : BaseFragment(R.layout.fragment_select_timeshare) {

    private lateinit var binding: FragmentSelectTimeshareBinding
    private var myTimeshareAdapter = MyTimeshareAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTimeshareBinding.inflate(layoutInflater, container, false)
        initRecyclerView()

        return binding.root
    }

    private fun initAdapter() {
        myTimeshareAdapter.submitList(Constant.listMyTimeShare)
    }

    private fun initRecyclerView() {
        // Set adapter for recyclerview
        binding.recyclerView.let {
            it.adapter = myTimeshareAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}