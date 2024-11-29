package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Fragment.TransactionAllFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter.MyTransactionAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.MyTransactionDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.ViewModel.MyTransactionViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTransactionAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionAllFragment : BaseFragment(R.layout.fragment_transaction_all) {

    private lateinit var binding: FragmentTransactionAllBinding
    private val myTransactionViewModel: AllViewModel by activityViewModels()
    private val myTransactionAdapter = MyTransactionAdapter()
    private lateinit var tokenManager: TokenManager

    companion object {
        const val WALLET_PAGE_SIZE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())
        initAdapter()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionAllBinding.inflate(inflater, container, false)
        observerData()
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
                    if (it.data?.totalPages == 0) {
                        (activity as MyTransactionActivity).showInfoDialog(
                            requireContext(),
                            "Bạn chưa có bài đăng giao dịch nào",
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    requireActivity().finish()
                                }
                            }
                        )
                    } else {
                        binding.lottieLoading.visibility = View.GONE
                        myTransactionViewModel.loadMoreWalletList(it.data!!.content)
                        myTransactionAdapter.submitList(myTransactionViewModel.getCurrentWalletList())
                    }
                }

                Status.ERROR -> {
                    binding.lottieLoading.visibility = View.GONE
                    (activity as MyTransactionActivity).showErrorToast(
                        "Có lỗi xảy ra", "Không thể tải dữ liệu"
                    )
                }
            }
        }
        myTransactionViewModel.currentWalletPage.observe(viewLifecycleOwner) {
            myTransactionViewModel.getWalletList(
                TokenManager(requireContext()).getAccessToken().toString(), it, WALLET_PAGE_SIZE
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