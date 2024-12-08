package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomFeedbackDialog.CustomFeedbackDialog
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.FeedbackDTO
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment.BookingDetailActivity.BookingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity.NotificationActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyOrderActivity.Adapter.MyBookingAdapter
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
    private var myBookingAdapter = MyBookingAdapter()
    private lateinit var tokenManager: TokenManager
    private lateinit var bookingDetailLauncher: ActivityResultLauncher<Intent>
    private var isNewLoad: Int = 0

    companion object {
        const val PAGE_SIZE = 15
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBookingDetailLauncher()
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
            myBookingAdapter.submitList(listOf())
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
                        myBookingAdapter.submitList(viewModel.getCurrentMyBookingList())
                    }
                    binding.animationViewLoadingMore.visibility = View.GONE
                }

                Status.ERROR -> {
                    resources.message?.let {
                        (activity as MainActivity).showErrorToast("Lỗi", "Không thể tải dữ liệu")

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
                    (activity as MainActivity).apply {
                        hideLoadingWaiting()
                        showErrorToast("Lỗi", "Lỗi Khi Gửi Phản Hồi")
                    }
                }
                Status.LOADING -> {
                    (activity as MainActivity).showLoadingWaiting(true)
                }
            }
        }
    }

    private fun initAdapter() {
        myBookingAdapter.submitList(listOf())
        myBookingAdapter.onItemClick = {
            val intent = Intent(requireContext(), BookingDetailActivity::class.java)
            if (it.source == "rental") {
                intent.putExtra(Constant.DEFAULT_MY_BOOKING_RENTAL, it.bookingId)
            } else {
                intent.putExtra(Constant.DEFAULT_MY_BOOKING_EXCHANGE, it.bookingId)
            }
            bookingDetailLauncher.launch(intent)
        }

        myBookingAdapter.onFeedbackClick = {
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
            (activity as MainActivity).showErrorToast(
                "Lỗi",
                "Bạn cần đăng nhập để thực hiện chức năng này"
            )
            return
        }

        val feedbackDTO = FeedbackDTO(rating, feedback, bookingId)
        if (feedbackDTO.bookingId !== 0 && feedbackDTO.ratingPoint !== 0) {
            viewModel.postFeedback(tokenManager.getAccessToken().toString(), feedbackDTO)
        } else {
            (activity as MainActivity).showErrorToast("Lỗi", "Vui lòng nhập đầy đủ thông tin")
        }

    }

    private fun setOrderList() {
        binding.rvOrderList.adapter = myBookingAdapter
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
        } else {
            viewModel.clearCurrentMyBookingList()
            myBookingAdapter.apply {
                submitList(listOf())
                notifyDataSetChanged()
            }
            viewModel.currentMyBookingPage.value = 0
        }

    }

    private fun initializeBookingDetailLauncher() {
        bookingDetailLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bookingStatus = result.data?.getStringExtra(Constant.DEFAULT_BOOKING_STATUS)
                val bookingId = result.data?.getIntExtra(Constant.DEFAULT_BOOKING_ID, 0)
                if (bookingId != null && bookingStatus != null) {
                    try {
                        myBookingAdapter.updateItemStatus(bookingId, bookingStatus)
                        viewModel.updateBookingItemById(bookingId, bookingStatus)
                    } catch (e: UnsupportedOperationException) {
                        e.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            "Không thể cập nhật danh sách",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


}