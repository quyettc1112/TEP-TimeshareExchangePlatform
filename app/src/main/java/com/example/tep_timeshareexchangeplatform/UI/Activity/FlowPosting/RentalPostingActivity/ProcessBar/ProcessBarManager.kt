package com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ProcessBar

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.FlowPosting.RentalPostingActivity.ViewModel.RentalPostingViewModel
import com.example.tep_timeshareexchangeplatform.databinding.CustomProgressBarBinding

class ProcessBarManager(private val binding: CustomProgressBarBinding, private val rentalPostingViewModel: RentalPostingViewModel) {
    val layoutStep = listOf(
        binding.layoutStep1,
        binding.layoutStep2,
        binding.layoutStep3,
        binding.layoutStep4,
        binding.layoutStep5,
        binding.layoutStep6
    )

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
                stepTexts[i].setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.darker_gray))
                if (i < stepLines.size) {
                    stepLines[i].setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.darker_gray))
                }
            }
        }
    }

    // Initialize the click listeners for each step layout
    init {
        for (i in layoutStep.indices) {
            layoutStep[i].setOnClickListener {
                // Handle step click here, navigate to the clicked step
                onStepClick(i + 1) // Pass the step number
            }
        }
    }
    // Function to handle step click
    private fun onStepClick(step: Int) {
        if (rentalPostingViewModel.canNavigateToStep(step)) {
            // Allow navigation and update the UI
            tryNavigateToStep(step)
        } else {
            // Show a message that the user cannot access this step yet
            Toast.makeText(binding.root.context, "You cannot access this step yet", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to update the progress UI
    private fun tryNavigateToStep(step: Int) {
        // Implement your step navigation logic here
        rentalPostingViewModel.updateStep(step)
        updateProgress(step)
    }




}