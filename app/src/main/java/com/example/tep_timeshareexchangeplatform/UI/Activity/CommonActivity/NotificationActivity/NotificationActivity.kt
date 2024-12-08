package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Booking.MyBookingRentalDetailResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification.NotificationResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingFragment.Companion.PAGE_SIZE
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.BookingFragment.BookingDetailActivity.BookingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangePostingActivity.MyExchangePostingDetail.MyExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.NotificationType.*
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.NotificationHelper
import com.example.tep_timeshareexchangeplatform.Until.NotificationHelper.Companion.NOTIFICATION_PERMISSION_REQUEST_CODE
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationAdapter = NotificationAdapter()
    private lateinit var notificationHelper: NotificationHelper
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        setNotificationAdapter()
        eventClickOptionNotificationSetting()
        notificationHelper = NotificationHelper(this)
        eventClickToolbar()
        if (tokenManager.isLoggedIn()) {
            observeViewModel()
        } else {
            finish()
        }
        checkPermission()
    }

    private fun observeViewModel() {
        viewModel.notificationResponse.observe(this) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.lottieAnimationView.visibility = View.GONE
                    resources.data?.let {
                        viewModel.loadMoreNotifications(it.content)
                        notificationAdapter.submitList(viewModel.getCurrentNotificationList())
                    }
                }

                Status.ERROR -> {
                    binding.lottieAnimationView.visibility = View.GONE
                    showWarningToast("Lỗi Tải Dữ Liệu", "Vui lòng thử lại sau")
                    Log.d("PublicPostingFragmenasdasdat", "observeViewModel: ${resources.message}")
                }

                Status.LOADING -> {
                    binding.lottieAnimationView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentNotificationPage.observe(this) {
            viewModel.callGetNotificationAPI(
                tokenManager.getAccessToken().toString(),
                it,
                PAGE_SIZE
            )
        }


    }


    private fun initAdapter() {
        notificationAdapter.submitList(listOf())
        notificationAdapter.onItemClick = {
            if(it.entityId != null) {
                Log.d("NotificationActivity", "initAdapter: ${it.entityId}")
                navigateToActivity(it)
            }
        }

    }

    private fun setNotificationAdapter() {
        binding.recyclerViewNotification.apply {
            layoutManager =
                LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)
            adapter = notificationAdapter
        }
        binding.recyclerViewNotification.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                val totalElementOfAPI =
                    viewModel.notificationResponse.value?.data?.totalElements ?: 0
                val currentListSizeOfAdapter = notificationAdapter.differ.currentList.size


                if (lastCompletelyVisibleItem == totalItemCount - 1 && currentListSizeOfAdapter < totalElementOfAPI) {
                    viewModel.incrementCurrentNotificationPage()
                }
            }
        })
    }

    private fun eventClickOptionNotificationSetting() {
        binding.ivClose.setOnClickListener {
            binding.optionOpenNotification.visibility = View.GONE
        }

        binding.tvSetting.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Nếu quyền chưa được cấp, yêu cầu quyền
                if (this is Activity) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                    )
                }
                return@setOnClickListener
            }
        }


    }

    private fun eventClickToolbar() {
        binding.customToolbar7.onStartIconClick = {
            onBackPressed()
        }
    }


    private fun checkPermission() {
        // Kiểm tra trạng thái quyền POST_NOTIFICATIONS
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Nếu quyền chưa được cấp, hiển thị UI yêu cầu quyền
            showRequestPermissionUI()
        } else {
            // Nếu quyền đã được cấp, ẩn UI yêu cầu quyền
            hideRequestPermissionUI()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NotificationHelper.NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền thông báo đã được cấp!", Toast.LENGTH_SHORT).show()
                hideRequestPermissionUI()
            } else {
                Toast.makeText(this, "Quyền thông báo bị từ chối!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideRequestPermissionUI() {
        binding.optionOpenNotification.visibility = View.GONE
    }

    private fun showRequestPermissionUI() {
        binding.optionOpenNotification.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun navigateToActivity(item: NotificationResponse.Content) {
        val notificationType = NotificationType.fromKey(item.type)
        val intent : Intent = when (notificationType) {
            // User Booking
            RENTAL_BOOKING -> {
                Log.d("NRENTAL_BOOKING"  , "navigateToActivity: ${item.entityId}")
                Intent(this, BookingDetailActivity::class.java).apply {
                    putExtra(Constant.DEFAULT_MY_BOOKING_RENTAL, item.entityId)
                }

            }
            EXCHANGE_BOOKING -> {
                Log.d("BRUHNEXCHANGE_BOOKING"  , "navigateToActivity: ${item.entityId}")
                Intent(this, BookingDetailActivity::class.java).apply {
                    putExtra(Constant.DEFAULT_MY_BOOKING_EXCHANGE, item.entityId)
                }
            }
            // Posting
            RENTAL_POSTING -> {
                Log.d("BRUHNRENTAL_POSTING"  , "navigateToActivity: ${item.entityId}")
                Intent(this, MyPostingDetailActivity::class.java).apply {
                    putExtra(Constant.DEFAULT_MY_POSTING_ID, item.entityId ?: 0)
                }
            }
            EXCHANGE_POSTING -> {
                Log.d("BRUHNEXCHANGE_POSTING"  , "navigateToActivity: ${item.entityId}")
                Intent(this, MyExchangeDetailActivity::class.java).apply {
                    putExtra(Constant.DEFAULT_MY_POSTING_ID, item.entityId ?: 0)
                }
            }
            EXCHANGE_REQUEST -> {
                Log.d("BRUHNEXCHANGE_REQUEST"  , "navigateToActivity: ${item.entityId}")
                Intent(this, MyExchangeDetailActivity::class.java).apply {
                    putExtra(Constant.DEFAULT_POSTING_ID, item.entityId ?: 0)
                }
            }
            null -> TODO()
        }

        intent?.let {
            startActivity(it)
        }
    }


}