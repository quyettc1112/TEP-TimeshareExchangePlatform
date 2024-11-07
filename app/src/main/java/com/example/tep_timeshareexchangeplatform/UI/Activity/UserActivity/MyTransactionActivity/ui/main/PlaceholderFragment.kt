package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTransactionModel
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter.MyTransactionAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ViewModel.MyTransactionViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.TransactionType
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTranscationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private val viewModel: MyTransactionViewModel by viewModels()
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
        }
        // Scroll Listener
        _binding!!.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.walletListResponse.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentWalletPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentWalletsPage()
                    Toast.makeText(requireContext(), "Load More", Toast.LENGTH_SHORT).show()
                }
            }
        })

        observeData()
        myTransactionAdapter.onItemClick = {
            val intent = Intent(requireContext(), MyTransactionDetailActivity::class.java)
            intent.putExtra(Constant.TRANSACTION_ID, it.id)
            startActivity(intent)
        }

        return root
    }

    private fun observeData() {
        viewModel.walletListResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.llLoading.visibility = View.GONE
                    it.data?.let { response ->
                        // Get the transaction type for this tab
                        val transactionType = when (arguments?.getInt(ARG_SECTION_NUMBER)) {
                            1 -> TransactionType.ALL
                            2 -> TransactionType.TRANSFER
                            3 -> TransactionType.WITHDRAW
                            else -> TransactionType.ALL
                        }

                        // Filter the data based on transaction type
                        val listTransaction = response.content.filter { transaction ->
                            when (transactionType) {
                                TransactionType.ALL -> true
                                TransactionType.TRANSFER -> transaction.money <= 0
                                TransactionType.WITHDRAW -> transaction.money > 0
                            }
                        }
                        Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                        viewModel.loadMoreWalletList(listTransaction)
                        myTransactionAdapter.submitList(viewModel.getCurrentWalletList())
                    }
                }

                Status.ERROR -> {
                    binding.llLoading.visibility = View.GONE
                    if (it.message!!.contains("404")) {
                        (activity as MyTransactionActivity).showInfoDialog(
                            requireContext(),
                            "Bạn chưa có giao dịch nào",
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    (activity as MyTransactionActivity).finish()
                                }
                            }
                        )
                    } else
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
                    binding.llLoading.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentWalletPage.observe(viewLifecycleOwner) {
            viewModel.getWalletList(TokenManager(requireContext()).getAccessToken().toString(), it, PAGE_SIZE)
        }
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

        const val PAGE_SIZE = 40
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }
}