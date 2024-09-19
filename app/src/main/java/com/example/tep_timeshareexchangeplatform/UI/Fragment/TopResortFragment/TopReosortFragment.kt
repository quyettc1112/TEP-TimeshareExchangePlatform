package com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopReosortBinding

class TopReosortFragment : BaseFragment(R.layout.fragment_top_reosort) {

    private lateinit var binding: FragmentTopReosortBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopReosortBinding.inflate(inflater, container, false)
        return binding.root
    }
}