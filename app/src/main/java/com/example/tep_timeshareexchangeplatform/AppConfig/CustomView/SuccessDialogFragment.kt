package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.tep_timeshareexchangeplatform.R

class SuccessDialogFragment() : DialogFragment() {


    // Define an interface to communicate with the Activity
    interface OnDialogDismissListener {
        fun onDialogDismissed()
    }

    var listener: OnDialogDismissListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the confirmation button click listener
        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            dismiss() // Close the dialog on button click

        }
    }

    override fun onStart() {
        super.onStart()

        // Set the dialog size to wrap content
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,  // Width wraps the content
            ViewGroup.LayoutParams.WRAP_CONTENT   // Height wraps the content
        )

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    // Override onDismiss to notify the listener (Activity) when the dialog is dismissed
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDialogDismissed() // Call the listener's method when dialog is dismissed
    }

    // Attach the listener from the activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDialogDismissListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnDialogDismissListener")
        }
    }

    // Detach the listener to prevent memory leaks
    override fun onDetach() {
        super.onDetach()
        listener = null
    }




}