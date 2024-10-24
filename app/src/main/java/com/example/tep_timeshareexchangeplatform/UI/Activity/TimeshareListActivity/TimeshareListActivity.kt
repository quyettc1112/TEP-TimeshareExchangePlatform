package com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareListActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.TimeshareDetailActivity.TimeshareDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.Fragment.TopResortFragment.ChildFragment.TimeshareFragment.TimeshareAdapterRV
import com.example.tep_timeshareexchangeplatform.databinding.ActivityTimeshareListBinding

class TimeshareListActivity : BaseActivity() {
    private lateinit var binding: ActivityTimeshareListBinding
    private var timeshareAdapter = TimeshareAdapterRV()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimeshareListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setCustomToolbarAction()
        initAdapter()
        setTimeShareListResort()
    }

    private fun initAdapter() {
       /* timeshareAdapter.submitList(Constant.timeshareList)*/
    }

    private fun setCustomToolbarAction() {
        binding.customToolBar.onStartIconClick = {
            finish()
        }
    }

    private fun enableEdgeToEdge() {
        window.setDecorFitsSystemWindows(false)
        window.navigationBarColor = getColor(R.color.white)
    }

    private fun setTimeShareListResort() {
        binding.rcTimeshare.apply {
            adapter = timeshareAdapter
            layoutManager = GridLayoutManager(this@TimeshareListActivity, 2)
        }

        timeshareAdapter.onItemClick = {
            startActivity(Intent(this, TimeshareDetailActivity::class.java))
        }
    }

}