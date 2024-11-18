package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CreateCustomerDialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog.ConfirmCallback
import com.example.tep_timeshareexchangeplatform.R

class CreateCustomerDialog constructor(
    context: Context,
    private val callback: ConfirmCallback?,
    private val positiveButtonTitle: String,
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)



    }

}