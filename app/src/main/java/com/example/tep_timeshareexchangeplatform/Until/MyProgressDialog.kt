package com.example.tep_timeshareexchangeplatform.Until

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.tep_timeshareexchangeplatform.R

class MyProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_progress_dialog_layout)

        // Đặt nền dialog trong suốt
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Tùy chọn: Vô hiệu hóa khả năng hủy dialog khi nhấn ra ngoài
        setCancelable(false)
    }
}
