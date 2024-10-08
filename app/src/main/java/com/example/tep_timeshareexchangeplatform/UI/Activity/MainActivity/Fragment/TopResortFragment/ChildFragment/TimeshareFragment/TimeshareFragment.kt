package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SuggestTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareListActivity.TimeshareListActivity
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTimeshareBinding

class TimeshareFragment : BaseFragment(R.layout.fragment_timeshare) {

    companion object {
        fun newInstance() = TimeshareFragment()
    }
    private val viewModel: TimeshareViewModel by viewModels()
    private lateinit var binding: FragmentTimeshareBinding
    private  var timeshareAdapter = TimeshareAdapterRV()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeshareAdapter.submitList(Constant.timeshareList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeshareBinding.inflate(layoutInflater, container, false)
        binding.rcTimeshare.layoutManager = GridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rcTimeshare.adapter = timeshareAdapter

        timeshareAdapter.onItemClick = {
            val intent = Intent(requireActivity(), TimeshareListActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }
}