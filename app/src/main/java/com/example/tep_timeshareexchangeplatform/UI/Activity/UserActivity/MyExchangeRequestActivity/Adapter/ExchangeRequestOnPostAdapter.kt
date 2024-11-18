package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.Adapter

import com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyExchangeRequestActivity.ExchangeRequestOnPostActivity.ExchangeRequestOnPostActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseAdapter
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseItemViewHolderCF
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyExchange.MyExchangeRequestResponse
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.MyPosting.ExchangeRequestPostingResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.MyExchangeRequestStatus
import com.example.tep_timeshareexchangeplatform.databinding.ItemExchangeRequestBinding
import com.example.tep_timeshareexchangeplatform.databinding.ItemRequestOnPostBinding

class ExchangeRequestOnPostAdapter(var context: ExchangeRequestOnPostActivity) :
    BaseAdapter<ExchangeRequestPostingResponse.Content, ExchangeRequestOnPostAdapter.ExchangeRequestOnPostViewHolder>() {

    var onItemClick: ((ExchangeRequestPostingResponse.Content) -> Unit)? = null


    inner class ExchangeRequestOnPostViewHolder(binding: ItemRequestOnPostBinding) :
        BaseItemViewHolderCF<ExchangeRequestPostingResponse.Content, ItemRequestOnPostBinding>(binding) {
        override fun bind(item: ExchangeRequestPostingResponse.Content) {

            // Posting Info
            binding.apply {
                tvOwnerFullName.text = "${item.ownerFullName}"
                tvRoomInfoCode.text = "Mã phòng: ${item.roomInfo.roomInfoCode}"
                tvCheckInDate.text =
                    Constant.formatDateByLocale(item.startDate, binding.root.context)
                tvCheckOutDate.text =
                    Constant.formatDateByLocale(item.endDate, binding.root.context)
                // Photo
                Glide.with(binding.root.context)
                    .load(item.ownerAvatar)
                    .placeholder(R.drawable.ripple_effect_white)
                    .into(binding.ivOwnerAvatar)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<ExchangeRequestPostingResponse.Content> {
        return object : DiffUtil.ItemCallback<ExchangeRequestPostingResponse.Content>() {
            override fun areItemsTheSame(
                oldItem: ExchangeRequestPostingResponse.Content,
                newItem: ExchangeRequestPostingResponse.Content
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ExchangeRequestPostingResponse.Content,
                newItem: ExchangeRequestPostingResponse.Content
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRequestOnPostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRequestOnPostBinding.inflate(layoutInflater, parent, false)
        return ExchangeRequestOnPostViewHolder(binding)
    }

}