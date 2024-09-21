package com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.ChildFragment.ResortFragment

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ResortAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.OnBottomNavVisibilityListener
import com.example.tep_timeshareexchangeplatform.databinding.FragmentHomeBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentResortBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopResortBinding

class ResortFragment : BaseFragment(R.layout.fragment_resort) {

    private lateinit var binding: FragmentResortBinding
    private var resortAdapter = ResortAdapterRV()

    private var bottomNavVisibilityListener: OnBottomNavVisibilityListener? = null

    companion object {
        fun newInstance() = ResortFragment()
    }
    private val viewModel: ResortViewModel by viewModels()


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
        resortAdapter.submitList(Constant.resortListMT)

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
        setResortList()

        return binding.root
    }

    private fun setResortList() {
        binding.rcTopResort.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = resortAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            })
        }
    }
}