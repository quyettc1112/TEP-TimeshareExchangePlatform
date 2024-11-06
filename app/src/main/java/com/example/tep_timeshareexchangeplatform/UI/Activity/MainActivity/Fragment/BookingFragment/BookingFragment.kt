package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetail.BookingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.Adapter.MyOrderAdapter
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentBookingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingFragment : BaseFragment(R.layout.fragment_booking) {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentBookingBinding
    private var myOrderAdapter = MyOrderAdapter()
    private lateinit var tokenManager: TokenManager

    companion object {
        const val PAGE_SIZE = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        checkLogin()


        return binding.root
    }

    private fun checkLogin() {
        if (!tokenManager.isLoggedIn()) {
            binding.llListContianer.visibility = View.GONE
            binding.llLoadingContainer.visibility = View.VISIBLE
        } else {

            viewModel.resetCurrentMyBookingPage()
            myOrderAdapter.submitList(listOf())

            observeData()
            setOrderList()
        }

    }

    private fun observeData() {
        viewModel.myBooking.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    if (resources.data?.content.isNullOrEmpty()) {
                        binding.llListContianer.visibility = View.GONE
                        binding.llLoadingContainer.visibility = View.VISIBLE
                        binding.tvDescription.text = "Bạn chưa có đơn đặt phòng nào"
                    } else {
                        binding.llListContianer.visibility = View.VISIBLE
                        binding.llLoadingContainer.visibility = View.GONE
                        resources.data?.content?.let { viewModel.loadMoreBookingList(it) }
                        myOrderAdapter.submitList(viewModel.getCurrentMyBookingList())
                    }
                    binding.animationViewLoadingMore.visibility = View.GONE
                }

                Status.ERROR -> {
                    resources.message?.let {
                        MotionToast.Companion.createColorToast(
                            requireActivity(),
                            "Error",
                            it,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )
                    }
                }

                Status.LOADING -> {
                    binding.animationViewLoadingMore.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentMyBookingPage.observe(viewLifecycleOwner) {
            val userStage = tokenManager.getUserLogState()
            when (userStage) {
                UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                    viewModel.getMyBooking(tokenManager.getAccessToken().toString(), it, PAGE_SIZE)
                }
                UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                    viewModel.getMyBooking(tokenManager.getAccessToken().toString(), it, PAGE_SIZE)
                }
                else -> {
                    binding.llListContianer.visibility = View.GONE
                    binding.llLoadingContainer.visibility = View.VISIBLE
                    binding.tvDescription.text = "Bạn cần cung cấp thông tin cá nhân để khám phá thêm"
                }

            }

        }
    }

    private fun initAdapter() {
        myOrderAdapter.submitList(listOf())
        myOrderAdapter.onItemClick = {
            val intent = Intent(requireContext(), BookingDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_BOOKING_SELECTED_ID, it.bookingId)
            startActivity(intent)
        }

    }

    private fun setOrderList() {
        binding.rvOrderList.adapter = myOrderAdapter
        // Scroll Listener
        binding.rvOrderList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.myBooking.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentMyBookingPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentMyBookingPage()
                    Toast.makeText(requireContext(), "Load More", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    override fun onResume() {
        super.onResume()
        if (!tokenManager.isLoggedIn()) {
            binding.llListContianer.visibility = View.GONE
            binding.llLoadingContainer.visibility = View.VISIBLE
            binding.tvDescription.text = "Bạn cần đăng nhập để xem thông tin đặt phòng"
        }

    }
}