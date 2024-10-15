package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTransactionModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter.MyTransactionAdapter
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.TransactionType
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTranscationBinding

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentTranscationBinding? = null

    private var myTransactionAdapter = MyTransactionAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }

        // Get the transaction type for this tab
        val transactionType = when (arguments?.getInt(ARG_SECTION_NUMBER)) {
            1 -> TransactionType.ALL
            2 -> TransactionType.TRANSFER
            3 -> TransactionType.WITHDRAW
            else -> TransactionType.ALL
        }
        // Filter the data based on transaction type
        val filteredTransactions = getTransactionData().filter { transaction ->
            when (transactionType) {
                TransactionType.ALL -> true
                TransactionType.TRANSFER -> transaction.type == 1
                TransactionType.WITHDRAW -> transaction.type == 2
            }
        }
        myTransactionAdapter.submitList(filteredTransactions)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTranscationBinding.inflate(inflater, container, false)
        val root = binding.root

        _binding!!.recyclerView.apply {
            adapter = myTransactionAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        return root
    }


    private fun getTransactionData(): List<MyTransactionModel> {
        // Return your actual data here
        return Constant.myTransactionList
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}