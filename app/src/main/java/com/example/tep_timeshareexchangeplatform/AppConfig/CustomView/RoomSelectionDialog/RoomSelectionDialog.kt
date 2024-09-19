package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog

import android.app.Dialog
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.DialogBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RoomSelectionDialog : BottomSheetDialogFragment() {

    private var roomCount = 1
    private var adultCount = 1
    private var childrenCount = 0

    // Khai báo binding
    private var _binding: DialogBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sử dụng View Binding để inflate layout
        _binding = DialogBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập các giá trị ban đầu
        binding.tvRoomCount.text = roomCount.toString()
        binding.tvAdultCount.text = adultCount.toString()
        binding.tvChildCount.text = childrenCount.toString()

        // Xử lý tăng giảm số phòng
        binding.imRemoveRoom.setOnClickListener {
            if (roomCount > 1) roomCount--
            binding.tvRoomCount.text = roomCount.toString()
        }

        binding.imAddRoom.setOnClickListener {
            roomCount++
            binding.tvRoomCount.text = roomCount.toString()
        }

        // Xử lý tăng giảm số người lớn
        binding.imRemoveAdult.setOnClickListener {
            if (adultCount > 1) adultCount--
            binding.tvAdultCount.text = adultCount.toString()
        }

        binding.imAddAdult.setOnClickListener {
            adultCount++
            binding.tvAdultCount.text = adultCount.toString()
        }

        // Xử lý tăng giảm số trẻ em
        binding.imRemoveChild.setOnClickListener {
            if (childrenCount > 0) childrenCount--
            binding.tvChildCount.text = childrenCount.toString()

            if (childrenCount == 0) {
                slideDown(binding.cstlChildAgeContainer)
                binding.cstlChildAgeContainer.visibility = View.GONE
            }
        }

        binding.imAddChild.setOnClickListener {
            childrenCount++
            binding.tvChildCount.text = childrenCount.toString()

            // Mở dialog nhập tuổi nếu số trẻ em > 1
            if (childrenCount > 0) {
                slideUp(binding.cstlChildAgeContainer)
               // binding.cstlChildAgeContainer.visibility = View.VISIBLE
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnAccept.setOnClickListener {
            // Logic xử lý khi nhấn "Chọn"
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): RoomSelectionDialog {
            return RoomSelectionDialog()
        }
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0f
        ) // toYDelta
        animate.duration = 200
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            0f,  // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 200
        animate.fillAfter = true
        view.startAnimation(animate)
    }
}