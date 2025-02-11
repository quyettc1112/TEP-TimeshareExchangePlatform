package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionReceivedFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter.MyTransactionAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment.AllViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment.TransactionAllFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment.TransactionAllFragment.Companion
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTransactionAllBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTransactionReciveBinding


class TransactionReceivedFragment : BaseFragment(R.layout.fragment_transaction_payment) {
    private lateinit var binding: FragmentTransactionReciveBinding
    private val myTransactionViewModel: ReceivedViewModel by activityViewModels()
    private val myTransactionAdapter = MyTransactionAdapter()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        tokenManager = TokenManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionReciveBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        //observerData()
        myTransactionAdapter.submitList(Constant.dummyWalletReciceList)
        setupRecyclerView()
        return binding.root
    }

    private fun initAdapter() {
        myTransactionAdapter.onItemClick = {
            val intent = Intent(requireContext(), MyTransactionDetailActivity::class.java)
            intent.putExtra(Constant.TRANSACTION_ID, it.id)
            startActivity(intent)
        }
    }

    private fun observerData() {
        myTransactionViewModel.walletListResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.lottieLoading.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.lottieLoading.visibility = View.GONE
                    myTransactionViewModel.loadMoreWalletList(it.data!!.content)
                    myTransactionAdapter.submitList(myTransactionViewModel.getCurrentWalletList())
                }

                Status.ERROR -> {
                    binding.lottieLoading.visibility = View.GONE
                    MotionToast.createToast(
                        requireActivity(),
                        "Error",
                        it.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }
        myTransactionViewModel.currentWalletPage.observe(viewLifecycleOwner) {
            myTransactionViewModel.getWalletList(
                TokenManager(requireContext()).getAccessToken().toString(), it,
                TransactionAllFragment.WALLET_PAGE_SIZE
            )
        }
    }

    private fun setupRecyclerView() {
        binding.rvTransactionAll.apply {
            adapter = myTransactionAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // Scroll Listener
        binding.rvTransactionAll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages =
                    myTransactionViewModel.walletListResponse.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && myTransactionViewModel.currentWalletPage.value!! < totalPages - 1) {
                    myTransactionViewModel.incrementCurrentWalletsPage()
                }
            }
        })
    }


}