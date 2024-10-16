package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelOfficial.Resort.ResortModel
import com.example.tep_timeshareexchangeplatform.Common.Adapter.LocationAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.ResortSearchedAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.LocationActivity.ViewModel.LocationViewModel
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetailActivity
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToast
import com.example.tep_timeshareexchangeplatform.Until.MotionToast.MotionToastStyle
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityLocationBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationActivity : BaseActivity() {

    private lateinit var binding : ActivityLocationBinding
    private val locationList = listOf(
        "Hồ Chí Minh", "Vũng Tàu", "Đà Lạt", "Phan Thiết", "Quy Nhơn", "Phú Quốc",
        "Đà Nẵng", "Nha Trang", "Hà Nội", "Sa Pa", "Hạ Long", "Hội An",
        "Hải Phòng", "Singapore", "Bangkok", "Phuket", "Kuala Lumpur",
        "Seoul", "Tokyo", "Bali"
    )
    private val locationAdapter  = LocationAdapter()
    private val resortSearchedAdapter  = ResortSearchedAdapter()

    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.rvResortSearched.visibility = View.GONE
        observeData()
        initAdapter()
        initRecyclerView()
        onItemClickEvent()
        handleClickEventButton()
        searchLocation()
        if (checkIntentFromPostingFlow()) {
            hideUI()
            binding.rvResortSearched.visibility = View.GONE

        }

    }

    private fun observeData() {
        locationViewModel.resortList.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    resortSearchedAdapter.submitList(it.data?.content)
                }

                Status.ERROR -> {
                    MotionToast.createToast(
                        this,
                        "Error",
                        "Error loading data",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }

                Status.LOADING -> {
                    // Do nothing
                }
            }
        }

        locationViewModel.resortName.observe(this) {
            locationViewModel.getResortList(0, 15, it)
        }
    }

    private fun initAdapter() {
        locationAdapter.submitList(locationList)
        resortSearchedAdapter.submitList(listOf())
    }

    private fun initRecyclerView() {
        // Location List
        binding.rvLocation.apply {
            val flexboxLayoutManager = FlexboxLayoutManager(this@LocationActivity)
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager = flexboxLayoutManager
            adapter = locationAdapter
        }
        binding.rvResortSearched.apply {
            layoutManager = LinearLayoutManager(this@LocationActivity)
            adapter = resortSearchedAdapter
        }
      
        
    }

    private fun onItemClickEvent() {
        // Location Click
        locationAdapter.onitemCLickListener = {
            intentExtraValueToHome(it + ", Việt Nam")
        }

        // Resort Searched Click
        resortSearchedAdapter.setItemOnclickListener { it ->
            if (checkIntentFromPostingFlow()) {
                intentExtraValueToPostingFlow(it)
                finish()
            } else intentValueToResortDetail(it.id)
        }
    }

    // Back to previous screen or Explore
    private fun handleClickEventButton() {
       binding.let {
           it.llExplreWorld.setOnClickListener { finish() }
           it.imCloseIcon.setOnClickListener{ finish() }
       }
    }

    // Search Location
    private fun searchLocation() {
        // Add a TextWatcher to EditText to filter results as the user types
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (checkIntentFromPostingFlow()) {
                    binding.llListLocation.visibility = View.GONE
                    binding.rvResortSearched.visibility = View.GONE
                    binding.llExplreWorld.visibility = View.GONE
                    binding.llNearMe.visibility = View.GONE
                    binding.llLine1.visibility = View.GONE
                    if (s?.trim()?.length!! > 0)
                        binding.rvResortSearched.visibility = View.VISIBLE
                    else binding.rvResortSearched.visibility = View.GONE

                } else {
                    if (s?.trim()?.length!! > 0) hideUI() else showUI()
                }
            }
            override fun afterTextChanged(s: Editable?) {
                if (s?.trim()?.length!! > 0) {
                    locationViewModel.setResortName(s.toString())
                }
            }
        })
    }




    private fun hideUI() {
        binding.let {
            it.llExplreWorld.visibility = View.GONE
            it.llNearMe.visibility = View.GONE
            it.llLine1.visibility = View.GONE
            it.llLine2.visibility = View.GONE
            it.llListLocation.visibility = View.GONE
            it.rvResortSearched.visibility = View.VISIBLE
        }
    }
    private fun showUI() {
        binding.let {
            it.llExplreWorld.visibility = View.VISIBLE
            it.llNearMe.visibility = View.VISIBLE
            it.llLine1.visibility = View.VISIBLE
            it.llLine2.visibility = View.VISIBLE
            it.llListLocation.visibility = View.VISIBLE
            it.rvResortSearched.visibility = View.GONE
        }
    }

    private fun intentValueToResortDetail(id : Int) {
        val intent = Intent(this, ResortDetailActivity::class.java)
        intent.putExtra(Constant.DEFAULT_RESORT_ID, id)
        startActivity(intent)
    }
    private fun intentExtraValueToHome(value : String) {
        val intent = Intent()
        // Replace "locationName" with the actual selected location
        intent.putExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY, value)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    private fun intentExtraValueToPostingFlow(content: ResortModel.Content) {
        val intent = Intent()
        // Replace "locationName" with the actual selected location
        intent.putExtra(Constant.DEFAULT_RESORT_SEARCHED_SELECTION, content)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    private fun checkIntentFromPostingFlow(): Boolean {
        if (intent.getStringExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW) != null) {
            return true
        }
        return false
    }





}