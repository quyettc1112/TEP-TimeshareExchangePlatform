package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.ExchangePostingFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.FragmentExchangePostingBinding

class ExchangePostingFragment : BaseFragment(R.layout.fragment_exchange_posting) {

    private lateinit var binding: FragmentExchangePostingBinding

    private val viewModel: ExchangePostingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangePostingBinding.inflate(inflater, container, false)
        return binding.root
    }
}