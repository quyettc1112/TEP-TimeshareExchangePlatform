package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.ExchangePostingFragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.PostingDetailActivity.PostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingFragment.Companion.PAGE_SIZE
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.SearchPostingViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentExchangePostingBinding

class ExchangePostingFragment : BaseFragment(R.layout.fragment_exchange_posting) {

    private lateinit var binding: FragmentExchangePostingBinding
    private val adapter = ExchangePostingAdapter()
    private val viewModel: SearchPostingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.submitList(listOf())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangePostingBinding.inflate(inflater, container, false)
        setPublicPostingListUI()
        binding.rcExchangePosting.adapter = adapter
        observeViewModel()
        return binding.root
    }


    private fun observeViewModel() {
        viewModel.publicExchangePosingList.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    resources.data?.let {
                        viewModel.loadMoreExchange(it.content)
                        adapter.submitList(viewModel.getCurrentExchangeList())
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.GONE
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Error",
                        resources.message ?: "Error",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentExchangePage.observe(viewLifecycleOwner) {
            viewModel.getExchangePostingList(it, PAGE_SIZE, "")
        }


    }


    private fun setPublicPostingListUI() {
        binding.rcExchangePosting.layoutManager =
            GridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rcExchangePosting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                val totalElementOfAPI =
                    viewModel.publicExchangePosingList.value?.data?.totalElements ?: 0
                val currentListSizeOfAdapter = adapter.differ.currentList.size


                if (lastCompletelyVisibleItem == totalItemCount - 1 && currentListSizeOfAdapter < totalElementOfAPI) {
                    viewModel.incrementCurrentExchangePage()
                }
            }
        })

        adapter.onItemClick = {
            val intent = Intent(requireActivity(), PostingDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_POSTING_ID, it.exchangePostingId)
            startActivity(intent)
        }
    }
}