package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.PublicPostingFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.TimeshareListActivity.TimeshareListActivity
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
    private var publicPostingAdapterRV = PublicPostingAdapterRV()
    private val viewModel: MainViewModel by activityViewModels()


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

        publicPostingAdapterRV.onItemClick = {
            val intent = Intent(requireActivity(), TimeshareListActivity::class.java)
            startActivity(intent)
        }

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

                if (lastCompletelyVisibleItem == totalItemCount - 1) {
                    /*viewModel.incrementCurrentPostingsPage()
                    Toast.makeText(
                        requireActivity(),
                        "Loading more, Page ${viewModel.currentPostingsPage.value}, ${publicPostingAdapterRV.differ.currentList.size}",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            }
        })
    }

    private fun observeViewModel() {
       /* viewModel.homePublicPosting.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    if (!it.data?.content.isNullOrEmpty()) {
                       *//* viewModel.loadMorePostings(it.data?.content ?: emptyList())
                        publicPostingAdapterRV.submitList(viewModel.getCurrentPostingList())*//*
                    }
                }

                Status.ERROR -> {
                    (activity as MainActivity).hideLoadingWaiting()
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
                    (activity as MainActivity).showLoadingWaiting(false)
                }
            }
        }
*/
       /* viewModel.currentPostingsPage.observe(viewLifecycleOwner) {
            viewModel.getPublicPostingsHome(it, PAGE_SIZE, "")
            Toast.makeText(requireActivity(), "Call Loading, Page $it", Toast.LENGTH_SHORT).show()
        }*/


    }

    override fun onResume() {
        super.onResume()


    }

}