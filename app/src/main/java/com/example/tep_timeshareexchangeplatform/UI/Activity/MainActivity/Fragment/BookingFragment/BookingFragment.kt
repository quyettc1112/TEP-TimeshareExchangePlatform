package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.Adapter.MyOrderAdapter
import com.example.tep_timeshareexchangeplatform.databinding.FragmentBookingBinding

class BookingFragment : BaseFragment(R.layout.fragment_booking) {

    private val viewModel: BookingViewModel by viewModels()
    private lateinit var binding: FragmentBookingBinding
    private var myOrderAdapter = MyOrderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        setOrderList()

        return binding.root
    }

    private fun initAdapter() {
        myOrderAdapter.submitList(Constant.myOrderList)
    }

    private fun setOrderList() {
        binding.rvOrderList.adapter = myOrderAdapter
    }


}