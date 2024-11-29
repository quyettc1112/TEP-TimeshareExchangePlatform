package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.ResortDetail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ImageDetailAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.Adapter.ImageViewPagerAdapter
import com.example.tep_timeshareexchangeplatform.Until.AutoScrollViewPagerHelper
import com.example.tep_timeshareexchangeplatform.databinding.ActivityImageListBinding

class ImageListActivity : BaseActivity() {

    private lateinit var binding: ActivityImageListBinding
    private  lateinit var recycler : ImageDetailAdapter
    private lateinit var viewPager: ImageViewPagerAdapter
    private var imagePosition: Int = 0
    private var listImage : List<String> = listOf()
    private val autoScrollHelper = AutoScrollViewPagerHelper(interval = 3000L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Get IntentValue
        // Retrieve data from the intent bundle
        imagePosition = intent.extras?.getInt(Constant.IMAGE_POSITION, 0) ?: 0
        listImage = intent.extras?.getStringArrayList(Constant.IMAGE_LIST) ?: listOf()

        // Set up RecyclerView with LinearLayoutManager
        setUpImageList(imagePosition, listImage)
        setToolbarEvent()

    }

    private fun setUpImageList(startPosition : Int, imageList : List<String>) {
        binding.viewPager.apply {
            viewPager = ImageViewPagerAdapter()
            viewPager.submitList(imageList)
            adapter = viewPager
            setCurrentItem(startPosition, false)
            binding.tvImageCount.text = "${startPosition + 1 } / ${imageList.size}"

        }

        binding.thumbnailRecyclerView.apply {
            recycler = ImageDetailAdapter(binding.thumbnailRecyclerView)
            recycler.submitList(imageList)
            recycler.onItemClick = {
                binding.viewPager.setCurrentItem(it, true)
            }
            recycler.setSelectedPosition(startPosition)
            recycler.smoothScrollToSelectedPosition(startPosition)
            adapter = recycler
            layoutManager = LinearLayoutManager(this@ImageListActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        // Syncing RecyclerView with ViewPager2
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                recycler.setSelectedPosition(position)
                recycler.smoothScrollToSelectedPosition(position)
                binding.tvImageCount.text = "${position + 1} / ${imageList.size}"
            }
        })

    }

    private fun setToolbarEvent() {
        binding.customToolbar.onStartIconClick = {
            finish()
        }

    }




}