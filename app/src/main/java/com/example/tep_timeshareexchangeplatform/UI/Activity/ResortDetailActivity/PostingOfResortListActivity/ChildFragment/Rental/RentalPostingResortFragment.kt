package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.ChildFragment.Rental

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.PostingDetailActivity.PostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingAdapterRV
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingFragment.Companion
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.SearchPostingViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.PostingOfResortActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.PostingOfResortViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentRentalPostingResortBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTimeshareBinding
import dagger.hilt.android.AndroidEntryPoint

class RentalPostingResortFragment : BaseFragment(R.layout.fragment_rental_posting_resort) {

    companion object {
        const val PAGE_SIZE = 16
    }
    private lateinit var binding: FragmentRentalPostingResortBinding
    private var publicPostingAdapterRV = PublicPostingAdapterRV()
    private val viewModel: PostingOfResortViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publicPostingAdapterRV.submitList(listOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRentalPostingResortBinding.inflate(layoutInflater, container, false)
        setPublicPostingListUI()
        binding.rcPosting.adapter = publicPostingAdapterRV
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.publicRentalPosingList.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    resources.data?.let {
                        viewModel.loadMorePostings(it.content)
                        publicPostingAdapterRV.submitList(viewModel.getCurrentPostingList())
                        Log.d("RentalPostingResortFragment", "onCreateView: viewModel.publicRentalPosingList.observe = ${viewModel.getCurrentPostingList()?.size}")
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.GONE
                    (activity as PostingOfResortActivity).showErrorToast("Lỗi", resources.message ?: "Không thể tải dữ liệu")
                }

                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentPostingsPage.observe(viewLifecycleOwner) {
            Log.d("RentalPostingResortFragment", "onCreateView: viewModel.currentPostingsPage.observe = $it")
            viewModel.getRentalPostingList(it, PAGE_SIZE)
        }


    }


    private fun setPublicPostingListUI() {
        binding.rcPosting.layoutManager =
            GridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rcPosting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()

                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.publicRentalPosingList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPostingsPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPostingsPage()
                }
            }
        })

        publicPostingAdapterRV.onItemClick = {
            val intent = Intent(requireActivity(), PostingDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_POSTING_ID, it.rentalPostingId)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
    }

}