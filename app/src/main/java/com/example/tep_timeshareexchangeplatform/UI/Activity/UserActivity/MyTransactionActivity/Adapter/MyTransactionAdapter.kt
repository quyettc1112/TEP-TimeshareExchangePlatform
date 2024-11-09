package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyTransactionActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.MyTransactionModel
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Wallet.WalletListResponse
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ItemTransactionBinding
import java.text.DecimalFormat

class MyTransactionAdapter :
    BaseAdapter<WalletListResponse.Content, MyTransactionAdapter.MyTransactionViewHolder>() {

    var onItemClick: ((WalletListResponse.Content) -> Unit)? = null

    inner class MyTransactionViewHolder(binding: ItemTransactionBinding) :
        BaseItemViewHolderCF<WalletListResponse.Content, ItemTransactionBinding>(binding) {
        override fun bind(item: WalletListResponse.Content) {
            // Check Transaction Type
            if (item.money <= 0) {
                binding.ivTypeTransIcon.setImageResource(R.drawable.ic_logo_only)
                binding.transactionAmount.setTextColor(binding.root.context.resources.getColor(R.color.red_light))
            } else {
                binding.ivTypeTransIcon.setImageResource(R.drawable.ic_deposit)
                binding.transactionAmount.setTextColor(binding.root.context.resources.getColor(R.color.blue_btn_search))
            }

            // Set Transaction Info
            binding.tvTransTo.text = item.description

            // Set Transaction Time
            binding.transactionTime.text = item.createdAt

            // Set Transaction Amount
            binding.transactionAmount.text = "${formatPrice(item.money)} VND"

            /*// Set Wallet Balance
            binding.walletNumber.text = item.*/

            // Set Payment Method
            binding.tvTransactionType.text ="Phương Thức: " + item.paymentMethod



            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }


        }

        fun formatPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }



    }

    override fun differCallBack(): DiffUtil.ItemCallback<WalletListResponse.Content> {
        return object : DiffUtil.ItemCallback<WalletListResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: WalletListResponse.Content,
                newItem: WalletListResponse.Content
            ): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(
                oldItem: WalletListResponse.Content,
                newItem: WalletListResponse.Content
            ): Boolean {
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