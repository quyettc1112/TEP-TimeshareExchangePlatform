package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.SuggestTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.TopResortViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareListActivity.TimeshareListActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTimeshareBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeshareFragment : BaseFragment(R.layout.fragment_timeshare) {

    companion object {
        fun newInstance() = TimeshareFragment()
    }

    private lateinit var binding: FragmentTimeshareBinding
    private var timeshareAdapter = TimeshareAdapterRV()
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeshareAdapter.submitList(listOf())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimeshareBinding.inflate(layoutInflater, container, false)
        binding.rcTimeshare.layoutManager =
            GridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rcTimeshare.adapter = timeshareAdapter

        timeshareAdapter.onItemClick = {
            val intent = Intent(requireActivity(), TimeshareListActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.postingsResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    Log.d("CheckValue Call", it.data.toString())
                    timeshareAdapter.submitList(it.data?.content)
                }

                Status.ERROR -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    MotionToast.Companion.createToast(
                        requireActivity(),
                        "Error",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    (activity as MainActivity).showLoadingWaiting(false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPostings(0,10,"")

    }
}