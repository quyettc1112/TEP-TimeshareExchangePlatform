package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTransactionModel
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemTransactionBinding

class MyTransactionAdapter : BaseAdapter<MyTransactionModel, MyTransactionAdapter.MyTransactionViewHolder>() {
    inner class MyTransactionViewHolder(binding: ItemTransactionBinding)
        : BaseItemViewHolderCF<MyTransactionModel, ItemTransactionBinding>(binding) {
        override fun bind(item: MyTransactionModel) {
            // Check Transaction Type
            if (item.type == 1) {
                binding.ivTypeTransIcon.setImageResource(R.drawable.ic_logo_only)
                binding.transactionAmount.setTextColor(binding.root.context.resources.getColor(R.color.red_light))
            } else {
                binding.ivTypeTransIcon.setImageResource(R.drawable.ic_deposit)
                binding.transactionAmount.setTextColor(binding.root.context.resources.getColor(R.color.blue_btn_search))
            }

            // Set Transaction Info
            binding.tvTransTo.text = "${item.transactionType} - ${item.recipientName}"

            // Set Transaction Time
            binding.transactionTime.text = item.transactionTime

            // Set Transaction Amount
            binding.transactionAmount.text = item.transactionAmount

            // Set Wallet Balance
            binding.walletNumber.text = item.walletBalance

            // Set Payment Method
            binding.tvTransactionType.text = item.paymentMethod


        }


    }

    override fun differCallBack(): DiffUtil.ItemCallback<MyTransactionModel> {
        return object : DiffUtil.ItemCallback<MyTransactionModel>() {
            override fun areItemsTheSame(oldItem: MyTransactionModel, newItem: MyTransactionModel): Boolean {
                return oldItem.transactionID == newItem.transactionID
            }

            override fun areContentsTheSame(oldItem: MyTransactionModel, newItem: MyTransactionModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionBinding.inflate(layoutInflater, parent, false)
        return MyTransactionViewHolder(binding)
    }
}