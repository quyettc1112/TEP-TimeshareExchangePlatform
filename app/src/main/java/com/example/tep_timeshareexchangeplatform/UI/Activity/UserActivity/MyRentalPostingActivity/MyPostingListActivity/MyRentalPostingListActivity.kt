package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingListActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.Adapter.MyPostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.MyPostingDetailActivity.MyPostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyRentalPostingActivity.PricingSupportActivity.PricingSupportActivity
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyPostingStatus
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.RentalPackageEnum
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRentalPostingListActivity : BaseActivity() {
    private lateinit var binding: ActivityMyPostingBinding
    private val viewModel: MyPostingViewModel by viewModels()
    private lateinit var tokenManager: TokenManager
    private lateinit var myPostingAdapter: MyPostingAdapter
    private lateinit var acceptPriceLauncher: ActivityResultLauncher<Intent>
    private var itemPosition = 0

    companion object {
        const val POSTING_PAGE_SIZE = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPostingBinding.inflate(layoutInflater)
        myPostingAdapter = MyPostingAdapter(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkUserStage()
        tokenManager = TokenManager(this)
        initActivityLauncher()
        innitAdapter()
        bindDataMyPostingList()

        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }

    }

    private fun checkUserStage() {
        val token = TokenManager(this)
        if (!token.isLoggedIn() || token.getAccessToken() == null) {
            showErrorToast(getString(R.string.msg_need_login))
            finish()
        }

        when (token.getUserLogState()) {
            UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER -> {
                observeMyPostingList()
            }

            UserLogState.LOGGED_IN_AS_CUSTOMER -> {
                observeMyPostingList()
            }

            UserLogState.LOGGED_IN_AS_USER -> {
                showInfoDialog()
            }

            UserLogState.LOGGED_OUT -> {
                finish()
            }
        }
    }

    private fun observeMyPostingList() {
        viewModel.myPostingList.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    if (it.data?.totalElements == 0) {
                        showInfoDialog()
                        return@observe
                    }
                    viewModel.loadMorePostingList(it.data?.content ?: listOf())
                    myPostingAdapter.submitList(viewModel.getCurrentPostingList())
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                    showErrorToast(getString(R.string.msg_cannot_load_data))
                    Log.e("MyPostingActivity", it.message ?: "Có lỗi xảy ra")
                }
            }
        }

        viewModel.currentPostingPage.observe(this) {
            viewModel.getMyPostingList(
                TokenManager(this).getAccessToken().toString(),
                it,
                POSTING_PAGE_SIZE
            )
        }

        viewModel.deactivateRentalPosting.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    showSuccessToast(getString(R.string.msg_hide_post_successful))
                    MotionToast.Companion.createColorToast(
                        this,
                        "Thành công",
                        "Ẩn bài đăng thành công",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_bold)
                    )
                    myPostingAdapter.updateItemStatus(itemPosition, MyPostingStatus.CLOSED.name)
                    val id = myPostingAdapter.getItemIdFromPosition(itemPosition) ?: 0
                    viewModel.updatePostingItem(id, MyPostingStatus.CLOSED.name)
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    MotionToast.Companion.createColorToast(
                        this,
                        "Lỗi",
                        it.message ?: "Có lỗi xảy ra",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, R.font.inter_bold)
                    )
                }
            }
        }

    }

    private fun innitAdapter() {
        myPostingAdapter.submitList(listOf())
        myPostingAdapter.onItemClick = {
            val intent = Intent(this, MyPostingDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ID, it.rentalPostingId)
            startActivity(intent)
        }

        myPostingAdapter.onItemPricingClick = {
            val intent = Intent(this, PricingSupportActivity::class.java)
            // Id
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ID, it.rentalPostingId)

            // Package Name
            intent.putExtra(Constant.DEFAULT_PACKAGE_SELECTION, it.rentalPackageName)

            // Night
            intent.putExtra(Constant.DEFAULT_MY_POSTING_NIGHT, it.nights)

            // Name
            intent.putExtra(Constant.DEFAULT_MY_POSTING_RESORT_NAME, it.resortName)

            // Room Name
            intent.putExtra(Constant.DEFAULT_MY_POSTING_ROOM_NAME, it.roomName)

            // Check In Date
            intent.putExtra(
                Constant.DEFAULT_MY_POSTING_CHECK_IN_DATE,
                Constant.formatDateByLocale(it.checkinDate, this)
            )

            // Check Out Date
            intent.putExtra(
                Constant.DEFAULT_MY_POSTING_CHECK_OUT_DATE,
                Constant.formatDateByLocale(it.checkoutDate, this)
            )

            val rentalPackageEnum = RentalPackageEnum.getPackageByName(it.rentalPackageName)
            when (rentalPackageEnum) {
                RentalPackageEnum.PREMIUM_SERVICE.packageModel -> {
                    intent.putExtra(Constant.staffRefinementPrice, it.staffRefinementPrice)
                }

                RentalPackageEnum.DELEGATED_SERVICE.packageModel -> {
                    intent.putExtra(Constant.priceValuation, it.priceValuation)
                }
            }
            acceptPriceLauncher.launch(intent)
        }

        myPostingAdapter.onHidePostingClick = {
            showConfirmDialog(
                "Ẩn bài đăng",
                getString(R.string.msg_confirm_hide_post),
                "Đồng ý",
                "Hủy",
                "",
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {
                        // Do nothing
                    }

                    override fun positiveAction() {
                        viewModel.deActiveRentalPosting(
                            tokenManager.getAccessToken().toString(),
                            it.rentalPostingId
                        )
                    }
                }
            )
        }

        myPostingAdapter.onHidePostingPositionClick = {
            itemPosition = it
        }
    }

    private fun bindDataMyPostingList() {
        binding.rvMyPosting.apply {
            adapter = myPostingAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@MyRentalPostingListActivity, LinearLayoutManager.VERTICAL, false)
        }

        // Scroll Listener
        binding.rvMyPosting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = viewModel.myPostingList.value?.data?.totalPages ?: 0
                if (lastCompletelyVisibleItem == (totalItemCount - 1) && viewModel.currentPostingPage.value!! < totalPages - 1) {
                    viewModel.incrementCurrentPostingsPage()
                }
            }
        })
    }

    private fun initActivityLauncher() {
        acceptPriceLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.clearCurrentPostingList()
                    myPostingAdapter.submitList(listOf())
                    viewModel.currentPostingPage.value = 0
                }
            }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showErrorToast(message: String) {
        // Show Error Toast
        MotionToast.createColorToast(
            this,
            getString(R.string.msg_load_data_error),
            message,
            MotionToastStyle.WARNING,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null
        )
    }

    private fun showSuccessToast(message: String) {
        // Show Success Toast
        MotionToast.createColorToast(
            this,
            "Thành Công",
            message,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            null
        )
    }

    private fun showInfoDialog() {
        showInfoDialog(
            this,
            getString(R.string.msg_no_posts),
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    finish()
                }
            }
        )
    }




}