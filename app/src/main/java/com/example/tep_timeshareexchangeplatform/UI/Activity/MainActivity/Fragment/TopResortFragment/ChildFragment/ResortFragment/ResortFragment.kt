package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant.Companion.DEFAULT_RESORT_ID
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.OnBottomNavVisibilityListener
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.ResortDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentResortBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResortFragment : BaseFragment(R.layout.fragment_resort) {

    private lateinit var binding: FragmentResortBinding
    private var resortAdapter = ResortAdapterRV()

    private var bottomNavVisibilityListener: OnBottomNavVisibilityListener? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    companion object {
        const val PAGE_SIZE = 15
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBottomNavVisibilityListener) {
            bottomNavVisibilityListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnBottomNavVisibilityListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        scrollToTop()
    }

    private fun scrollToTop() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Scroll to the top of the RecyclerView
                    binding.rcTopResort.smoothScrollToPosition(0)
                    // Show Bottom Navigation
                    (activity as? OnBottomNavVisibilityListener)?.showBottomNav()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResortBinding.inflate(inflater, container, false)
        observeResortList()
        setResortListAdapter()
        setResortClickListener()

        return binding.root
    }

    private fun initAdapter() {
        resortAdapter.submitList(listOf())
    }

    private fun setResortListAdapter() {
        binding.rcTopResort.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = resortAdapter
        }
        binding.rcTopResort.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                val totalElementOfAPI =
                    mainViewModel.resort_TopResort.value?.data?.totalElements ?: 0
                val currentListSizeOfAdapter = resortAdapter.differ.currentList.size


                if (lastCompletelyVisibleItem == totalItemCount - 3 && currentListSizeOfAdapter < totalElementOfAPI) {
                    mainViewModel.incrementCurrentResortPage()
                    Toast.makeText(context, "Load More", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeResortList() {

        // Observe the Resort Response from the API
        mainViewModel.resort_TopResort.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    response.data?.let {
                        mainViewModel.loadMoreResorts(it.content ?: emptyList())
                        resortAdapter.submitList(mainViewModel.getCurrentResortList())
                        Toast.makeText(
                            context,
                            "Current List Size: ${mainViewModel.getCurrentResortList()?.size}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }
            }
        }

        mainViewModel.currentResortPage.observe(viewLifecycleOwner) {
            if (mainViewModel._isNewResortlist.value == true && it == 0) {
                resortAdapter.clearData()
                resortAdapter.submitList(listOf())
                mainViewModel.updateIsResortNewList(false)
                binding.rcTopResort.smoothScrollToPosition(0)
                mainViewModel.getResortONTopResort(0, PAGE_SIZE, "")

            } else {
                mainViewModel.getResortONTopResort(it, PAGE_SIZE, "")
            }
        }




    }


    private fun setResortClickListener() {
        resortAdapter.onItemClick = {
            val intent = Intent(requireContext(), ResortDetailActivity::class.java)
            intent.putExtra(DEFAULT_RESORT_ID, it.id)
            startActivity(intent)
        }
    }
}