package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.PublicPostingFragment

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
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.TimeshareListActivity.TimeshareListActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment.ResortFragment
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTimeshareBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublicPostingFragment : BaseFragment(R.layout.fragment_timeshare) {

    companion object {
        fun newInstance() = PublicPostingFragment()
        const val PAGE_SIZE = 8
    }

    private lateinit var binding: FragmentTimeshareBinding
    var publicPostingAdapterRV = PublicPostingAdapterRV()
    private val mainViewModel: MainViewModel by activityViewModels()

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
                    mainViewModel.posting_TopResort.value?.data?.totalElements ?: 0
                val currentListSizeOfAdapter = publicPostingAdapterRV.differ.currentList.size


                if (lastCompletelyVisibleItem == totalItemCount - 1 && currentListSizeOfAdapter < totalElementOfAPI) {
                    mainViewModel.incrementCurrentPostingsPage()
                }
            }
        })

        publicPostingAdapterRV.onItemClick = {
            val intent = Intent(requireActivity(), PostingDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_POSTING_ID, it.rentalPostingId)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        mainViewModel.posting_TopResort.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    if (!it.data?.content.isNullOrEmpty()) {
                        mainViewModel.loadMorePostings(it.data?.content ?: emptyList())
                        publicPostingAdapterRV.submitList(mainViewModel.getCurrentPostingList())
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
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
                    binding.animLoadingMore.visibility = View.VISIBLE
                }
            }
        }
        mainViewModel.currentPostingsPage.observe(viewLifecycleOwner) {
            if (mainViewModel._isNewPostinglist.value == true && it == 0) {
                publicPostingAdapterRV.clearData()
                publicPostingAdapterRV.submitList(listOf())
                mainViewModel.updateIsPostingNewList(false)
                binding.rcPosting.smoothScrollToPosition(0)
                mainViewModel.getPostingOnTopResort(0, PAGE_SIZE, "")

            } else {
                mainViewModel.getPostingOnTopResort(it, PAGE_SIZE, "")
            }
        }


    }

    override fun onResume() {
        super.onResume()


    }

}