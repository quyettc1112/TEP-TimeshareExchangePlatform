package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog

import android.app.Dialog
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity.MainViewModel
import com.example.tep_timeshareexchangeplatform.databinding.DialogBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RoomSelectionDialog : BottomSheetDialogFragment() {
    // Sử dụng activityViewModels để chia sẻ ViewModel với Activity hoặc Fragment khác
    private val roomSelectionViewModel: MainViewModel by activityViewModels()

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
        // Quan sát dữ liệu từ ViewModel
        roomSelectionViewModel.roomCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvRoomCount.text = count.toString()
        })

        roomSelectionViewModel.adultCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvAdultCount.text = count.toString()
        })

        roomSelectionViewModel.childrenCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvChildCount.text = count.toString()
        })

        // Xử lý tăng giảm số phòng
        binding.imRemoveRoom.setOnClickListener {
            val newRoomCount = (roomSelectionViewModel.roomCount.value ?: 1) - 1
            if (newRoomCount >= 1) {
                roomSelectionViewModel.updateRoomCount(newRoomCount)
            }
        }

        binding.imAddRoom.setOnClickListener {
            val newRoomCount = (roomSelectionViewModel.roomCount.value ?: 1) + 1
            roomSelectionViewModel.updateRoomCount(newRoomCount)
        }

        // Xử lý tăng giảm số người lớn
        binding.imRemoveAdult.setOnClickListener {
            val newAdultCount = (roomSelectionViewModel.adultCount.value ?: 1) - 1
            if (newAdultCount >= 1) {
                roomSelectionViewModel.updateAdultCount(newAdultCount)
            }
        }

        binding.imAddAdult.setOnClickListener {
            val newAdultCount = (roomSelectionViewModel.adultCount.value ?: 1) + 1
            roomSelectionViewModel.updateAdultCount(newAdultCount)
        }

        // Xử lý tăng giảm số trẻ em
        binding.imRemoveChild.setOnClickListener {
            val newChildrenCount = (roomSelectionViewModel.childrenCount.value ?: 0) - 1
            if (newChildrenCount >= 0) {
                roomSelectionViewModel.updateChildrenCount(newChildrenCount)
            }

            if (newChildrenCount == 0) {
                slideDown(binding.cstlChildAgeContainer)
                binding.cstlChildAgeContainer.visibility = View.GONE
            }
        }

        binding.imAddChild.setOnClickListener {
            val newChildrenCount = (roomSelectionViewModel.childrenCount.value ?: 0) + 1
            roomSelectionViewModel.updateChildrenCount(newChildrenCount)
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnAccept.setOnClickListener {
            dismiss() // Logic xử lý khi nhấn "Chọn"
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