package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.MyTimeshareDetailAcitivity.MyTimeshareDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.PostingFlow.Adapter.MyTimeshareAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTimeshareActivity.ViewModel.MyTimeshareViewModel
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMyTimeshareBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTimeshareActivity : BaseActivity() {
    private lateinit var binding: ActivityMyTimeshareBinding
    private lateinit var myTimeshareAdapter : MyTimeshareAdapter
    private lateinit var tokenManager: TokenManager
    private val myTimeshareViewModel: MyTimeshareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTimeshareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)
        getIntentValue()
        if (!tokenManager.isLoggedIn()) {
            showErrorToast( "Lỗi","Bạn chưa đăng nhập" )
            finish()
        } else {
            myTimeshareViewModel.currentPage.value = 0
        }

        initAdapter()
        observeData()
        setEventItemClick()

        binding.toolbar.onStartIconClick = {
            finish()
        }
    }

    private fun getIntentValue() {
        val intent = intent
        if (intent.hasExtra(Constant.REQUEST_GET_MY_TIMESHARE)) {
            myTimeshareAdapter = MyTimeshareAdapter(true)
        } else {
            myTimeshareAdapter = MyTimeshareAdapter(false)
        }
    }

    private fun initAdapter() {
        myTimeshareAdapter.submitList(listOf())
        binding.recyclerView.let {
            it.adapter = myTimeshareAdapter
            it.layoutManager = LinearLayoutManager(this)
        }
        // Scroll Listener
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val totalPages = myTimeshareViewModel.myTimeshareList.value?.data?.totalPages ?: 0

                if (lastCompletelyVisibleItem == (totalItemCount - 1) && myTimeshareViewModel.currentPage.value!! < totalPages - 1) {
                    Log.d("CurrentysdpasfdCRRR", myTimeshareViewModel.currentPage.value.toString())
                    Log.d("Currentysdpasfd", totalPages.toString())
                    myTimeshareViewModel.incrementCurrentPage()
                }
            }
        })
    }

    private fun observeData() {
        myTimeshareViewModel.myTimeshareList.observe(this) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    binding.animationView.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    binding.animationView.visibility = View.GONE
                    resources.data?.let {
                        if (it.totalPages == 0) {
                            showInfoDialog(
                                this,
                                "Bạn chưa có Timeshare nào",
                                object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        finish()
                                    }
                                }
                            )
                        } else {
                            myTimeshareViewModel.loadMoreMyTimeshareList(it.content)
                            myTimeshareAdapter.submitList(myTimeshareViewModel.getCurrentMyTimeshareList())
                        }

                    }
                }

                Status.ERROR -> {
                    binding.animationView.visibility = View.GONE
                    if (resources.message!!.contains("404")) {
                        showInfoDialog(
                            this,
                            "Bạn chưa có Timeshare nào",
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    finish()
                                }
                            }
                        )
                    } else
                        showErrorToast("Lỗi", resources.message)
                    Log.d("ErrorMyTimeshare", resources.message.toString())
                }
            }
        }

        myTimeshareViewModel.currentPage.observe(this) {
            myTimeshareViewModel.getMyTimeshareList(tokenManager.getAccessToken().toString(), it, 15)
        }


    }

    private fun setEventItemClick() {
        // Item click
        myTimeshareAdapter.setItemOnclickListener {
            val intent = Intent(this, MyTimeshareDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, it.timeShareId)
            startActivity(intent)

        }
        myTimeshareAdapter.onSelectExchangeItemClick = {
            returnSelectedTimeshare(it.timeShareId)
        }

    }

    private fun returnSelectedTimeshare(timeshareID: Int) {
        val resultIntent = Intent().apply {
            putExtra(Constant.DEFAULT_SELECTION_MY_TIMESHARE, timeshareID)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish() // Đóng Activity và quay lại Activity gọi
    }

    override fun onBackPressed() {
        super.onBackPressed()
        myTimeshareViewModel.clearCurrentMyTimeshareList()
        finish()
    }

    override fun onResume() {
        super.onResume()
    }
}