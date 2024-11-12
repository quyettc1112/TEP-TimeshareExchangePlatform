package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare.MyTimeshareResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity.MyTimeshareDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.MyTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.PostingFlowActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.ViewModel.PostingFlowViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentSelectTimeshareBinding

class Step_3_SelectTimeshareFragment : BaseFragment(R.layout.fragment_select_timeshare) {

    private lateinit var binding: FragmentSelectTimeshareBinding
    private var myTimeshareAdapter = MyTimeshareAdapter()
    private val postingFlowViewModel: PostingFlowViewModel by activityViewModels()
    private lateinit var selectMyTimeshareResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var tokenManager: TokenManager

    companion object {
        const val PAGE_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        tokenManager = TokenManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTimeshareBinding.inflate(layoutInflater, container, false)
        // Set adapter for recyclerview
        setMyTimeshareList()
        observeData()
        initActivityResultLauncher()
        setEventItemClick()
        return binding.root
    }

    private fun setMyTimeshareList() {
        binding.recyclerView.let {
            it.adapter = myTimeshareAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = postingFlowViewModel.myTimeshareList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 2) && postingFlowViewModel.currentMyTimesharePage.value!! < totalPages - 1) {
                    postingFlowViewModel.incrementCurrentMyTimesharePage()
                    Toast.makeText(requireContext(), "Load More", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeData() {
        // Tracking data my timeshare list
        postingFlowViewModel.myTimeshareList.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    binding.lottieLoading.visibility = View.VISIBLE
                    myTimeshareAdapter.submitList(listOf())
                    postingFlowViewModel.clearCurrentMyTimeshareList()
                }

                Status.SUCCESS -> {
                    binding.lottieLoading.visibility = View.GONE
                    resources.data?.let {
                        postingFlowViewModel.loadMoreTimeshareList(it.content)
                        myTimeshareAdapter.submitList(postingFlowViewModel.getCurrentMyTimeshareList())
                    }
                }

                Status.ERROR -> {
                    binding.lottieLoading.visibility = View.GONE
                    MotionToast.Companion.createToast(
                        requireActivity(),
                        "Error",
                        "Error ${resources.message}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
        }

        postingFlowViewModel.currentMyTimesharePage.observe(viewLifecycleOwner) {
            postingFlowViewModel.getMyTimeshareList(
                tokenManager.getAccessToken().toString(),
                it,
                PAGE_SIZE
            )
        }

    }

    private fun initAdapter() {
        myTimeshareAdapter.submitList(listOf())

    }

    private fun setEventItemClick() {
        // Item click
        myTimeshareAdapter.setItemOnclickListener {
            val intent = Intent(requireContext(), MyTimeshareDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, it.timeShareId)
            selectMyTimeshareResultLauncher.launch(intent)
        }

        // Select button click
        myTimeshareAdapter.onItemClick = {
            (activity as PostingFlowActivity).showConfirmDialog(
                title = "Confirm",
                message = "Are you sure you want to select this Timeshare?",
                positiveButtonTitle = "Yes",
                negativeButtonTitle = "No",
                textButton = null,
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {

                    }

                    override fun positiveAction() {
                        postingFlowViewModel.updateMyTimeshareModel(it)
                        postingFlowViewModel.updateStep(4)
                    }
                }
            )
        }
    }

    private fun initActivityResultLauncher() {
        selectMyTimeshareResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedMyTimeshare: MyTimeshareResponse.Content? =
                        data?.getParcelableExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE)
                    if (selectedMyTimeshare != null) {
                        postingFlowViewModel.updateMyTimeshareModel(selectedMyTimeshare)
                        Toast.makeText(requireContext(), "Selected", Toast.LENGTH_SHORT).show()
                        postingFlowViewModel.updateStep(4)
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        myTimeshareAdapter.submitList(listOf())
        postingFlowViewModel.clearCurrentMyTimeshareList()
        postingFlowViewModel.currentMyTimesharePage.value = 0

    }


}