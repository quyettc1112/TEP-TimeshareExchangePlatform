package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.LocationAdapter
import com.example.tep_timeshareexchangeplatform.Common.Adapter.LocationSearchedAdapter
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetailActivity
import com.example.tep_timeshareexchangeplatform.databinding.ActivityLocationBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class LocationActivity : BaseActivity() {

    private lateinit var binding : ActivityLocationBinding
    private val locationList = listOf(
        "Hồ Chí Minh", "Vũng Tàu", "Đà Lạt", "Phan Thiết", "Quy Nhơn", "Phú Quốc",
        "Đà Nẵng", "Nha Trang", "Hà Nội", "Sa Pa", "Hạ Long", "Hội An",
        "Hải Phòng", "Singapore", "Bangkok", "Phuket", "Kuala Lumpur",
        "Seoul", "Tokyo", "Bali"
    )
    private val locationAdapter  = LocationAdapter()
    private val locationAdapterSearched  = LocationSearchedAdapter()
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
        binding.rvLocationSearched.visibility = View.GONE
        initAdapter()
        initRecyclerView()
        onItemClickEvent()
        handleClickEventButton()
        searchLocation()
        if (checkIntentFromPostingFlow()) {
            hideUI()
            binding.rvLocationSearched.visibility = View.GONE

        }

    }

    private fun initAdapter() {
        locationAdapter.submitList(locationList)
        locationAdapterSearched.submitOriginalList(Constant.cityList)
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
        // Location Search List
        binding.rvLocationSearched.apply {
            layoutManager = LinearLayoutManager(this@LocationActivity)
            adapter = locationAdapterSearched
        }
    }


    private fun onItemClickEvent() {
        // Location Click
        locationAdapter.onitemCLickListener = {
            intentExtraValueToHome(it + ", Việt Nam")
        }
        // Location Searched Click
        locationAdapterSearched.onItemClickListener = { it ->
            if (checkIntentFromPostingFlow()) {
                intentExtraValueToPostingFlow(it.name + ", " + it.location)
                finish()
            } else {
                if (it.type == 1) { intentExtraValueToHome(it.name + ", " + it.location) }
                else {
                    startActivity(Intent(this, ResortDetailActivity::class.java))
                    finish()
                }
            }
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
                    binding.rvLocationSearched.visibility = View.GONE
                    binding.llExplreWorld.visibility = View.GONE
                    binding.llNearMe.visibility = View.GONE
                    binding.llLine1.visibility = View.GONE
                    if (s?.trim()?.length!! > 0)
                        binding.rvLocationSearched.visibility = View.GONE
                    else binding.rvLocationSearched.visibility = View.VISIBLE
                    locationAdapterSearched.filter.filter("query=${s.toString()}&type=2")

                } else {
                    if (s?.trim()?.length!! > 0) hideUI() else showUI()
                    locationAdapterSearched.filter.filter("query=${s.toString()}")
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // No action needed after the text changes
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
            it.rvLocationSearched.visibility = View.VISIBLE
        }
    }

    private fun showUI() {
        binding.let {
            it.llExplreWorld.visibility = View.VISIBLE
            it.llNearMe.visibility = View.VISIBLE
            it.llLine1.visibility = View.VISIBLE
            it.llLine2.visibility = View.VISIBLE
            it.llListLocation.visibility = View.VISIBLE
            it.rvLocationSearched.visibility = View.GONE
        }
    }
    private fun intentExtraValueToHome(value : String) {
        val intent = Intent()
        // Replace "locationName" with the actual selected location
        intent.putExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY, value)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun intentExtraValueToPostingFlow(value : String) {
        val intent = Intent()
        // Replace "locationName" with the actual selected location
        intent.putExtra(Constant.DEFAULT_SELECTION_LOCATION_KEY_POSTING_FLOW, value)
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