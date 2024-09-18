package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.LocationAdapter
import com.example.tep_timeshareexchangeplatform.R
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
        setLocationRecyclerView()
        handleClickEventButton()
        searchLocation()
    }

    // List of Location
    private fun setLocationRecyclerView() {
        locationAdapter.submitList(locationList)
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvLocation.let {
            it.layoutManager = flexboxLayoutManager
            it.adapter = locationAdapter
        }
        // Click event
        locationAdapter.onitemCLickListener = {
            Toast.makeText(this, "Clicked: $it", Toast.LENGTH_SHORT).show()
        }
    }

    // Back to previous screen or Explore
    private fun handleClickEventButton() {
       binding.let {
           it.llExplreWorld.setOnClickListener {
               finish()
           }
           it.imCloseIcon.setOnClickListener{
               finish()
           }
       }
    }
    // Nearby Location

    // Search Location
    private fun searchLocation() {
        // Add a TextWatcher to EditText to filter results as the user types
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.trim()?.length!! > 0) hideUI() else showUI()


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
        }
    }

    private fun showUI() {
        binding.let {
            it.llExplreWorld.visibility = View.VISIBLE
            it.llNearMe.visibility = View.VISIBLE
            it.llLine1.visibility = View.VISIBLE
            it.llLine2.visibility = View.VISIBLE
            it.llListLocation.visibility = View.VISIBLE
        }
    }





}