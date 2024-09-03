package com.example.tep_timeshareexchangeplatform.Until

import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2

class AutoScrollViewPagerHelper(private val interval: Long = 3000L) {

    private val handler = Handler(Looper.getMainLooper())
    private val runnableMap = mutableMapOf<ViewPager2, Runnable>()

    fun setupAutoScroll(viewPager: ViewPager2) {
        // Tạo Runnable để thực hiện cuộn tự động
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem < (viewPager.adapter?.itemCount ?: 1) - 1) currentItem + 1 else 0
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, interval)
            }
        }
        runnableMap[viewPager] = runnable
        // Đăng ký callback để xử lý khi người dùng tương tác với ViewPager2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, interval)
            }
        })

        // Bắt đầu cuộn tự động
        handler.postDelayed(runnable, interval)
    }

    /**
     * Tạm dừng tự động cuộn cho tất cả các ViewPager2 đã được thiết lập
     */
    fun pauseAutoScroll() {
        handler.removeCallbacksAndMessages(null)  // Dừng tất cả các runnable
    }

    /**
     * Tiếp tục tự động cuộn cho tất cả các ViewPager2 đã được thiết lập
     */
    fun resumeAutoScroll() {
        for ((viewPager, runnable) in runnableMap) {
            handler.postDelayed(runnable, interval)
        }
    }

    /**
     * Hủy tự động cuộn và xóa tất cả các thiết lập liên quan đến ViewPager2 đã được cung cấp
     */
    fun clearAutoScroll(viewPager: ViewPager2) {
        runnableMap[viewPager]?.let {
            handler.removeCallbacks(it)
        }
        runnableMap.remove(viewPager)
    }
}