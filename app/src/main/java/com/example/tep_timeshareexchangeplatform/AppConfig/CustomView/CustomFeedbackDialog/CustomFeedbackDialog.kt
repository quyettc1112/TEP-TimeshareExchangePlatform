package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomFeedbackDialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.DialogFeedbackBinding

class CustomFeedbackDialog(
    context: Context,
    private val onSendClick: (rating: Int, feedback: String) -> Unit
) {
    private val dialog: Dialog = Dialog(context)
    private val binding: DialogFeedbackBinding = DialogFeedbackBinding.inflate(dialog.layoutInflater)

    private var selectedRating = 0


    init {
        dialog.setContentView(binding.root)

        // Set dialog attributes
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        setupRating()
        setupActions()
    }

    private fun setupRating() {
        val ratingViews = listOf(
            binding.fb1,
            binding.fb2,
            binding.fb3,
            binding.fb4,
            binding.fb5
        )
        val unselectedAnimations = listOf(
            R.raw.anim_fb_1_non,
            R.raw.anim_fb_2_non,
            R.raw.anim_fb_3_non,
            R.raw.anim_fb_4_nom,
            R.raw.anim_fb_5_non
        )

        val selectedAnimations = listOf(
            R.raw.anim_fb_1,
            R.raw.anim_fb_2,
            R.raw.anim_fb_3,
            R.raw.anim_fb_4,
            R.raw.anim_fb_5
        )

        ratingViews.forEachIndexed { index, lottieView ->
            lottieView.setOnClickListener {
                selectedRating = index + 1
                updateLottieAnimations(ratingViews, selectedAnimations, unselectedAnimations, selectedRating)
            }
        }
    }

    private fun updateLottieAnimations(
        views: List<LottieAnimationView>,
        selectedAnimations: List<Int>,
        unselectedAnimations: List<Int>,
        selected: Int
    ) {
        views.forEachIndexed { index, lottieView ->
            if (index == selected - 1) {
                lottieView.setAnimation(selectedAnimations[index]) // Set animation cho trạng thái selected
            } else {
                lottieView.setAnimation(unselectedAnimations[index]) // Set animation cho trạng thái unselected
            }
            lottieView.playAnimation()
        }
    }

    private fun setupActions() {
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnSend.setOnClickListener {
            if (selectedRating == 0) {
                // Hiển thị thông báo nếu chưa chọn rating
                Toast.makeText(binding.root.context, "Vui lòng chọn mức độ hài lòng!", Toast.LENGTH_SHORT).show()
            } else {
                val feedbackText = binding.edtFeedback.text.toString()
                onSendClick.invoke(selectedRating, feedbackText) // Gửi rating và feedback đến Activity
                dismiss()
            }
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }


}