package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CreateCustomerDialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.example.tep_timeshareexchangeplatform.R

class DialogUpdateCustomer constructor(
    context: Context,
    private val callback: ConfirmCallback?,
    private val positiveButtonTitle: String,
) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_update_customer)

        // Làm cho nền trong suốt
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )



    }

    interface ConfirmCallback {
        fun positiveAction()
    }

}