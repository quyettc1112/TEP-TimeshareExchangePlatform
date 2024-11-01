package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.ResortFragment

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
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
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.OnBottomNavVisibilityListener
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.ResortDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentResortBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResortFragment : BaseFragment(R.layout.fragment_resort) {

    private lateinit var binding: FragmentResortBinding
    private var resortAdapter = ResortAdapterRV()

    private var bottomNavVisibilityListener: OnBottomNavVisibilityListener? = null
    private val mainViewModel: MainViewModel by activityViewModels()


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
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
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

    private fun observeResortList() {

        // Observe the Resort Response from the API
        mainViewModel.resortResponseOnTopResort.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    response.data?.let { resortAdapter.submitList(it.content) }
                }
                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }
            }
        }

        // Observe the Resort Response from the API





    }

    private fun setResortListAdapter() {
        binding.rcTopResort.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = resortAdapter
            /*addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && bottomNavVisibilityListener != null) {
                        // The user is actively dragging
                        if (recyclerView.canScrollVertically(-1)) {
                            // Can scroll up further, so user is scrolling down
                            bottomNavVisibilityListener!!.hideBottomNav()
                        } else if (recyclerView.canScrollVertically(1)) {
                            // Can scroll down further, so user is scrolling up
                            bottomNavVisibilityListener!!.showBottomNav()
                        }
                    }
                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && bottomNavVisibilityListener != null) {
                        // User scrolls down
                        bottomNavVisibilityListener!!.hideBottomNav()
                    } else if (dy < 0 && bottomNavVisibilityListener != null) {
                        // User scrolls up
                        bottomNavVisibilityListener!!.showBottomNav()
                    }
                }
            })*/
        }
    }

    private fun setResortClickListener() {
        resortAdapter.onItemClick = {
            startActivity(Intent(requireContext(), ResortDetailActivity::class.java))
        }
    }
}