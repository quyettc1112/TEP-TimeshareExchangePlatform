package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingStream.RentalPostingActivity

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.CustomProgressBarBinding

class CustomProgressBar(private val view: CustomProgressBarBinding) {

    private val stepTextViews = listOf(
        view.step1Text,
        view.step2Text,
        view.step3Text,
        view.step4Text,
        view.step5Text,
        view.step6Text
    )

    private val stepViews = listOf(
        view.step1View,
        view.step2View,
        view.step3View,
        view.step4View,
        view.step5View,
        view.step6View
    )

    fun updateProgress(level: Int) {
        for (i in 0 until level) {
            stepTextViews[i].setTextColor(ContextCompat.getColor(view.root.context, android.R.color.holo_green_dark))
            stepViews[i].setBackgroundColor(ContextCompat.getColor(view.root.context, android.R.color.holo_green_dark))
        }
    }
}