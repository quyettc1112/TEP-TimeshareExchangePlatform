package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.FragmentCreateTimeshareBinding

class CreateTimeshareFragment : BaseFragment(R.layout.fragment_create_timeshare) {

    private lateinit var binding: FragmentCreateTimeshareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTimeshareBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}