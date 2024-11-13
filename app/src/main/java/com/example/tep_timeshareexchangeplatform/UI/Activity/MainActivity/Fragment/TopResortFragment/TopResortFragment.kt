package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.Adapter.ResortAdapterRV
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.FragmentTopResortBinding


class TopResortFragment : BaseFragment(R.layout.fragment_top_resort) {

    private lateinit var binding: FragmentTopResortBinding
    private val resortAdapter = ResortAdapterRV()
    private val topResortViewModel: TopResortViewModel by activityViewModels()


    companion object {
        const val PAGE_SIZE = 15
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resortAdapter.submitList(emptyList())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopResortBinding.inflate(inflater, container, false)
        setUpTopResortList()
        observeData()
        return binding.root
    }

    private fun observeData() {
        topResortViewModel.resortList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.animationView.visibility = View.GONE
                    topResortViewModel.loadMoreResortList(it.data?.content ?: emptyList())
                    resortAdapter.submitList(topResortViewModel.getCurrentResortList())
                }

                Status.ERROR -> {
                    binding.animationView.visibility = View.GONE
                    MotionToast.createToast(
                        requireActivity(),
                        "Error",
                        it.message ?: "Error",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    binding.animationView.visibility = View.VISIBLE
                }
            }
        }

        // Auto Call First Page Resort List When Fragment is Created
        topResortViewModel.currentResortPage.observe(viewLifecycleOwner) {
            topResortViewModel.getResortList(it, PAGE_SIZE, "")
        }


    }

    private fun setUpTopResortList() {
        binding.rvTopResort.apply {
            adapter = resortAdapter
        }

        binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val view = binding.nestedScrollView.getChildAt(binding.nestedScrollView.childCount - 1)
            val diff = (view.bottom - (binding.nestedScrollView.height + scrollY))

            if (diff == 0) { // Kiểm tra cuộn đến cuối cùng
                val layoutManager = binding.rvTopResort.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = topResortViewModel.resortList.value?.data?.totalPages ?: 0
                if (lastVisibleItem == totalItemCount - 1 &&
                    topResortViewModel.currentResortPage.value!! < totalPages - 1) {
                    topResortViewModel.incrementCurrentResortsPage()
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()

    }

}