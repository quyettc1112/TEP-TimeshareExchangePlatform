package com.example.tep_timeshareexchangeplatform.UI.Activity.PostingStream.RentalPostingActivity.ProcessBar

import androidx.core.content.ContextCompat
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityRentalPostingBinding
import com.example.tep_timeshareexchangeplatform.databinding.CustomProgressBarBinding

class ProcessBarManager(private val binding: CustomProgressBarBinding) {

    val stepCircles = listOf(
        binding.step1Circle,
        binding.step2Circle,
        binding.step3Circle,
        binding.step4Circle,
        binding.step5Circle,
        binding.step6Circle
    )
    val stepTexts = listOf(
        binding.step1Text,
        binding.step2Text,
        binding.step3Text,
        binding.step4Text,
        binding.step5Text,
        binding.step6Text
    )

    val stepLabels = listOf(
        binding.step1Label,
        binding.step2Label,
        binding.step3Label,
        binding.step4Label,
        binding.step5Label,
        binding.step6Label
    )

    val stepLines = listOf(
        binding.line1,
        binding.line2,
        binding.line3,
        binding.line4,
        binding.line5
    )

    fun updateProgress(currentStep: Int) {

        for (i in stepCircles.indices) {
            if (i < currentStep - 1) {
                // Đánh dấu bước hoàn thành
                stepCircles[i].setBackgroundResource(R.drawable.circle_completed)
                stepLabels[i].setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.holo_blue_dark))
                stepTexts[i].setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
                if (i < stepLines.size) {
                    stepLines[i].setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.holo_blue_dark))
                }
            } else if (i == currentStep - 1) {
                // Đánh dấu bước hiện tại
                stepCircles[i].setBackgroundResource(R.drawable.circle_in_progress)
                stepLabels[i].setTextColor(ContextCompat.getColor(binding.root.context,R.color.primaryColor))
                stepTexts[i].setTextColor(ContextCompat.getColor(binding.root.context, R.color.primaryColor))
            } else {
                // Các bước còn lại chưa hoàn thành
                stepCircles[i].setBackgroundResource(R.drawable.circle_pending)
                stepLabels[i].setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.darker_gray))
                if (i < stepLines.size) {
                    stepLines[i].setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.darker_gray))
                }
            }
        }

    }

}