package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter.MyTransactionAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ViewModel.MyTransactionViewModel
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTransactionAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionAllFragment : BaseFragment(R.layout.fragment_transaction_all) {

    private lateinit var binding: FragmentTransactionAllBinding
    private val myTransactionViewModel: AllViewModel by activityViewModels()
    private val myTransactionAdapter = MyTransactionAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observerData() {

    }



}