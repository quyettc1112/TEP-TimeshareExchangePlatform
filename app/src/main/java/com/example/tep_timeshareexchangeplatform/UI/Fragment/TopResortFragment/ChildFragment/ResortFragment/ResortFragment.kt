package com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.ChildFragment.ResortFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ResortAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.FragmentHomeBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentResortBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopResortBinding

class ResortFragment : BaseFragment(R.layout.fragment_resort) {

    private lateinit var binding: FragmentResortBinding
    private var resortAdapter = ResortAdapterRV()

    companion object {
        fun newInstance() = ResortFragment()
    }
    private val viewModel: ResortViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resortAdapter.submitList(Constant.resortListMT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResortBinding.inflate(inflater, container, false)
        setResortList()

        return binding.root
    }

    private fun setResortList() {
        binding.rcTopResort.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = resortAdapter
        }


    }
}