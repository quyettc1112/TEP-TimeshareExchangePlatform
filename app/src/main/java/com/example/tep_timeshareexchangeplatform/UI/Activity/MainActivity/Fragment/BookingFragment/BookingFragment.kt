package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomFeedbackDialog.CustomFeedbackDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BookingDetailActivity.BookingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity.NotificationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
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
        eventClickNotification()
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
                    binding.tvDescription.text =
                        "Bạn cần cung cấp thông tin cá nhân để khám phá thêm"
                }

            }

        }

        // Posting feedback
        viewModel.feedbackResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    (activity as MainActivity).showDoneFeedbackDialog(requireContext(),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {

                            }
                        })
                }

                Status.ERROR -> {
                    (activity as MainActivity).hideLoadingWaiting()
                    it.message?.let {
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
                    (activity as MainActivity).showLoadingWaiting(true)
                }
            }
        }
    }

    private fun initAdapter() {
        myOrderAdapter.submitList(listOf())
        myOrderAdapter.onItemClick = {
            val intent = Intent(requireContext(), BookingDetailActivity::class.java)
            if(it.source == "rental") {
                intent.putExtra(Constant.DEFAULT_MY_BOOKING_RENTAL, it.bookingId)
            } else {
                intent.putExtra(Constant.DEFAULT_MY_BOOKING_EXCHANGE, it.bookingId)
            }
            startActivity(intent)
        }

        myOrderAdapter.onFeedbackClick = {
            val feedbackDialog = CustomFeedbackDialog(requireContext()) { rating, feedback ->
                callSendFeedBack(rating, feedback, it.bookingId) // Gọi hàm xử lý feedback
            }

            feedbackDialog.show()
        }
    }

    private fun eventClickNotification() {
        binding.imNotification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }
    }

    private fun callSendFeedBack(rating: Int, feedback: String, bookingId: Int) {
        if (!tokenManager.isLoggedIn()) {
            MotionToast.Companion.createColorToast(
                requireActivity(),
                "Error",
                "Bạn cần đăng nhập để thực hiện chức năng này",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
            return
        }

        val feedbackDTO = FeedbackDTO(rating, feedback, bookingId)
        if (feedbackDTO.bookingId !== 0 && feedbackDTO.ratingPoint !== 0) {
            viewModel.postFeedback(tokenManager.getAccessToken().toString(), feedbackDTO)
        } else {
            MotionToast.Companion.createColorToast(
                requireActivity(),
                "Error",
                "Vui lòng nhập đầy đủ thông tin",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null
            )
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