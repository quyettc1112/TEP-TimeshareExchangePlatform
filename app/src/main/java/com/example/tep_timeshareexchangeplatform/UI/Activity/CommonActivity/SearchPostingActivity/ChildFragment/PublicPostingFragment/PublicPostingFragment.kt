package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment

import android.content.Intent
import android.os.Bundle
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
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.SearchPostingViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTimeshareBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublicPostingFragment : BaseFragment(R.layout.fragment_timeshare) {

    companion object {
        fun newInstance() = PublicPostingFragment()
        const val PAGE_SIZE = 16
    }

    private lateinit var binding: FragmentTimeshareBinding
    var publicPostingAdapterRV = PublicPostingAdapterRV()
    private val viewModel: SearchPostingViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publicPostingAdapterRV.submitList(listOf())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeshareBinding.inflate(layoutInflater, container, false)
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

        viewModel.currentPostingsPage.observe(viewLifecycleOwner) {
            viewModel.getRentalPostingList(it, PAGE_SIZE, "")
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

                val totalElementOfAPI =
                    viewModel.publicRentalPosingList.value?.data?.totalElements ?: 0
                val currentListSizeOfAdapter = publicPostingAdapterRV.differ.currentList.size


                if (lastCompletelyVisibleItem == totalItemCount - 1 && currentListSizeOfAdapter < totalElementOfAPI) {
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