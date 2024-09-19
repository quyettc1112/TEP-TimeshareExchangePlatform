package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.RoomSelectionDialog

import android.app.Dialog
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            // Ẩn phần nhập tuổi nếu không có trẻ em
            // Sử dụng TransitionManager để có hiệu ứng khi ẩn
            if (childrenCount == 0) {
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
                binding.cstlChildAgeContainer.visibility = View.GONE
            }
        }

        binding.imAddChild.setOnClickListener {
            childrenCount++
            binding.tvChildCount.text = childrenCount.toString()

            // Mở dialog nhập tuổi nếu số trẻ em > 1
            if (childrenCount > 0) {
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
                binding.cstlChildAgeContainer.visibility = View.VISIBLE


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

  /*  private fun openChildrenAgeDialog(childrenCount: Int) {
        val dialog = ChildrenAgeDialog.newInstance(childrenCount)
        dialog.show(parentFragmentManager, "ChildrenAgeDialog")
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): RoomSelectionDialog {
            return RoomSelectionDialog()
        }
    }
}