package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.PostingBottomNavDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.tep_timeshareexchangeplatform.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PostOptionsBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_posting_bottom_nav, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton: ImageView = view.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
        }

        val rentTimeshareOption: LinearLayout = view.findViewById(R.id.ll_layout_rent_timeshare)
        rentTimeshareOption.setOnClickListener {
            // Handle Rent Timeshare action
        }

        val exchangeTimeshareOption: LinearLayout = view.findViewById(R.id.ll_layout_exchange_timeshare)
        exchangeTimeshareOption.setOnClickListener {
            // Handle Exchange Timeshare action
        }
    }
}