package com.example.tep_timeshareexchangeplatform.Until

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginItemDecoration(private val horizontalMarginInDp: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val margin = (horizontalMarginInDp * view.context.resources.displayMetrics.density).toInt()

        // Apply margin to the right side of each item
        outRect.right = margin

        // Apply margin to the left side of each item, except the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0
        } else {
            outRect.left = margin
        }
    }
}