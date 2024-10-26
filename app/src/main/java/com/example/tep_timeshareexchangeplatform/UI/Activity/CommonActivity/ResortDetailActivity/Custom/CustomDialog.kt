package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.ResortDetailActivity.Custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.tep_timeshareexchangeplatform.databinding.DialogResortDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CustomDialog(context: Context) :  BottomSheetDialog(context) {

    private lateinit var binding: DialogResortDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = DialogResortDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* // Thiết lập sự kiện cho nút "Close"
        binding.btnClose.setOnClickListener {
            dismiss()  // Đóng dialog
        }*/
    }

   /* // Phương thức để bind dữ liệu vào dialog
    fun bindData(title: String, description: String) {
        titleTextView.text = title
        descriptionTextView.text = description
    }*/

}