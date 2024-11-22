package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.NotificationActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityNotificationBinding

class NotificationActivity : BaseActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationAdapter = NotificationAdapter()

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
    }


    private fun initAdapter(){
        notificationAdapter.submitList(Constant.notificationList)
        notificationAdapter.onItemClick = {
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setNotificationAdapter() {
        binding.recyclerViewNotification.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)
            adapter = notificationAdapter
        }

    }
}