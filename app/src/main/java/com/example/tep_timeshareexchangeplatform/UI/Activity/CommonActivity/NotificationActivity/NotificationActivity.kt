package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.NotificationHelper
import com.example.tep_timeshareexchangeplatform.Until.NotificationHelper.Companion.NOTIFICATION_PERMISSION_REQUEST_CODE
import com.example.tep_timeshareexchangeplatform.databinding.ActivityNotificationBinding

class NotificationActivity : BaseActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationAdapter = NotificationAdapter()
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        setNotificationAdapter()
        eventClickOptionNotificationSetting()
        notificationHelper = NotificationHelper(this)
        checkPermission()
        binding.btnTestNotification.setOnClickListener {
            notificationHelper.makeNotification(
                this,
                "Thông Báo Quan Trọng",
                "Đây là thông báo quan trọng"
            )
        }
    }


    private fun initAdapter() {
        notificationAdapter.submitList(Constant.notificationList)
        notificationAdapter.onItemClick = {
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setNotificationAdapter() {
        binding.recyclerViewNotification.apply {
            layoutManager =
                LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)
            adapter = notificationAdapter
        }

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


}